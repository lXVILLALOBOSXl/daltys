package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.MesaDAO;
import com.daltysfood.model.daltys.Usuario;
import com.daltysfood.model.restaurante.Mesa;
import com.daltysfood.util.ConexionBDRestaurante;
import com.daltysfood.util.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TerrazaController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton buttonMesa21;

    @FXML
    private JFXButton buttonMesa22;

    @FXML
    private JFXButton buttonMesa23;

    @FXML
    private JFXButton buttonMesa24;

    @FXML
    private JFXButton buttonMesa25;

    @FXML
    private JFXButton buttonMesa26;

    @FXML
    private JFXButton buttonMesa27;

    @FXML
    private JFXButton buttonMesa28;

    @FXML
    private JFXButton buttonMesa29;

    @FXML
    private JFXButton buttonMesa30;

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
                Desktop.getDesktop().browse(new URL(TerrazaController.URL).toURI());
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
    void mesa21(ActionEvent event) throws SQLException {
        cargarInformacionMesa(21);
    }

    @FXML
    void mesa22(ActionEvent event) throws SQLException {
        cargarInformacionMesa(22);
    }

    @FXML
    void mesa23(ActionEvent event) throws SQLException {
        cargarInformacionMesa(23);
    }

    @FXML
    void mesa24(ActionEvent event) throws SQLException {
        cargarInformacionMesa(24);

    }

    @FXML
    void mesa25(ActionEvent event) throws SQLException {
        cargarInformacionMesa(25);

    }

    @FXML
    void mesa26(ActionEvent event) throws SQLException {
        cargarInformacionMesa(26);

    }

    @FXML
    void mesa27(ActionEvent event) throws SQLException {
        cargarInformacionMesa(27);

    }

    @FXML
    void mesa28(ActionEvent event) throws SQLException {
        cargarInformacionMesa(28);

    }

    @FXML
    void mesa29(ActionEvent event) throws SQLException {
        cargarInformacionMesa(29);

    }

    @FXML
    void mesa30(ActionEvent event) throws SQLException {
        cargarInformacionMesa(30);
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

