/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import daos.UsuarioDAO;
import modelo.Usuario;
import modelo.Constantes;
import java.sql.SQLException;
/**
 *
 * @author jeffm
 */
public class LoginControlador {
     private static Usuario sesionActual = null;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario login(String usuario, String contraseña) throws SQLException {
        Usuario user = usuarioDAO.encontrarPorUsuario(usuario, contraseña);
        if (user == null) return null;
        sesionActual = user; 
        return user;
    }

    public void logout() {
        sesionActual = null;
    }

    public static Usuario getSesionActual() {
        return sesionActual;
    }

    public static boolean esSuperAdmin() {
        return sesionActual != null &&
               Constantes.ROL_SUPER_ADMIN.equals(sesionActual.getRol().getNombre());
    }

    public static boolean esAdminTienda() {
        return sesionActual != null &&
               Constantes.ROL_ADMIN_TIENDA.equals(sesionActual.getRol().getNombre());
    }

    public static boolean esJugador() {
        return sesionActual != null &&
               Constantes.ROL_JUGADOR.equals(sesionActual.getRol().getNombre());
    }
}
