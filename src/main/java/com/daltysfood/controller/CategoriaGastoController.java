package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CategoriaDeGastoDAO;
import com.daltysfood.dao.restaurante.MovimientosDeCajaDAO;
import com.daltysfood.model.restaurante.CategoriaDeGasto;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.MovimientoDeCaja;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class CategoriaGastoController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button buttonNuevaCategoria;

    @FXML
    private TableView<CategoriaDeGasto> tableCategoriaDeGastos;

    @FXML
    private TableColumn<String, CategoriaDeGasto> tableCNombre;

    @FXML
    private VBox informacionCategoria;

    @FXML
    private Label visualizacionCategoria;

    private NuevaCategoriaController nuevaCategoriaController;
    private Stage stageCategoria;

    private ObservableList<CategoriaDeGasto> listaCategorias = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    CategoriaDeGastoDAO categoriaDeGastoDAO;
    private ObjectProperty<CategoriaDeGasto> categoriaDeGasto = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listarCategorias();

        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tableCategoriaDeGastos.setItems(listaCategorias);
        categoriaDeGasto.bind(tableCategoriaDeGastos.getSelectionModel().selectedItemProperty());
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para agregar una nueva categoria
     * Y carga la vista padre con la nueva infromacion
     * @param actionEvent Click en botón "nueva categoria"
     * @throws IOException Si no encuentra el archivo fxml
     */
    public void nuevaCategoria(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaCategoria.fxml"));
        AnchorPane ap = loader.load();
        nuevaCategoriaController = loader.getController();
        Scene scene = new Scene(ap);
        stageCategoria = new Stage();
        stageCategoria.setScene(scene);
        stageCategoria.initOwner(root.getScene().getWindow());
        stageCategoria.initModality(Modality.WINDOW_MODAL);
        stageCategoria.initStyle(StageStyle.DECORATED);
        stageCategoria.setResizable(false);
        stageCategoria.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageCategoria.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageCategoria.setResizable(false);
        root.setEffect(new GaussianBlur(7.0));
        stageCategoria.showAndWait();
        listarCategorias();
    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae las categorias de gastos
     */
    void listarCategorias(){
        Task<List<CategoriaDeGasto>> listTask = new Task<List<CategoriaDeGasto>>() {
            @Override
            protected List<CategoriaDeGasto> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                categoriaDeGastoDAO = new CategoriaDeGastoDAO(conexionBDRestaurante);
                return categoriaDeGastoDAO.getCategoriasDeGasto();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableCategoriaDeGastos.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableCategoriaDeGastos.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaCategorias.setAll(listTask.getValue());
            tableCategoriaDeGastos.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableCategoriaDeGastos,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableCategoriaDeGastos.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de las categorias de gasto
     * y carga la informacion de dicha categoria seleccionada
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(MouseEvent mouseEvent) {
        CategoriaDeGasto categoriaDeGasto = tableCategoriaDeGastos.getSelectionModel().getSelectedItem();
        if(categoriaDeGasto == null){
            return;
        }
        String infoCategoria = "";
        infoCategoria += "Nombre\t " + categoriaDeGasto.getNombre() +  "\n";
        infoCategoria += "Activa\t " + categoriaDeGasto.getEstaActiva() +  "\n";
        visualizacionCategoria.setText(infoCategoria);
    }
}
