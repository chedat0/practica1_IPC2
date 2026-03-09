/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.enums;

/**
 *
 * @author jeffm
 */
public enum EstadoPedido {
    RECIBIDA,
    PREPARANDO,
    EN_HORNO,
    LISTA,
    CANCELADA,
    NO_ENTREGADO;

    public boolean esFinalizado() {
        return this == LISTA || this == CANCELADA || this == NO_ENTREGADO;
    }

    public boolean sePuedeCancelar() {
        return this == RECIBIDA || this == PREPARANDO;
    }

    public EstadoPedido siguiente() {
        switch (this) {
            case RECIBIDA:   return PREPARANDO;
            case PREPARANDO: return EN_HORNO;
            case EN_HORNO:   return LISTA;
            default:         return null;
        }
    }

    public static EstadoPedido fromString(String s) {
        for (EstadoPedido e : values()) {
            if (e.name().equalsIgnoreCase(s)) return e;
        }
        return null;
    }
}
