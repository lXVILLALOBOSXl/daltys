package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.*;
import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class NuevaCuentaCorrienteController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private ChoiceBox<Proveedor> choiceBoxProveedor;

    @FXML
    private TextField textFieldMonto;

    @FXML
    private ChoiceBox<MedioDePago> choiceBoxMedioDePago;

    @FXML
    private CheckBox checkBoxArqueo;

    @FXML
    private TextArea textAreaComentario;

    private ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    private ProveedorDAO proveedorDAO;
    private MedioDePagoDAO medioDePagoDAO;
    private CuentaCorriente cuentaCorriente;
    private CuentaCorrienteDAO cuentaCorrienteDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conexionBDRestaurante.conectarBDRestaurante();
        medioDePagoDAO = new MedioDePagoDAO(conexionBDRestaurante);
        try {
            choiceBoxMedioDePago.setItems(medioDePagoDAO.getMediosDePagoActivos());
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
        
    }

    public void guardar(ActionEvent actionEvent) throws SQLException {
        if(choiceBoxProveedor.getSelectionModel().isEmpty() || textFieldMonto.getText().isEmpty() || choiceBoxMedioDePago.getSelectionModel().isEmpty()){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese los campos obligatorios").position(Pos.CENTER).showWarning();
            return;
        }

        try {
            Double.parseDouble(textFieldMonto.getText());
        }catch (Exception ex){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese Datos correctos").position(Pos.CENTER).showWarning();
        }

        cuentaCorriente = new CuentaCorriente();

        if(checkBoxArqueo.isSelected()){
            conexionBDRestaurante.conectarBDRestaurante();
            ArqueoDeCajaDAO arqueoDeCajaDAO = new ArqueoDeCajaDAO(conexionBDRestaurante);
            cuentaCorriente.setArqueoDeCaja(arqueoDeCajaDAO.getArqueoActivo());
            conexionBDRestaurante.cerrarBDRestaurante();
        }else{
            cuentaCorriente.setArqueoDeCaja(null);
        }

        TipoDeMovimiento tipoDeMovimiento = new TipoDeMovimiento();
        tipoDeMovimiento.setIdTipoDeMovimiento(3);
        cuentaCorriente.setEstaPagado(false);
        cuentaCorriente.setFecha(LocalDateTime.now());
        cuentaCorriente.setMonto(Double.parseDouble(textFieldMonto.getText()));
        cuentaCorriente.setMedioDePago(choiceBoxMedioDePago.getSelectionModel().getSelectedItem());
        cuentaCorriente.setTipoDeMovimiento(tipoDeMovimiento);
        cuentaCorriente.setProveedor(choiceBoxProveedor.getSelectionModel().getSelectedItem());
        cuentaCorriente.setComentario(textAreaComentario.getText());

        conexionBDRestaurante.conectarBDRestaurante();
        cuentaCorrienteDAO = new CuentaCorrienteDAO(conexionBDRestaurante);
        try {
            int guardar = cuentaCorrienteDAO.guardar(cuentaCorriente);
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
    }
}
