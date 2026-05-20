/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guia4_progra3.dao;

import guia4_progra3.db.ConexionDB;
import guia4_progra3.model.Alumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    public boolean agregar(Alumno a) {
        String sql = "INSERT INTO alumnos (nombres, apellidos, carnet, carrera, fecha_inscripcion) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConexionDB.Obtener_Conexion().prepareStatement(sql)) {
            ps.setString(1, a.getNombres());
            ps.setString(2, a.getApellidos());
            ps.setString(3, a.getCarnet());
            ps.setString(4, a.getCarrera());
            ps.setString(5, a.getFechaInscripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Alumno a) {
        String sql = "UPDATE alumnos SET nombres=?, apellidos=?, carnet=?, carrera=?, "
                   + "fecha_inscripcion=? WHERE id_alumno=?";
        try (PreparedStatement ps = ConexionDB.Obtener_Conexion().prepareStatement(sql)) {
            ps.setString(1, a.getNombres());
            ps.setString(2, a.getApellidos());
            ps.setString(3, a.getCarnet());
            ps.setString(4, a.getCarrera());
            ps.setString(5, a.getFechaInscripcion());
            ps.setInt(6, a.getIdAlumno());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idAlumno) {
        String sql = "DELETE FROM alumnos WHERE id_alumno = ?";
        try (PreparedStatement ps = ConexionDB.Obtener_Conexion().prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    public List<Alumno> listarTodos() {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alumnos ORDER BY id_alumno";
        try (Statement st = ConexionDB.Obtener_Conexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Alumno(
                    rs.getInt("id_alumno"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("carnet"),
                    rs.getString("carrera"),
                    rs.getString("fecha_inscripcion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public boolean carnetExiste(String carnet, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM alumnos WHERE carnet = ? AND id_alumno <> ?";
        try (PreparedStatement ps = ConexionDB.Obtener_Conexion().prepareStatement(sql)) {
            ps.setString(1, carnet);
            ps.setInt(2, idExcluir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar carnet: " + e.getMessage());
        }
        return false;
    }
}