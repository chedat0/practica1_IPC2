/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jeffm
 */
public class Sucursal {
    private int idSucursal;
    private String nombre;
    private String dirección;
    private boolean activa;
    
    public Sucursal (){}

    public Sucursal(int idSucursal, String nombre, String dirección, boolean activa) {
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.dirección = dirección;
        this.activa = activa;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDirección() {
        return dirección;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    
}
