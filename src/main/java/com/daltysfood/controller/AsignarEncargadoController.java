package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.MesaDAO;
import com.daltysfood.dao.restaurante.RolDAO;
import com.daltysfood.dao.restaurante.TrabajadorDAO;
import com.daltysfood.dao.restaurante.TurnoDAO;
import com.daltysfood.model.restaurante.Mesa;
import com.daltysfood.model.restaurante.Trabajador;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AsignarEncargadoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private ChoiceBox<Trabajador> choiceBoxTrabajador;

    private ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    private MesaDAO mesaDAO;
    private TrabajadorDAO trabajadorDAO;
    private Integer mesa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conexionBDRestaurante.conectarBDRestaurante();
        trabajadorDAO = new TrabajadorDAO(conexionBDRestaurante);
        try {
            choiceBoxTrabajador.setItems(trabajadorDAO.getMeseros());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
    }

    public void guardar(ActionEvent actionEvent) {
        if(choiceBoxTrabajador.getSelectionModel().isEmpty()){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese los campos requeridos").position(Pos.CENTER).showWarning();
            return;
        }

        conexionBDRestaurante.conectarBDRestaurante();
        mesaDAO = new MesaDAO(conexionBDRestaurante);

        try{
            int guardar = mesaDAO.asignarEncargado(mesa,choiceBoxTrabajador.getSelectionModel().getSelectedItem());
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        conexionBDRestaurante.cerrarBDRestaurante();
    }

    public Integer getMesa() {
        return mesa;
    }

    public void setMesa(Integer mesa) {
        this.mesa = mesa;
    }
}
