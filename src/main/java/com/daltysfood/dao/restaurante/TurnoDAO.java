package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.Rol;
import com.daltysfood.model.restaurante.Turno;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class TurnoDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public TurnoDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public Turno getTurno(int idTurno) throws SQLException {
        Turno turno = new Turno();

        String sql = "SELECT * FROM Turno WHERE Turno.idTurno = " + idTurno + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            turno.setIdTurno(resultSet.getInt("idTurno"));
            turno.setNombre(resultSet.getString("nombre"));
            turno.setHoraInicio(resultSet.getTime("horaInicio"));
            turno.setHoraFin(resultSet.getTime("horaFin"));
        }

        return turno;
    }

    public ObservableList<Turno> getTurnos() throws SQLException {
        Turno turno = null;
        ObservableList<Turno> turnos = FXCollections.observableArrayList();
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT * FROM Turno");

        while (resultSet.next()){
            turno = new Turno();
            turno.setIdTurno(resultSet.getInt("idTurno"));
            turno.setNombre(resultSet.getString("nombre"));

            turnos.add(turno);
        }

        return turnos;
    }
}
