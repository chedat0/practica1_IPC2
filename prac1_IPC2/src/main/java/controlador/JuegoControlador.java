/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import daos.*;
import modelo.*;
import modelo.enums.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jeffm
 */
public class JuegoControlador {
    private final PartidaDAO             partidaDAO    = new PartidaDAO();
    private final PedidoDAO              pedidoDAO     = new PedidoDAO();
    private final DetallePedidoDAO       detalleDAO    = new DetallePedidoDAO();
    private final HistorialEstadoPedidoDAO historialDAO = new HistorialEstadoPedidoDAO();
    private final NivelPartidaDAO        nivelDAO      = new NivelPartidaDAO();
    private final ParametroJuegoDAO      paramDAO      = new ParametroJuegoDAO();
    private final ProductoDAO            productoDAO   = new ProductoDAO();
    private final ProductoSucursalDAO    psDao         = new ProductoSucursalDAO();

    private Partida        partida;
    private List<Pedido>   pedidosActivos;
    private int            nivelActual;
    private int            turnoRestante;
    private int            contadorPedidos;
    private int            segundosSinGenerar;
    private final Random   random = new Random();

    // Parametros cargados de BD
    private int maxPedidosActivos;
    private int duracionTurno;
    private int tiempoNivel1, tiempoNivel2, tiempoNivel3;
    private int puntosOk, puntosBonus, puntosCancelado, puntosNoEntregado;
    private int pedidosNivel2, pedidosNivel3;
    private int intervaloGeneracion;
    
    private int maxPedidosActivosActual;
    private int intervaloActual;

    public void iniciarPartida(Usuario jugador) throws SQLException {
        cargarParametros();
        List<Producto> disponibles = productoDAO.obtenerDisponiblesPorSucursal(
                jugador.getSucursal().getIdSucursal());
        if (disponibles.isEmpty()) {
            throw new IllegalStateException(
                    "No hay productos con stock disponible en esta sucursal.\n"
                    + "El administrador debe reponer el stock antes de jugar.");
        }
        pedidosActivos    = new ArrayList<>();
        nivelActual       = 1;
        turnoRestante     = duracionTurno;
        contadorPedidos   = 0;
        segundosSinGenerar = intervaloGeneracion;

        partida = new Partida();
        partida.setUsuario(jugador);
        partida.setSucursal(jugador.getSucursal());
        partidaDAO.ingresar(partida);

        maxPedidosActivosActual = maxPedidosActivos;
        intervaloActual = intervaloGeneracion;
        
        // Registrar nivel 1
        registrarNivel(1);
    }

    // Llamado cada segundo por el Timer de JuegoView
    public void tick() throws SQLException {
        if (partida == null || partida.getEstado() == EstadoPartida.FINALIZADA) return;

        turnoRestante--;
        segundosSinGenerar++;

        pedidosActivos.removeIf(p -> p.getEstado().esFinalizado());
        // Verificar pedidos vencidos
        for (Pedido p : new ArrayList<>(pedidosActivos)) {
            if (p.getEstado().esFinalizado()) continue;
            p.disminuirBloqueo();
            p.disminuirTiempo();
            if (p.tiempoAgotado()) {
                cambiarEstado(p, EstadoPedido.NO_ENTREGADO, Origen.SISTEMA);
                partida.incrementarNoEntregados();
                partida.sumarPuntos(puntosNoEntregado);
            }
        }

        

        // Generar nuevo pedido si corresponde
        if (segundosSinGenerar >= intervaloActual &&
            pedidosActivos.size() < maxPedidosActivosActual &&
            turnoRestante > 0) {
            generarPedidoAleatorio();
            segundosSinGenerar = 0;
        }

        // Verificar fin de turno
        if (turnoRestante <= 0) {
            finalizarPartida();
        }
    }

