package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.MedioDePago;
import com.daltysfood.model.restaurante.TipoDeMovimiento;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoDeMovimientoDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public TipoDeMovimientoDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public ObservableList<TipoDeMovimiento> getTiposDeMovimiento() throws SQLException {
        TipoDeMovimiento tipoDeMovimiento = null;
        ObservableList<TipoDeMovimiento> tiposDeMovimientos = FXCollections.observableArrayList();
        String sql = "SELECT * FROM `TipoDeMovimiento`";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        while(resultSet.next()){
            tipoDeMovimiento = new TipoDeMovimiento();
            tipoDeMovimiento.setIdTipoDeMovimiento(resultSet.getInt("idTipoDeMovimiento"));
            tipoDeMovimiento.setConcepto(resultSet.getString("concepto"));
            tiposDeMovimientos.add(tipoDeMovimiento);
        }

        return  tiposDeMovimientos;
    }
}
