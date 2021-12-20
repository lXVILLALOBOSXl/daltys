package com.daltysfood.controller;

import animatefx.animation.Tada;
import com.daltysfood.dao.restaurante.*;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.Cocina;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;

public class NuevaCategoriaProductoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField textFieldNombreCategoria;

    @FXML
    private ChoiceBox<Cocina> choiceBoxCocina;

    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    CategoriaDeProductoDAO categoriaDeProductoDAO;
    CocinaDAO cocinaDAO;
    CategoriaDeProducto categoriaDeProducto;

    /***
     * Pide y carga las llaves foraneas para que se puedan seleccionar al momento de crear un nueva categoria
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conexionBDRestaurante.conectarBDRestaurante();
        cocinaDAO = new CocinaDAO(conexionBDRestaurante);

        try {
            choiceBoxCocina.setItems(cocinaDAO.getCocinas());
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
        if(textFieldNombreCategoria.getText().isEmpty()){
            new Tada(textFieldNombreCategoria).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese el nombre de la categoria").position(Pos.CENTER).showWarning();
            return;
        }

        if(getCategoriaDeProducto() ==null) {
            categoriaDeProducto = new CategoriaDeProducto();
        }

        categoriaDeProducto.setNombre(textFieldNombreCategoria.getText());
        categoriaDeProducto.setCocina(choiceBoxCocina.getSelectionModel().getSelectedItem());

        conexionBDRestaurante.conectarBDRestaurante();
        categoriaDeProductoDAO = new CategoriaDeProductoDAO(conexionBDRestaurante);

        try{
            int guardar = categoriaDeProductoDAO.guardar(categoriaDeProducto);
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        conexionBDRestaurante.cerrarBDRestaurante();
    }

    /**
     * Crga la informacion en el formulario de la categoria de producto que se va editar
     * @param categoriaDeProducto informacion de la categoria de producto que se va a editar
     */
    public void setCategoriaDeProducto(CategoriaDeProducto categoriaDeProducto) {
        this.categoriaDeProducto = categoriaDeProducto;
        textFieldNombreCategoria.setText(categoriaDeProducto.getNombre());
        if(categoriaDeProducto.getCocina() != null){
            choiceBoxCocina.getSelectionModel().select(categoriaDeProducto.getCocina().getIdCocina()-1);
        }
    }

    public CategoriaDeProducto getCategoriaDeProducto() {
        return categoriaDeProducto;
    }
}
