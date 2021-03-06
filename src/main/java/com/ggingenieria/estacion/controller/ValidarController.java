package com.ggingenieria.estacion.controller;

import com.ggingenieria.estacion.DAO.DAO;
import com.ggingenieria.estacion.modelos.Empresa;
import com.ggingenieria.estacion.modelos.Registro;
import com.ggingenieria.estacion.modelos.Vehiculo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RestController
@RequestMapping("/validar")
public class ValidarController {

    @RequestMapping("/vehiculo")
    public boolean vehiculo() {

        Vehiculo v = DAO.getInstance().getVehiculo(14);
        Registro r = DAO.getInstance().getUltimaVenta(v.getVehiculoId());

        Calendar calendar = GregorianCalendar.getInstance();
        Date fechaActual = calendar.getTime();
        Date proximaVenta = r.getFechaProximaVenta();

        long diferencia = fechaActual.getTime() - proximaVenta.getTime();
        v.setAutorizado(diferencia >= 0);
        DAO.getInstance().update(v);

        return diferencia >= 0;
    }

}