    public String avanzarEstado(Pedido pedido) throws SQLException {
        if (pedido.getEstado().esFinalizado())
            return "Este pedido ya esta finalizado.";

        if (pedido.estaBloqueado())
            return "Espera " + pedido.getTiempoBloqueo() + "s para avanzar este pedido.";
         
        EstadoPedido siguiente = pedido.getEstado().siguiente();
        if (siguiente == null) return "No hay estado siguiente.";

        EstadoPedido anterior = pedido.getEstado();
        cambiarEstado(pedido, siguiente, Origen.JUGADOR);

        if (siguiente == EstadoPedido.LISTA) {
            
            for (modelo.DetallePedido det : pedido.getDetalles()){
                psDao.decrementarStock(det.getProducto().getIdProducto(), partida.getSucursal().getIdSucursal(), det.getCantidad());
            }
            
            int tiempoUsado = pedido.getTiempoLimiteDeg() - pedido.getTiempoRestante();
            pedido.setTiempoUsado(tiempoUsado);
            int puntos = calcularPuntos(pedido);
            pedido.setPuntosObtenidos(puntos);
            partida.sumarPuntos(puntos);
            partida.incrementarCompletados();
            pedidoDAO.actualizar(pedido);
            verificarSubidaNivel();
            return "Pedido #" + pedido.getNumeroPedido() + " entregado! +" + puntos + " puntos";
        }

        int numProductos = pedido.getDetalles().size();
        int baseBloqueo = 4 + random.nextInt(9); // 4 a 12 segundos
        int extraPorProductos = Math.max(0, numProductos - 1) * 2;
        pedido.setTiempoBloqueo(baseBloqueo + extraPorProductos);
        
        return "Pedido #" + pedido.getNumeroPedido() + " avanzado a " + siguiente.name();
    }

    public String cancelarPedido(Pedido pedido) throws SQLException {
        if (!pedido.getEstado().sePuedeCancelar())
            return "No se puede cancelar en el estado actual.";

        cambiarEstado(pedido, EstadoPedido.CANCELADA, Origen.JUGADOR);
        partida.sumarPuntos(puntosCancelado);
        partida.incrementarCancelados();
        pedidoDAO.actualizar(pedido);
        pedidosActivos.remove(pedido);
        return "Pedido #" + pedido.getNumeroPedido() + " cancelado. " + puntosCancelado + " puntos.";
    }

    private void generarPedidoAleatorio() throws SQLException {
        List<Producto> disponibles = productoDAO.obtenerDisponiblesPorSucursal(
            partida.getSucursal().getIdSucursal());                
        
        if (disponibles.isEmpty()) return;

        contadorPedidos++;
        Pedido p = new Pedido();
        p.setIdPartida(partida.getIdPartida());
        p.setNumeroPedido(contadorPedidos);
        p.setNivelAlCrear(nivelActual);
        int tiempoPedido = getTiempoSegunNivel();
        p.setTiempoLimiteDeg(tiempoPedido);
        p.setTiempoRestante(tiempoPedido);
        pedidoDAO.ingresar(p);

        // Agregar entre 1 y la  cantidad de productos existentes con stock y activos aleatorios
        int cantProductos = 1 + random.nextInt(disponibles.size());
        List<Producto> seleccionados = new ArrayList<>();
        for (int i = 0; i < cantProductos; i++) {
            Producto prod = disponibles.get(random.nextInt(disponibles.size()));
            if (!seleccionados.contains(prod)) {
                seleccionados.add(prod);
                DetallePedido det = new DetallePedido(p.getIdPedido(), prod, 1);
                detalleDAO.ingresar(det);
                p.getDetalles().add(det);
            }
        }

        registrarHistorial(p, null, EstadoPedido.RECIBIDA, Origen.SISTEMA);
        pedidosActivos.add(p);
    }

    private int calcularPuntos(Pedido p) {
        int puntos = puntosOk;
        int mitadTiempo = p.getTiempoLimiteDeg() / 2;
        if (p.getTiempoUsado() <= mitadTiempo) puntos += puntosBonus;
        return puntos;
    }

