package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.MedioDePago;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedioDePagoDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public MedioDePagoDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /***
     * Prepara y pide la consulta para traer de la base de datos los medios de pago
     * que son utilizados para el chocie box
     * @return Objetos tipo medio de pago
     * @throws SQLException Si hubo algun error en la transaccion
     */
    public ObservableList<MedioDePago> getMediosDePagoActivos() throws SQLException {
        MedioDePago medioDePago = null;
        ObservableList<MedioDePago> mediosDePagoActivos = FXCollections.observableArrayList();

        String sql = "SELECT * FROM MedioDePago WHERE estaActiva = 1;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        while(resultSet.next()){
            medioDePago = new MedioDePago();
            medioDePago.setIdMedioDePago(resultSet.getInt("idMedioDePago"));
            medioDePago.setNombre(resultSet.getString("nombre"));
            mediosDePagoActivos.add(medioDePago);
        }

        return  mediosDePagoActivos;
    }
}
