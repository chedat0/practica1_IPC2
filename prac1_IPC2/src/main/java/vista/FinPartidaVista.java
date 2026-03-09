/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Partida;

import javax.swing.*;
import java.awt.*;
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
        setSize(420, 380);
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

        // Botones
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBtns.setBackground(new Color(44, 62, 80));

        JButton btnMenu = new JButton("Menu Principal");
        btnMenu.setBackground(new Color(41, 128, 185));
        btnMenu.setForeground(Color.WHITE);
        btnMenu.setFocusPainted(false);
        btnMenu.addActionListener(e -> {
            dispose();
            menuParent.setVisible(true);
        });

        JButton btnRanking = new JButton("Ver Ranking");
        btnRanking.setBackground(new Color(142, 68, 173));
        btnRanking.setForeground(Color.WHITE);
        btnRanking.setFocusPainted(false);
        btnRanking.addActionListener(e -> new RankingVista());

        panelBtns.add(btnMenu);
        panelBtns.add(btnRanking);
        panel.add(panelBtns, BorderLayout.SOUTH);

        add(panel);
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
