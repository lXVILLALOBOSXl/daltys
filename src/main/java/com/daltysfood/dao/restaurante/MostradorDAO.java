package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MostradorDAO {
    private ConexionBDRestaurante conexionBDRestaurante;


    public MostradorDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public List<Pedido> getPedidosPendientes() throws SQLException {

        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido = null;
        Mesa mesa = null;
        Comensal comensal = null;

        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Pedido.numeroDePedido, Pedido.fechaInicio, Mesa.numeroDeMesa, Comensal.nombre, Pedido.montoTotal FROM Pedido INNER JOIN Comensal ON Pedido.idComensal = Comensal.idComensal INNER JOIN Mesa ON Mesa.numeroDeMesa = Comensal.idMesa INNER JOIN EstadoPedido ON Pedido.idEstadoPedido = EstadoPedido.idEstadoPedido WHERE EstadoPedido.idEstadoPedido = 1 ORDER BY Pedido.numeroDePedido");

        while (resultSet.next()){

            pedido = new Pedido();
            mesa = new Mesa();
            comensal = new Comensal();

            pedido.setNumeroDePedido(resultSet.getInt("numeroDePedido"));
            pedido.setFechaInicio(resultSet.getTimestamp("fechaInicio").toLocalDateTime());
            mesa.setNumeroDeMesa(resultSet.getInt("numeroDeMesa"));
            comensal.setNombre(resultSet.getString("nombre"));
            pedido.setMontoTotal(resultSet.getDouble("montoTotal"));
            pedido.setMesa(mesa);
            pedido.setComensal(comensal);

            pedidos.add(pedido);

        }
        return pedidos;
    }

}
