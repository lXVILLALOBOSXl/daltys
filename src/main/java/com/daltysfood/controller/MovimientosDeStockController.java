package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CategoriaDeProductoDAO;
import com.daltysfood.dao.restaurante.MovimientoDeStockDAO;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.MovimientoDeStock;
import com.daltysfood.model.restaurante.Pedido;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class MovimientosDeStockController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private TableView<MovimientoDeStock> tableMovimientos;

    @FXML
    private TableColumn<MovimientoDeStock, String> tableCEvento;

    @FXML
    private TableColumn<MovimientoDeStock, String> tableCNombre;

    @FXML
    private TableColumn<MovimientoDeStock, Integer> tableCStockAnterior;

    @FXML
    private TableColumn<MovimientoDeStock, Integer> tableCStockActual;

    @FXML
    private TableColumn<MovimientoDeStock, Integer> tableCDiferencia;

    @FXML
    private TableColumn<MovimientoDeStock, LocalDateTime> tableCFecha;

    @FXML
    private VBox informacionMovimientos;

    @FXML
    private Label visualizacionMovimientos;

    private ObservableList<MovimientoDeStock> listaMovimientosDeStock = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    MovimientoDeStockDAO movimientoDeStockDAO;
    private static MovimientoDeStock movimientoDeStockSeleccionado;
    private ObjectProperty<MovimientoDeStock> movimientoDeStock = new SimpleObjectProperty<>();

    /**
     * Se encarga de pedir la informacion e inicializar la tabla con dicha informacion
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listarMovimientos();
        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCStockActual.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        tableCStockAnterior.setCellValueFactory(new PropertyValueFactory<>("stockAnterior"));
        tableCFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        tableCNombre.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MovimientoDeStock,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MovimientoDeStock, String> param) {
                return new SimpleStringProperty(param.getValue().getProducto().getNombre());
            }
        });
        tableCEvento.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MovimientoDeStock,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MovimientoDeStock, String> param) {
                return new SimpleStringProperty(param.getValue().getTipoDeMovimientoDeStock().getNombreMovimientoDeStock());
            }
        });
        tableCDiferencia.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MovimientoDeStock,Integer>, ObservableValue<Integer>>(){

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<MovimientoDeStock, Integer> param) {
                return new ReadOnlyObjectWrapper((param.getValue().getStockActual())-(param.getValue().getStockAnterior()));
            }
        });
        tableMovimientos.setItems(listaMovimientosDeStock);
        movimientoDeStock.bind(tableMovimientos.getSelectionModel().selectedItemProperty());
    }

    /**
     * Pide e inicializa la informacion para despues mostrarla en la tabla
     */
    private void listarMovimientos() {
        Task<List<MovimientoDeStock>> listTask = new Task<List<MovimientoDeStock>>() {
            @Override
            protected List<MovimientoDeStock> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                movimientoDeStockDAO = new MovimientoDeStockDAO(conexionBDRestaurante);
                return movimientoDeStockDAO.getMovimientosDeStock();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableMovimientos.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableMovimientos.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaMovimientosDeStock.setAll(listTask.getValue());
            tableMovimientos.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableMovimientos,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableMovimientos.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /**
     * DEtermina cual es el objeto sobre el qe se hizo click y mustra la informacion en el panel lateral dela vista
     * @param mouseEvent Click en elemento de la tabla
     */
    public void getItem(MouseEvent mouseEvent) {
        movimientoDeStockSeleccionado = tableMovimientos.getSelectionModel().getSelectedItem();
        if(movimientoDeStockSeleccionado == null){
            return;
        }
        String infoStock = "";
        infoStock += "Evento\t " + movimientoDeStockSeleccionado.getTipoDeMovimientoDeStock().getNombreMovimientoDeStock() +  "\n";
        infoStock += "Producto\t " + movimientoDeStockSeleccionado.getProducto().getNombre() +  "\n";
        infoStock += "Stock Anterior\t " + movimientoDeStockSeleccionado.getStockAnterior() +  "\n";
        infoStock += "Stock Actual\t " + movimientoDeStockSeleccionado.getStockActual() +  "\n";
        infoStock += "Diferencia\t " + (movimientoDeStockSeleccionado.getStockActual() - movimientoDeStockSeleccionado.getStockAnterior()) +  "\n";
        infoStock += "Fecha\t " + movimientoDeStockSeleccionado.getFecha() +  "\n";
        if(movimientoDeStockSeleccionado.getPedidoProducto().getPedido().getFechaInicio() != null){
            infoStock += "VENTA\t " + "\n";
            infoStock += "Hora Inicio\t " + movimientoDeStockSeleccionado.getPedidoProducto().getPedido().getFechaInicio() +  "\n";
            infoStock += "Hora Cierre\t " + movimientoDeStockSeleccionado.getPedidoProducto().getPedido().getFechaFin() +  "\n";
            infoStock += "Total de la venta\t " + movimientoDeStockSeleccionado.getPedidoProducto().getPedido().getMontoTotal() +  "\n";
            infoStock += "Mesa\t " + movimientoDeStockSeleccionado.getPedidoProducto().getPedido().getMesa().getNumeroDeMesa() +  "\n";
        }
        visualizacionMovimientos.setText(infoStock);
    }
}
