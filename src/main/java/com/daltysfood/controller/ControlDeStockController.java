package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CategoriaDeProductoDAO;
import com.daltysfood.dao.restaurante.ProductoDAO;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.Producto;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class ControlDeStockController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<Producto> tableControl;

    @FXML
    private TableColumn<Integer, Producto> tableCProducto;

    @FXML
    private TableColumn<Integer, Producto> tableCStock;

    @FXML
    private TableColumn<Integer, Producto> tableCStockMinimo;

    @FXML
    private VBox informacionControl;

    @FXML
    private Label visualizacionControl;

    private EditarStockController editarStockController;
    private Stage stageStock;

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    private ProductoDAO productoDAO;
    private static Producto productoSeleccionado;
    private ObjectProperty<Producto> producto = new SimpleObjectProperty<>();
    private static final Button buttonEditar = new Button("Editar");

    /**
     * Inicializa la tabla y agrega el listener para editar el stock de un producto
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonEditar.setOnAction(e -> {
            try {
                editarStock();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        listarControl();
        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tableCStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tableCStockMinimo.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
        tableControl.setItems(listaProductos);
        producto.bind(tableControl.getSelectionModel().selectedItemProperty());
    }

    /**
     * Se encargas de obtener la informcion para llenar la tabla
     */
    private void listarControl(){
        Task<List<Producto>> listTask = new Task<List<Producto>>() {
            @Override
            protected List<Producto> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                productoDAO = new ProductoDAO(conexionBDRestaurante);
                return productoDAO.getProductos();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableControl.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableControl.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaProductos.setAll(listTask.getValue());
            tableControl.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableControl,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableControl.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /**
     * Se encarga de cargar la informacion y el boton de cada producto seleccionado en la tabla
     * @param mouseEvent Click en un elemento de la tabla
     */
    public void getItem(MouseEvent mouseEvent) {
        productoSeleccionado = tableControl.getSelectionModel().getSelectedItem();
        if(productoSeleccionado == null){
            return;
        }
        String infoStock = "";
        infoStock += "Disponible\t " + productoSeleccionado.getStock() +  "\n";
        infoStock += "Stock Minimo\t " + productoSeleccionado.getStockMinimo() +  "\n";
        infoStock += "Proveedor\t " + productoSeleccionado.getProveedor().getNombres() +  "\n";
        infoStock += "Permitir vender sin disponibilidad\t " + productoSeleccionado.getPermitirVender() +  "\n";
        visualizacionControl.setText(infoStock);
        if(!informacionControl.getChildren().contains(buttonEditar)){
            informacionControl.getChildren().add(buttonEditar);
        }

    }

    /**
     * Inicializa el formulario para editar el stock de un producto seleccionado
     * @throws IOException Si no encuentra el archivo visual para ediar el stock
     */
    private void editarStock() throws IOException {
        if(productoSeleccionado == null){
            return;
        }
        root.setEffect(new GaussianBlur(7.0));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditarStock.fxml"));
        AnchorPane ap = loader.load();
        editarStockController = loader.getController();
        Scene scene = new Scene(ap);
        stageStock = new Stage();
        stageStock.setScene(scene);
        stageStock.initOwner(root.getScene().getWindow());
        stageStock.initModality(Modality.WINDOW_MODAL);
        stageStock.initStyle(StageStyle.DECORATED);
        stageStock.setResizable(false);
        stageStock.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageStock.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        editarStockController.setProducto(productoSeleccionado);
        stageStock.setTitle("Editar Stock de Producto");
        stageStock.showAndWait();
        listarControl();
    }
}
