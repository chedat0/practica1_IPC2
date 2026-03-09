/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import daos.*;
import modelo.*;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeffm
 */
public class SuperAdminControlador {
    
    private final UsuarioDAO        usuarioDAO        = new UsuarioDAO();
    private final SucursalDAO       sucursalDAO       = new SucursalDAO();
    private final RolDAO            rolDAO            = new RolDAO();
    private final ParametroJuegoDAO parametroDAO      = new ParametroJuegoDAO();

    // Manejo de las sucursales en global
    public List<Sucursal> obtenerSucursales() throws SQLException {
        return sucursalDAO.obtenerTodas();
    }

    public boolean crearSucursal(String nombre, String direccion) throws SQLException {
        if (nombre == null || nombre.trim().isEmpty()) return false;
        Sucursal s = new Sucursal();
        s.setNombre(nombre.trim());
        s.setDirección(direccion);       
        s.setActiva(true);
        return sucursalDAO.ingresar(s);
    }

    public boolean actualizarSucursal(Sucursal s) throws SQLException {
        return sucursalDAO.actualizar(s);
    }

    public boolean desactivarSucursal(int id) throws SQLException {
        return sucursalDAO.eliminar(id);
    }

    // Manejo de usuarios en Global
    public List<Usuario> obtenerUsuarios() throws SQLException {
        return usuarioDAO.encontrarTodos();
    }

    public boolean registrarUsuario(String usuario, String contraseña, String nombre,
                                     String apellido, String email, int idRol,
                                     Integer idSucursal) throws SQLException {
        if (usuarioDAO.existeUsuario(usuario))
            throw new IllegalArgumentException("El usuario '" + usuario + "' ya existe.");

        Usuario u = new Usuario();
        u.setUsuario(usuario.trim());
        u.setContraseña(contraseña);
        u.setNombre(nombre.trim());
        u.setApellido(apellido);
        u.setEmail(email);
        u.setRol(rolDAO.encontrarPorId(idRol));
        if (idSucursal != null) u.setSucursal(new SucursalDAO().obtenerSucursal(idSucursal));
        u.setActivo(true);
        return usuarioDAO.ingresar(u);
    }

    public boolean desactivarUsuario(int id) throws SQLException {
        return usuarioDAO.eliminar(id);
    }

    //Obtener y cambiar los parametros del juego en global
    public List<ParametroJuego> obtenerParametros() throws SQLException {
        return parametroDAO.obtenerTodos();
    }
    
    public boolean guardarParametro (ParametroJuego p) throws SQLException {
        return parametroDAO.ingresar(p);
    }

    public boolean actualizarParametro(ParametroJuego p) throws SQLException {
        return parametroDAO.actualizar(p);
    }

    // ver los roles disponibles
    public List<Rol> obtenerRoles() throws SQLException {
        return rolDAO.encontrarTodos();
    }
}
