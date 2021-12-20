package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.EstadoDeMesa;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadoDeMesaDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public EstadoDeMesaDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public EstadoDeMesa getEstadoDeMesa(int idEstadoMesa) throws SQLException {
        EstadoDeMesa estadoDeMesa = new EstadoDeMesa();

        String sql = "SELECT * FROM EstadoDeMesa WHERE idEstadoDeMesa = " + idEstadoMesa + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            estadoDeMesa.setIdEstadoDeMesa(resultSet.getInt("idEstadoDeMesa"));
            estadoDeMesa.setEstado(resultSet.getString("estado"));
        }

        return estadoDeMesa;
    }
}
