/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.LoginControlador;
import daos.PartidaDAO;
import modelo.Partida;
import modelo.Usuario;
import modelo.Constantes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author jeffm
 */
public class RankingVista extends JFrame{
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public RankingVista() {
        initComponents();
        cargarDatos();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Ranking de Jugadores");
        setSize(700, 480);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Ranking de Jugadores", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(241, 196, 15));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"#", "Jugador", "Sucursal", "Mejor Puntaje", "Partidas", "Nivel Max"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setBackground(new Color(52, 73, 94));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(44, 62, 80));
        tabla.setRowHeight(26);
        tabla.getTableHeader().setBackground(new Color(41, 128, 185));
        tabla.getTableHeader().setForeground(Color.black);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.setSelectionBackground(new Color(41, 128, 185));

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panel);
    }

    private void cargarDatos() {
        try {
            PartidaDAO dao = new PartidaDAO();
            List<Partida> partidas;
            Usuario sesion = LoginControlador.getSesionActual();

            // Admin de tienda
            if (sesion != null && Constantes.ROL_ADMIN_TIENDA.equals(sesion.getRol().getNombre())
                    && sesion.getSucursal() != null) {
                partidas = dao.obtenerFinalizadasPorSucursal(sesion.getSucursal().getIdSucursal());
            } else if (sesion != null && Constantes.ROL_JUGADOR.equals(sesion.getRol().getNombre())
                    && sesion.getSucursal() != null) {
                partidas = dao.obtenerFinalizadasPorSucursal(sesion.getSucursal().getIdSucursal());
            } else {
                partidas = dao.obtenerFinalizadas();
            }

            // Agrupar por usuario por mejor punteo
            List<Partida> rankingList = partidas.stream()
                    .collect(Collectors.groupingBy(
                            p -> (p.getUsuario() != null ? p.getUsuario().getIdUsuario() : 0),
                            Collectors.toList()
                    ))
                    .values().stream()
                    .map(lista -> lista.stream()
                    .max(Comparator.comparingInt(Partida::getPuntajeTotal))
                    .orElse(null))
                    .filter(p -> p != null)
                    .sorted(Comparator.comparingInt(Partida::getPuntajeTotal).reversed())
                    .collect(Collectors.toList());

            modeloTabla.setRowCount(0);
            int pos = 1;
            for (Partida p : rankingList) {               
                long totalPartidas = partidas.stream()
                        .filter(x -> x.getUsuario() != null && p.getUsuario() != null
                        && x.getUsuario().getIdUsuario() == p.getUsuario().getIdUsuario())
                        .count();

                modeloTabla.addRow(new Object[]{
                    pos++,
                    p.getUsuario() != null ? p.getUsuario().getNombreCompleto() : "N/A",
                    p.getSucursal() != null ? p.getSucursal().getNombre() : "N/A",
                    p.getPuntajeTotal(),
                    totalPartidas,
                    p.getNivelMaximo()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar ranking: " + e.getMessage());
        }
    }
}
