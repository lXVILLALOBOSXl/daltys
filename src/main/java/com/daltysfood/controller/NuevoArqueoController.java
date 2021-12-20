package com.daltysfood.controller;

import animatefx.animation.Tada;
import com.daltysfood.dao.restaurante.ArqueoDeCajaDAO;
import com.daltysfood.dao.restaurante.MovimientosDeCajaDAO;
import com.daltysfood.model.restaurante.ArqueoDeCaja;
import com.daltysfood.util.ConexionBDRestaurante;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class NuevoArqueoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField textFieldMonto;

    private ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    private ArqueoDeCajaDAO arqueoDeCajaDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void guardar(ActionEvent actionEvent) throws SQLException {

        if (textFieldMonto.getText().isEmpty()) {
            new Tada(textFieldMonto).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese el monto del movimiento").position(Pos.CENTER).showWarning();
            return;
        }

        conexionBDRestaurante.conectarBDRestaurante();
        arqueoDeCajaDAO = new ArqueoDeCajaDAO(conexionBDRestaurante);

        /***
         * Si ya existe un arqueo activo no se puede crear otro
         */
        if(arqueoDeCajaDAO.getArqueoActivo() != null){
            new Tada(textFieldMonto).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Actualmente ya existe un arqueo activo, cierrelo para crear otro nuevo").position(Pos.CENTER).showWarning();
            conexionBDRestaurante.cerrarBDRestaurante();
            return;
        }

        ArqueoDeCaja arqueoDeCaja = new ArqueoDeCaja();
        double monto = 0;

        try {
            monto = (Double.parseDouble(textFieldMonto.getText()));
        } catch (Exception ex) {
            new Tada(textFieldMonto).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Precio no valido\n" + ex.getMessage()).position(Pos.CENTER).showError();
            return;
        }
        arqueoDeCaja.setMontoInicial(monto);
        arqueoDeCaja.setSistema(monto);
        arqueoDeCaja.setFechaInicio(LocalDateTime.now());

        conexionBDRestaurante.conectarBDRestaurante();
        arqueoDeCajaDAO = new ArqueoDeCajaDAO(conexionBDRestaurante);
        try {
            int guardar = arqueoDeCajaDAO.guardar(arqueoDeCaja);
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
    }
}
