package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VentasDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public VentasDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /***
     * Consulta las ventas y guarda los resultados de la transaccion
     * @return Lista de pedidos vendidos
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<Pedido> getVentas() throws SQLException {

        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido = null;
        Mesa mesa = null;
        Comensal comensal = null;
        EstadoPedido estadoPedido = null;
        Trabajador trabajador = null;
        LocalDateTime fechaFin = null;

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Pedido.numeroDePedido, Pedido.fechaInicio, Pedido.fechaFin, EstadoPedido.estado, Mesa.numeroDeMesa, Trabajador.nombres, Comensal.nombre, Pedido.montoTotal FROM Pedido INNER JOIN Comensal ON Pedido.idComensal = Comensal.idComensal INNER JOIN Mesa ON Mesa.numeroDeMesa = Comensal.idMesa INNER JOIN EstadoPedido ON Pedido.idEstadoPedido = EstadoPedido.idEstadoPedido INNER JOIN Trabajador ON Mesa.idTrabajador = Trabajador.idTrabajador ORDER BY Pedido.numeroDePedido");

        while (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            pedido = new Pedido();
            mesa = new Mesa();
            comensal = new Comensal();
            estadoPedido = new EstadoPedido();
            trabajador = new Trabajador();

            pedido.setNumeroDePedido(resultSet.getInt("numeroDePedido"));
            pedido.setFechaInicio(resultSet.getTimestamp("fechaInicio", Calendar.getInstance()).toLocalDateTime());
            fechaFin = resultSet.getTimestamp("fechaFin") != null ? resultSet.getTimestamp("fechaFin").toLocalDateTime() : null;
            pedido.setFechaFin(fechaFin);
            estadoPedido.setEstado(resultSet.getString("estado"));
            mesa.setNumeroDeMesa(resultSet.getInt("numeroDeMesa"));
            trabajador.setNombres(resultSet.getString("nombres"));
            comensal.setNombre(resultSet.getString("nombre"));
            pedido.setMontoTotal(resultSet.getDouble("montoTotal"));
            mesa.setTrabajador(trabajador);
            pedido.setEstadoPedido(estadoPedido);
            pedido.setMesa(mesa);
            pedido.setComensal(comensal);

            pedidos.add(pedido);

        }
        return pedidos;
    }

    /***
     * Consulta la informacion para el resumen de las ventas
     * @return Arreglo con la infromacion consultada
     * @throws SQLException Si hubo algun problema en la transaccion
     */
    public List<Double> getInfo() throws SQLException{
        List<Double> infoVentas = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT (SELECT SUM(montoTotal) FROM Pedido ) AS totalVentas, (SELECT COUNT(*) FROM Pedido) AS totalPedidos, (SELECT COUNT(*) FROM Comensal) AS totalComensales FROM DUAL");
        if(resultSet.next()){
            infoVentas = new ArrayList<>();
            infoVentas.add(resultSet.getDouble("totalVentas"));
            infoVentas.add(resultSet.getDouble("totalPedidos"));
            infoVentas.add(resultSet.getDouble("totalComensales"));
        }
        return infoVentas;
    }
}
