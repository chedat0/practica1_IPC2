/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.Producto;
import modelo.ProductoSucursal;
import modelo.Sucursal;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class ProductoSucursalDAO {
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final SucursalDAO sucursalDAO = new SucursalDAO();
    
    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public ProductoSucursalDAO(){
            conn = connMySQL.conectar();
    }
    
    public ProductoSucursal obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM producto_sucursal WHERE id_producto_sucursal = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }
   
    public List<ProductoSucursal> obtenerTodas() throws SQLException {
        List<ProductoSucursal> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto_sucursal";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<ProductoSucursal> obtenerPorSucursal(int idSucursal) throws SQLException {
        List<ProductoSucursal> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto_sucursal WHERE id_sucursal = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
   
    public boolean ingresar(ProductoSucursal ps2) throws SQLException {
        String sql = "INSERT INTO producto_sucursal (id_producto, id_sucursal, disponible) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ps2.getProducto().getIdProducto());
            ps.setInt(2, ps2.getSucursal().getIdSucursal());
            ps.setBoolean(3, ps2.isDisponible());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(ProductoSucursal ps2) throws SQLException {
        String sql = "UPDATE producto_sucursal SET disponible=? WHERE id_producto_sucursal=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, ps2.isDisponible());
            ps.setInt(2, ps2.getIdProductoSucursal());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean toggleDisponible(int idProducto, int idSucursal, boolean disponible) throws SQLException {
        String sql = "UPDATE producto_sucursal SET disponible=? WHERE id_producto=? AND id_sucursal=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, disponible);
            ps.setInt(2, idProducto);
            ps.setInt(3, idSucursal);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM producto_sucursal WHERE id_producto_sucursal = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private ProductoSucursal mapear(ResultSet rs) throws SQLException {
        ProductoSucursal ps2 = new ProductoSucursal();
        ps2.setIdProductoSucursal(rs.getInt("id_producto_sucursal"));
        ps2.setProducto(productoDAO.obtenerPorID(rs.getInt("id_producto")));
        ps2.setSucursal(sucursalDAO.obtenerSucursal(rs.getInt("id_sucursal")));
        ps2.setDisponible(rs.getBoolean("disponible"));
        return ps2;
    }
}
