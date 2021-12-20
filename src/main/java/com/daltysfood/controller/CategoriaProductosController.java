package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CategoriaDeProductoDAO;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class CategoriaProductosController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private Button buttonNuevaCategoria;

    @FXML
    private TableView<CategoriaDeProducto> tableCategoriaDeProductos;

    @FXML
    private TableColumn<CategoriaDeProducto, String> tableCNombre;

    @FXML
    private TableColumn<CategoriaDeProducto, String> tableCCocina;

    @FXML
    private TableColumn<CategoriaDeProducto, Integer> tableCNumeroProductos;

    @FXML
    private VBox informacionCategoria;

    @FXML
    private Label visualizacionCategoria;

    private NuevaCategoriaProductoController nuevaCategoriaProductoController;
    private Stage stageCategoria;

    private ObservableList<CategoriaDeProducto> listaCategorias = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    CategoriaDeProductoDAO categoriaDeProductoDAO;
    private static CategoriaDeProducto categoriaDeProductoSeleccionada;
    private ObjectProperty<CategoriaDeProducto> categoriaDeProducto = new SimpleObjectProperty<>();
    private static final Button buttonEliminar = new Button("Eliminar");
    private static final Button buttonEditar = new Button("Editar");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonEditar.setOnAction(e -> {
            try {
                editarCategoria();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        buttonEliminar.setOnAction(event -> {eliminarCategoria(); });
        listarCategorias();

        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tableCCocina.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CategoriaDeProducto,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CategoriaDeProducto, String> param) {
                return new SimpleStringProperty(param.getValue().getCocina().getNombre());
            }
        });
        tableCNumeroProductos.setCellValueFactory(new PropertyValueFactory<>("numeroDeProductos"));
        tableCategoriaDeProductos.setItems(listaCategorias);
        categoriaDeProducto.bind(tableCategoriaDeProductos.getSelectionModel().selectedItemProperty());
    }


    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de las categorias de producto
     * y guarda la informacion del elemento que se desea eliminar
     */
    private void eliminarCategoria() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar la categoria?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText("Categoria: " + categoriaDeProductoSeleccionada.getNombre());
        if (a.showAndWait().get() == ButtonType.YES) {
            conexionBDRestaurante.cerrarBDRestaurante();
            conexionBDRestaurante.conectarBDRestaurante();
            categoriaDeProductoDAO = new CategoriaDeProductoDAO(conexionBDRestaurante);
            try {
                boolean delete = categoriaDeProductoDAO.deleteItem(categoriaDeProductoSeleccionada.getIdCategoriaDeProducto());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            conexionBDRestaurante.cerrarBDRestaurante();
            listarCategorias();
        }
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para editar una categoria de producto
     * Y carga la vista padre con la nueva infromacion
     * @throws IOException Si no encuentra el archivo fxml
     */
    private void editarCategoria() throws IOException {
        if(categoriaDeProductoSeleccionada == null){
            return;
        }
        root.setEffect(new GaussianBlur(7.0));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaCategoriaProducto.fxml"));
        AnchorPane ap = loader.load();
        nuevaCategoriaProductoController = loader.getController();
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
        nuevaCategoriaProductoController.setCategoriaDeProducto(categoriaDeProductoSeleccionada);
        stageCategoria.setTitle("Editar Categoria de Producto");
        stageCategoria.showAndWait();
        listarCategorias();
    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae las categorias de gastos
     */
    void listarCategorias(){
        Task<List<CategoriaDeProducto>> listTask = new Task<List<CategoriaDeProducto>>() {
            @Override
            protected List<CategoriaDeProducto> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                categoriaDeProductoDAO = new CategoriaDeProductoDAO(conexionBDRestaurante);
                return categoriaDeProductoDAO.getCategoriasDeProducto();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableCategoriaDeProductos.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableCategoriaDeProductos.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaCategorias.setAll(listTask.getValue());
            tableCategoriaDeProductos.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableCategoriaDeProductos,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableCategoriaDeProductos.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de las categorias de producto
     * y carga la informacion de dicha categoria seleccionada
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(MouseEvent mouseEvent) {
        categoriaDeProductoSeleccionada = tableCategoriaDeProductos.getSelectionModel().getSelectedItem();
        if(categoriaDeProductoSeleccionada == null){
            return;
        }
        String infoCategoria = "";
        infoCategoria += "Nombre\t " + categoriaDeProductoSeleccionada.getNombre() +  "\n";
        infoCategoria += "Cocina\t " + categoriaDeProductoSeleccionada.getCocina().getNombre() +  "\n";
        visualizacionCategoria.setText(infoCategoria);
        if(!informacionCategoria.getChildren().contains(buttonEditar)){
            informacionCategoria.getChildren().add(buttonEditar);
        }
        if( categoriaDeProductoSeleccionada.getNumeroDeProductos() != 0){
            if(informacionCategoria.getChildren().contains(buttonEliminar)){
                informacionCategoria.getChildren().remove(buttonEliminar);
            }
        }else{
            if(!informacionCategoria.getChildren().contains(buttonEliminar)){
                informacionCategoria.getChildren().add(buttonEliminar);
            }
        }
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para agregar una categoria de producto
     * Y carga la vista padre con la nueva infromacion
     * @param actionEvent Click en botón "nueva categoria"
     * @throws IOException Si no encuentra el archivo fxml
     */
    public void nuevaCategoria(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaCategoriaProducto.fxml"));
        AnchorPane ap = loader.load();
        nuevaCategoriaProductoController = loader.getController();
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
}
