package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.PedidoDAO;
import com.daltysfood.model.restaurante.Pedido;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class PedidosPendientesController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button buttonNuevoPedido;

    @FXML
    private TableView<Pedido> tablaPendientes;

    @FXML
    private TableColumn<Pedido, Integer> idPendientes;

    @FXML
    private TableColumn<Pedido, LocalDate> horaInicioPendientes;

    @FXML
    private TableColumn<Pedido, Integer>origenPendientes;

    @FXML
    private TableColumn<Pedido, String> clientesPendientes;

    @FXML
    private TableColumn<Pedido, Double> totalPendientes;

    @FXML
    private Label visualizacionPedido;

    private ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    PedidoDAO pedidoDAO;
    private ObjectProperty<Pedido> pedido = new SimpleObjectProperty<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listarPedidosPendientes();
        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        idPendientes.setCellValueFactory(new PropertyValueFactory<>("numeroDePedido"));
        horaInicioPendientes.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        origenPendientes.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,Integer>, ObservableValue<Integer>>(){

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Pedido, Integer> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getMesa().getNumeroDeMesa());
            }
        });
        clientesPendientes.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getComensal().getNombre());
            }
        });
        totalPendientes.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));

        tablaPendientes.setItems(listaPedidos);
        pedido.bind(tablaPendientes.getSelectionModel().selectedItemProperty());

    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae los pedidos pendientes
     */
    void listarPedidosPendientes(){
        Task<List<Pedido>> listTask = new Task<List<Pedido>>() {
            @Override
            protected List<Pedido> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                pedidoDAO = new PedidoDAO(conexionBDRestaurante);
                return pedidoDAO.getPedidos();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tablaPendientes.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tablaPendientes.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaPedidos.setAll(listTask.getValue());
            tablaPendientes.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tablaPendientes,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tablaPendientes.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    @FXML
    void nuevoPedido(ActionEvent event) {

    }


    public void getItems(MouseEvent mouseEvent) throws SQLException {
        Pedido pedido = tablaPendientes.getSelectionModel().getSelectedItem();
        if(pedido == null){
            return;
        }
        conexionBDRestaurante.conectarBDRestaurante();
        pedidoDAO = new PedidoDAO(conexionBDRestaurante);
        visualizacionPedido.setText(pedidoDAO.getSelectedItem(pedido.getNumeroDePedido()));
    }
}
