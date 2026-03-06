/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.Rol;
import modelo.ConnectionMySQL;        
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class RolDAO {
    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public RolDAO() {
        conn = connMySQL.conectar();
    }
    
    public Rol encontrarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM roles WHERE id_rol = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }
  
    public List<Rol> encontrarTodos() throws SQLException {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT * FROM roles WHERE activo = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Rol encontrarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM roles WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }
 
    public boolean ingresar(Rol rol) throws SQLException {
        String sql = "INSERT INTO roles (nombre, descripcion, activo) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.setBoolean(3, rol.isActivo());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(Rol rol) throws SQLException {
        String sql = "UPDATE roles SET nombre=?, descripcion=?, activo=? WHERE id_rol=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.setBoolean(3, rol.isActivo());
            ps.setInt(4, rol.getIdRol());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE roles SET activo = FALSE WHERE id_rol = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Rol mapear(ResultSet rs) throws SQLException {
        return new Rol(
            rs.getInt("id_rol"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getBoolean("activo")
        );
    }
}
