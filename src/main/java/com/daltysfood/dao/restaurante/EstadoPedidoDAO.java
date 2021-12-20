package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.EstadoPedido;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadoPedidoDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public EstadoPedidoDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public EstadoPedido getEstadoPedido(int idEstadoDePedido) throws SQLException {
        EstadoPedido estadoPedido = new EstadoPedido();

        String sql = "SELECT * FROM EstadoPedido WHERE EstadoPedido.idEstadoPedido = " + idEstadoDePedido + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            estadoPedido.setIdEstadoPedido(resultSet.getInt("idEstadoPedido"));
            estadoPedido.setEstado(resultSet.getString("estado"));
        }

        return estadoPedido;
    }
}
