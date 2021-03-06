package com.ggingenieria.estacion.port;

import com.ggingenieria.estacion.DAO.DAO;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by francisco on 15/06/15.
 */
@Component("lectorLlavero")
public class LectorLlavero implements Runnable, SerialPortEventListener {

    public static final int SLEEP_TIME = 60000;
    private SerialPort port;

    @Autowired
    private SimpMessagingTemplate template;

    @Value("${puerto.nombre}")
    private String puertoName;
    @Value("${puerto.baudRate}")
    private int baudRate;
    @Value("${puerto.dataBits}")
    private int dataBits;
    @Value("${puerto.stopBits}")
    private int stopBits;
    @Value("${puerto.parity}")
    private int parity;

    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void run() {
            while (true) {
                try {
                    Thread.sleep(SLEEP_TIME);
                    initPort();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (SerialPortException e) {
                    port = null;
                }
            }
    }

    private void initPort() throws SerialPortException {
        if(port == null) {
            port = new SerialPort(puertoName);
            port.openPort();//Open port
            port.setParams(baudRate, dataBits, stopBits, parity);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            port.setEventsMask(mask);//Set mask
            port.addEventListener(this);//Add SerialPortEventListener
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.isRXCHAR()) {//If data is available
            //if (serialPortEvent.getEventValue() == 14) {//Check bytes count in the input buffer
            //Read data, if 10 bytes available
            try {
                byte buffer[] = port.readBytes(14);
                if (buffer[0] == 0x02) {
                    String key = new String(buffer).substring(1, 11);
                    System.out.println(key);
                    template.convertAndSend("/wsclave/getclave", key);
                    Map<String, Object> autorizado = DAO.getInstance().autorizacion(key);
                    if (autorizado != null) {
                        template.convertAndSend("/wsvehiculo/getvehiculo", autorizado);
                    }
                }
            } catch (SerialPortException ex) {
                throw new RuntimeException(ex);
            }
            //}
        }
    }
}
