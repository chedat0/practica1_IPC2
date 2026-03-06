/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;
import modelo.enums.EstadoPartida;

/**
 *
 * @author jeffm
 */
public class Partida {
    private int            idPartida;
    private Usuario        usuario;
    private Sucursal       sucursal;
    private LocalDateTime  fechaInicio;
    private LocalDateTime  fechaFin;
    private EstadoPartida  estado;
    private int            puntajeTotal;
    private int            nivelMaximo;
    private int            pedidosCompletados;
    private int            pedidosCancelados;
    private int            pedidosNoEntregados;

    public Partida() {
        this.estado      = EstadoPartida.EN_CURSO;
        this.nivelMaximo = 1;
    }

    public int           getIdPartida()            { return idPartida; }
    public Usuario       getUsuario()               { return usuario; }
    public Sucursal      getSucursal()              { return sucursal; }
    public LocalDateTime getFechaInicio()           { return fechaInicio; }
    public LocalDateTime getFechaFin()              { return fechaFin; }
    public EstadoPartida getEstado()                { return estado; }
    public int           getPuntajeTotal()          { return puntajeTotal; }
    public int           getNivelMaximo()           { return nivelMaximo; }
    public int           getPedidosCompletados()    { return pedidosCompletados; }
    public int           getPedidosCancelados()     { return pedidosCancelados; }
    public int           getPedidosNoEntregados()   { return pedidosNoEntregados; }

    public void setIdPartida(int i)              { this.idPartida            = i; }
    public void setUsuario(Usuario u)            { this.usuario              = u; }
    public void setSucursal(Sucursal s)          { this.sucursal             = s; }
    public void setFechaInicio(LocalDateTime f)  { this.fechaInicio          = f; }
    public void setFechaFin(LocalDateTime f)     { this.fechaFin             = f; }
    public void setEstado(EstadoPartida e)       { this.estado               = e; }
    public void setPuntajeTotal(int p)           { this.puntajeTotal         = p; }
    public void setNivelMaximo(int n)            { this.nivelMaximo          = n; }
    public void setPedidosCompletados(int p)     { this.pedidosCompletados   = p; }
    public void setPedidosCancelados(int p)      { this.pedidosCancelados    = p; }
    public void setPedidosNoEntregados(int p)    { this.pedidosNoEntregados  = p; }

    public void sumarPuntos(int puntos)          { this.puntajeTotal        += puntos; }
    public void incrementarCompletados()         { this.pedidosCompletados++; }
    public void incrementarCancelados()          { this.pedidosCancelados++; }
    public void incrementarNoEntregados()        { this.pedidosNoEntregados++; }

}
