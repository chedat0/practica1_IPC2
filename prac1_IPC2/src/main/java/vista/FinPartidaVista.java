/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import daos.PartidaDAO;
import modelo.Partida;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author jeffm
 */
public class FinPartidaVista extends JFrame {
    
    public FinPartidaVista(Partida partida, JFrame menuParent) {
        initComponents(partida, menuParent);
        setVisible(true);
    }

    private void initComponents(Partida partida, JFrame menuParent) {
        setTitle("Fin de Partida");
        setSize(500, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        
        JLabel lblTitulo = new JLabel("Partida Finalizada", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(241, 196, 15));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Estadisticas
        JPanel panelStats = new JPanel(new GridLayout(6, 2, 10, 12));
        panelStats.setBackground(new Color(52, 73, 94));
        panelStats.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        agregarStat(panelStats, "Puntaje Total:", String.valueOf(partida.getPuntajeTotal()));
        agregarStat(panelStats, "Nivel Maximo:", String.valueOf(partida.getNivelMaximo()));
        agregarStat(panelStats, "Pedidos Completados:", String.valueOf(partida.getPedidosCompletados()));
        agregarStat(panelStats, "Pedidos Cancelados:", String.valueOf(partida.getPedidosCancelados()));
        agregarStat(panelStats, "Pedidos No Entregados:", String.valueOf(partida.getPedidosNoEntregados()));
        panel.add(panelStats, BorderLayout.CENTER);

        String posicionTexto = calcularPosicionRanking(partida);
        agregarStat(panelStats, "Posicion en Ranking:", posicionTexto);

        panel.add(panelStats, BorderLayout.CENTER);
        
        // Botones
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBtns.setBackground(new Color(44, 62, 80));

        JButton btnMenu = new JButton("Menu Principal");
        btnMenu.setBackground(new Color(41, 128, 185));
        btnMenu.setForeground(Color.BLACK);
        btnMenu.setFocusPainted(false);
        btnMenu.addActionListener(e -> {
            dispose();
            menuParent.setVisible(true);
        });
        

        panelBtns.add(btnMenu);       
        panel.add(panelBtns, BorderLayout.SOUTH);

        add(panel);
    }

    private String calcularPosicionRanking(Partida partida) {
        try {
            PartidaDAO dao = new PartidaDAO();
            List<Partida> todasFinalizadas;
            if (partida.getSucursal() != null) {
                todasFinalizadas = dao.obtenerFinalizadasPorSucursal(partida.getSucursal().getIdSucursal());
            } else {
                todasFinalizadas = dao.obtenerFinalizadas();
            }

            // Mejor puntaje por jugador
            List<Integer> mejoresPuntajes = todasFinalizadas.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getUsuario() != null ? p.getUsuario().getIdUsuario() : 0,
                    Collectors.maxBy(Comparator.comparingInt(Partida::getPuntajeTotal))
                ))
                .values().stream()
                .filter(opt -> opt.isPresent())
                .map(opt -> opt.get().getPuntajeTotal())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

            int pos = 1;
            for (int puntaje : mejoresPuntajes) {
                if (partida.getPuntajeTotal() >= puntaje) break;
                pos++;
            }
            return "#" + pos + " de " + mejoresPuntajes.size();
        } catch (SQLException e) {
            return "N/A";
        }
    }
    
    private void agregarStat(JPanel panel, String etiqueta, String valor) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(189, 195, 199));

        JLabel val = new JLabel(valor, SwingConstants.CENTER);
        val.setFont(new Font("Arial", Font.BOLD, 14));
        val.setForeground(Color.WHITE);

        panel.add(lbl);
        panel.add(val);
    }
}
