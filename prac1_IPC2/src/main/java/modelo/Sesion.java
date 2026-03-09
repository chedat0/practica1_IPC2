/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jeffm
 */
public class Sesion {
    private static Usuario usuarioActual = null;

    public static void iniciar(Usuario usuario) { 
        usuarioActual = usuario; 
    }
    
    public static void cerrar() { 
        usuarioActual = null; 
    }
    
    public static Usuario getUsuario() { 
        return usuarioActual; 
    }
    
    public static boolean estaActiva(){ 
        return usuarioActual != null; 
    }
}
