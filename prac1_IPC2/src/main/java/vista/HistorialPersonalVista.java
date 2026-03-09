/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.LoginControlador;
import daos.PartidaDAO;
import modelo.Partida;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class HistorialPersonalVista extends JFrame{
    
    private JTable            tabla;
    private DefaultTableModel modeloTabla;
    private JLabel            lblTotalPartidas, lblMejorPuntaje, lblTotalPuntos;

    public HistorialPersonalVista() {
        initComponents();
        cargarHistorial();
        setVisible(true);
    }

    private void initComponents() {
        Usuario sesion = LoginControlador.getSesionActual();
        setTitle("Mi Historial - " + sesion.getNombreCompleto());
        setSize(750, 500);
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

        // Tabla
        String[] columnas = {"#", "Fecha", "Sucursal", "Puntaje", "Nivel Max",
                             "Completados", "Cancelados", "No Entregados", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setBackground(new Color(52, 73, 94));
        tabla.setForeground(Color.black);
        tabla.setGridColor(new Color(44, 62, 80));
        tabla.setRowHeight(26);
        tabla.getTableHeader().setBackground(new Color(41, 128, 185));
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setSelectionBackground(new Color(41, 128, 185));

        JPanel panelCentro = new JPanel(new BorderLayout(0, 5));
        panelCentro.setBackground(new Color(44, 62, 80));
        panelCentro.add(panelResumen, BorderLayout.NORTH);
        panelCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);
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
            List<Partida> lista = dao.obtenerPorUsuario(idUsuario);

            modeloTabla.setRowCount(0);
            int mejorPuntaje = 0;
            int sumaPuntaje  = 0;
            int fila         = 1;

            for (Partida p : lista) {
                String fecha = p.getFechaInicio() != null
                    ? p.getFechaInicio().toString().replace("T", " ").substring(0, 16)
                    : "—";

                modeloTabla.addRow(new Object[]{
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

            lblTotalPartidas.setText("Partidas jugadas: " + lista.size());
            lblMejorPuntaje.setText("Mejor puntaje: " + mejorPuntaje);
            lblTotalPuntos.setText("Puntaje acumulado: " + sumaPuntaje);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
        }
    }

    private JLabel crearLabelResumen(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(Color.black);
        return lbl;
    }
}
