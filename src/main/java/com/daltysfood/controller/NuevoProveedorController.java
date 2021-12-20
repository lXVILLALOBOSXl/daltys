package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CategoriaDeProductoDAO;
import com.daltysfood.dao.restaurante.CocinaDAO;
import com.daltysfood.dao.restaurante.ProveedorDAO;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.Proveedor;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class NuevoProveedorController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField textFieldNombre;

    @FXML
    private JFXTextField textFieldCorreo;

    @FXML
    private JFXTextField textFieldTelefono;

    @FXML
    private JFXTextField textFieldCalle;

    @FXML
    private CheckBox checkBoxActivo;

    @FXML
    private JFXTextField textFieldRfc;

    @FXML
    private TextArea textAreaComentario;

    @FXML
    private TextField textFieldNumeroExt;

    @FXML
    private TextField textFieldNumeroInt;

    @FXML
    private TextField textFieldCP;

    @FXML
    private JFXTextField textFieldApelllidoP;

    @FXML
    private JFXTextField textFieldApelllidoM;

    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    ProveedorDAO proveedorDAO;
    private Proveedor proveedor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /***
     * Verifica que los datos del formulario sean correctos y envia la informacion ingresada para
     * guardarlo en la base de datos
     * @param actionEvent Click en el boton guardar
     */
    public void guardar(ActionEvent actionEvent) {
        if(textFieldNombre.getText().isEmpty() || textFieldCalle.getText().isEmpty() || textFieldNumeroExt.getText().isEmpty() || textFieldCP.getText().isEmpty()){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese los campos obligatorios").position(Pos.CENTER).showWarning();
            return;
        }

        if(getProveedor() == null){
            proveedor = new Proveedor();
        }

        try {
            Long.valueOf(textFieldTelefono.getText());
            Integer.parseInt(textFieldNumeroExt.getText());
            Integer.parseInt(textFieldNumeroInt.getText());
            Integer.parseInt(textFieldCP.getText());
        }catch (Exception ex){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese Datos correctos").position(Pos.CENTER).showWarning();
        }

        proveedor.setNombres(textFieldNombre.getText());
        proveedor.setCorreo(textFieldCorreo.getText());
        proveedor.setTelefono(textFieldTelefono.getText());
        proveedor.setCalle(textFieldCalle.getText());
        proveedor.setNumeroExterior(textFieldNumeroExt.getText());
        proveedor.setNumeroInterior(textFieldNumeroInt.getText());
        proveedor.setCodigoPostal(textFieldCP.getText());
        proveedor.setComentario(textAreaComentario.getText());
        proveedor.setEstaActivo(checkBoxActivo.isSelected());
        proveedor.setRfc(textFieldRfc.getText());
        proveedor.setApellidoPaterno(textFieldApelllidoP.getText());
        proveedor.setApellidoMaterno(textFieldApelllidoM.getText());

        conexionBDRestaurante.conectarBDRestaurante();
        proveedorDAO = new ProveedorDAO(conexionBDRestaurante);

        try{
            int guardar = proveedorDAO.guardar(proveedor);
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        conexionBDRestaurante.cerrarBDRestaurante();
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * Crga la informacion en el formulario del proveedor que se va editar
     * @param proveedor informacion del proveedor que se va a editar
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
        textFieldNombre.setText(proveedor.getNombres());
        textFieldApelllidoP.setText(proveedor.getApellidoPaterno());
        textFieldApelllidoM.setText(proveedor.getApellidoMaterno());
        textFieldCorreo.setText(proveedor.getCorreo());
        textFieldTelefono.setText(proveedor.getTelefono());
        textFieldCalle.setText(proveedor.getCalle());
        textFieldNumeroExt.setText(proveedor.getNumeroExterior());
        textFieldNumeroInt.setText(proveedor.getNumeroInterior());
        textFieldCP.setText(proveedor.getCodigoPostal());
        textFieldRfc.setText(proveedor.getRfc());
        textAreaComentario.setText(proveedor.getComentario());
        if(proveedor.getEstaActivo()){
            checkBoxActivo.setSelected(true);
        }
    }
}
