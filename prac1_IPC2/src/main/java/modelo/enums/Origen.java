/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.enums;

/**
 *
 * @author jeffm
 */
public enum Origen {
    JUGADOR,
    SISTEMA;
    
    public static Origen fromString (String s){
    return (s != null && s.equalsIgnoreCase("SISTEMA")) ? SISTEMA : JUGADOR; 
    }
}
