/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jeffm
 */
public class ProductoSucursal {
    private int idProductoSucursal;
    private Producto producto;
    private Sucursal sucursal;
    private boolean disponible;
    
    public ProductoSucursal (){        
    }

    public ProductoSucursal(int idProductoSucursal, Producto producto, Sucursal sucursal, boolean disponible) {
        this.idProductoSucursal = idProductoSucursal;
        this.producto = producto;
        this.sucursal = sucursal;
        this.disponible = disponible;
    }

    public int getIdProductoSucursal() {
        return idProductoSucursal;
    }

    public Producto getProducto() {
        return producto;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setIdProductoSucursal(int idProductoSucursal) {
        this.idProductoSucursal = idProductoSucursal;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    
}
