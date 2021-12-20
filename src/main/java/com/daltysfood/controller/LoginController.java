package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.UsuarioDAO;
import com.daltysfood.model.daltys.Usuario;
import com.daltysfood.util.ConexionBDDaltys;
import com.daltysfood.util.ConexionBDRestaurante;
import com.daltysfood.util.Sesion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private HBox root;

    @FXML
    private JFXButton btnWhatsapp;

    @FXML
    private JFXButton btnPagina;

    @FXML
    private JFXTextField cjUsername;

    @FXML
    private JFXPasswordField cjPassword;

    @FXML
    private JFXButton btnIniciar;

    private ConexionBDDaltys conexionBDDaltys;
    private ConexionBDRestaurante conexionBDRestaurante;
    private UsuarioDAO usuarioDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /***
     * Se encarga de recibir los datos del fxml, recibe los datos de la transaccion
     * y segun los datos nos da una respuesta
     * @param event Click en iniciar sesion
     * @throws SQLException Si la conexion es incorrecta
     * @throws IOException Si alguno de los archivos es incorrecto
     */
    @FXML
    void login(ActionEvent event) throws SQLException, IOException {

        String username = cjUsername.getText().trim();
        String password = cjPassword.getText().trim();
        conexionBDDaltys = new ConexionBDDaltys();
        conexionBDDaltys.conectarBDDaltys();
        usuarioDAO = new UsuarioDAO(conexionBDDaltys);
        Usuario usuario = usuarioDAO.getUsuario(username, password);

        //La conexion es correcta y la transaccion tambien, sin embargo no se encontro en la base de datos
        if (usuario == null){
            //Anunciar con ventana emergente que el usuario que se ingreso es incorrecto
            new animatefx.animation.Tada(cjUsername).play();
            new animatefx.animation.Tada(cjPassword).play();

            org.controlsfx.control.Notifications
                    .create()
                    .title("Mensaje")
                    .text("El usuario o contraseña no coinciden")
                    .position(Pos.CENTER).showInformation();
            return;
        }

        //Se encontro en la BD, pero su licencia no es valida
        if(!usuario.getLicencia().isValid()){
            //Anunciar con ventana emergente que el usuario no tiene una licencia valida
            new animatefx.animation.Tada(cjUsername).play();
            new animatefx.animation.Tada(cjPassword).play();

            org.controlsfx.control.Notifications
                    .create()
                    .title("Mensaje")
                    .text("Vuelve con nosotros, activa tu licencia")
                    .position(Pos.CENTER).showInformation();
            return;
        }

        //Establece una conexion con la bd del restaurante ingresado
        conexionBDRestaurante = new ConexionBDRestaurante(usuario);
        conexionBDRestaurante.conectarBDRestaurante();

        //Se carga la siguiente escena
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
        BorderPane borderPane = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(borderPane));
        stage.setTitle(Sesion.getSesion(null).getCliente().getRestaurante().getNombreRestaurante());
        stage.setMaximized(true);
        com.daltysfood.util.Metodos.closeEffect(root);
        stage.show();

    }

    public void openWhatsapp(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URL("https://www.facebook.com/daltysfood").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void openDaltysPage(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URL("https://daltysfood.com/").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
