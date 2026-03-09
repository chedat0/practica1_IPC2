/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.superadmin;

import controlador.SuperAdminControlador;
import modelo.Sucursal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class GestionSucursalesVista extends JFrame{
    
    private final SuperAdminControlador controller = new SuperAdminControlador();
    private JTable            tabla;
    private DefaultTableModel modeloTabla;
    private JTextField        txtNombre, txtDireccion, txtTelefono;
    private List<Sucursal>    sucursales;

    public GestionSucursalesVista() {
        initComponents();
        cargarSucursales();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Gestion de Sucursales");
        setSize(760, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Nombre", "Direccion", "Activa"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionarFila());
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(52, 73, 94));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Datos",
            0, 0, new Font("Arial", Font.BOLD, 12), Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtNombre    = new JTextField(18);
        txtDireccion = new JTextField(18);        

        agregarCampo(formPanel, gbc, "Nombre:",    txtNombre,    0);
        agregarCampo(formPanel, gbc, "Direccion:", txtDireccion, 1);        

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        btns.setOpaque(false);
        btns.add(crearBoton("Nuevo",      new Color(39, 174, 96),  e -> limpiar()));
        btns.add(crearBoton("Guardar",    new Color(41, 128, 185), e -> guardar()));
        btns.add(crearBoton("Desactivar", new Color(192, 57, 43),  e -> desactivar()));

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(btns, gbc);

        panel.add(formPanel, BorderLayout.EAST);
        add(panel);
    }

    private void cargarSucursales() {
        try {
            sucursales = controller.obtenerSucursales();
            modeloTabla.setRowCount(0);
            for (Sucursal s : sucursales) {
                modeloTabla.addRow(new Object[]{
                    s.getIdSucursal(), s.getNombre(), s.getDirección(), s.isActiva() ? "Si" : "No"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void seleccionarFila() {
        int f = tabla.getSelectedRow();
        if (f < 0 || f >= sucursales.size()) return;
        Sucursal s = sucursales.get(f);
        txtNombre.setText(s.getNombre());
        txtDireccion.setText(s.getDirección());        
    }

    private void guardar() {
        try {
            int fila = tabla.getSelectedRow();
            if (fila >= 0 && fila < sucursales.size()) {
                Sucursal s = sucursales.get(fila);
                s.setNombre(txtNombre.getText().trim());
                s.setDirección(txtDireccion.getText().trim());               
                controller.actualizarSucursal(s);
                JOptionPane.showMessageDialog(this, "Sucursal actualizada.");
            } else {
                controller.crearSucursal(txtNombre.getText().trim(),
                    txtDireccion.getText().trim());
                JOptionPane.showMessageDialog(this, "Sucursal creada.");
            }
            cargarSucursales(); limpiar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void desactivar() {
        int f = tabla.getSelectedRow();
        if (f < 0) return;
        int op = JOptionPane.showConfirmDialog(this, "Desactivar sucursal?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            try {
                controller.desactivarSucursal(sucursales.get(f).getIdSucursal());
                cargarSucursales(); limpiar();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void limpiar() {
        tabla.clearSelection();
        txtNombre.setText(""); txtDireccion.setText(""); 
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
