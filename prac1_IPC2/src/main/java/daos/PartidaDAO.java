/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.Partida;
import modelo.enums.EstadoPartida;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class PartidaDAO {
    private final UsuarioDAO  usuarioDAO  = new UsuarioDAO();
    private final SucursalDAO sucursalDAO = new SucursalDAO();
    
    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public PartidaDAO(){
            conn = connMySQL.conectar();
    }
    
    public Partida obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM partida WHERE id_partida = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

  
    public List<Partida> obtenerTodas() throws SQLException {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT * FROM partida ORDER BY fecha_inicio DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Partida> obtenerFinalizadasPorSucursal(int idSucursal) throws SQLException {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT * FROM partida WHERE id_sucursal = ? AND estado = 'FINALIZADA' ORDER BY puntaje_total DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
    
    public List<Partida> obtenerPorUsuario(int idUsuario) throws SQLException {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT * FROM partida WHERE id_usuario = ? ORDER BY fecha_inicio DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Partida> obtenerFinalizadas() throws SQLException {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT * FROM partida WHERE estado = 'FINALIZADA' ORDER BY puntaje_total DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }
   
    public boolean ingresar(Partida p) throws SQLException {
        String sql = "INSERT INTO partida (id_usuario, id_sucursal, fecha_inicio, estado, puntaje_total, nivel_maximo, pedidos_completados, pedidos_cancelados, pedidos_no_entregados) VALUES (?,?,NOW(),'EN_CURSO',0,1,0,0,0)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getUsuario().getIdUsuario());
            ps.setInt(2, p.getSucursal().getIdSucursal());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) p.setIdPartida(keys.getInt(1));
            }
            return rows > 0;
        }
    }
    
    public boolean actualizar(Partida p) throws SQLException {
        String sql = "UPDATE partida SET estado=?, puntaje_total=?, nivel_maximo=?, " +
                     "pedidos_completados=?, pedidos_cancelados=?, pedidos_no_entregados=?, fecha_fin=NOW() " +
                     "WHERE id_partida=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getEstado().name());
            ps.setInt(2, p.getPuntajeTotal());
            ps.setInt(3, p.getNivelMaximo());
            ps.setInt(4, p.getPedidosCompletados());
            ps.setInt(5, p.getPedidosCancelados());
            ps.setInt(6, p.getPedidosNoEntregados());
            ps.setInt(7, p.getIdPartida());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean elminar(int id) throws SQLException {
        String sql = "UPDATE partida SET estado = 'ABANDONADA' WHERE id_partida = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Partida mapear(ResultSet rs) throws SQLException {
        Partida p = new Partida();
        p.setIdPartida(rs.getInt("id_partida"));
        p.setUsuario(usuarioDAO.encontrarPorId(rs.getInt("id_usuario")));
        p.setSucursal(sucursalDAO.obtenerSucursal(rs.getInt("id_sucursal")));
        p.setEstado(EstadoPartida.fromString(rs.getString("estado")));
        p.setPuntajeTotal(rs.getInt("puntaje_total"));
        p.setNivelMaximo(rs.getInt("nivel_maximo"));
        p.setPedidosCompletados(rs.getInt("pedidos_completados"));
        p.setPedidosCancelados(rs.getInt("pedidos_cancelados"));
        p.setPedidosNoEntregados(rs.getInt("pedidos_no_entregados"));
        Timestamp ts = rs.getTimestamp("fecha_inicio");
        if (ts != null) p.setFechaInicio(ts.toLocalDateTime());
        return p;
    }
}
