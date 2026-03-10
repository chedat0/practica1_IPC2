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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {       
            System.out.println("Error al iniciar el sistema");
        }      
        SwingUtilities.invokeLater(() -> new LoginVista());
    }
}
