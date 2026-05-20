/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guia4_progra3.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties props = new Properties();

    static {
        try {

            InputStream in = Config.class.getClassLoader()
                    .getResourceAsStream("config.properties");

            if (in == null) {
                throw new RuntimeException(
                        """
                    No se encontro el archivo config.properties en el classpath.
                    Verifica que esté en src (o 'Other Sources')
                    y se llame exactamente 'config.properties'.""");
            }
            props.load(in);

        } catch (IOException e) {
            throw new RuntimeException("Error cargando configuración: "
                    + e.getMessage(), e);
        }
    }

    public static String get(String clave) {
        return props.getProperty(clave);
    }

}
