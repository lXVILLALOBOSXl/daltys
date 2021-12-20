package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CategoriaDeProductoDAO;
import com.daltysfood.dao.restaurante.MovimientoDeStockDAO;
import com.daltysfood.dao.restaurante.ProductoDAO;
import com.daltysfood.dao.restaurante.TrabajadorDAO;
import com.daltysfood.model.daltys.Usuario;
import com.daltysfood.model.restaurante.MovimientoDeStock;
import com.daltysfood.model.restaurante.Producto;
import com.daltysfood.model.restaurante.Trabajador;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Builder;
import javafx.util.Callback;

public class ConfiguracionGeneralController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private VBox vboxConfiguraciones;

    @FXML
    private Button buttonUsuarios;

    @FXML
    private Button buttonRoles;

    @FXML
    private Button buttonTurnos;

    @FXML
    private Button buttonMenu;

    @FXML
    private Button buttonMediosDePago;

    @FXML
    private Button buttonNuevoItem;

    @FXML
    private VBox informacionItem;

    @FXML
    private Label visualizacionItem;

    @FXML
    private GridPane gridTable;

    private TableColumn<Trabajador, String> tableCUsuario;
    private TableColumn<Trabajador, String> tableCRol;
    private TableColumn<Trabajador, String> tableCNombre;
    private TableView<Trabajador> tableTrabajador;
    private TrabajadorDAO trabajadorDAO;
    private ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    private ObservableList<Trabajador> listaTrabajadores = FXCollections.observableArrayList();
    private ObjectProperty<Trabajador> trabajador = new SimpleObjectProperty<>();
    private NuevoTrabajadorController nuevoTrabajadorController;
    private Stage stageTrabajador;
    private Trabajador trabajadorSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonUsuarios.setOnAction(e -> {checkLayout(0);});
        buttonRoles.setOnAction(e -> {checkLayout(1);});
        buttonTurnos.setOnAction(e -> {checkLayout(2);});
        buttonMenu.setOnAction(e -> {checkLayout(3);});
        buttonMediosDePago.setOnAction(e -> {checkLayout(4);});
    }

    private void getTrabajador() {
        trabajadorSeleccionado = tableTrabajador.getSelectionModel().getSelectedItem();
        if(trabajadorSeleccionado == null){
            return;
        }
        String infoTrabajador = "";
        infoTrabajador += "Usuario\t " + trabajadorSeleccionado.getUsuario() +  "\n";
        visualizacionItem.setText(infoTrabajador);
    }

    private void checkLayout(int indexButton) {
        if(!gridTable.getChildren().isEmpty()){
            gridTable.getChildren().remove(0);
            informacionItem.getChildren().clear();
        }
        switch (indexButton){
            case 0:
                createTableUsuario();
                break;
            case 1:
                createTableRolesDeUsuario();
                break;
            case 2:
                createTableTurnos();
                break;
            case 3:
                createMenu();
                break;
            case 4:
                createTableMetodosDePago();
                break;
            default:
                break;
        }


    }

    private void createTableUsuario(){
        tableCUsuario = new TableColumn<>("Usuario");
        tableCRol = new TableColumn<>("Rol");
        tableCNombre = new TableColumn<>("Nombre");
        tableTrabajador = new TableView<>();
        tableTrabajador.setOnMouseClicked(e -> {getTrabajador();});
        tableTrabajador.getColumns().add(tableCUsuario);
        tableTrabajador.getColumns().add(tableCRol);
        tableTrabajador.getColumns().add(tableCNombre);
        fillTableUsuario();
        Button buttonAgregar = new Button("+ Trabajador");
        buttonAgregar.setOnAction(e -> {
            try {
                nuevoUsuario();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttonAgregar.setAlignment(Pos.TOP_LEFT);
        informacionItem.getChildren().add(buttonAgregar);
        Button buttonEditar = new Button("Editar");
        buttonEditar.setOnAction(e -> {
            try {
                editarUsuario();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttonEditar.setAlignment(Pos.TOP_LEFT);
        informacionItem.getChildren().add(buttonEditar);
        gridTable.getChildren().add(tableTrabajador);
    }

    private void nuevoUsuario() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoTrabajador.fxml"));
        AnchorPane ap = loader.load();
        nuevoTrabajadorController = loader.getController();
        Scene scene = new Scene(ap);
        stageTrabajador = new Stage();
        stageTrabajador.setScene(scene);
        stageTrabajador.initOwner(root.getScene().getWindow());
        stageTrabajador.initModality(Modality.WINDOW_MODAL);
        stageTrabajador.initStyle(StageStyle.DECORATED);
        stageTrabajador.setResizable(false);
        stageTrabajador.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageTrabajador.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageTrabajador.setResizable(false);
        root.setEffect(new GaussianBlur(7.0));
        stageTrabajador.showAndWait();
        fillTableUsuario();
    }

    private void editarUsuario() throws IOException {
        if(trabajadorSeleccionado == null){
            return;
        }
        root.setEffect(new GaussianBlur(7.0));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoTrabajador.fxml"));
        AnchorPane ap = loader.load();
        nuevoTrabajadorController = loader.getController();
        Scene scene = new Scene(ap);
        stageTrabajador = new Stage();
        stageTrabajador.setScene(scene);
        stageTrabajador.initOwner(root.getScene().getWindow());
        stageTrabajador.initModality(Modality.WINDOW_MODAL);
        stageTrabajador.initStyle(StageStyle.DECORATED);
        stageTrabajador.setResizable(false);
        stageTrabajador.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageTrabajador.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        nuevoTrabajadorController.setTrabajador(trabajadorSeleccionado);
        stageTrabajador.setTitle("Editar Trabajador");
        stageTrabajador.showAndWait();
        fillTableUsuario();
    }

    private void fillTableUsuario(){
        Task<List<Trabajador>> listTask = new Task<List<Trabajador>>() {
            @Override
            protected List<Trabajador> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                trabajadorDAO = new TrabajadorDAO(conexionBDRestaurante);
                return trabajadorDAO.getTrabajadores();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableTrabajador.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableTrabajador.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaTrabajadores.setAll(listTask.getValue());
            tableTrabajador.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableTrabajador,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableTrabajador.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
        setUsuarios();
    }

    /***
     *Se encarga de inicializar las columnas de las tablas y agregar la informacion
     */
    private void setUsuarios(){
        tableCUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        tableCNombre.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        tableCRol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Trabajador,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Trabajador, String> param) {
                return new SimpleStringProperty(param.getValue().getRol().getRol());
            }
        });
        tableTrabajador.setItems(listaTrabajadores);
        trabajador.bind(tableTrabajador.getSelectionModel().selectedItemProperty());
    }

    private void createTableRolesDeUsuario(){
        tableCUsuario = new TableColumn<>("Usuario");
        tableCRol = new TableColumn<>("Rol");
        tableCNombre = new TableColumn<>("Nombre");
        tableTrabajador = new TableView<>();
        tableTrabajador.getColumns().add(tableCUsuario);
        tableTrabajador.getColumns().add(tableCRol);
        tableTrabajador.getColumns().add(tableCNombre);
        fillTableUsuario();
        Button button = new Button("+ 4");
        button.setAlignment(Pos.TOP_LEFT);
        informacionItem.getChildren().add(button);
        gridTable.getChildren().add(tableTrabajador);
    }

    private void createTableTurnos(){
        tableCUsuario = new TableColumn<>("Usuario");
        tableCRol = new TableColumn<>("Rol");
        tableCNombre = new TableColumn<>("Nombre");
        tableTrabajador = new TableView<>();
        tableTrabajador.getColumns().add(tableCUsuario);
        tableTrabajador.getColumns().add(tableCRol);
        tableTrabajador.getColumns().add(tableCNombre);
        fillTableUsuario();
        Button button = new Button("+ 3");
        button.setAlignment(Pos.TOP_LEFT);
        informacionItem.getChildren().add(button);
        gridTable.getChildren().add(tableTrabajador);
    }

    private void createMenu(){
        tableCUsuario = new TableColumn<>("Usuario");
        tableCRol = new TableColumn<>("Rol");
        tableCNombre = new TableColumn<>("Nombre");
        tableTrabajador = new TableView<>();
        tableTrabajador.getColumns().add(tableCUsuario);
        tableTrabajador.getColumns().add(tableCRol);
        tableTrabajador.getColumns().add(tableCNombre);
        fillTableUsuario();
        Button button = new Button("+ 2");
        button.setAlignment(Pos.TOP_LEFT);
        informacionItem.getChildren().add(button);
        gridTable.getChildren().add(tableTrabajador);
    }

    private void createTableMetodosDePago(){
        tableCUsuario = new TableColumn<>("Usuario");
        tableCRol = new TableColumn<>("Rol");
        tableCNombre = new TableColumn<>("Nombre");
        tableTrabajador = new TableView<>();
        tableTrabajador.getColumns().add(tableCUsuario);
        tableTrabajador.getColumns().add(tableCRol);
        tableTrabajador.getColumns().add(tableCNombre);
        fillTableUsuario();
        Button button = new Button("+ 1");
        button.setAlignment(Pos.TOP_LEFT);
        informacionItem.getChildren().add(button);
        gridTable.getChildren().add(tableTrabajador);
    }
}
