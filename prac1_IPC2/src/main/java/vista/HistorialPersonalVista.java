/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.LoginControlador;
import daos.PartidaDAO;
import daos.PedidoDAO;
import modelo.Partida;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import modelo.Pedido;

/**
 *
 * @author jeffm
 */
public class HistorialPersonalVista extends JFrame{
    
    private JTable tablaPartidas, tablaPedidos;
    private DefaultTableModel modeloPartidas, modeloPedidos;
    private JLabel lblTotalPartidas, lblMejorPuntaje, lblTotalPuntos;
    private JLabel lblDetallePartida;
    private List<Partida> partidas;
    
    public HistorialPersonalVista() {
        initComponents();
        cargarHistorial();
        setVisible(true);
    }

    private void initComponents() {
        Usuario sesion = LoginControlador.getSesionActual();
        setTitle("Mi Historial - " + sesion.getNombreCompleto());
        setSize(950, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Mi Historial de Partidas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(241, 196, 15));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

       // Resumen 
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 8));
        panelResumen.setBackground(new Color(52, 73, 94));
        panelResumen.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185)));

        lblTotalPartidas = crearLabelResumen("Partidas jugadas: 0");
        lblMejorPuntaje  = crearLabelResumen("Mejor puntaje: 0");
        lblTotalPuntos   = crearLabelResumen("Puntaje acumulado: 0");

        panelResumen.add(lblTotalPartidas);
        panelResumen.add(lblMejorPuntaje);
        panelResumen.add(lblTotalPuntos);

        // Tablas
        String[] columnasPartidas = {"#", "Fecha", "Sucursal", "Puntaje", "Nivel Max",
                             "Completados", "Cancelados", "No Entregados", "Estado"};
        modeloPartidas = new DefaultTableModel(columnasPartidas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPartidas = new JTable(modeloPartidas);
        estilizarTabla(tablaPartidas);
        
        tablaPartidas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarPedidosDePartida();
        });
        
        lblDetallePartida = new JLabel("Selecciona una partida para ver sus pedidos", SwingConstants.CENTER);
        lblDetallePartida.setFont(new Font("Arial", Font.ITALIC, 12));
        lblDetallePartida.setForeground(new Color(189, 195, 199));
        
        String[] colsPedidos = {"#Pedido", "Productos", "Estado", "Tiempo Usado", "Puntos", "Nivel"};
        modeloPedidos = new DefaultTableModel(colsPedidos, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPedidos = new JTable(modeloPedidos);
        estilizarTabla(tablaPedidos);
        
        //Panel para detalle de pedidos
        JPanel panelDetalle = new JPanel(new BorderLayout(0, 4));
        panelDetalle.setBackground(new Color(44, 62, 80));
        panelDetalle.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185)),
            "Detalle de pedidos", 0, 0, new Font("Arial", Font.BOLD, 12), Color.WHITE));
        panelDetalle.add(lblDetallePartida, BorderLayout.NORTH);
        JScrollPane scrollPedidos = new JScrollPane(tablaPedidos);
        scrollPedidos.setPreferredSize(new Dimension(0, 170));
        panelDetalle.add(scrollPedidos, BorderLayout.CENTER);
        
        //panel central
        JPanel panelCentro = new JPanel(new BorderLayout(0, 5));
        panelCentro.setBackground(new Color(44, 62, 80));        
        panelCentro.add(panelResumen, BorderLayout.NORTH);
        
        JScrollPane scrollPartidas = new JScrollPane(tablaPartidas);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPartidas, panelDetalle);
        split.setResizeWeight(0.55);
        split.setDividerSize(5);
        split.setBackground(new Color(44, 62, 80));
        panelCentro.add(split, BorderLayout.CENTER);

        panel.add(panelCentro, BorderLayout.CENTER);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(127, 140, 141));
        btnCerrar.setForeground(Color.black);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(new Color(44, 62, 80));
        panelSur.add(btnCerrar);
        panel.add(panelSur, BorderLayout.SOUTH);

        add(panel);
    }

    private void cargarHistorial() {
        try {
            PartidaDAO dao     = new PartidaDAO();
            int idUsuario      = LoginControlador.getSesionActual().getIdUsuario();
            partidas = dao.obtenerPorUsuario(idUsuario);

            modeloPartidas.setRowCount(0);
            int mejorPuntaje = 0;
            int sumaPuntaje  = 0;
            int fila         = 1;

            for (Partida p : partidas) {
                String fecha = p.getFechaInicio() != null
                    ? p.getFechaInicio().toString().replace("T", " ").substring(0, 16)
                    : "—";

                modeloPartidas.addRow(new Object[]{
                    fila++,
                    fecha,
                    p.getSucursal() != null ? p.getSucursal().getNombre() : "—",
                    p.getPuntajeTotal(),
                    p.getNivelMaximo(),
                    p.getPedidosCompletados(),
                    p.getPedidosCancelados(),
                    p.getPedidosNoEntregados(),
                    p.getEstado().name()
                });

                if (p.getPuntajeTotal() > mejorPuntaje) mejorPuntaje = p.getPuntajeTotal();
                sumaPuntaje += p.getPuntajeTotal();
            }

            lblTotalPartidas.setText("Partidas jugadas: " + partidas.size());
            lblMejorPuntaje.setText("Mejor puntaje: " + mejorPuntaje);
            lblTotalPuntos.setText("Puntaje acumulado: " + sumaPuntaje);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
        }
    }
    
    private void cargarPedidosDePartida() {
        int fila = tablaPartidas.getSelectedRow();
        if (fila < 0 || fila >= partidas.size()) return;
        Partida p = partidas.get(fila);

        try {
            PedidoDAO pedidoDAO  = new PedidoDAO();
            List<Pedido> pedidos = pedidoDAO.obtenerPorPartida(p.getIdPartida());

            modeloPedidos.setRowCount(0);
            for (Pedido ped : pedidos) {
                modeloPedidos.addRow(new Object[]{
                    "#" + ped.getNumeroPedido(),
                    ped.getResumenProducto(),
                    ped.getEstado().name(),
                    ped.getTiempoUsado() > 0 ? ped.getTiempoUsado() + "s" : "—",
                    ped.getPuntosObtenidos() != 0 ? ped.getPuntosObtenidos() : "—",
                    ped.getNivelAlCrear()
                });
            }
            String info = pedidos.isEmpty()
                ? "Esta partida no tiene pedidos registrados"
                : "Partida #" + p.getIdPartida() + "  |  " + pedidos.size() + " pedidos  |  Puntaje: " + p.getPuntajeTotal();
            lblDetallePartida.setText(info);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar pedidos: " + e.getMessage());
        }
    }

    private JLabel crearLabelResumen(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(Color.black);
        return lbl;
    }
    
    private void estilizarTabla(JTable t) {
        t.setBackground(new Color(52, 73, 94));
        t.setForeground(Color.WHITE);
        t.setGridColor(new Color(44, 62, 80));
        t.setRowHeight(24);
        t.getTableHeader().setBackground(new Color(41, 128, 185));
        t.getTableHeader().setForeground(Color.black);
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.setSelectionBackground(new Color(41, 128, 185));
    }
}
