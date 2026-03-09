/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.superadmin;

import controlador.SuperAdminControlador;
import modelo.ParametroJuego;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class ParametrosJuegoVista extends JFrame {
    
    private final SuperAdminControlador controller = new SuperAdminControlador();
    private JTable            tabla;
    private DefaultTableModel modeloTabla;
    private JTextField        txtValor;
    private JLabel            lblClave, lblDescripcion;
    private List<ParametroJuego> parametros;

    public ParametrosJuegoVista() {
        initComponents();
        cargarParametros();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Parametros del Juego");
        setSize(700, 460);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Clave", "Valor", "Descripcion"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setBackground(new Color(52, 73, 94));
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(44, 62, 80));
        tabla.setRowHeight(24);
        tabla.getTableHeader().setBackground(new Color(41, 128, 185));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setSelectionBackground(new Color(41, 128, 185));
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionar());
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        //Panel edicion
        JPanel panelEdit = new JPanel(new GridBagLayout());
        panelEdit.setBackground(new Color(52, 73, 94));
        panelEdit.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Editar Parametro",
            0, 0, new Font("Arial", Font.BOLD, 12), Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        lblClave = new JLabel("Selecciona un parametro");
        lblClave.setForeground(new Color(241, 196, 15));
        lblClave.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelEdit.add(lblClave, gbc);

        lblDescripcion = new JLabel("");
        lblDescripcion.setForeground(new Color(189, 195, 199));
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridy = 1;
        panelEdit.add(lblDescripcion, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel lv = new JLabel("Nuevo valor:"); lv.setForeground(Color.WHITE);
        panelEdit.add(lv, gbc);
        gbc.gridx = 1;
        txtValor = new JTextField(12);
        panelEdit.add(txtValor, gbc);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(41, 128, 185));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardar());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelEdit.add(btnGuardar, gbc);

        panel.add(panelEdit, BorderLayout.EAST);
        add(panel);
    }

    private void cargarParametros() {
        try {
            parametros = controller.obtenerParametros();
            modeloTabla.setRowCount(0);
            for (ParametroJuego p : parametros) {
                modeloTabla.addRow(new Object[]{
                    p.getIdParametro(), p.getClave(), p.getValor(), p.getDescripcion()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void seleccionar() {
        int f = tabla.getSelectedRow();
        if (f < 0 || f >= parametros.size()) return;
        ParametroJuego p = parametros.get(f);
        lblClave.setText(p.getClave());
        lblDescripcion.setText(p.getDescripcion() != null ? p.getDescripcion() : "");
        txtValor.setText(p.getValor());
    }

    private void guardar() {
        int f = tabla.getSelectedRow();
        if (f < 0) { JOptionPane.showMessageDialog(this, "Selecciona un parametro."); return; }
        String nuevoValor = txtValor.getText().trim();
        if (nuevoValor.isEmpty()) { JOptionPane.showMessageDialog(this, "El valor no puede estar vacio."); return; }
        try {
            ParametroJuego p = parametros.get(f);
            p.setValor(nuevoValor);
            controller.actualizarParametro(p);
            JOptionPane.showMessageDialog(this, "Parametro actualizado correctamente.");
            cargarParametros();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
