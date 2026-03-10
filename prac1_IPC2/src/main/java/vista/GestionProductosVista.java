/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.AdminControlador;
import controlador.LoginControlador;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class GestionProductosVista extends JFrame{
    private final AdminControlador controller = new AdminControlador();
    private JTable            tabla;
    private DefaultTableModel modeloTabla;
    private JTextField        txtNombre, txtDescripcion, txtPrecio, txtCategoria, txtStock;
    private List<Producto>    productos;
    private final int         idSucursal;

    public GestionProductosVista() {
        int suc = 0;
        if (LoginControlador.getSesionActual().getSucursal() != null)
            suc = LoginControlador.getSesionActual().getSucursal().getIdSucursal();
        this.idSucursal = suc;
        initComponents();
        cargarProductos();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Gestion de Productos");
        setSize(850, 580);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] cols = {"ID", "Nombre", "Descripcion", "Precio", "Categoria", "Stock","Activo"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionarFila());

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(52, 73, 94));
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Datos del Producto",
            0, 0, new Font("Arial", Font.BOLD, 12), Color.black));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtNombre      = new JTextField(18);
        txtDescripcion = new JTextField(18);
        txtPrecio      = new JTextField(18);
        txtCategoria   = new JTextField(18);
        txtStock       = new JTextField(18);

        int row = 0;
        agregarCampo(panelForm, gbc, "Nombre:",      txtNombre,      row++);
        agregarCampo(panelForm, gbc, "Descripcion:",  txtDescripcion, row++);
        agregarCampo(panelForm, gbc, "Precio:",       txtPrecio,      row++);
        agregarCampo(panelForm, gbc, "Categoria:",    txtCategoria,   row++);
        agregarCampo(panelForm, gbc, "Stock:",        txtStock, row++);
        
        // Botones
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        panelBtns.setOpaque(false);
        panelBtns.add(crearBoton("Nuevo",      new Color(39, 174, 96),   e -> limpiarForm()));
        panelBtns.add(crearBoton("Guardar",    new Color(41, 128, 185),  e -> guardar()));
        panelBtns.add(crearBoton("Activar/Desactivar", new Color(192, 57, 43),   e -> toggleActivo()));

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panelForm.add(panelBtns, gbc);

        panel.add(panelForm, BorderLayout.EAST);
        add(panel);
    }

    private void cargarProductos() {
        try {
            int idSucursal = LoginControlador.getSesionActual().getSucursal() != null
                ? LoginControlador.getSesionActual().getSucursal().getIdSucursal() : 0;
            if (idSucursal > 0)
                productos = controller.obtenerProductosSucursal(idSucursal);
            else
                productos = controller.obtenerTodosProductos();

            modeloTabla.setRowCount(0);
            for (Producto p : productos) {
                modeloTabla.addRow(new Object[]{
                    p.getIdProducto(), p.getNombre(), p.getDescripcion(),
                    String.format("Q%.2f", p.getPrecio()), p.getCategoria(),
                    p.getStock(),
                    p.isActivo() ? "Si" : "No"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= productos.size()) return;
        Producto p = productos.get(fila);
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtCategoria.setText(p.getCategoria());
        txtStock.setText(String.valueOf(p.getStock()));
    }

    private void guardar() {
        try {
            int fila = tabla.getSelectedRow();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            if (stock < 0) {JOptionPane.showMessageDialog(this, "El stock no puede ser negativo"); return; }
            if (fila >= 0 && fila < productos.size()) {
                Producto p = productos.get(fila);
                p.setNombre(txtNombre.getText().trim());
                p.setDescripcion(txtDescripcion.getText().trim());
                p.setPrecio(precio);
                p.setCategoria(txtCategoria.getText().trim());
                p.setStock(stock);
                controller.actualizarProducto(p);
                JOptionPane.showMessageDialog(this, "Producto actualizado.");
            } else {
                controller.crearProducto(
                    txtNombre.getText().trim(),
                    txtDescripcion.getText().trim(),
                    precio,
                    txtCategoria.getText().trim(),
                    idSucursal > 0 ? idSucursal : 1,
                    stock
                );
                JOptionPane.showMessageDialog(this, "Producto creado.");
            }
            cargarProductos();
            limpiarForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio y stock debe ser un numero valido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void toggleActivo() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un producto."); return; }
        Producto p = productos.get(fila);
        String accion = p.isActivo() ? "desactivar" : "activar";
        int op = JOptionPane.showConfirmDialog(this,
            "¿Deseas " + accion + " el producto: " + p.getNombre() + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            try {
                p.setActivo(!p.isActivo());
                controller.actualizarProducto(p);
                cargarProductos();
                limpiarForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void limpiarForm() {
        tabla.clearSelection();
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtCategoria.setText("");
        txtStock.setText("");
    }

    private void agregarCampo(JPanel p, GridBagConstraints gbc,
                                String lbl, JTextField txt, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        JLabel label = new JLabel(lbl);
        label.setForeground(Color.WHITE);
        p.add(label, gbc);
        gbc.gridx = 1;
        p.add(txt, gbc);
    }

    private void estilizarTabla(JTable t) {
        t.setBackground(new Color(52, 73, 94));
        t.setForeground(Color.BLACK);
        t.setGridColor(new Color(44, 62, 80));
        t.setRowHeight(24);
        t.getTableHeader().setBackground(new Color(41, 128, 185));
        t.getTableHeader().setForeground(Color.BLACK);
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        t.setSelectionBackground(new Color(41, 128, 185));
    }

    private JButton crearBoton(String txt, Color bg, java.awt.event.ActionListener a) {
        JButton btn = new JButton(txt);
        btn.setBackground(bg);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        btn.addActionListener(a);
        return btn;
    }
}
