/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.Sucursal;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class SucursalDAO {
    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public SucursalDAO() {
        conn = connMySQL.conectar();

    }
     
    public Sucursal obtenerSucursal(int id) throws SQLException {
        String sql = "SELECT * FROM sucursales WHERE id_sucursal = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }
    
    public List<Sucursal> obtenerTodas() throws SQLException {
        List<Sucursal> lista = new ArrayList<>();
        String sql = "SELECT * FROM sucursales WHERE activa = TRUE ORDER BY nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
    
    public boolean ingresar(Sucursal s) throws SQLException {
        String sql = "INSERT INTO sucursales (nombre, direccion, telefono, activa) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDirección());            
            ps.setBoolean(4, s.isActiva());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(Sucursal s) throws SQLException {
        String sql = "UPDATE sucursales SET nombre=?, direccion=?, telefono=?, activa=? WHERE id_sucursal=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDirección());
            ps.setBoolean(4, s.isActiva());
            ps.setInt(5, s.getIdSucursal());
            return ps.executeUpdate() > 0;
        }
    }
   
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE sucursales SET activa = FALSE WHERE id_sucursal = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Sucursal mapear(ResultSet rs) throws SQLException {
        return new Sucursal(
            rs.getInt("id_sucursal"),
            rs.getString("nombre"),
            rs.getString("direccion"),          
            rs.getBoolean("activa")
        );
    }
}
