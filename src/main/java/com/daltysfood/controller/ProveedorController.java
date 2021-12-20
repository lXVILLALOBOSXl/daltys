package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CategoriaDeProductoDAO;
import com.daltysfood.dao.restaurante.ProveedorDAO;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.Proveedor;
import com.daltysfood.util.ConexionBDRestaurante;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class ProveedorController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button buttonNuevoProveedor;

    @FXML
    private TableView<Proveedor> tableProveedor;

    @FXML
    private TableColumn<Proveedor, String> tableCNombre;

    @FXML
    private TableColumn<Proveedor, String> tableCEmail;

    @FXML
    private TableColumn<Proveedor, String> tableCTelefono;

    @FXML
    private TableColumn<Proveedor, String> tableCDireccion;

    @FXML
    private TableColumn<Proveedor, Double> tableCSaldo;

    @FXML
    private TableColumn<Proveedor, Boolean> tableCActivo;

    @FXML
    private VBox informacionProveedor;

    @FXML
    private Label visualizacionProveedor;

    private NuevoProveedorController nuevoProveedorController;
    private Stage stageProveedor;

    private ObservableList<Proveedor> listaProveedores = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    ProveedorDAO proveedorDAO;
    private static Proveedor proveedorSeleccionado;
    private ObjectProperty<Proveedor> proveedor = new SimpleObjectProperty<>();
    private static final Button buttonEliminar = new Button("Eliminar");
    private static final Button buttonEditar = new Button("Editar");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonEditar.setOnAction(e -> {
            try {
                editarProveedor();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttonEliminar.setOnAction(event -> {eliminarProveedor(); });
        listarProveedores();
        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCNombre.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        tableCEmail.setCellValueFactory(new PropertyValueFactory<>("correo"));
        tableCTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tableCDireccion.setCellValueFactory(new PropertyValueFactory<>("calle"));
        tableCSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        tableCActivo.setCellValueFactory(new PropertyValueFactory<>("estaActivo"));
        tableProveedor.setItems(listaProveedores);
        proveedor.bind(tableProveedor.getSelectionModel().selectedItemProperty());
    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae los proveeodres
     */
    void listarProveedores(){
        Task<List<Proveedor>> listTask = new Task<List<Proveedor>>() {
            @Override
            protected List<Proveedor> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                proveedorDAO = new ProveedorDAO(conexionBDRestaurante);
                return proveedorDAO.getProveedores();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableProveedor.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableProveedor.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaProveedores.setAll(listTask.getValue());
            tableProveedor.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableProveedor,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableProveedor.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para agregar un nuevo proveedor
     * Y carga la vista padre con la nueva infromacion
     * @param actionEvent Click en botón "nuevo proveedor"
     * @throws IOException Si no encuentra el archivo fxml
     */
    public void nuevoProveedor(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoProveedor.fxml"));
        AnchorPane ap = loader.load();
        nuevoProveedorController = loader.getController();
        Scene scene = new Scene(ap);
        stageProveedor = new Stage();
        stageProveedor.setScene(scene);
        stageProveedor.initOwner(root.getScene().getWindow());
        stageProveedor.initModality(Modality.WINDOW_MODAL);
        stageProveedor.initStyle(StageStyle.DECORATED);
        stageProveedor.setResizable(false);
        stageProveedor.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageProveedor.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        root.setEffect(new GaussianBlur(7.0));
        stageProveedor.showAndWait();
        listarProveedores();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de los proveedores
     * y carga la informacion de dicho proveedor
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(MouseEvent mouseEvent) {
        proveedorSeleccionado = tableProveedor.getSelectionModel().getSelectedItem();
        if(proveedorSeleccionado == null){
            return;
        }
        String infoProveedor = "";
        visualizacionProveedor.setText(infoProveedor);
        infoProveedor += "Nombre\t " + proveedorSeleccionado.getNombres() +  "\n";
        infoProveedor += "Email\t " + proveedorSeleccionado.getCorreo() +  "\n";
        infoProveedor += "Teléfono\t " + proveedorSeleccionado.getTelefono() +  "\n";
        infoProveedor += "Dirección\t " + proveedorSeleccionado.getCalle() + " " + proveedorSeleccionado.getNumeroExterior().toString() +  "\n";
        infoProveedor += "RFC\t " + proveedorSeleccionado.getRfc() +  "\n";
        infoProveedor += "Comnatario\t " + proveedorSeleccionado.getComentario() +  "\n";
        infoProveedor += "Activo\t " + proveedorSeleccionado.getEstaActivo() +  "\n";
        visualizacionProveedor.setText(infoProveedor);
        if(!informacionProveedor.getChildren().contains(buttonEditar) ||!informacionProveedor.getChildren().contains(buttonEliminar)){
            informacionProveedor.getChildren().add(buttonEditar);
            informacionProveedor.getChildren().add(buttonEliminar);
        }
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de los proveedores
     * y guarda la informacion del elemento que se desea eliminar
     */
    private void eliminarProveedor() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar la categoria?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText("proveedor: " + proveedorSeleccionado.getNombres());
        if (a.showAndWait().get() == ButtonType.YES) {
            conexionBDRestaurante.cerrarBDRestaurante();
            conexionBDRestaurante.conectarBDRestaurante();
            proveedorDAO = new ProveedorDAO(conexionBDRestaurante);
            try {
                int delete = proveedorDAO.deleteItem(proveedorSeleccionado.getIdProveedor());
            } catch (SQLException throwables) {
                org.controlsfx.control.Notifications.create().title("Aviso").text("Este proveedor ya ha sido utilizado").position(Pos.CENTER).showWarning();
            }
            conexionBDRestaurante.cerrarBDRestaurante();
            listarProveedores();
        }
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para editar un proveedor
     * Y carga la vista padre con la nueva infromacion
     * @throws IOException Si no encuentra el archivo fxml
     */
    private void editarProveedor() throws IOException {
        if(proveedorSeleccionado == null){
            return;
        }
        root.setEffect(new GaussianBlur(7.0));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoProveedor.fxml"));
        AnchorPane ap = loader.load();
        nuevoProveedorController = loader.getController();
        Scene scene = new Scene(ap);
        stageProveedor = new Stage();
        stageProveedor.setScene(scene);
        stageProveedor.initOwner(root.getScene().getWindow());
        stageProveedor.initModality(Modality.WINDOW_MODAL);
        stageProveedor.initStyle(StageStyle.DECORATED);
        stageProveedor.setResizable(false);
        stageProveedor.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageProveedor.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        nuevoProveedorController.setProveedor(proveedorSeleccionado);
        stageProveedor.setTitle("Editar Proveedor");
        stageProveedor.showAndWait();
        listarProveedores();
    }


}
