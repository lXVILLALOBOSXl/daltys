package com.daltysfood.controller;

import animatefx.animation.Tada;
import com.daltysfood.dao.restaurante.ArqueoDeCajaDAO;
import com.daltysfood.dao.restaurante.MedioDePagoDAO;
import com.daltysfood.dao.restaurante.MovimientosDeCajaDAO;
import com.daltysfood.dao.restaurante.TipoDeMovimientoDAO;
import com.daltysfood.model.restaurante.ArqueoDeCaja;
import com.daltysfood.model.restaurante.MedioDePago;
import com.daltysfood.model.restaurante.MovimientoDeCaja;
import com.daltysfood.model.restaurante.TipoDeMovimiento;
import com.daltysfood.util.ConexionBDRestaurante;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NuevoMovimientoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private ChoiceBox<TipoDeMovimiento> choiceBoxTipo;

    @FXML
    private ChoiceBox<MedioDePago> choiceBoxMedio;

    @FXML
    private JFXTextField textFieldMonto;

    @FXML
    private JFXTextArea textFieldComentario;

    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    MedioDePagoDAO medioDePagoDAO;
    TipoDeMovimientoDAO tipoDeMovimientoDAO;
    MovimientosDeCajaDAO movimientosDeCajaDAO;
    ArqueoDeCajaDAO arqueoDeCajaDAO;

    /***
     * Pide y carga las llaves foraneas para que se puedan seleccionar al momento de crear un nuevo movimientp
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conexionBDRestaurante.conectarBDRestaurante();
        medioDePagoDAO = new MedioDePagoDAO(conexionBDRestaurante);
        try {
            choiceBoxMedio.setItems(medioDePagoDAO.getMediosDePagoActivos());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
        conexionBDRestaurante.conectarBDRestaurante();
        tipoDeMovimientoDAO = new TipoDeMovimientoDAO(conexionBDRestaurante);
        try {
            choiceBoxTipo.setItems(tipoDeMovimientoDAO.getTiposDeMovimiento());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /***
     * Verifica que los datos del formulario sean correctos y envia la informacion ingresada para
     * guardarlo en la base de datos
     * @param event Click en el boton guardar
     */
    @FXML
    void guardar(ActionEvent event) throws SQLException {
        if (textFieldMonto.getText().isEmpty()) {
            new Tada(textFieldMonto).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese el monto del movimiento").position(Pos.CENTER).showWarning();
            return;
        }

        if (choiceBoxMedio.getSelectionModel().isEmpty()) {
            new Tada(textFieldMonto).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Seleccione un medio de pago").position(Pos.CENTER).showWarning();
            return;
        }

        if (choiceBoxTipo.getSelectionModel().isEmpty()) {
            new Tada(textFieldMonto).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Seleccione un tipo de movimiento").position(Pos.CENTER).showWarning();
            return;
        }

        MovimientoDeCaja movimientoDeCaja = new MovimientoDeCaja();
        TipoDeMovimiento tipoDeMovimiento = new TipoDeMovimiento();
        MedioDePago medioDePago = new MedioDePago();

        double monto = 0;
        String comentario = (textFieldComentario.getText().isEmpty()) ? "" :  textFieldComentario.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            monto = (Double.parseDouble(textFieldMonto.getText()));
        } catch (Exception ex) {
            new Tada(textFieldMonto).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Precio no valido\n" + ex.getMessage()).position(Pos.CENTER).showError();
            return;
        }

        movimientoDeCaja.setMonto(monto);
        movimientoDeCaja.setFecha(LocalDateTime.now());
        movimientoDeCaja.setComentario(comentario);
        movimientoDeCaja.setMedioDePago(choiceBoxMedio.getSelectionModel().getSelectedItem());
        movimientoDeCaja.setTipoDeMovimiento(choiceBoxTipo.getSelectionModel().getSelectedItem());

        conexionBDRestaurante.conectarBDRestaurante();
        arqueoDeCajaDAO = new ArqueoDeCajaDAO(conexionBDRestaurante);
        movimientoDeCaja.setArqueoDeCaja(arqueoDeCajaDAO.getArqueoActivo());
        conexionBDRestaurante.cerrarBDRestaurante();

        conexionBDRestaurante.conectarBDRestaurante();
        movimientosDeCajaDAO = new MovimientosDeCajaDAO(conexionBDRestaurante);
        try {
            int guardar = movimientosDeCajaDAO.guardar(movimientoDeCaja);
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
    }
}
