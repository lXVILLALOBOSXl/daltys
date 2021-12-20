package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrabajadorDAO {
    private ConexionBDRestaurante conexionBDRestaurante;
    private static final String SQL_INSERT = "INSERT INTO `Trabajador` (`usuario`, `contrasena`, `telefono`, `nombres`, `apellidoPaterno`, `apellidoMaterno`, `estaActivo`, `idRol`, `idTurno`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE `Trabajador` SET `usuario` = ?, `contrasena` = ?, `telefono` = ?, `nombres` = ?, `apellidoPaterno` = ?, `apellidoMaterno` = ?, `estaActivo` = ?, `idRol` = ?, `idTurno` = ? WHERE `Trabajador`.`idTrabajador` = ?";

    public TrabajadorDAO(ConexionBDRestaurante conexionBDRestaurante)  {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public Trabajador getTrabajador(int idTrabajador) throws SQLException {
        Trabajador trabajador = new Trabajador();

        String sql = "SELECT * FROM Trabajador WHERE Trabajador.idTrabajador = " + idTrabajador + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            trabajador.setIdTrabajador(resultSet.getInt("idTrabajador"));
            trabajador.setUsuario(resultSet.getString("usuario"));
            trabajador.setContrasena(resultSet.getString("contrasena"));
            trabajador.setTelefono(resultSet.getString("telefono"));
            trabajador.setNombres(resultSet.getString("nombres"));
            trabajador.setApellidoPaterno(resultSet.getString("apellidoPaterno"));
            trabajador.setApellidoMaterno(resultSet.getString("apellidoMaterno"));
            trabajador.setCorreo(resultSet.getString("correo"));
            int idRol = resultSet.getInt("idRol");
            int idEstadoDeTrabajador = resultSet.getInt("idEstadoDeTrabajador");
            int idTurno = resultSet.getInt("idTurno");
            trabajador.setRol(new RolDAO(conexionBDRestaurante).getRol(idRol));
            trabajador.setEstaActivo(resultSet.getBoolean("estaActivo"));
            trabajador.setTurno(new TurnoDAO(conexionBDRestaurante).getTurno(idTurno));

        }

        return trabajador;
    }

    public Trabajador getTrabajdorMesa(int idTrabajador) throws SQLException {
        Trabajador trabajador = new Trabajador();

        String sql = "SELECT * FROM Trabajador WHERE Trabajador.idTrabajador = " + idTrabajador + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            trabajador.setNombres(resultSet.getString("nombres"));
            trabajador.setApellidoPaterno(resultSet.getString("apellidoPaterno"));
        }

        return trabajador;
    }

    /**
     * Prepara y ejecuta la transaccion para consultar la informacion que se debe mostrar en los movimientos de stock
     * @return informacion de los movimientos de stock
     * @throws SQLException si hubo algun error al ejecutar la transaccion
     */
    public List<MovimientoDeStock> getMovimientosDeStock() throws SQLException {
        List<MovimientoDeStock> movimientosDeStock = new ArrayList<>();
        MovimientoDeStock movimientoDeStock = null;
        Producto producto = null;
        PedidoProducto pedidoProducto = null;
        TipoDeMovimientoDeStock tipoDeMovimientoDeStock = null;
        Mesa mesa = null;
        Pedido pedido = null;
        LocalDateTime fechaInicio = null;
        LocalDateTime fechaFin = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT MovimientoDeStock.idMovimientoDeStock, MovimientoDeStock.fecha, MovimientoDeStock.stockAnterior, MovimientoDeStock.stockActual, Pedido_Producto.idPedidoProducto, Pedido_Producto.numeroDePedido, Pedido.numeroDePedido, Producto.idProducto, Producto.nombre, TipoDeMovimientoDeStock.idTipoDeMovimientoDeStock, TipoDeMovimientoDeStock.nombreMovimientoDeStock, Pedido.fechaFin, Pedido.montoTotal, Pedido.fechaInicio, Mesa.numeroDeMesa FROM MovimientoDeStock INNER JOIN TipoDeMovimientoDeStock ON TipoDeMovimientoDeStock.idTipoDeMovimientoDeStock = MovimientoDeStock.idTipoDeMovimientoDeStock LEFT JOIN Producto ON Producto.idProducto = MovimientoDeStock.idProducto LEFT JOIN Pedido_Producto ON Pedido_Producto.idPedidoProducto = MovimientoDeStock.idPedidoProducto LEFT JOIN Pedido ON Pedido_Producto.numeroDePedido = Pedido.numeroDePedido LEFT JOIN Mesa ON Pedido.idMesa = Mesa.numeroDeMesa");


        while (resultSet.next()){
            movimientoDeStock = new MovimientoDeStock();
            producto = new Producto();
            pedidoProducto = new PedidoProducto();
            tipoDeMovimientoDeStock = new TipoDeMovimientoDeStock();
            mesa = new Mesa();
            pedido = new Pedido();
            movimientoDeStock.setIdMovimientoDeStock(resultSet.getInt("idMovimientoDeStock"));
            movimientoDeStock.setFecha(resultSet.getTimestamp("fecha", Calendar.getInstance()).toLocalDateTime());
            movimientoDeStock.setStockActual(resultSet.getInt("stockActual"));
            movimientoDeStock.setStockAnterior(resultSet.getInt("stockAnterior"));
            pedidoProducto.setIdPedidoProducto(resultSet.getInt("idPedidoProducto"));
            producto.setIdProducto(resultSet.getInt("idProducto"));
            producto.setNombre(resultSet.getString("nombre"));
            tipoDeMovimientoDeStock.setIdTipoDeMovimientoDeStock(resultSet.getInt("idTipoDeMovimientoDeStock"));
            tipoDeMovimientoDeStock.setNombreMovimientoDeStock(resultSet.getString("nombreMovimientoDeStock"));
            fechaFin = resultSet.getTimestamp("fechaFin") != null ? resultSet.getTimestamp("fechaFin").toLocalDateTime() : null;
            fechaInicio = resultSet.getTimestamp("fechaInicio") != null ? resultSet.getTimestamp("fechaInicio").toLocalDateTime() : null;
            pedido.setFechaInicio(fechaInicio);
            pedido.setFechaFin(fechaFin);
            pedido.setMontoTotal(resultSet.getDouble("montoTotal"));
            mesa.setNumeroDeMesa(resultSet.getInt("numeroDeMesa"));
            pedido.setNumeroDePedido(resultSet.getInt("numeroDePedido"));
            pedido.setMesa(mesa);
            pedidoProducto.setPedido(pedido);
            movimientoDeStock.setTipoDeMovimientoDeStock(tipoDeMovimientoDeStock);
            movimientoDeStock.setPedidoProducto(pedidoProducto);
            movimientoDeStock.setProducto(producto);
            movimientosDeStock.add(movimientoDeStock);
        }
        return movimientosDeStock;
    }/**
     * Prepara y ejecuta la transaccion para consultar la informacion que se debe mostrar en los movimientos de stock
     * @return informacion de los movimientos de stock
     * @throws SQLException si hubo algun error al ejecutar la transaccion
     */
    public List<Trabajador> getTrabajadores() throws SQLException {
        List<Trabajador> trabajadores = new ArrayList<>();
        Trabajador trabajador = null;
        Rol rol = null;
        Turno turno = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Trabajador.*, Rol.rol, Turno.nombre FROM Trabajador INNER JOIN Rol ON Trabajador.idRol = Rol.idRol INNER JOIN Turno ON Trabajador.idRol = Rol.idRol");


        while (resultSet.next()){
            trabajador = new Trabajador();
            rol = new Rol();
            turno = new Turno();
            rol.setIdRol(resultSet.getInt("idRol"));
            rol.setRol(resultSet.getString("rol"));
            turno.setIdTurno(resultSet.getInt("idTurno"));
            turno.setNombre("nombre");
            trabajador.setTurno(turno);
            trabajador.setEstaActivo(resultSet.getBoolean("estaActivo"));
            trabajador.setRol(rol);
            trabajador.setIdTrabajador(resultSet.getInt("idTrabajador"));
            trabajador.setUsuario(resultSet.getString("usuario"));
            trabajador.setContrasena(resultSet.getString("contrasena"));
            trabajador.setTelefono(resultSet.getString("telefono"));
            trabajador.setNombres(resultSet.getString("nombres"));
            trabajador.setApellidoPaterno(resultSet.getString("apellidoPaterno"));
            trabajador.setApellidoMaterno(resultSet.getString("apellidoMaterno"));
            trabajadores.add(trabajador);
        }
        return trabajadores;
    }

    public int guardar(Trabajador trabajador) throws SQLException{
        int affectedRows = 0;
        if(trabajador.getIdTrabajador() == null){
            PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_INSERT);
            preparedStatement.setString(1,trabajador.getUsuario());
            preparedStatement.setString(2,trabajador.getContrasena());
            preparedStatement.setString(3,trabajador.getTelefono());
            preparedStatement.setString(4,trabajador.getNombres());
            preparedStatement.setString(5,trabajador.getApellidoPaterno());
            preparedStatement.setString(6,trabajador.getApellidoMaterno());
            String activo = (trabajador.getEstaActivo()) ? "1" : "0";
            preparedStatement.setString(7,activo);
            preparedStatement.setString(8,trabajador.getRol().getIdRol().toString());
            preparedStatement.setString(9,trabajador.getTurno().getIdTurno().toString());
            affectedRows = preparedStatement.executeUpdate();
            conexionBDRestaurante.cerrarBDRestaurante();
            return affectedRows;
        }else {
            PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1,trabajador.getUsuario());
            preparedStatement.setString(2,trabajador.getContrasena());
            preparedStatement.setString(3,trabajador.getTelefono());
            preparedStatement.setString(4,trabajador.getNombres());
            preparedStatement.setString(5,trabajador.getApellidoPaterno());
            preparedStatement.setString(6,trabajador.getApellidoMaterno());
            String activo = (trabajador.getEstaActivo()) ? "1" : "0";
            preparedStatement.setString(7,activo);
            preparedStatement.setString(8,trabajador.getRol().getIdRol().toString());
            preparedStatement.setString(9,trabajador.getTurno().getIdTurno().toString());
            preparedStatement.setString(10,trabajador.getIdTrabajador().toString());
            affectedRows = preparedStatement.executeUpdate();
            conexionBDRestaurante.cerrarBDRestaurante();
            return affectedRows;
        }
    }

    public ObservableList<Trabajador> getMeseros() throws SQLException {
        Trabajador trabajador = null;
        ObservableList<Trabajador> trabajadores = FXCollections.observableArrayList();
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT * FROM Trabajador where idRol = 1");

        while (resultSet.next()){
            trabajador = new Trabajador();
            trabajador.setIdTrabajador(resultSet.getInt("idTrabajador"));
            trabajador.setNombres(resultSet.getString("nombres"));
            trabajadores.add(trabajador);
        }

        return trabajadores;
    }
}
