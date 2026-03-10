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
    private JTextField        txtClave, txtValor, txtDescripcion; 
    private JButton           btnGuardar, btnNuevo;
    private List<ParametroJuego> parametros;

    public ParametrosJuegoVista() {
        initComponents();
        cargarParametros();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Parametros del Juego");
        setSize(900, 580);
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
        tabla.getTableHeader().setForeground(Color.black);
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

        txtClave       = new JTextField(18);
        txtValor       = new JTextField(18);
        txtDescripcion = new JTextField(18);

        agregarCampo(panelEdit, gbc, "Clave *:",       txtClave,       0);
        agregarCampo(panelEdit, gbc, "Valor *:",       txtValor,       1);
        agregarCampo(panelEdit, gbc, "Descripcion:",   txtDescripcion, 2);


        gbc.gridy = 4;
        JTextArea txtAviso = new JTextArea(
            "Selecciona un parametro de la tabla para editarlo, " +
            "o completa todos los campos para crear uno nuevo.");
        txtAviso.setLineWrap(true);
        txtAviso.setWrapStyleWord(true);
        txtAviso.setEditable(false);
        txtAviso.setOpaque(false);
        txtAviso.setForeground(new Color(189, 195, 199));
        txtAviso.setFont(new Font("Arial", Font.ITALIC, 11));
        txtAviso.setPreferredSize(new Dimension(220, 50));
        panelEdit.add(txtAviso, gbc);
        
        
        JPanel btns = new JPanel(new GridLayout(2, 1, 0, 8));
        btns.setOpaque(false);
        btns.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btns.add(crearBoton("Limpiar",   new Color(39, 174, 96),  e -> limpiar()));
        btns.add(crearBoton("Guardar",  new Color(192, 57, 43),  e -> guardar()));
        
        gbc.gridy = 5;
        panelEdit.add(btns, gbc);

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
        txtClave.setText(p.getClave());
        txtValor.setText(p.getValor());
        txtDescripcion.setText(p.getDescripcion() != null ? p.getDescripcion() : "");
        txtClave.setEditable(false);
        txtClave.setBackground(new Color(200, 200, 200));
    }

    private void guardar() {
        String clave = txtClave.getText().trim();
        String valor = txtValor.getText().trim();

        if (clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La clave es obligatoria.",
                "Campo requerido", JOptionPane.WARNING_MESSAGE);
            txtClave.requestFocus();
            return;
        }
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El valor es obligatorio.",
                "Campo requerido", JOptionPane.WARNING_MESSAGE);
            txtValor.requestFocus();
            return;
        }

        try {
            int fila = tabla.getSelectedRow();
            if (fila >= 0 && fila < parametros.size()) {
                // Editar parametro existente (solo cambia valor y descripcion)
                ParametroJuego p = parametros.get(fila);
                p.setValor(valor);
                p.setDescripcion(txtDescripcion.getText().trim());
                controller.actualizarParametro(p);
                JOptionPane.showMessageDialog(this, "Parametro actualizado correctamente.");
            } else {
                // Crear parametro nuevo
                ParametroJuego nuevo = new ParametroJuego();
                nuevo.setClave(clave.toUpperCase().replace(" ", "_"));
                nuevo.setValor(valor);
                nuevo.setDescripcion(txtDescripcion.getText().trim());
                controller.guardarParametro(nuevo);
                JOptionPane.showMessageDialog(this, "Parametro creado correctamente.");
            }
            cargarParametros();
            limpiar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void limpiar() {
        tabla.clearSelection();
        txtClave.setText("");
        txtClave.setEditable(true);
        txtClave.setBackground(Color.WHITE);
        txtValor.setText("");
        txtDescripcion.setText("");
        txtClave.requestFocus();
    }
  
    private void agregarCampo(JPanel p, GridBagConstraints gbc,
                               String etiqueta, JTextField campo, int fila) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        p.add(lbl, gbc);
        gbc.gridx = 1;
        p.add(campo, gbc);
    }
    
    private JButton crearBoton(String txt, Color bg, java.awt.event.ActionListener a) {
        JButton b = new JButton(txt);
        b.setBackground(bg); b.setForeground(Color.black);
        b.setFocusPainted(false); b.addActionListener(a); return b;
    }
}
