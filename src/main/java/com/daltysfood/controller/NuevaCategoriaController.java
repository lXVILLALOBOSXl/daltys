package com.daltysfood.controller;

import animatefx.animation.Tada;
import com.daltysfood.dao.restaurante.ArqueoDeCajaDAO;
import com.daltysfood.dao.restaurante.CategoriaDeGastoDAO;
import com.daltysfood.model.restaurante.CategoriaDeGasto;
import com.daltysfood.util.ConexionBDRestaurante;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NuevaCategoriaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField textFieldNombreCategoria;

    @FXML
    private CheckBox checkEstaActiva;

    private ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    private CategoriaDeGastoDAO categoriaDeGastoDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void guardar(ActionEvent actionEvent) {
        if (textFieldNombreCategoria.getText().isEmpty()) {
            new Tada(textFieldNombreCategoria).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese el nombre de la categoría").position(Pos.CENTER).showWarning();
            return;
        }

        CategoriaDeGasto categoriaDeGasto = new CategoriaDeGasto();
        categoriaDeGasto.setNombre(textFieldNombreCategoria.getText());
        Boolean estaActiva = checkEstaActiva.isSelected() ? true : false;
        categoriaDeGasto.setEstaActiva(estaActiva);

        conexionBDRestaurante.conectarBDRestaurante();
        categoriaDeGastoDAO = new CategoriaDeGastoDAO(conexionBDRestaurante);
        try {
            int guardar = categoriaDeGastoDAO.guardar(categoriaDeGasto);
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();
    }

}
