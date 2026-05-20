/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guia4_progra3.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author josjo
 */
public class ConexionDB {

    public static Connection Obtener_Conexion() {
            try {
                String url = Config.get("db.url");
                String user = Config.get("db.user");
                String pass = Config.get("db.password");

                Class.forName(Config.get("db.driver")); // Cargar Driver JDBC
                return DriverManager.getConnection(url, user, pass);

            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Error en conexión: " + e.getMessage());
                return null;
            }
        }
    }

