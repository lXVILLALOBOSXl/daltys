package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.Cocina;
import com.daltysfood.model.restaurante.Rol;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RolDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public RolDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public Rol getRol(int idRol) throws SQLException {
        Rol rol = new Rol();

        String sql = "SELECT * FROM Rol WHERE Rol.idRol = " + idRol + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            rol.setIdRol(resultSet.getInt("idRol"));
            rol.setRol(resultSet.getString("rol"));
        }

        return rol;
    }

    public ObservableList<Rol> getRoles() throws SQLException {
        Rol rol = null;
        ObservableList<Rol> roles = FXCollections.observableArrayList();
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT * FROM Rol");

        while (resultSet.next()){
            rol = new Rol();
            rol.setIdRol(resultSet.getInt("idRol"));
            rol.setRol(resultSet.getString("rol"));
            roles.add(rol);
        }

        return roles;
    }
}
