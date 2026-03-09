/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.AdminControlador;
import controlador.LoginControlador;
import modelo.Partida;
import modelo.Usuario;
import modelo.Constantes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class ReportesVista extends JFrame {
    
    private final AdminControlador controller = new AdminControlador();
    private JTable            tabla;
    private DefaultTableModel modeloTabla;
    private List<Partida>     partidas;
    private JLabel            lblTotal, lblPuntajeMax, lblPromedio;

    public ReportesVista() {
        initComponents();
        cargarDatos();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Estadisticas del juego y generación de Reportes");
        setSize(820, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Resumen
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        panelResumen.setBackground(new Color(52, 73, 94));
        panelResumen.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185)));
        lblTotal       = crearLabelResumen("Partidas: 0");
        lblPuntajeMax  = crearLabelResumen("Puntaje max: 0");
        lblPromedio    = crearLabelResumen("Promedio: 0");
        panelResumen.add(lblTotal);
        panelResumen.add(lblPuntajeMax);
        panelResumen.add(lblPromedio);
        panel.add(panelResumen, BorderLayout.NORTH);

        //  Tabla
        String[] cols = {"ID", "Jugador", "Sucursal", "Puntaje", "Nivel", "Completados", "Cancelados", "No Entregados"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setBackground(new Color(52, 73, 94));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(44, 62, 80));
        tabla.setRowHeight(24);
        tabla.getTableHeader().setBackground(new Color(41, 128, 185));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setSelectionBackground(new Color(41, 128, 185));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtns.setBackground(new Color(44, 62, 80));

        JButton btnExportar = new JButton("Exportar CSV");
        btnExportar.setBackground(new Color(39, 174, 96));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.addActionListener(e -> exportarCSV());
        panelBtns.add(btnExportar);

        panel.add(panelBtns, BorderLayout.SOUTH);
        add(panel);
    }

    private void cargarDatos() {
        try {
            Usuario sesion = LoginControlador.getSesionActual();
            if (sesion != null && Constantes.ROL_SUPER_ADMIN.equals(sesion.getRol().getNombre())) {
                partidas = controller.obtenerPartidasSucursal(0);
                // Super admin: todas
                partidas = new daos.PartidaDAO().obtenerFinalizadas();
            } else if (sesion != null && sesion.getSucursal() != null) {
                partidas = controller.obtenerPartidasSucursal(sesion.getSucursal().getIdSucursal());
            } else {
                partidas = new java.util.ArrayList<>();
            }

            modeloTabla.setRowCount(0);
            int maxPuntaje = 0;
            long sumPuntaje = 0;

            for (Partida p : partidas) {
                modeloTabla.addRow(new Object[]{
                    p.getIdPartida(),
                    p.getUsuario() != null ? p.getUsuario().getNombreCompleto() : "N/A",
                    p.getSucursal() != null ? p.getSucursal().getNombre() : "N/A",
                    p.getPuntajeTotal(), p.getNivelMaximo(),
                    p.getPedidosCompletados(), p.getPedidosCancelados(), p.getPedidosNoEntregados()
                });
                if (p.getPuntajeTotal() > maxPuntaje) maxPuntaje = p.getPuntajeTotal();
                sumPuntaje += p.getPuntajeTotal();
            }

            int prom = partidas.isEmpty() ? 0 : (int)(sumPuntaje / partidas.size());
            lblTotal.setText("Partidas: " + partidas.size());
            lblPuntajeMax.setText("Puntaje max: " + maxPuntaje);
            lblPromedio.setText("Promedio: " + prom);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void exportarCSV() {
        if (partidas == null || partidas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay datos para exportar.");
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("reporte_partidas.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                controller.exportarCSV(partidas, fc.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "CSV exportado correctamente.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage());
            }
        }
    }

    private JLabel crearLabelResumen(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }
}
