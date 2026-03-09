/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.NivelPartida;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class NivelPartidaDAO {     
    
    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public NivelPartidaDAO(){
            conn = connMySQL.conectar();
    }
    
    public NivelPartida obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM niveles_partida WHERE id_nivel_partida = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }
    
    public List<NivelPartida> obtenerTodas() throws SQLException {
        return new ArrayList<>();
    }

    public List<NivelPartida> obtenerPorPartida(int idPartida) throws SQLException {
        List<NivelPartida> lista = new ArrayList<>();
        String sql = "SELECT * FROM niveles_partida WHERE id_partida = ? ORDER BY nivel";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPartida);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
   
    public boolean ingresar (NivelPartida n) throws SQLException {
        String sql = "INSERT INTO niveles_partida (id_partida, nivel, fecha_alcanzado, puntaje_por_alcanzar, pedidos_por_alcanzar) VALUES (?,?,NOW(),?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, n.getIdPartida());
            ps.setInt(2, n.getNivel());
            ps.setInt(3, n.getPuntajeAlAlcanzar());
            ps.setInt(4, n.getPedidosAlAlcanzar());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(NivelPartida n) throws SQLException { return false; }
   
    public boolean eliminar(int id) throws SQLException { return false; }

    private NivelPartida mapear(ResultSet rs) throws SQLException {
        NivelPartida n = new NivelPartida();
        n.setIdNivelPartida(rs.getInt("id_nivel_partida"));
        n.setIdPartida(rs.getInt("id_partida"));
        n.setNivel(rs.getInt("nivel"));
        n.setPuntajeAlAlcanzar(rs.getInt("puntaje_por_alcanzar"));
        n.setPedidosAlAlcanzar(rs.getInt("pedidos_por_alcanzar"));
        return n;
    }
}
