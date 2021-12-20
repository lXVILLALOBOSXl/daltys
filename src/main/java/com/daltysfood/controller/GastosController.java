package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.GastosDAO;
import com.daltysfood.dao.restaurante.MovimientosDeCajaDAO;
import com.daltysfood.model.restaurante.Gasto;
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
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class GastosController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private Button buttonNuevoGasto;

    @FXML
    private GridPane tableResumen;

    @FXML
    private Label labelRegistros;

    @FXML
    private Label labelTotal;

    @FXML
    private TableView<Gasto> tableGasto;

    @FXML
    private TableColumn<Gasto, LocalDate> tableCFecha;

    @FXML
    private TableColumn<Gasto, String> tableCProveedor;

    @FXML
    private TableColumn<Gasto, String> tableCCategoria;

    @FXML
    private TableColumn<Gasto, String> tableCComentario;

    @FXML
    private TableColumn<Gasto, Double> tableCImporte;

    @FXML
    private VBox informacionGasto;

    @FXML
    private Label visualizacionGasto;

    private static final Button buttonEliminarGasto = new Button("Eliminar");

    private NuevoGastoController nuevoGastoController;
    private Stage stageGasto;

    private ObservableList<Gasto> listaGasto = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    GastosDAO gastosDAO;
    private ObjectProperty<Gasto> gasto = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listarGastos();

        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        tableCImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
        tableCProveedor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Gasto,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Gasto, String> param) {
                return new SimpleStringProperty(param.getValue().getProveedor().getNombres());
            }
        });
        tableCCategoria.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Gasto,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Gasto, String> param) {
                return new SimpleStringProperty(param.getValue().getCategoriaDeGasto().getNombre());
            }
        });
        tableCComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));

        tableGasto.setItems(listaGasto);
        gasto.bind(tableGasto.getSelectionModel().selectedItemProperty());
    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae los gastos
     */
    void listarGastos(){
        try {
            setInfo();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Task<List<Gasto>> listTask = new Task<List<Gasto>>() {
            @Override
            protected List<Gasto> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                gastosDAO = new GastosDAO(conexionBDRestaurante);
                return gastosDAO.getGastos();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableGasto.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableGasto.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaGasto.setAll(listTask.getValue());
            tableGasto.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableGasto,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableGasto.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Muestra y pide la suma de los gastos y el numero de gastos
     * @throws SQLException Si hubo problema al traer la infromacion
     */
    void setInfo() throws SQLException {
        conexionBDRestaurante.cerrarBDRestaurante();
        conexionBDRestaurante.conectarBDRestaurante();
        gastosDAO = new GastosDAO(conexionBDRestaurante);
        List<Double> infoGastos = gastosDAO.getInfo();
        labelTotal.setText(infoGastos.get(0).toString());
        labelRegistros.setText(String.valueOf(infoGastos.get(1).intValue()));
        conexionBDRestaurante.cerrarBDRestaurante();
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para agregar un gasto
     * Y carga la vista padre con la nueva infromacion
     * @param actionEvent Click en botón "nuevo gasto"
     * @throws IOException Si no encuentra el archivo fxml
     */
    public void nuevoGasto(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoGasto.fxml"));
        AnchorPane ap = loader.load();
        nuevoGastoController = loader.getController();
        Scene scene = new Scene(ap);
        stageGasto = new Stage();
        stageGasto.setScene(scene);
        stageGasto.initOwner(root.getScene().getWindow());
        stageGasto.initModality(Modality.WINDOW_MODAL);
        stageGasto.initStyle(StageStyle.DECORATED);
        stageGasto.setResizable(false);
        stageGasto.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageGasto.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageGasto.setResizable(false);
        root.setEffect(new GaussianBlur(7.0));
        stageGasto.showAndWait();
        listarGastos();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de los movimientos de caja
     * y carga la informacion de dicho movimiento seleccionado
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(MouseEvent mouseEvent) {
        Gasto gasto = tableGasto.getSelectionModel().getSelectedItem();
        if(gasto == null){
            return;
        }
        String infoGasto = "";
        infoGasto += "Fecha\t " + gasto.getFecha() +  "\n";
        infoGasto += "Importe\t " + gasto.getImporte() +  "\n";
        infoGasto += "Medio de pago\t " + gasto.getMedioDePago().getNombre() +  "\n";
        infoGasto += "Proveedor\t " + gasto.getProveedor().getNombres() +  "\n";
        infoGasto += "Categoría\t " + gasto.getCategoriaDeGasto().getNombre() +  "\n";
        infoGasto += "Numero Comprobante\t " + gasto.getNumeroDeComprobante() +  "\n";
        infoGasto += "Usar en Arqueo\t " + gasto.getArqueoDeCaja().getEstaActiva() +  "\n";
        infoGasto += "Comentario\t " + gasto.getComentario() +  "\n";
        visualizacionGasto.setText(infoGasto);
        if(informacionGasto.getChildren().contains(buttonEliminarGasto)){
            return;
        }else{
            /***
             * Si el boton no esta dentro de la vista, lo agregamos y le asignamos una funcion para eliminar el pedido
             * seleccionado
             */
            informacionGasto.getChildren().add(buttonEliminarGasto);
            buttonEliminarGasto.setOnMouseClicked(e-> {
                conexionBDRestaurante.conectarBDRestaurante();
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar el movimiento?", ButtonType.YES, ButtonType.NO);
                a.setHeaderText("Gasto: " + gasto.getIdGasto().toString());
                if (a.showAndWait().get() == ButtonType.YES) {
                    conexionBDRestaurante.cerrarBDRestaurante();
                    conexionBDRestaurante.conectarBDRestaurante();
                    gastosDAO = new GastosDAO(conexionBDRestaurante);
                    try {
                        boolean delete = gastosDAO.deleteItem(gasto.getIdGasto());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    listarGastos();
                    conexionBDRestaurante.cerrarBDRestaurante();
                }
            });
        }
    }
}
