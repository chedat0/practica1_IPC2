/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jeffm
 */
public class DetallePedido {
    private int idDetalle;
    private int idPedido;
    private Producto producto;
    private int cantidad;

    public DetallePedido() {
    }

    public DetallePedido(int idPedido, Producto producto, int cantidad) {
        this.idPedido = idPedido;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setIdDetalle(int i) {
        this.idDetalle = i;
    }

    public void setIdPedido(int i) {
        this.idPedido = i;
    }

    public void setProducto(Producto p) {
        this.producto = p;
    }

    public void setCantidad(int c) {
        this.cantidad = c;
    }

}