    private void verificarSubidaNivel() throws SQLException {
        int completados = partida.getPedidosCompletados();
        int nuevoNivel  = nivelActual;

        if (nivelActual < 3 && completados >= pedidosNivel3) nuevoNivel = 3;
        else if (nivelActual < 2 && completados >= pedidosNivel2) nuevoNivel = 2;

        if (nuevoNivel > nivelActual) {
            nivelActual = nuevoNivel;
            partida.setNivelMaximo(nuevoNivel);
            maxPedidosActivosActual = maxPedidosActivos + (nuevoNivel -1);
            intervaloActual = Math.max(8, intervaloGeneracion - (nuevoNivel == 2 ? 4 : 8));
            registrarNivel(nuevoNivel);
        }
    }

    private void cambiarEstado(Pedido p, EstadoPedido nuevo, Origen origen) throws SQLException {
        EstadoPedido anterior = p.getEstado();
        p.setEstado(nuevo);
        pedidoDAO.actualizarEstado(p.getIdPedido(), nuevo);
        registrarHistorial(p, anterior, nuevo, origen);
    }

    private void registrarHistorial(Pedido p, EstadoPedido anterior,
                                    EstadoPedido nuevo, Origen origen) throws SQLException {
        HistorialEstadoPedido h = new HistorialEstadoPedido(p.getIdPedido(), anterior, nuevo, origen);
        historialDAO.ingresar(h);
    }

    private void registrarNivel(int nivel) throws SQLException {
        NivelPartida np = new NivelPartida(
            partida.getIdPartida(), nivel,
            partida.getPuntajeTotal(),
            partida.getPedidosCompletados()
        );
        nivelDAO.ingresar(np);
    }

    public void finalizarPartida() throws SQLException {
        if (partida.getEstado() == EstadoPartida.FINALIZADA) return;
        // Penalizar pedidos activos restantes
        for (Pedido p : pedidosActivos) {
            if (!p.getEstado().esFinalizado()) {
                cambiarEstado(p, EstadoPedido.NO_ENTREGADO, Origen.SISTEMA);
                partida.sumarPuntos(puntosNoEntregado);
                partida.incrementarNoEntregados();
                pedidoDAO.actualizar(p);
            }
        }
        pedidosActivos.clear();
        partida.setEstado(EstadoPartida.FINALIZADA);
        partidaDAO.actualizar(partida);
    }

    private int getTiempoSegunNivel() {
        switch (nivelActual) {
            case 2:  return tiempoNivel2;
            case 3:  return tiempoNivel3;
            default: return tiempoNivel1;
        }
    }

    private void cargarParametros() {
        tiempoNivel1        = paramDAO.getValorInt(Constantes.PARAM_TIEMPO_NIVEL_1, 60);
        tiempoNivel2        = paramDAO.getValorInt(Constantes.PARAM_TIEMPO_NIVEL_2, 50);
        tiempoNivel3        = paramDAO.getValorInt(Constantes.PARAM_TIEMPO_NIVEL_3, 40);
        maxPedidosActivos   = paramDAO.getValorInt(Constantes.PARAM_MAX_PEDIDOS_ACTIVOS, 5);
        duracionTurno       = paramDAO.getValorInt(Constantes.PARAM_DURACION_TURNO, 300);
        puntosOk            = paramDAO.getValorInt(Constantes.PARAM_PUNTOS_PEDIDO_OK, 100);
        puntosBonus         = paramDAO.getValorInt(Constantes.PARAM_PUNTOS_BONUS_EFIC, 50);
        puntosCancelado     = paramDAO.getValorInt(Constantes.PARAM_PUNTOS_CANCELADO, -30);
        puntosNoEntregado   = paramDAO.getValorInt(Constantes.PARAM_PUNTOS_NO_ENTREGADO, -50);
        pedidosNivel2       = paramDAO.getValorInt(Constantes.PARAM_PEDIDOS_NIVEL_2, 5);
        pedidosNivel3       = paramDAO.getValorInt(Constantes.PARAM_PEDIDOS_NIVEL_3, 10);
        intervaloGeneracion = paramDAO.getValorInt(Constantes.PARAM_INTERVALO_GENERACION, 15);
    }

    // ── Getters para la Vista de partida
    public Partida       getPartida()         { return partida; }
    public List<Pedido>  getPedidosActivos()  { return pedidosActivos; }
    public int           getNivelActual()     { return nivelActual; }
    public int           getTurnoRestante()   { return turnoRestante; }
}
