/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import modelo.enums.EstadoPedido;
import modelo.enums.Origen;
import java.time.LocalDateTime;

/**
 *
 * @author jeffm
 */
public class HistorialEstadoPedido {
    private int           idHistorial;
    private int           idPedido;
    private EstadoPedido  estadoAnterior;
    private EstadoPedido  estadoNuevo;
    private LocalDateTime fechaCambio;
    private Origen  origen;

    public HistorialEstadoPedido() {}

    public HistorialEstadoPedido(int idPedido, EstadoPedido anterior, EstadoPedido nuevo, Origen origen) {
        this.idPedido       = idPedido;
        this.estadoAnterior = anterior;
        this.estadoNuevo    = nuevo;
        this.origen         = origen;
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public EstadoPedido getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoPedido estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoPedido getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoPedido estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Origen getOrigen() {
        return origen;
    }

    public void setOrigen(Origen origen) {
        this.origen = origen;
    }
        
}
