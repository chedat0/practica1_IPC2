/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.enums;

/**
 *
 * @author jeffm
 */
public enum EstadoPartida {
    EN_CURSO,
    FINALIZADA,
    ABANDONADA;

    public static EstadoPartida fromString(String s) {
        for (EstadoPartida e : values()) {
            if (e.name().equalsIgnoreCase(s)) return e;
        }
        return EN_CURSO;
    }
}
