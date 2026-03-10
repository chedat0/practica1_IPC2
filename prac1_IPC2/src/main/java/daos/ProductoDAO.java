/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.Producto;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class ProductoDAO {
    
    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public ProductoDAO(){
            conn = connMySQL.conectar();
    }
    
    public Producto obtenerPorID(int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Producto> obtenerActivos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE activo = TRUE ORDER BY nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Producto> obtenerDisponiblesPorSucursal(int idSucursal) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.* FROM productos p " +
                     "INNER JOIN producto_sucursal ps ON p.id_producto = ps.id_producto " +
                     "WHERE ps.id_sucursal = ? AND ps.disponible = TRUE AND p.activo = TRUE " +
                     "ORDER BY p.nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
    
    public boolean ingresar(Producto p) throws SQLException {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, categoria, imagen, activo, stock) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setString(4, p.getCategoria());
            ps.setString(5, p.getImagen());
            ps.setBoolean(6, p.isActivo());
            ps.setInt(7, p.getStock());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(Producto p) throws SQLException {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, categoria=?, imagen=?, activo=?, stock=? WHERE id_producto=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setString(4, p.getCategoria());
            ps.setString(5, p.getImagen());
            ps.setBoolean(6, p.isActivo());
            ps.setInt(7, p.getStock());
            ps.setInt(7, p.getIdProducto());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE productos SET activo = FALSE WHERE id_producto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean decrementarStock(int idProducto, int cantidad) throws SQLException {
        String sql = "UPDATE productos SET stock = GREATEST(0, stock - ?) WHERE id_producto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("id_producto"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getDouble("precio"),
            rs.getString("categoria"),
            rs.getString("imagen"),
            rs.getBoolean("activo"),
            rs.getInt("stock")
        );
    }
}
