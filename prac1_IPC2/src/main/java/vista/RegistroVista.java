/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import daos.RolDAO;
import daos.SucursalDAO;
import daos.UsuarioDAO;
import modelo.Rol;
import modelo.Sucursal;
import modelo.Usuario;
import modelo.Constantes;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author jeffm
 */
public class RegistroVista extends JFrame{
    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmarPassword;
    private JTextField     txtNombre;
    private JTextField     txtApellido;
    private JTextField     txtEmail;
    private JComboBox<Sucursal> cmbSucursal;
    private JLabel         lblMensaje;
    private JButton        btnRegistrar;
    private JButton        btnVolver;

    public RegistroVista() {
        initComponents();
        cargarSucursales();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Pizza Express Tycoon - Registro de Jugador");
        setSize(460, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(44, 62, 80));

        // Encabezado
        JPanel panelTop = new JPanel(new GridLayout(2, 1));
        panelTop.setBackground(new Color(44, 62, 80));
        panelTop.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));

        JLabel lblTitulo = new JLabel("Crear Cuenta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Registro de Jugador", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSub.setForeground(new Color(189, 195, 199));

        panelTop.add(lblTitulo);
        panelTop.add(lblSub);
        panelPrincipal.add(panelTop, BorderLayout.NORTH);

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(44, 62, 80));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 5, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtUsername          = new JTextField(18);
        txtPassword          = new JPasswordField(18);
        txtConfirmarPassword = new JPasswordField(18);
        txtNombre            = new JTextField(18);
        txtApellido          = new JTextField(18);
        txtEmail             = new JTextField(18);
        cmbSucursal          = new JComboBox<>();

        agregarCampo(panelForm, gbc, "Usuario *",            txtUsername,          0);
        agregarCampo(panelForm, gbc, "Contraseña *",         txtPassword,          1);
        agregarCampo(panelForm, gbc, "Confirmar contraseña *", txtConfirmarPassword, 2);
        agregarCampo(panelForm, gbc, "Nombre *",             txtNombre,            3);
        agregarCampo(panelForm, gbc, "Apellido",             txtApellido,          4);
        agregarCampo(panelForm, gbc, "Email",                txtEmail,             5);

        // Sucursal
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        JLabel lblSucursal = new JLabel("Sucursal *");
        lblSucursal.setForeground(Color.WHITE);
        panelForm.add(lblSucursal, gbc);
        gbc.gridx = 1;
        panelForm.add(cmbSucursal, gbc);

        // Mensaje de error/exito
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 12));
        panelForm.add(lblMensaje, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setOpaque(false);

        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBackground(new Color(39, 174, 96));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> registrar());

        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(127, 140, 141));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> volver());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        gbc.gridy = 8;
        panelForm.add(panelBotones, gbc);

        panelPrincipal.add(panelForm, BorderLayout.CENTER);

        // Nota campos obligatorios
        JLabel lblNota = new JLabel("* Campos obligatorios", SwingConstants.CENTER);
        lblNota.setFont(new Font("Arial", Font.ITALIC, 11));
        lblNota.setForeground(new Color(149, 165, 166));
        lblNota.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelPrincipal.add(lblNota, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void cargarSucursales() {
        try {
            SucursalDAO sucursalDAO = new SucursalDAO();
            List<Sucursal> sucursales = sucursalDAO.obtenerTodas();

            cmbSucursal.removeAllItems();
            if (sucursales.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay sucursales activas disponibles.\nContacta a un administrador.",
                    "Sin sucursales", JOptionPane.WARNING_MESSAGE);
            }
            for (Sucursal s : sucursales) {
                cmbSucursal.addItem(s);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar sucursales: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrar() {
        // Limpiar mensaje anterior
        lblMensaje.setText("");

        // Obtener valores
        String username  = txtUsername.getText().trim();
        String password  = new String(txtPassword.getPassword());
        String confirmar = new String(txtConfirmarPassword.getPassword());
        String nombre    = txtNombre.getText().trim();
        String apellido  = txtApellido.getText().trim();
        String email     = txtEmail.getText().trim();
        Sucursal sucursal = (Sucursal) cmbSucursal.getSelectedItem();

        // ── Validaciones 
        if (username.isEmpty()) {
            mostrarError("El nombre de usuario es obligatorio.");
            txtUsername.requestFocus();
            return;
        }
        if (username.length() < 4) {
            mostrarError("El usuario debe tener al menos 4 caracteres.");
            txtUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mostrarError("La contraseña es obligatoria.");
            txtPassword.requestFocus();
            return;
        }
        if (password.length() < 4) {
            mostrarError("La contraseña debe tener al menos 4 caracteres.");
            txtPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmar)) {
            mostrarError("Las contraseñas no coinciden.");
            txtConfirmarPassword.setText("");
            txtConfirmarPassword.requestFocus();
            return;
        }
        if (nombre.isEmpty()) {
            mostrarError("El nombre es obligatorio.");
            txtNombre.requestFocus();
            return;
        }
        if (sucursal == null) {
            mostrarError("Debes seleccionar una sucursal.");
            return;
        }

        // ── Guardar en BD 
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            RolDAO     rolDAO     = new RolDAO();

            // Verificar que el username no exista
            if (usuarioDAO.existeUsuario(username)) {
                mostrarError("El usuario '" + username + "' ya existe.");
                txtUsername.requestFocus();
                return;
            }

            // Obtener el rol JUGADOR
            Rol rolJugador = rolDAO.encontrarPorNombre(Constantes.ROL_JUGADOR);
            if (rolJugador == null) {
                mostrarError("Error interno: rol JUGADOR no encontrado.");
                return;
            }

            // Construir el usuario
            Usuario nuevo = new Usuario();
            nuevo.setUsuario(username);
            nuevo.setContraseña(password); 
            nuevo.setNombre(nombre);
            nuevo.setApellido(apellido.isEmpty() ? null : apellido);
            nuevo.setEmail(email.isEmpty() ? null : email);
            nuevo.setRol(rolJugador);
            nuevo.setSucursal(sucursal);
            nuevo.setActivo(true);

            boolean ok = usuarioDAO.ingresar(nuevo);

            if (ok) {
                JOptionPane.showMessageDialog(this,
                    "Cuenta creada correctamente.\nYa puedes iniciar sesion con: " + username,
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                volver();
            } else {
                mostrarError("No se pudo crear la cuenta. Intenta de nuevo.");
            }

        } catch (SQLException e) {
            mostrarError("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void volver() {
        dispose();
        new LoginVista();
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(new Color(231, 76, 60));
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc,
                               String etiqueta, JComponent campo, int fila) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Color.WHITE);
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(campo, gbc);
    }
}
