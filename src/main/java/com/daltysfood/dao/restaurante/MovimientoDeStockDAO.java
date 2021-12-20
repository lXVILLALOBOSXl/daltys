package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;
import org.exolab.castor.types.DateTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MovimientoDeStockDAO {
    private ConexionBDRestaurante conexionBDRestaurante;
    private static final String SQL_INSERT = "INSERT INTO `MovimientoDeStock` (`fecha`, `stockAnterior`, `stockActual`, `idProducto`, `idTipoDeMovimientoDeStock`) VALUES (?, ?, ?, ?, ?);";

    public MovimientoDeStockDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /**
     * Prepara y ejecuta una transaccion para registrar un cambio en el stock de los productos
     * @param producto informacion con el producto por el cual se modifica el stock
     * @param itemsAgregados diferencia del stock
     * @return registros afectados
     * @throws SQLException si hay un error con la transaccion
     */
    public int agregarStockManual(Producto producto, Integer itemsAgregados) throws SQLException {
        int affectedRows = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_INSERT);
        preparedStatement.setString(1, formatter.format(LocalDateTime.now()));
        preparedStatement.setString(2, producto.getStock().toString());
        preparedStatement.setString(3, Integer.toString(producto.getStock() + itemsAgregados));
        preparedStatement.setString(4, producto.getIdProducto().toString());
        preparedStatement.setString(5, "2");
        affectedRows = preparedStatement.executeUpdate();
        conexionBDRestaurante.cerrarBDRestaurante();

        return affectedRows;
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
    }

}
