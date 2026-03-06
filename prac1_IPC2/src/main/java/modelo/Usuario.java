/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jeffm
 */
public class Usuario {
    private int idUsuario;
    private String usuario;
    private String contraseña;
    private String nombre;
    private String apellido;
    private String email;
    private Rol rol;
    private Sucursal sucursal;
    private boolean activo;

    public Usuario(int idUsuario, String usuario,String contraseña, String nombre, String apellido, String email, Rol rol, Sucursal sucursal, boolean activo) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
        this.sucursal = sucursal;
        this.activo = activo;
    }

    public Usuario (){}
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña(){
        return contraseña;
    }
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public Rol getRol() {
        return rol;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
        
    public String getNombreCompleto(){
        return nombre + (apellido != null ? " "+ apellido : "");
    }
    
    @Override
    public String toString() { return usuario; }
}
