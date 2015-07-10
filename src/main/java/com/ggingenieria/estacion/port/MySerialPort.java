package com.ggingenieria.estacion.port;

import com.ggingenieria.estacion.configuration.Configuracion;
import jssc.SerialPortList;

/**
 * Created by francisco on 15/06/15.
 */
public class MySerialPort {
    public static void main(String[] args) {
        String[] portNames = SerialPortList.getPortNames();
        for(int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
        }

        LectorLlavero ll = new LectorLlavero(new Configuracion());

        Thread tr = new Thread(ll);

        tr.start();

    }
}
