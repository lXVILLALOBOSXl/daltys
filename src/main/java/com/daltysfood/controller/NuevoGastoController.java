package com.daltysfood.controller;

import animatefx.animation.Tada;
import com.daltysfood.dao.restaurante.*;
import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

public class NuevoGastoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private CheckBox checkBoxArqueo;

    @FXML
    private ChoiceBox<CategoriaDeGasto> choiceBoxCategoria;

    @FXML
    private ChoiceBox<MedioDePago> choiceBoxMedio;

    @FXML
    private ChoiceBox<Proveedor> choiceBoxProveedor;

    @FXML
    private JFXTextField textFieldMonto;

    @FXML
    private JFXTextField textFieldComprobante;

    @FXML
    private JFXTextArea textFieldComentario;

    ConexionBDRestaurante conexionBDRestaurante;
    GastosDAO gastosDAO;
    MedioDePagoDAO medioDePagoDAO;
    CategoriaDeGastoDAO categoriaDeGastoDAO;
    ProveedorDAO proveedorDAO;
    ArqueoDeCajaDAO arqueoDeCajaDAO;


    /***
     * Pide y carga las llaves foraneas para que se puedan seleccionar al momento de crear un nuevo  gasto
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conexionBDRestaurante = new ConexionBDRestaurante();
        conexionBDRestaurante.cerrarBDRestaurante();
        conexionBDRestaurante.conectarBDRestaurante();medioDePagoDAO = new MedioDePagoDAO(conexionBDRestaurante);
        try {
            choiceBoxMedio.setItems(medioDePagoDAO.getMediosDePagoActivos());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
        conexionBDRestaurante.conectarBDRestaurante();
        proveedorDAO = new ProveedorDAO(conexionBDRestaurante);
        try {
            choiceBoxProveedor.setItems(proveedorDAO.getProveedoresActivos());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
        conexionBDRestaurante.conectarBDRestaurante();
        categoriaDeGastoDAO = new CategoriaDeGastoDAO(conexionBDRestaurante);
        try {
            choiceBoxCategoria.setItems(categoriaDeGastoDAO.getCategoriasActivas());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();

    }

    /***
     * Verifica que los datos del formulario sean correctos y envia la informacion ingresada para
     * guardarlo en la base de datos
     * @param actionEvent Click en el boton guardar
     */
    public void guardar(ActionEvent actionEvent) throws SQLException {
        if (textFieldMonto.getText().isEmpty() || choiceBoxMedio.getSelectionModel().isEmpty() || choiceBoxProveedor.getSelectionModel().isEmpty() || textFieldComprobante.getText().isEmpty() || textFieldMonto.getText().isEmpty()) {
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese los campos obligatorios").position(Pos.CENTER).showWarning();
            return;
        }

        Gasto gasto = new Gasto();
        CategoriaDeGasto categoriaDeGasto = new CategoriaDeGasto();
        double importe = 0;
        int numeroDeComprobante = 0;
        String comentario = (textFieldComentario.getText().isEmpty()) ? "" :  textFieldComentario.getText().trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            importe = (Double.parseDouble(textFieldMonto.getText()));
        } catch (Exception ex) {
            org.controlsfx.control.Notifications.create().title("Aviso").text("Importe no valido\n" + ex.getMessage()).position(Pos.CENTER).showError();
            return;
        }

        try {
            numeroDeComprobante = (Integer.parseInt(textFieldComprobante.getText()));
        } catch (Exception ex) {
            org.controlsfx.control.Notifications.create().title("Aviso").text("Comprobante no valido\n" + ex.getMessage()).position(Pos.CENTER).showError();
            return;
        }

        gasto.setImporte(importe);
        gasto.setFecha(LocalDateTime.now());
        gasto.setComentario(comentario);
        gasto.setMedioDePago(choiceBoxMedio.getSelectionModel().getSelectedItem());
        gasto.setCategoriaDeGasto(choiceBoxCategoria.getSelectionModel().getSelectedItem());
        gasto.setProveedor(choiceBoxProveedor.getSelectionModel().getSelectedItem());
        gasto.setNumeroDeComprobante(numeroDeComprobante);

        if(checkBoxArqueo.isSelected()){
            conexionBDRestaurante.conectarBDRestaurante();
            arqueoDeCajaDAO = new ArqueoDeCajaDAO(conexionBDRestaurante);
            gasto.setArqueoDeCaja(arqueoDeCajaDAO.getArqueoActivo());
            conexionBDRestaurante.cerrarBDRestaurante();
        }else{
            gasto.setArqueoDeCaja(null);
        }

        conexionBDRestaurante.conectarBDRestaurante();
        gastosDAO = new GastosDAO(conexionBDRestaurante);
        try {
            int guardar = gastosDAO.guardar(gasto);
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
    }
}
