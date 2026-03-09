/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.superadmin;

import controlador.SuperAdminControlador;
import daos.RolDAO;
import modelo.Rol;
import modelo.Sucursal;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import modelo.Constantes;

/**
 *
 * @author jeffm
 */
public class GestionUsuariosVista extends JFrame{
    
    private final SuperAdminControlador controller = new SuperAdminControlador();
    private JTable              tabla;
    private DefaultTableModel   modeloTabla;
    private JTextField          txtUsername, txtNombre, txtApellido, txtEmail, txtPassword;
    private JComboBox<Rol>      cmbRol;
    private JComboBox<Sucursal> cmbSucursal;
    private List<Usuario>       usuarios;

    public GestionUsuariosVista() {
        initComponents();
        cargarDatos();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Gestion de Usuarios");
        setSize(860, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Username", "Nombre", "Email", "Rol", "Sucursal", "Activo"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(52, 73, 94));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Registrar Administrador de Tienda",
            0, 0, new Font("Arial", Font.BOLD, 12), Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtUsername  = new JTextField(16);
        txtPassword  = new JTextField(16);
        txtNombre    = new JTextField(16);
        txtApellido  = new JTextField(16);
        txtEmail     = new JTextField(16);        
        cmbSucursal  = new JComboBox<>();

        agregarCampo(formPanel, gbc, "Usuario:",  txtUsername, 0);
        agregarCampo(formPanel, gbc, "Contraseña:",  txtPassword, 1);
        agregarCampo(formPanel, gbc, "Nombre:",    txtNombre,   2);
        agregarCampo(formPanel, gbc, "Apellido:",  txtApellido, 3);
        agregarCampo(formPanel, gbc, "Email:",     txtEmail,    4);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        JLabel lRol = new JLabel("Rol:"); lRol.setForeground(Color.WHITE);
        formPanel.add(lRol, gbc);
        gbc.gridx = 1; formPanel.add(cmbRol, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lSuc = new JLabel("Sucursal:"); lSuc.setForeground(Color.WHITE);
        formPanel.add(lSuc, gbc);
        gbc.gridx = 1; formPanel.add(cmbSucursal, gbc);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        btns.setOpaque(false);
        btns.add(crearBoton("Registrar",   new Color(39, 174, 96),  e -> registrar()));
        btns.add(crearBoton("Desactivar",  new Color(192, 57, 43),  e -> desactivar()));

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        formPanel.add(btns, gbc);

        panel.add(formPanel, BorderLayout.EAST);
        add(panel);
    }

    private void cargarDatos() {
        try {
            usuarios = controller.obtenerUsuarios();
            modeloTabla.setRowCount(0);
            for (Usuario u : usuarios) {
                modeloTabla.addRow(new Object[]{
                    u.getIdUsuario(), u.getUsuario(), u.getNombreCompleto(),
                    u.getEmail(),
                    u.getRol() != null ? u.getRol().getNombre() : "",
                    u.getSucursal() != null ? u.getSucursal().getNombre() : "Global",
                    u.isActivo() ? "Si" : "No"
                });
            }            
            cmbRol.removeAllItems();
            for (Rol r : controller.obtenerRoles()) cmbRol.addItem(r);

            cmbSucursal.removeAllItems();
            cmbSucursal.addItem(null);
            for (Sucursal s : controller.obtenerSucursales()) cmbSucursal.addItem(s);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void registrar() {
        try {
            Rol rolAdmin = new RolDAO().encontrarPorNombre(Constantes.ROL_ADMIN_TIENDA);
            Sucursal sucursal = (Sucursal) cmbSucursal.getSelectedItem();            

            controller.registrarUsuario(
                txtUsername.getText().trim(),
                txtPassword.getText().trim(),
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtEmail.getText().trim(),
                rolAdmin.getIdRol(),
                sucursal != null ? sucursal.getIdSucursal() : null
            );
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            cargarDatos();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void desactivar() {
        int f = tabla.getSelectedRow();
        if (f < 0) { JOptionPane.showMessageDialog(this, "Selecciona un usuario."); return; }
        int op = JOptionPane.showConfirmDialog(this, "Desactivar usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            try {
                controller.desactivarUsuario(usuarios.get(f).getIdUsuario());
                cargarDatos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void agregarCampo(JPanel p, GridBagConstraints gbc, String lbl, JTextField txt, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        JLabel l = new JLabel(lbl); l.setForeground(Color.WHITE); p.add(l, gbc);
        gbc.gridx = 1; p.add(txt, gbc);
    }

    private void estilizarTabla(JTable t) {
        t.setBackground(new Color(52, 73, 94)); t.setForeground(Color.WHITE);
        t.setGridColor(new Color(44, 62, 80)); t.setRowHeight(24);
        t.getTableHeader().setBackground(new Color(41, 128, 185));
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        t.setSelectionBackground(new Color(41, 128, 185));
    }

    private JButton crearBoton(String txt, Color bg, java.awt.event.ActionListener a) {
        JButton b = new JButton(txt);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.addActionListener(a); return b;
    }
}
