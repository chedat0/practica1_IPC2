/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.HistorialEstadoPedido;
import modelo.enums.EstadoPedido;
import modelo.enums.Origen;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class HistorialEstadoPedidoDAO {

    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public HistorialEstadoPedidoDAO(){
            conn = connMySQL.conectar();
    }
    
    public HistorialEstadoPedido obtenerPorId(int id) throws SQLException { return null; }
    
    public List<HistorialEstadoPedido> obenerTodos() throws SQLException { return new ArrayList<>(); }

    public List<HistorialEstadoPedido> obtenerPorPedido(int idPedido) throws SQLException {
        List<HistorialEstadoPedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_estados_pedido WHERE id_pedido = ? ORDER BY fecha_cambio";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HistorialEstadoPedido h = new HistorialEstadoPedido();
                h.setIdHistorial(rs.getInt("id_historial"));
                h.setIdPedido(rs.getInt("id_pedido"));
                String ant = rs.getString("estado_anterior");
                if (ant != null) h.setEstadoAnterior(EstadoPedido.fromString(ant));
                h.setEstadoNuevo(EstadoPedido.fromString(rs.getString("estado_nuevo")));
                h.setOrigen(Origen.fromString(rs.getString("origen")));
                lista.add(h);
            }
        }
        return lista;
    }
  
    public boolean ingresar(HistorialEstadoPedido h) throws SQLException {
        String sql = "INSERT INTO historial_estados_pedido (id_pedido, estado_anterior, estado_nuevo, fecha_cambio, origen) VALUES (?,?,?,NOW(),?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, h.getIdPedido());
            if (h.getEstadoAnterior() != null) ps.setString(2, h.getEstadoAnterior().name());
            else                               ps.setNull(2, Types.VARCHAR);
            ps.setString(3, h.getEstadoNuevo().name());
            ps.setString(4, h.getOrigen().name());
            return ps.executeUpdate() > 0;
        }
    }
   
    public boolean update(HistorialEstadoPedido h) throws SQLException { return false; }
    
    public boolean delete(int id) throws SQLException { return false; }

}
