/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;
import java.util.List;
import modelo.enums.EstadoPedido;
import java.util.ArrayList;

/**
 *
 * @author jeffm
 */
public class Pedido {
    private int idPedido;
    private int idPartida;
    private int numeroPedido;
    private int nivelAlCrear;
    private int tiempoLimiteDeg;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaFinalizacion;
    private EstadoPedido estado; 
    private int tiempoUsado;
    private int puntosObtenidos;
    private List<DetallePedido> detalles;
    private int tiempoRestante;
    
    
    public Pedido(){
        this.estado = EstadoPedido.RECIBIDA;
        this.detalles = new ArrayList<>();
    }

    public Pedido(int idPedido, int idPartida, int numeroPedido, int nivelAlCrear, int tiempoLimiteDeg, LocalDateTime fechaCreacion, LocalDateTime fechaFinalizacion, EstadoPedido estado, int puntosObtenidos, List<DetallePedido> detalles) {
        this.idPedido = idPedido;
        this.idPartida = idPartida;
        this.numeroPedido = numeroPedido;
        this.nivelAlCrear = nivelAlCrear;
        this.tiempoLimiteDeg = tiempoLimiteDeg;
        this.fechaCreacion = fechaCreacion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.estado = estado;
        this.puntosObtenidos = puntosObtenidos;
        this.detalles = detalles;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public int getNivelAlCrear() {
        return nivelAlCrear;
    }

    public int getTiempoLimiteDeg() {
        return tiempoLimiteDeg;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public EstadoPedido getEstado() {
        return estado;
    }
    
    public int getTiempoUsado() {
        return tiempoUsado;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public int getPuntosObtenidos() {
        return puntosObtenidos;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public void setNivelAlCrear(int nivelAlCrear) {
        this.nivelAlCrear = nivelAlCrear;
    }

    public void setTiempoLimiteDeg(int tiempoLimiteDeg) {
        this.tiempoLimiteDeg = tiempoLimiteDeg;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setPuntosObtenidos(int puntosObtenidos) {
        this.puntosObtenidos = puntosObtenidos;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public void setTiempoUsado(int tiempoUsado) {
        this.tiempoUsado = tiempoUsado;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }
    
    public void disminuirTiempo(){
        if (tiempoRestante > 0) tiempoRestante--;
    }
    
    public boolean tiempoAgotado (){
        return tiempoRestante <= 0;       
    }
    
    public String getResumenProducto(){
        if (detalles == null || detalles.isEmpty()) return "No hay productos";
        StringBuilder sb = new StringBuilder();
        for (DetallePedido detalle : detalles) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(detalle.getCantidad()).append("x ").append(detalle.getProducto().getNombre());            
        }
        return sb.toString();
    }
}
