package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.Cocina;
import com.daltysfood.model.restaurante.MedioDePago;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CocinaDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public CocinaDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public Cocina getCocina(int idDeCocina) throws SQLException {
        Cocina cocina = new Cocina();

        String sql = "SELECT * FROM Cocina WHERE Cocina.idCocina =" + idDeCocina + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            cocina.setIdCocina(resultSet.getInt("idCocina"));
            cocina.setNombre(resultSet.getString("nombre"));
        }

        return cocina;
    }

    /***
     * Prepara y pide la consulta para traer de la base de datos los cocinas
     * que son utilizados para el choice box
     * @return Objetos tipo cocina
     * @throws SQLException Si hubo algun error en la transaccion
     */
    public ObservableList<Cocina> getCocinas() throws SQLException {
        Cocina cocina = null;
        ObservableList<Cocina> cocinas = FXCollections.observableArrayList();
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT * FROM Cocina");

        while (resultSet.next()){
            cocina = new Cocina();
            cocina.setIdCocina(resultSet.getInt("idCocina"));
            cocina.setNombre(resultSet.getString("nombre"));
            cocinas.add(cocina);
        }

        return  cocinas;

    }
}
