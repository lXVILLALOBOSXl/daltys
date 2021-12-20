package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.Comensal;
import com.daltysfood.model.restaurante.Mesa;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ComensalDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public ComensalDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public Comensal getComensal(Mesa mesa) throws SQLException {
        Comensal comensal = new Comensal();

        String sql = "SELECT * FROM Comensal WHERE Comensal.estaActivo = 1 AND Comensal.idMesa = " + mesa.getNumeroDeMesa() + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            comensal.setIdComensal(resultSet.getInt("idComensal"));
            comensal.setNombre(resultSet.getString("nombre"));
            if(resultSet.getInt("estaActivo") == 1){
                comensal.setEstaActivo(true);
            }else{
                comensal.setEstaActivo(false);
            }
        }

        return comensal;
    }
}
