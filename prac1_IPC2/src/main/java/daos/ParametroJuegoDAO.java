/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.ParametroJuego;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class ParametroJuegoDAO {
    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public ParametroJuegoDAO(){
            conn = connMySQL.conectar();
    }
    
    public ParametroJuego obtenerPorId (int id) throws SQLException{
        String sql = "SELECT * FROM parametros_juego WHERE id_parametro = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }
    
    public ParametroJuego obtenerPorClave(String clave) throws SQLException {
        String sql = "SELECT * FROM parametros_juego WHERE clave = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clave);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public int getValorInt(String clave, int defecto) {
        try {
            ParametroJuego p = obtenerPorClave(clave);
            return (p != null) ? p.getValorInt() : defecto;
        } catch (SQLException e) {
            return defecto;
        }
    }

    public List<ParametroJuego> obtenerTodos() throws SQLException {
        List<ParametroJuego> lista = new ArrayList<>();
        String sql = "SELECT * FROM parametros_juego ORDER BY clave";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
    
    public boolean ingresar(ParametroJuego p) throws SQLException {
        String sql = "INSERT INTO parametros_juego (clave, valor, descripcion) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getClave());
            ps.setString(2, p.getValor());
            ps.setString(3, p.getDescripcion());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(ParametroJuego p) throws SQLException {
        String sql = "UPDATE parametros_juego SET valor=?, descripcion=? WHERE id_parametro=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getValor());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getIdParametro());
            return ps.executeUpdate() > 0;
        }
    }
   
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM parametros_juego WHERE id_parametro = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private ParametroJuego mapear(ResultSet rs) throws SQLException {
        return new ParametroJuego(
            rs.getInt("id_parametro"),
            rs.getString("clave"),
            rs.getString("valor"),
            rs.getString("descripcion")
        );
    }
}
