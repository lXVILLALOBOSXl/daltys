package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.PedidoDAO;
import com.daltysfood.dao.restaurante.VentasDAO;
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

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class PropinasController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private GridPane tableResumen;

    @FXML
    private Label labelRegistros;

    @FXML
    private Label labelTotalPropinas;

    @FXML
    private TableView<Pedido> tablePropinas;

    @FXML
    private TableColumn<Pedido, LocalDate> tableCFecha;

    @FXML
    private TableColumn<Pedido, Integer> tableCMesa;

    @FXML
    private TableColumn<Pedido, String> tableCCamarero;

    @FXML
    private TableColumn<Pedido, String> tableCCliente;

    @FXML
    private TableColumn<Pedido, String> tableCMedioDePago;

    @FXML
    private TableColumn<Pedido, Double> tableCTotalVenta;

    @FXML
    private TableColumn<Pedido, Double> tableCMonto;

    @FXML
    private Label visualizacionPropinas;

    private ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    PedidoDAO pedidoDAO;
    private ObjectProperty<Pedido> pedido = new SimpleObjectProperty<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listarPropinas();

        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        tableCMesa.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,Integer>, ObservableValue<Integer>>(){

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Pedido, Integer> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getMesa().getNumeroDeMesa());
            }
        });
        tableCCamarero.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getMesa().getTrabajador().getNombres());
            }
        });
        tableCCliente.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getComensal().getNombre());
            }
        });
        tableCMedioDePago.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getMedioDePago().getNombre());
            }
        });
        tableCMonto.setCellValueFactory(new PropertyValueFactory<>("propina"));
        tableCTotalVenta.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));

        tablePropinas.setItems(listaPedidos);
        pedido.bind(tablePropinas.getSelectionModel().selectedItemProperty());

    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae las ventas
     */
    void listarPropinas(){
        Task<List<Pedido>> listTask = new Task<List<Pedido>>() {
            @Override
            protected List<Pedido> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                pedidoDAO = new PedidoDAO(conexionBDRestaurante);
                return pedidoDAO.getPropinas();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tablePropinas.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tablePropinas.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaPedidos.setAll(listTask.getValue());
            Double sumaTotalPropinas = 0d;
            for (Pedido pedido : listaPedidos) {
                sumaTotalPropinas += pedido.getPropina();
            }
            labelTotalPropinas.setText(sumaTotalPropinas.toString());
            labelRegistros.setText("" + listaPedidos.size());
            tablePropinas.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tablePropinas,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tablePropinas.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de las propinas
     * y carga la informacion de dicha propina seleccionada
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(javafx.scene.input.MouseEvent mouseEvent) {
        Pedido pedido = tablePropinas.getSelectionModel().getSelectedItem();
        String infoPedido = "";
        if(pedido == null){
            return;
        }

        infoPedido += "PROPINA " +  "\n";
        infoPedido += "\tFecha " + pedido.getFechaFin() +  "\n";
        infoPedido += "\tMesa " + pedido.getMesa().getNumeroDeMesa() +  "\n";
        infoPedido += "\tCamar / Repar " + pedido.getMesa().getTrabajador().getNombres() +  "\n";
        infoPedido += "\tCliente " + pedido.getComensal().getNombre() +  "\n";
        infoPedido += "\tMedio de pago " + pedido.getMedioDePago().getNombre() +  "\n";
        infoPedido += "\tMonto " + pedido.getPropina() +  "\n";
        infoPedido += "VENTA " +  "\n";
        infoPedido += "\tHora Inicio " + pedido.getFechaInicio() +  "\n";
        infoPedido += "\tHora Cierre " + pedido.getFechaFin() +  "\n";
        infoPedido += "\tTotal de la venta " + pedido.getMontoTotal() +  "\n";

        visualizacionPropinas.setText(infoPedido);
    }
}
