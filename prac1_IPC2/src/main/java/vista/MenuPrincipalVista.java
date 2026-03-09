/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.LoginControlador;
import modelo.Usuario;
import modelo.Constantes;
import vista.superadmin.*;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author jeffm
 */
public class MenuPrincipalVista extends JFrame{
    
    private final Usuario usuario = LoginControlador.getSesionActual();

    public MenuPrincipalVista() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Menu Principal - " + usuario.getNombreCompleto());
        setSize(480, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(44, 62, 80));

        // Encabezado
        JPanel panelTop = new JPanel(new GridLayout(3, 1));
        panelTop.setBackground(new Color(44, 62, 80));
        panelTop.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblBienvenido = new JLabel("Bienvenido, " + usuario.getNombreCompleto(), SwingConstants.CENTER);
        lblBienvenido.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenido.setForeground(Color.BLACK);

        JLabel lblRol = new JLabel("Rol: " + usuario.getRol().getNombre(), SwingConstants.CENTER);
        lblRol.setFont(new Font("Arial", Font.PLAIN, 13));
        lblRol.setForeground(new Color(189, 195, 199));

        String sucursalTxt = usuario.getSucursal() != null ? usuario.getSucursal().getNombre() : "Global";
        JLabel lblSucursal = new JLabel("Sucursal: " + sucursalTxt, SwingConstants.CENTER);
        lblSucursal.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSucursal.setForeground(new Color(189, 195, 199));

        panelTop.add(lblBienvenido);
        panelTop.add(lblRol);
        panelTop.add(lblSucursal);
        panel.add(panelTop, BorderLayout.NORTH);

        // Botones segun rol
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 0, 10));
        panelBotones.setBackground(new Color(44, 62, 80));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 60, 15, 60));

        String rol = usuario.getRol().getNombre();

        if (Constantes.ROL_JUGADOR.equals(rol)) {
            panelBotones.add(crearBoton("Jugar", new Color(39, 174, 96), e -> abrirJuego()));
            panelBotones.add(crearBoton("Ver Ranking", new Color(41, 128, 185), e -> abrirRanking()));
            panelBotones.add(crearBoton("Mi Historial", new Color(142, 68, 173), e -> new HistorialPersonalVista()));        
        }

        if (Constantes.ROL_ADMIN_TIENDA.equals(rol)) {
            panelBotones.add(crearBoton("Gestionar Productos", new Color(39, 174, 96),
                e -> new GestionProductosVista()));
            panelBotones.add(crearBoton("Estadisticas / Reportes", new Color(41, 128, 185),
                e -> new ReportesVista()));
            panelBotones.add(crearBoton("Ranking de Sucursal", new Color(142, 68, 173),
                e -> abrirRanking()));
        }

        if (Constantes.ROL_SUPER_ADMIN.equals(rol)) {
            panelBotones.add(crearBoton("Gestionar Sucursales", new Color(39, 174, 96),
                e -> new GestionSucursalesVista()));
            panelBotones.add(crearBoton("Gestionar Usuarios", new Color(41, 128, 185),
                e -> new GestionUsuariosVista()));
            panelBotones.add(crearBoton("Parametros del Juego", new Color(142, 68, 173),
                e -> new ParametrosJuegoVista()));
            panelBotones.add(crearBoton("Estadisticas Globales", new Color(230, 126, 34),
                e -> new ReportesVista()));
            panelBotones.add(crearBoton("Ranking Global", new Color(22, 160, 133),
                e -> abrirRanking()));
        }

        panelBotones.add(crearBoton("Cerrar Sesion", new Color(192, 57, 43), e -> cerrarSesion()));
        panel.add(panelBotones, BorderLayout.CENTER);

        add(panel);
    }

    private JButton crearBoton(String texto, Color color, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(0, 40));
        btn.addActionListener(accion);
        return btn;
    }

    private void abrirJuego() {
        dispose();
        new JuegoVista(this);
    }

    private void abrirRanking() {
        new RankingVista();
    }

    private void cerrarSesion() {
        new LoginControlador().logout();
        dispose();
        new LoginVista();
    }
}
