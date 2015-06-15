package com.ggingenieria.estacion.DAO;

import com.ggingenieria.estacion.modelos.Empresa;
import com.ggingenieria.estacion.modelos.Producto;
import com.ggingenieria.estacion.modelos.Registro;
import com.ggingenieria.estacion.modelos.Surtidor;
import com.ggingenieria.estacion.modelos.Usuario;
import com.ggingenieria.estacion.modelos.Vehiculo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class DAO {

    private static DAO instance = null;
    private static SessionFactory sessionFactory;

    private DAO() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException he) {
            System.err.println("Ocurrió un error en la inicialización de la SessionFactory: " + he);
            throw new ExceptionInInitializerError(he);
        }
    }

    public static DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    //USUARIOS
    public ArrayList<Usuario> getUsuarios() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Usuario> usuarios = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Usuario");
            usuarios = (List<Usuario>) q.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return (ArrayList<Usuario>) usuarios;
        }
    }

    public Usuario getUsuario(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Usuario usuarios = null;
        try {
            tx = session.beginTransaction();
            usuarios = (Usuario) session.get(Usuario.class, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return usuarios;
        }
    }

    public Usuario getUsuarioPorNombre(String username) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Usuario usuario = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Usuario WHERE nombreUsuario = :username").setParameter("username", username);
            usuario = (Usuario) q.list().get(0);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return usuario;
        }
    }

    //VEHICULOS
    public ArrayList<Vehiculo> getVehiculos() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Vehiculo> vehiculo = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Vehiculo");
            vehiculo = (List<Vehiculo>) q.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return (ArrayList<Vehiculo>) vehiculo;
        }
    }

    public Vehiculo getVehiculo(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Vehiculo vehiculo = null;
        try {
            tx = session.beginTransaction();
            vehiculo = (Vehiculo) session.get(Vehiculo.class, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return vehiculo;
        }
    }

    public Vehiculo getVehiculoPorClave(String clave) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Vehiculo vehiculo = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Vehiculo WHERE tarjetaId = :tarjetaId").setParameter("tarjetaId", clave);
            vehiculo = (Vehiculo) q.list().get(0);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return vehiculo;
    }


    //EMPRESA
    public ArrayList<Empresa> getEmpresas() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Empresa> empresa = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Empresa");
            empresa = (List<Empresa>) q.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return (ArrayList<Empresa>) empresa;
        }
    }

    public Empresa getEmpresa(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Empresa empresa = null;
        try {
            tx = session.beginTransaction();
            empresa = (Empresa) session.get(Empresa.class, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return empresa;
        }
    }


    //PRODUCTOS

    public ArrayList<Producto> getProductoPorEmpresa(Empresa empresa) {

        ArrayList<Producto> productos = getProductos();

        for (Producto producto : productos) {
            producto.setPuntosConDescuento((int) (producto.getPuntos() * (1 - empresa.getDescuento() / 100)));
        }

        return productos;
    }

    public ArrayList<Producto> getProductos() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Producto> producto = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Producto");
            producto = (List<Producto>) q.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return (ArrayList<Producto>) producto;
        }
    }

    public Producto getProducto(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Producto producto = null;
        try {
            tx = session.beginTransaction();
            producto = (Producto) session.get(Producto.class, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return producto;
        }
    }


    //REGISTROS
    public ArrayList<Registro> getRegistroVentas(String fecha, int empresaId) {
        return getRegistros(fecha, empresaId, "VENTA");
    }

    public ArrayList<Registro> getRegistroCambios(String fecha, int empresaId) {
        return getRegistros(fecha, empresaId, "CAMBIO_DE_PUNTOS");
    }

    public ArrayList<Registro> getRegistros(String fecha, int empresaId, String tipo) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Registro> registro = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("FROM Registro WHERE fechaVencimiento > :fecha AND empresaId=:empresaId AND accion = :accion");
            q.setTimestamp("fecha", format.parse(fecha));
            q.setParameter("empresaId", empresaId);
            q.setParameter("accion", tipo);
            registro = (List<Registro>) q.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return (ArrayList<Registro>) registro;
        }
    }

    public Registro getUltimaVenta(final int vehiculoId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Registro> registro = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("FROM Registro WHERE vehiculoId = :vehiculoId AND accion = 'VENTA' ORDER BY fechaRegistro DESC");
            q.setParameter("vehiculoId", vehiculoId);
            registro = (List<Registro>) q.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return registro.get(0);
        }
    }

    public Registro getRegistro(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Registro registro = null;
        try {
            tx = session.beginTransaction();
            registro = (Registro) session.get(Registro.class, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return registro;
        }
    }


    //PRODUCTOS
    public ArrayList<Surtidor> getSurtidores() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Surtidor> surtidor = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Surtidor");
            surtidor = (List<Surtidor>) q.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return (ArrayList<Surtidor>) surtidor;
        }
    }

    public Surtidor getSurtidor(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Surtidor surtidor = null;
        try {
            tx = session.beginTransaction();
            surtidor = (Surtidor) session.get(Surtidor.class, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            return surtidor;
        }
    }

    //OPEN SESSION
    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void delete(Object u) {
        Session s = sessionFactory.openSession();
        s.beginTransaction();

        s.delete(u);
        s.getTransaction().commit();
        s.close();
    }

    public void update(Object u) {
        Session s = sessionFactory.openSession();
        s.beginTransaction();

        s.update(u);
        s.getTransaction().commit();
        s.close();
    }

    //INSERTS
    public void add(Object u) {
        Session s = sessionFactory.openSession();
        s.beginTransaction();

        s.save(u);
        s.getTransaction().commit();
        s.close();
    }


}