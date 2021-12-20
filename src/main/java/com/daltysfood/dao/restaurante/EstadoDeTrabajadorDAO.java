package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.EstadoDeTrabajador;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadoDeTrabajadorDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public EstadoDeTrabajadorDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public EstadoDeTrabajador getEstadoDeTrabajador(int idEstadoDeTrabajador) throws SQLException {
        EstadoDeTrabajador estadoDeTrabajador = new EstadoDeTrabajador();

        String sql = "SELECT * FROM EstadoDeTrabajador WHERE EstadoDeTrabajador.idEstadoDeTrabajador = " + idEstadoDeTrabajador + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            estadoDeTrabajador.setIdEstadoDeTrabajador(resultSet.getInt("idEstadoDetrabajador"));
            estadoDeTrabajador.setEstado(resultSet.getString("estado"));
        }

        return estadoDeTrabajador;
    }
}
