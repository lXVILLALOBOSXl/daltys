package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.*;
import com.daltysfood.model.daltys.Usuario;
import com.daltysfood.model.restaurante.Producto;
import com.daltysfood.model.restaurante.Rol;
import com.daltysfood.model.restaurante.Trabajador;
import com.daltysfood.model.restaurante.Turno;
import com.daltysfood.util.ConexionBDRestaurante;
import com.daltysfood.util.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class NuevoTrabajadorController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField textFieldUsuario;

    @FXML
    private Label labelDominio;

    @FXML
    private TextField textFieldClave;

    @FXML
    private TextField textFieldClave2;

    @FXML
    private ChoiceBox<Rol> choiceBoxRol;

    @FXML
    private TextField textFieldNombre;

    @FXML
    private TextField textFieldApellidoPaterno;

    @FXML
    private TextField textFieldApellidoMaterno;

    @FXML
    private TextField textFieldTelefono;

    @FXML
    private ChoiceBox<Turno> choiceBoxTurno;

    @FXML
    private CheckBox checkBoxEstaActivo;

    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    RolDAO rolDAO;
    TurnoDAO turnoDAO;
    TrabajadorDAO trabajadorDAO;
    Trabajador trabajador;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelDominio.setText("@" + Sesion.getSesion(null).getCliente().getRestaurante().getNombreRestaurante().toLowerCase().replace(" ",""));
        conexionBDRestaurante.conectarBDRestaurante();
        rolDAO = new RolDAO(conexionBDRestaurante);
        turnoDAO = new TurnoDAO(conexionBDRestaurante);
        try {
            choiceBoxRol.setItems(rolDAO.getRoles());
            choiceBoxTurno.setItems(turnoDAO.getTurnos());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
        
    }

    public void guardar(ActionEvent actionEvent) {
        if(textFieldNombre.getText().isEmpty() || choiceBoxRol.getSelectionModel().isEmpty() || textFieldClave.getText().isEmpty() || textFieldClave2.getText().isEmpty() || textFieldUsuario.getText().isEmpty() || choiceBoxTurno.getSelectionModel().isEmpty()){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese los campos requeridos").position(Pos.CENTER).showWarning();
            return;
        }

        if(getTrabajador() == null){
            trabajador = new Trabajador();
        }

        trabajador.setNombres(textFieldNombre.getText());
        String apellidoPaterno = (textFieldApellidoPaterno.getText().isEmpty()) ? null : textFieldApellidoPaterno.getText();
        String apellidoMaterno = (textFieldApellidoMaterno.getText().isEmpty()) ? null : textFieldApellidoMaterno.getText();
        String telefono = (textFieldTelefono.getText().isEmpty()) ? null : textFieldTelefono.getText();
        trabajador.setApellidoPaterno(textFieldApellidoPaterno.getText());
        trabajador.setApellidoMaterno(textFieldApellidoMaterno.getText());
        trabajador.setTelefono(textFieldTelefono.getText());
        trabajador.setRol(choiceBoxRol.getValue());
        trabajador.setUsuario(textFieldUsuario.getText() + "@" + Sesion.getSesion(null).getCliente().getRestaurante().getNombreRestaurante().toLowerCase().replace(" ",""));
        trabajador.setEstaActivo(checkBoxEstaActivo.isSelected());
        trabajador.setTurno(choiceBoxTurno.getValue());
        if(textFieldClave.getText().equals(textFieldClave2.getText())){
            trabajador.setContrasena(textFieldClave.getText());
        }else{
            org.controlsfx.control.Notifications.create().title("Aviso").text("Las contraseñas no coinciden").position(Pos.CENTER).showWarning();
            return;
        }

        conexionBDRestaurante.conectarBDRestaurante();
        trabajadorDAO = new TrabajadorDAO(conexionBDRestaurante);

        try{
            int guardar = trabajadorDAO.guardar(trabajador);
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        conexionBDRestaurante.cerrarBDRestaurante();
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
        textFieldNombre.setText(trabajador.getNombres());
        if(trabajador.getTelefono() != null){
            textFieldTelefono.setText(trabajador.getTelefono());
        }
        if(trabajador.getApellidoMaterno() != null){
            textFieldApellidoMaterno.setText(trabajador.getApellidoMaterno());
        }
        if(trabajador.getApellidoPaterno() != null){
            textFieldApellidoPaterno.setText(trabajador.getApellidoPaterno());
        }
        if(trabajador.getEstaActivo()){
            checkBoxEstaActivo.setSelected(true);
        }
        try {
            choiceBoxRol.getSelectionModel().select(trabajador.getRol().getIdRol());
            choiceBoxTurno.getSelectionModel().select(trabajador.getTurno().getIdTurno());
        }catch (Exception ex){
            System.out.println("Error de index");
        }
        int indexArroba = trabajador.getUsuario().indexOf('@');
        textFieldUsuario.setText(trabajador.getUsuario().substring(0,indexArroba));

    }
}
