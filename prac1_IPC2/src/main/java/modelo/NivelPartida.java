/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;

/**
 *
 * @author jeffm
 */
public class NivelPartida {
    private int           idNivelPartida;
    private int           idPartida;
    private int           nivel;
    private LocalDateTime fechaAlcanzado;
    private int           puntajeAlAlcanzar;
    private int           pedidosAlAlcanzar;

    public NivelPartida() {}

    public NivelPartida(int idPartida, int nivel, int puntaje, int pedidos) {
        this.idPartida         = idPartida;
        this.nivel             = nivel;
        this.puntajeAlAlcanzar = puntaje;
        this.pedidosAlAlcanzar = pedidos;
    }

    public int getIdNivelPartida() {
        return idNivelPartida;
    }

    public void setIdNivelPartida(int idNivelPartida) {
        this.idNivelPartida = idNivelPartida;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public LocalDateTime getFechaAlcanzado() {
        return fechaAlcanzado;
    }

    public void setFechaAlcanzado(LocalDateTime fechaAlcanzado) {
        this.fechaAlcanzado = fechaAlcanzado;
    }

    public int getPuntajeAlAlcanzar() {
        return puntajeAlAlcanzar;
    }

    public void setPuntajeAlAlcanzar(int puntajeAlAlcanzar) {
        this.puntajeAlAlcanzar = puntajeAlAlcanzar;
    }

    public int getPedidosAlAlcanzar() {
        return pedidosAlAlcanzar;
    }

    public void setPedidosAlAlcanzar(int pedidosAlAlcanzar) {
        this.pedidosAlAlcanzar = pedidosAlAlcanzar;
    }  
}
