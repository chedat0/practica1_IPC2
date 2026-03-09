/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import daos.*;
import modelo.*;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author jeffm
 */
public class AdminControlador {
    private final ProductoDAO        productoDAO        = new ProductoDAO();
    private final ProductoSucursalDAO psDAO             = new ProductoSucursalDAO();
    private final PartidaDAO         partidaDAO         = new PartidaDAO();

    
    //Manejar productos en cada sucursal
    public List<Producto> obtenerTodosProductos() throws SQLException {
        return productoDAO.obtenerTodos();
    }

    public List<Producto> obtenerProductosSucursal(int idSucursal) throws SQLException {
        return productoDAO.obtenerDisponiblesPorSucursal(idSucursal);
    }

    public boolean crearProducto(String nombre, String descripcion, double precio,
                                   String categoria, int idSucursal) throws SQLException {
        if (nombre == null || nombre.trim().isEmpty()) return false;
        Producto p = new Producto();
        p.setNombre(nombre.trim());
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setCategoria(categoria);
        p.setActivo(true);
        boolean ok = productoDAO.ingresar(p);
        // Se asocia a la sucursal actual
        if (ok) {
            List<Producto> todos = productoDAO.obtenerActivos();
            Producto nuevo = todos.stream()
                .filter(x -> x.getNombre().equals(nombre.trim()))
                .findFirst().orElse(null);
            if (nuevo != null) {
                ProductoSucursal ps = new ProductoSucursal();
                ps.setProducto(nuevo);
                Sucursal s = new Sucursal(); s.setIdSucursal(idSucursal);
                ps.setSucursal(s);
                ps.setDisponible(true);
                psDAO.ingresar(ps);
            }
        }
        return ok;
    }

    public boolean actualizarProducto(Producto p) throws SQLException {
        return productoDAO.actualizar(p);
    }

    public boolean desactivarProducto(int id) throws SQLException {
        return productoDAO.eliminar(id);
    }

    public boolean toggleDisponibilidad(int idProducto, int idSucursal, boolean disponible) throws SQLException {
        return psDAO.toggleDisponible(idProducto, idSucursal, disponible);
    }

    // Muestra las estadiscticas de cada sucursal
    public List<Partida> obtenerPartidasSucursal(int idSucursal) throws SQLException {
        return partidaDAO.obtenerFinalizadasPorSucursal(idSucursal);
    }

    public void exportarCSV(List<Partida> partidas, String rutaArchivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            pw.println("ID,Jugador,Sucursal,Puntaje,Nivel,Completados,Cancelados,NoEntregados");
            for (Partida p : partidas) {
                pw.printf("%d,%s,%s,%d,%d,%d,%d,%d%n",
                    p.getIdPartida(),
                    p.getUsuario() != null ? p.getUsuario().getNombreCompleto() : "",
                    p.getSucursal() != null ? p.getSucursal().getNombre() : "",
                    p.getPuntajeTotal(),
                    p.getNivelMaximo(),
                    p.getPedidosCompletados(),
                    p.getPedidosCancelados(),
                    p.getPedidosNoEntregados()
                );
            }
        }
    }
}
