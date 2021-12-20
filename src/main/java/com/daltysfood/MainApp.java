package com.daltysfood;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Se encarga de cargar el archivo fxml login al stage
     * @param primaryStage Stage donde carga el archivo fxml del login
     * @throws Exception Si no encuentra el archivo
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        HBox hBox = fxmlLoader.load(getClass().getResource("/fxml/Login.fxml"));
        primaryStage.setScene(new Scene(hBox));
        primaryStage.show();
    }
}
