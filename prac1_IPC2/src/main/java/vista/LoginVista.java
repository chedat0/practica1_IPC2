/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.LoginControlador;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 *
 * @author jeffm
 */
public class LoginVista extends JFrame{
     private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnLogin;
    private JButton        btnRegistro;
    private JLabel         lblMensaje;

    public LoginVista() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Pizza Express Tycoon - Login");
        setSize(420, 390);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(44, 62, 80));

        // ── Encabezado ───────────────────────────────────────
        JLabel lblTitulo = new JLabel("Pizza Express Tycoon", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // ── Formulario ───────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(44, 62, 80));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Campo usuario
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(Color.WHITE);
        panelForm.add(lblUser, gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField(16);
        panelForm.add(txtUsername, gbc);

        // Campo contraseña
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPass = new JLabel("Contrasena:");
        lblPass.setForeground(Color.WHITE);
        panelForm.add(lblPass, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(16);
        panelForm.add(txtPassword, gbc);

        // Boton iniciar sesion
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        btnLogin = new JButton("Iniciar Sesion");
        btnLogin.setBackground(new Color(231, 76, 60));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        panelForm.add(btnLogin, gbc);

        // Separador visual
        gbc.gridy = 3;
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(127, 140, 141));
        panelForm.add(separador, gbc);

        // Texto no tienes cuenta
        gbc.gridy = 4;
        JLabel lblNoCuenta = new JLabel("No tienes cuenta?", SwingConstants.CENTER);
        lblNoCuenta.setForeground(new Color(189, 195, 199));
        lblNoCuenta.setFont(new Font("Arial", Font.PLAIN, 12));
        panelForm.add(lblNoCuenta, gbc);

        // Boton registrarse
        gbc.gridy = 5;
        btnRegistro = new JButton("Crear Cuenta");
        btnRegistro.setBackground(new Color(39, 174, 96));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistro.setFocusPainted(false);
        panelForm.add(btnRegistro, gbc);

        // Etiqueta de mensaje de error
        gbc.gridy = 6;
        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setForeground(new Color(231, 76, 60));
        panelForm.add(lblMensaje, gbc);

        panelPrincipal.add(panelForm, BorderLayout.CENTER);
        add(panelPrincipal);

        // ── Acciones ─────────────────────────────────────────
        btnLogin.addActionListener(e -> realizarLogin());
        txtPassword.addActionListener(e -> realizarLogin());
        btnRegistro.addActionListener(e -> abrirRegistro());
    }

    private void realizarLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Ingresa usuario y contrasena.");
            return;
        }

        try {
            LoginControlador auth = new LoginControlador();
            Usuario usuario = auth.login(username, password);
            if (usuario == null) {
                lblMensaje.setText("Credenciales incorrectas.");
            } else {
                dispose();
                new MenuPrincipalVista();
            }
        } catch (SQLException ex) {
            lblMensaje.setText("Error de conexion a la base de datos.");
            ex.printStackTrace();
        }
    }

    private void abrirRegistro() {
        dispose();
        new RegistroVista();
    }
}
