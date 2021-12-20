package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.Area;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AreaDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public AreaDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public Area getArea(int idArea) throws SQLException {
        Area area = new Area();

        String sql = "SELECT * FROM Area WHERE Area.idArea = " + idArea + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            area.setIdArea(resultSet.getInt("idArea"));
            area.setNombreArea(resultSet.getString("nombreArea"));
        }

        return area;
    }
}
