package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CategoriaDeProductoDAO;
import com.daltysfood.dao.restaurante.MovimientoDeStockDAO;
import com.daltysfood.dao.restaurante.MovimientosDeCajaDAO;
import com.daltysfood.dao.restaurante.ProductoDAO;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.MovimientoDeCaja;
import com.daltysfood.model.restaurante.Pedido;
import com.daltysfood.model.restaurante.Producto;
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
import javafx.event.Event;
import javafx.event.EventHandler;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import javax.swing.text.View;

public class ProductosController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private VBox vboxCategorias;

    @FXML
    private Button buttonNuevoProducto;

    @FXML
    private TableView<Producto> tableProducto;

    @FXML
    private TableColumn<Producto, Integer> tableCCodigo;

    @FXML
    private TableColumn<Producto, String> tableCNombre;

    @FXML
    private TableColumn<Producto, Double> tableCPrecio;

    @FXML
    private TableColumn<Producto, Double> tableCCosto;

    @FXML
    private TableColumn<Producto, Double> tableCMargen;

    @FXML
    private VBox informacionProducto;

    @FXML
    private Label visualizacionProducto;


    private List<Producto> productos;
    private CategoriaDeProductoDAO categoriaDeProductoDAO;
    private ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    private ProductoDAO productoDAO;
    private MovimientoDeStockDAO movimientoDeStockDAO;
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private ObjectProperty<Producto> producto = new SimpleObjectProperty<>();
    private static final Button buttonEditar = new Button("Editar");
    private static final Button buttonAgregar = new Button("+");
    private static final TextField textFieldCantidad = new TextField();
    private static final Label labelAgregarProductos = new Label("Agregar productos:");
    private static final HBox hboxAgregar = new HBox();
    private static Producto productoSeleccionado;

    private NuevoProductoController nuevoProductoController;
    private Stage stageProducto;

    /**
     * Agrega evento alos botones dinamicos, consulta los productos de la categoria donde
     * se hace click
     */
    private EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                if(conexionBDRestaurante.getConexionBDRestaurante().isClosed()){
                    conexionBDRestaurante.conectarBDRestaurante();
                }
                productoDAO = new ProductoDAO(conexionBDRestaurante);
                productos = productoDAO.getProductsByCategory(((Button)event.getSource()).getText());
                listarProductos();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFieldCantidad.setMaxSize(50,10);
        buttonNuevoProducto.setVisible(false);
        setCategoriesButtons(getCategories());
    }

    /***
     * Consulta las categorias de los productos
     * @return lista con categorias de productos
     */
    private List<CategoriaDeProducto> getCategories(){
        conexionBDRestaurante.conectarBDRestaurante();
        categoriaDeProductoDAO = new CategoriaDeProductoDAO(conexionBDRestaurante);
        try {
            return categoriaDeProductoDAO.getCategoriasDeProducto();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Agrega botones al layout, 1 boton por categoria
     * @param categorias categorias de productos
     */
    private void setCategoriesButtons(List<CategoriaDeProducto> categorias){
        Button buttonCategory = null;
        for (CategoriaDeProducto c : categorias) {
            buttonCategory = new Button(c.getNombre());
            buttonCategory.setOnAction(buttonHandler);
            vboxCategorias.getChildren().add(buttonCategory);
        }
    }

    /***
     * Se encarga de  crear los espacios correspondientes a la respuesta de la transaccion
     * que trae los productos segun en la categoria que se encuentran
     */
    private void listarProductos(){
        Task<List<Producto>> listTask = new Task<List<Producto>>() {
            @Override
            protected List<Producto> call() throws Exception {
                return productos;
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableProducto.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableProducto.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaProductos.setAll(listTask.getValue());
            tableProducto.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableProducto,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableProducto.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
        setProductos();
    }

    /***
     *Se encarga de inicializar las columnas de las tablas y agregar la informacion
     */
    private void setProductos(){
        if(!buttonNuevoProducto.isVisible()){
            buttonNuevoProducto.setVisible(true);
        }
        if(productos.size() != 0){
            tableCNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tableCCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
            tableCCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
            tableCPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            tableCMargen.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Producto,Double>, ObservableValue<Double>>(){
                @Override
                public ObservableValue<Double> call(TableColumn.CellDataFeatures<Producto, Double> param) {
                    return new ReadOnlyObjectWrapper((param.getValue().getPrecio())-(param.getValue().getCosto()));
                }
            });
            tableProducto.setItems(listaProductos);
            producto.bind(tableProducto.getSelectionModel().selectedItemProperty());
            return;
        }
        if(tableProducto.getItems().size() > 0){
            tableProducto.getItems().clear();
        }
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de laos productos
     * y carga la informacion de dicho producto seleccionado
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(MouseEvent mouseEvent) {
        productoSeleccionado = tableProducto.getSelectionModel().getSelectedItem();
        if(productoSeleccionado == null){
            return;
        }
        String infoProducto = "";
        infoProducto += "Nombre\t " + productoSeleccionado.getNombre() +  "\n";
        infoProducto += "Categoria\t " + productoSeleccionado.getCategoriaDeProducto().getNombre() +  "\n";
        infoProducto += "Costo\t " + productoSeleccionado.getCosto() +  "\n";
        infoProducto += "Precio\t " + productoSeleccionado.getPrecio() +  "\n";
        infoProducto += "Codigo\t " + productoSeleccionado.getCodigo() +  "\n";
        infoProducto += "Cocina\t " + productoSeleccionado.getCocina().getNombre() +  "\n";
        infoProducto += "Activo\t " + productoSeleccionado.getEstaActivo() +  "\n";
        infoProducto += "Proveedor\t " + productoSeleccionado.getProveedor().getNombres() +  "\n";
        infoProducto += "Descripcion\t " + productoSeleccionado.getDescripcion() +  "\n";
        infoProducto += "Control de Stock\t " + productoSeleccionado.getControl() +  "\n";
        infoProducto += "Permitir vender sin disponibilidad\t " + productoSeleccionado.getPermitirVender() +  "\n";
        visualizacionProducto.setText(infoProducto);
        if(informacionProducto.getChildren().contains(buttonEditar)){
            return;
        }else{
            /***
             * Si el boton no esta dentro de la vista, lo agregamos y le asignamos una funcion para eliminar el pedido
             * seleccionado
             */
            hboxAgregar.getChildren().add(labelAgregarProductos);
            hboxAgregar.getChildren().add(textFieldCantidad);
            hboxAgregar.getChildren().add(buttonAgregar);
            informacionProducto.getChildren().add(hboxAgregar);
            informacionProducto.getChildren().add(buttonEditar);
            buttonEditar.setOnAction(e-> {
                try {
                    editarProducto();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            buttonAgregar.setOnAction(e-> {
                try {
                    int nuevoStock = Integer.parseInt(textFieldCantidad.getText());
                    if(nuevoStock < 1 || agregarProducto(nuevoStock) < 1){
                        org.controlsfx.control.Notifications.create().title("Error").text("Verifique la cantidad a ingresar").position(Pos.CENTER).showWarning();
                        return;
                    }
                    textFieldCantidad.clear();
                    listarProductos();
                } catch (SQLException throwables) {
                    org.controlsfx.control.Notifications.create().title("Error").text("Verifique la cantidad a ingresar").position(Pos.CENTER).showWarning();
                    throwables.printStackTrace();
                }
            });

        }
    }

    /**
     * Se encarga de verificar el formulario que sirve para agregar unidades de un producto seleccionado
     * @param nuevoStock unidades que se desean agregar
     * @return registros afectados
     * @throws SQLException si hay algun problema con la transaccion
     */
    private int agregarProducto(int nuevoStock) throws SQLException {
        if(conexionBDRestaurante.getConexionBDRestaurante().isClosed()){
            conexionBDRestaurante.conectarBDRestaurante();
        }
        movimientoDeStockDAO = new MovimientoDeStockDAO(conexionBDRestaurante);
        return movimientoDeStockDAO.agregarStockManual(productoSeleccionado, nuevoStock);

    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para editar un producto
     * Y carga la vista padre con la nueva infromacion
     * @throws IOException Si no encuentra el archivo fxml
     */
    private void editarProducto() throws IOException{
        if(productoSeleccionado == null){
            return;
        }
        root.setEffect(new GaussianBlur(7.0));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoProducto.fxml"));
        AnchorPane ap = loader.load();
        nuevoProductoController = loader.getController();
        Scene scene = new Scene(ap);
        stageProducto = new Stage();
        stageProducto.setScene(scene);
        stageProducto.initOwner(root.getScene().getWindow());
        stageProducto.initModality(Modality.WINDOW_MODAL);
        stageProducto.initStyle(StageStyle.DECORATED);
        stageProducto.setResizable(false);
        stageProducto.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageProducto.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        nuevoProductoController.setProducto(productoSeleccionado);
        stageProducto.setTitle("Editar Producto");
        stageProducto.showAndWait();
        listarProductos();
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para agregar un nuevo producto
     * Y carga la vista padre con la nueva infromacion
     * @param actionEvent Click en botón "nuev o producto"
     * @throws IOException Si no encuentra el archivo fxml
     */
    public void nuevoProducto(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoProducto.fxml"));
        AnchorPane ap = loader.load();
        nuevoProductoController = loader.getController();
        Scene scene = new Scene(ap);
        stageProducto = new Stage();
        stageProducto.setScene(scene);
        stageProducto.initOwner(root.getScene().getWindow());
        stageProducto.initModality(Modality.WINDOW_MODAL);
        stageProducto.initStyle(StageStyle.DECORATED);
        stageProducto.setResizable(false);
        stageProducto.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageProducto.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageProducto.setResizable(false);
        root.setEffect(new GaussianBlur(7.0));
        stageProducto.showAndWait();
        listarProductos();
    }
}
