/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.Rol;
import modelo.Sucursal;
import modelo.Usuario;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jeffm
 */
public class UsuarioDAO {
    private final RolDAO      rolDAO      = new RolDAO();
    private final SucursalDAO sucursalDAO = new SucursalDAO();

    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public UsuarioDAO(){
            conn = connMySQL.conectar();
    }
    
    public Usuario encontrarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public Usuario encontrarPorUsuario(String usuario, String contraseña) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND contraseña = ? AND activo = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString (2,contraseña);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public List<Usuario> encontrarPorSucursal(int idSucursal) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE id_sucursal = ? AND activo = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public boolean existeUsuario(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    
    public List<Usuario> encontrarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE activo = TRUE ORDER BY nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
    
    public boolean ingresar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (username, password_hash, nombre, apellido, email, id_rol, id_sucursal, activo) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getContraseña());
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getApellido());
            ps.setString(5, u.getEmail());
            ps.setInt(6, u.getRol().getIdRol());
            if (u.getSucursal() != null) ps.setInt(7, u.getSucursal().getIdSucursal());
            else                          ps.setNull(7, Types.INTEGER);
            ps.setBoolean(8, true);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, email=?, id_rol=?, id_sucursal=?, activo=? WHERE id_usuario=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getEmail());
            ps.setInt(4, u.getRol().getIdRol());
            if (u.getSucursal() != null) ps.setInt(5, u.getSucursal().getIdSucursal());
            else                          ps.setNull(5, Types.INTEGER);
            ps.setBoolean(6, u.isActivo());
            ps.setInt(7, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE usuarios SET activo = FALSE WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setUsuario(rs.getString("username"));
        u.setContraseña(rs.getString("password_hash"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setEmail(rs.getString("email"));
        u.setActivo(rs.getBoolean("activo"));
        u.setRol(rolDAO.encontrarPorId(rs.getInt("id_rol")));
        int idSuc = rs.getInt("id_sucursal");
        if (!rs.wasNull()) u.setSucursal(sucursalDAO.obtenerSucursal(idSuc));
        return u;
    }
}
