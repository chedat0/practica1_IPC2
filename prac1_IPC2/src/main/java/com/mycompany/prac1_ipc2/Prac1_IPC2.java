/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prac1_ipc2;

import vista.LoginVista;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
/**
 *
 * @author jeffm
 */
public class Prac1_IPC2 {

    public static void main(String[] args) {
        // Intentar usar el look and feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, se usa el look and feel por defecto de Java
        }

        // Toda GUI de Swing debe iniciarse en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new LoginVista());
    }
}
