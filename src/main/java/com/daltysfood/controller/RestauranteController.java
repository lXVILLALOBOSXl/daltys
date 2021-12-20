package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.*;
import com.daltysfood.model.daltys.Licencia;
import com.daltysfood.model.daltys.Usuario;
import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;
import com.daltysfood.util.Sesion;
import com.jfoenix.controls.JFXTextField;
import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class RestauranteController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button buttonConfigurarMesas;

    @FXML
    private JFXButton buttonMesa1;

    @FXML
    private JFXButton buttonMesa2;

    @FXML
    private JFXButton buttonMesa3;

    @FXML
    private JFXButton buttonMesa4;

    @FXML
    private JFXButton buttonMesa5;

    @FXML
    private JFXButton buttonMesa6;

    @FXML
    private JFXButton buttonMesa7;

    @FXML
    private JFXButton buttonMesa8;

    @FXML
    private JFXButton buttonMesa9;

    @FXML
    private JFXButton buttonMesa10;

    @FXML
    private JFXButton buttonMesa11;

    @FXML
    private JFXButton buttonMesa12;

    @FXML
    private JFXButton buttonMesa13;

    @FXML
    private JFXButton buttonMesa14;

    @FXML
    private JFXButton buttonMesa15;

    @FXML
    private JFXButton buttonMesa16;

    @FXML
    private JFXButton buttonMesa17;

    @FXML
    private JFXButton buttonMesa18;

    @FXML
    private JFXButton buttonMesa19;

    @FXML
    private JFXButton buttonMesa20;

    @FXML
    private VBox visualizacionMesa;

    private MesaDAO mesaDAO;
    private ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante(Sesion.getSesion(new Usuario()));


    private Label seleccionaMesa;
    private Label informacionMesa = new Label();
    private Button buttonAgregarPersona = new Button("Agregar Persona");
    private Button buttonAsignarEncargado = new Button("Asignar Encargado");
    private static final String URL = "https://daltysfood.com/menu_online/index.php?restaurante=" + Sesion.getSesion(new Usuario()).getIdUsuario().toString() ;
    private AsignarEncargadoController asignarEncargadoController;
    private Stage stageEncargado;
    private Integer indexMesaSeleccionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(seleccionaMesa == null){
            seleccionaMesa = new Label("< Selecciona una mesa");
            visualizacionMesa.getChildren().add(seleccionaMesa);
        }

        buttonAgregarPersona.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URL(RestauranteController.URL).toURI());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

        buttonAsignarEncargado.setOnMouseClicked(event -> {
            try {
                asignarEncargado();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void asignarEncargado() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AsignarEncargado.fxml"));
        AnchorPane ap = loader.load();
        asignarEncargadoController = loader.getController();
        Scene scene = new Scene(ap);
        stageEncargado = new Stage();
        stageEncargado.setScene(scene);
        stageEncargado.initOwner(root.getScene().getWindow());
        stageEncargado.initModality(Modality.WINDOW_MODAL);
        stageEncargado.initStyle(StageStyle.DECORATED);
        stageEncargado.setResizable(false);
        stageEncargado.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageEncargado.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        asignarEncargadoController.setMesa(indexMesaSeleccionada);
        stageEncargado.setResizable(false);
        root.setEffect(new GaussianBlur(7.0));
        stageEncargado.showAndWait();
    }

    @FXML
    void mesa1(ActionEvent event) throws SQLException {
        cargarInformacionMesa(1);
    }

    @FXML
    void mesa10(ActionEvent event) throws SQLException {
        cargarInformacionMesa(10);
    }

    @FXML
    void mesa11(ActionEvent event) throws SQLException {
        cargarInformacionMesa(11);
    }

    @FXML
    void mesa12(ActionEvent event) throws SQLException {
        cargarInformacionMesa(12);
    }

    @FXML
    void mesa13(ActionEvent event) throws SQLException {
        cargarInformacionMesa(13);
    }

    @FXML
    void mesa14(ActionEvent event) throws SQLException {
        cargarInformacionMesa(14);
    }

    @FXML
    void mesa15(ActionEvent event) throws SQLException {
        cargarInformacionMesa(15);
    }

    @FXML
    void mesa16(ActionEvent event) throws SQLException {
        cargarInformacionMesa(16);
    }

    @FXML
    void mesa17(ActionEvent event) throws SQLException {
        cargarInformacionMesa(17);
    }

    @FXML
    void mesa18(ActionEvent event) throws SQLException {
        cargarInformacionMesa(18);
    }

    @FXML
    void mesa19(ActionEvent event) throws SQLException {
        cargarInformacionMesa(19);
    }

    @FXML
    void mesa2(ActionEvent event) throws SQLException {
        cargarInformacionMesa(2);
    }

    @FXML
    void mesa20(ActionEvent event) throws SQLException {
        cargarInformacionMesa(20);
    }

    @FXML
    void mesa3(ActionEvent event) throws SQLException {
        cargarInformacionMesa(3);
    }

    @FXML
    void mesa4(ActionEvent event) throws SQLException {
        cargarInformacionMesa(4);
    }

    @FXML
    void mesa5(ActionEvent event) throws SQLException {
        cargarInformacionMesa(5);
    }

    @FXML
    void mesa6(ActionEvent event) throws SQLException {
        cargarInformacionMesa(6);
    }

    @FXML
    void mesa7(ActionEvent event) throws SQLException {
        cargarInformacionMesa(7);
    }

    @FXML
    void mesa8(ActionEvent event) throws SQLException {
        cargarInformacionMesa(8);
    }

    @FXML
    void mesa9(ActionEvent event) throws SQLException {
        cargarInformacionMesa(9);
    }


    /**
     * Carga la informacion segun la mesa seleccionada
     * @param numeroDeMesa numero de mesa selcccionada
     */
    public void cargarInformacionMesa(int numeroDeMesa) throws SQLException {

        conexionBDRestaurante.conectarBDRestaurante();
        mesaDAO = new MesaDAO(conexionBDRestaurante);
        String infoMesa = mesaDAO.getMesa(numeroDeMesa);
        indexMesaSeleccionada = numeroDeMesa;
        informacionMesa.setMinSize(200,200);

        //Si ya existe una etiqueta indicandonds que seleccionemos una mesa
        if(seleccionaMesa != null) {
            //La eliminamos
            visualizacionMesa.getChildren().remove(seleccionaMesa);
        }
        //Si mi contenedor ya tiene una etiqueta de alguna mesa
        if(visualizacionMesa.getChildren().contains(informacionMesa)){
            //Solo modificamos el nombre
            informacionMesa.setText(infoMesa);
            return;
        }
        //Si no la tiene, la agregamos
        informacionMesa.setText(infoMesa);
        visualizacionMesa.getChildren().add(informacionMesa);
        visualizacionMesa.getChildren().add(buttonAgregarPersona);
        visualizacionMesa.getChildren().add(buttonAsignarEncargado);
    }

}
