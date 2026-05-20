/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guia4_progra3.model;

/**
 *
 * @author josjo
 */
public class Alumno {
    
    private int    idAlumno;
    private String nombres;
    private String apellidos;
    private String carnet;
    private String carrera;
    private String fechaInscripcion;

    public Alumno() {}
    
    public Alumno(int idAlumno, String nombres, String apellidos,
                  String carnet, String carrera, String fechaInscripcion) {
        this.idAlumno         = idAlumno;
        this.nombres          = nombres;
        this.apellidos        = apellidos;
        this.carnet           = carnet;
        this.carrera          = carrera;
        this.fechaInscripcion = fechaInscripcion;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }


    
}
