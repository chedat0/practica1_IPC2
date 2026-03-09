/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.JuegoControlador;
import controlador.LoginControlador;
import modelo.Pedido;
import modelo.enums.EstadoPedido;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class JuegoVista extends JFrame{
    private final JuegoControlador controller = new JuegoControlador();
    private final JFrame           menuParent;

    private JLabel     lblTurno;
    private JLabel     lblPuntaje;
    private JLabel     lblNivel;
    private JPanel     panelPedidos;
    private JScrollPane scrollPedidos;
    private javax.swing.Timer timerJuego;

    public JuegoVista(JFrame menuParent) {
        this.menuParent = menuParent;
        initComponents();
        iniciarJuego();
    }

    private void initComponents() {
        setTitle("Pizza Express ");
        setSize(900, 620);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(5, 5));
        panelPrincipal.setBackground(new Color(44, 62, 80));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Panel superior:
        JPanel panelHUD = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 6));
        panelHUD.setBackground(new Color(52, 73, 94));
        panelHUD.setBorder(BorderFactory.createLineBorder(new Color(231, 76, 60), 2));

        lblTurno  = crearLabelHUD("Tiempo: 300s", new Color(231, 76, 60));
        lblPuntaje = crearLabelHUD("Puntaje: 0",  new Color(39, 174, 96));
        lblNivel  = crearLabelHUD("Nivel: 1",     new Color(241, 196, 15));

        panelHUD.add(lblTurno);
        panelHUD.add(lblPuntaje);
        panelHUD.add(lblNivel);
        panelPrincipal.add(panelHUD, BorderLayout.NORTH);

        //Panel central: pedidos activos
        panelPedidos = new JPanel();
        panelPedidos.setLayout(new BoxLayout(panelPedidos, BoxLayout.Y_AXIS));
        panelPedidos.setBackground(new Color(44, 62, 80));

        scrollPedidos = new JScrollPane(panelPedidos);
        scrollPedidos.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Pedidos Activos",
            0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        scrollPedidos.getViewport().setBackground(new Color(44, 62, 80));
        panelPrincipal.add(scrollPedidos, BorderLayout.CENTER);

        // Boton finzalizar partida
        JButton btnRendirse = new JButton("Finalizar Partida");
        btnRendirse.setBackground(new Color(192, 57, 43));
        btnRendirse.setForeground(Color.WHITE);
        btnRendirse.setFont(new Font("Arial", Font.BOLD, 13));
        btnRendirse.setFocusPainted(false);
        btnRendirse.addActionListener(e -> confirmarFinalizacion());
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(new Color(44, 62, 80));
        panelSur.add(btnRendirse);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        add(panelPrincipal);
        setVisible(true);
    }

    private void iniciarJuego() {
        try {
            controller.iniciarPartida(LoginControlador.getSesionActual());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al iniciar la partida: " + e.getMessage());
            return;
        }

        timerJuego = new javax.swing.Timer(1000, e -> {
            try {
                controller.tick();
                actualizarHUD();
                actualizarPedidos();
                if (controller.getPartida() != null &&
                    controller.getTurnoRestante() <= 0) {
                    timerJuego.stop();
                    mostrarFinPartida();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        timerJuego.start();
    }

    private void actualizarHUD() {
        if (controller.getPartida() == null) return;
        lblTurno.setText("Tiempo: " + controller.getTurnoRestante() + "s");
        lblPuntaje.setText("Puntaje: " + controller.getPartida().getPuntajeTotal());
        lblNivel.setText("Nivel: " + controller.getNivelActual());
    }

    private void actualizarPedidos() {
        panelPedidos.removeAll();
        List<Pedido> pedidos = controller.getPedidosActivos();
        if (pedidos.isEmpty()) {
            JLabel lblVacio = new JLabel("Esperando pedidos...", SwingConstants.CENTER);
            lblVacio.setForeground(Color.GRAY);
            lblVacio.setFont(new Font("Arial", Font.ITALIC, 14));
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPedidos.add(lblVacio);
        } else {
            for (Pedido p : pedidos) {
                panelPedidos.add(new PedidoPanel(p));
                panelPedidos.add(Box.createVerticalStrut(6));
            }
        }
        panelPedidos.revalidate();
        panelPedidos.repaint();
    }

    private void confirmarFinalizacion() {
        int op = JOptionPane.showConfirmDialog(this,
            "Deseas finalizar la partida ahora?", "Finalizar",
            JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            timerJuego.stop();
            try { controller.finalizarPartida(); } catch (SQLException e) { e.printStackTrace(); }
            mostrarFinPartida();
        }
    }

    private void mostrarFinPartida() {
        dispose();
        new FinPartidaVista(controller.getPartida(), menuParent);
    }

    private JLabel crearLabelHUD(String texto, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(color);
        return lbl;
    }

    // Panel de cada pedido
    private class PedidoPanel extends JPanel {

        private final Pedido pedido;

        PedidoPanel(Pedido pedido) {
            this.pedido = pedido;
            setLayout(new BorderLayout(8, 4));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorEstado(pedido.getEstado()), 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            setBackground(new Color(52, 73, 94));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

            // Info izquierda
            JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 2));
            infoPanel.setOpaque(false);

            JLabel lblNum = new JLabel("Pedido #" + pedido.getNumeroPedido() +
                "  |  Estado: " + pedido.getEstado().name());
            lblNum.setFont(new Font("Arial", Font.BOLD, 13));
            lblNum.setForeground(Color.WHITE);

            JLabel lblProductos = new JLabel(pedido.getResumenProducto());
            lblProductos.setFont(new Font("Arial", Font.PLAIN, 12));
            lblProductos.setForeground(new Color(189, 195, 199));

            Color timerColor = pedido.getTiempoRestante()<= 10
                ? new Color(231, 76, 60) : new Color(241, 196, 15);
            JLabel lblTimer = new JLabel("Tiempo restante: " + pedido.getTiempoRestante() + "s");
            lblTimer.setFont(new Font("Arial", Font.BOLD, 12));
            lblTimer.setForeground(timerColor);

            infoPanel.add(lblNum);
            infoPanel.add(lblProductos);
            infoPanel.add(lblTimer);
            add(infoPanel, BorderLayout.CENTER);

            // Botones derecha
            JPanel panelBtns = new JPanel(new GridLayout(1, 2, 5, 0));
            panelBtns.setOpaque(false);

            JButton btnAvanzar = new JButton("Avanzar");
            btnAvanzar.setBackground(new Color(39, 174, 96));
            btnAvanzar.setForeground(Color.WHITE);
            btnAvanzar.setFocusPainted(false);
            btnAvanzar.setEnabled(!pedido.getEstado().esFinalizado());
            btnAvanzar.addActionListener(e -> {
                try {
                    String msg = controller.avanzarEstado(pedido);
                    JOptionPane.showMessageDialog(JuegoVista.this, msg, "Estado actualizado",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) { ex.printStackTrace(); }
            });

            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setBackground(new Color(192, 57, 43));
            btnCancelar.setForeground(Color.WHITE);
            btnCancelar.setFocusPainted(false);
            btnCancelar.setEnabled(pedido.getEstado().sePuedeCancelar());
            btnCancelar.addActionListener(e -> {
                try {
                    int op = JOptionPane.showConfirmDialog(JuegoVista.this,
                        "Cancelar pedido #" + pedido.getNumeroPedido() + "?",
                        "Cancelar pedido", JOptionPane.YES_NO_OPTION);
                    if (op == JOptionPane.YES_OPTION) {
                        String msg = controller.cancelarPedido(pedido);
                        JOptionPane.showMessageDialog(JuegoVista.this, msg);
                    }
                } catch (SQLException ex) { ex.printStackTrace(); }
            });

            panelBtns.add(btnAvanzar);
            panelBtns.add(btnCancelar);
            add(panelBtns, BorderLayout.EAST);
        }

        private Color colorEstado(EstadoPedido estado) {
            switch (estado) {
                case RECIBIDA:   return new Color(52, 152, 219);
                case PREPARANDO: return new Color(241, 196, 15);
                case EN_HORNO:   return new Color(230, 126, 34);
                case LISTA:      return new Color(39, 174, 96);
                case CANCELADA:  return new Color(149, 165, 166);
                default:         return new Color(192, 57, 43);
            }
        }
    }
}
