/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import modelo.DetallePedido;
import modelo.ConnectionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class DetallePedidoDAO {
    
    private final ProductoDAO productoDAO = new ProductoDAO();
    
    ConnectionMySQL connMySQL = new ConnectionMySQL();
    Connection conn = null;
    
    public DetallePedidoDAO(){
            conn = connMySQL.conectar();
    }
       
    public DetallePedido obtenerDetalle(int id) throws SQLException {
        return null;
    }
   
    public List<DetallePedido> obtenerTodos() throws SQLException { return new ArrayList<>(); }

    public List<DetallePedido> obtenerPorPedido(int idPedido) throws SQLException {
        List<DetallePedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedido WHERE id_pedido = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetallePedido d = new DetallePedido();
                d.setIdDetalle(rs.getInt("id_detalle"));
                d.setIdPedido(rs.getInt("id_pedido"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setProducto(productoDAO.obtenerPorID(rs.getInt("id_producto")));
                lista.add(d);
            }
        }
        return lista;
    }
   
    public boolean ingresar(DetallePedido d) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getIdPedido());
            ps.setInt(2, d.getProducto().getIdProducto());
            ps.setInt(3, d.getCantidad());
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(DetallePedido d) throws SQLException { return false; }
   
    public boolean eliminar(int id) throws SQLException { return false; }

}
