/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.Pedido;
import modelo.enums.EstadoPedido;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class PedidoDAO {
    private final DetallePedidoDAO detalleDAO = new DetallePedidoDAO();
    
    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public PedidoDAO(){
            conn = connMySQL.conectar();
    }
    
    public Pedido obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pedidos WHERE id_pedido = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Pedido p = mapear(rs);
                p.setDetalles(detalleDAO.obtenerPorPedido(p.getIdPedido()));
                return p;
            }
        }
        return null;
    }

    public List<Pedido> obtenerTodos() throws SQLException { return new ArrayList<>(); }

    public List<Pedido> obtenerPorPartida(int idPartida) throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE id_partida = ? ORDER BY numero_pedido";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPartida);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pedido p = mapear(rs);
                p.setDetalles(detalleDAO.obtenerPorPedido(p.getIdPedido()));
                lista.add(p);
            }
        }
        return lista;
    }
    
    public boolean ingresar(Pedido p) throws SQLException {
        String sql = "INSERT INTO pedidos (id_partida, numero_pedido, nivel_al_crear, tiempo_limite_seg, fecha_creacion, estado, puntos_obtenidos) VALUES (?,?,?,?,NOW(),'RECIBIDA',0)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getIdPartida());
            ps.setInt(2, p.getNumeroPedido());
            ps.setInt(3, p.getNivelAlCrear());
            ps.setInt(4, p.getTiempoLimiteDeg());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) p.setIdPedido(keys.getInt(1));
            }
            return rows > 0;
        }
    }
   
    public boolean actualizar(Pedido p) throws SQLException {
        String sql = "UPDATE pedidos SET estado=?, tiempo_usado_seg=?, puntos_obtenidos=?, fecha_finalizacion=? WHERE id_pedido=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getEstado().name());
            ps.setInt(2, p.getTiempoUsado());
            ps.setInt(3, p.getPuntosObtenidos());
            if (p.getEstado().esFinalizado()) ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            else                              ps.setNull(4, Types.TIMESTAMP);
            ps.setInt(5, p.getIdPedido());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstado(int idPedido, EstadoPedido estado) throws SQLException {
        String sql = "UPDATE pedidos SET estado=? WHERE id_pedido=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setInt(2, idPedido);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean eliminar(int id) throws SQLException { return false; }

    private Pedido mapear(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("id_pedido"));
        p.setIdPartida(rs.getInt("id_partida"));
        p.setNumeroPedido(rs.getInt("numero_pedido"));
        p.setNivelAlCrear(rs.getInt("nivel_al_crear"));
        p.setTiempoLimiteDeg(rs.getInt("tiempo_limite_seg"));
        p.setEstado(EstadoPedido.fromString(rs.getString("estado")));
        p.setTiempoUsado(rs.getInt("tiempo_usado_seg"));
        p.setPuntosObtenidos(rs.getInt("puntos_obtenidos"));
        return p;
    }
}
