package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.MesaDAO;
import com.daltysfood.dao.restaurante.PedidoDAO;
import com.daltysfood.model.daltys.Usuario;
import com.daltysfood.model.restaurante.Pedido;
import com.daltysfood.util.ConexionBDRestaurante;
import com.daltysfood.util.Sesion;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class PedidosCerradosController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private TableView<Pedido> tablaCerrados;

    @FXML
    private TableColumn<Pedido, Integer> idCerrados;

    @FXML
    private TableColumn<Pedido, Date> horaInicioCerrados;

    @FXML
    private TableColumn<Pedido, String> estadoCerrados;

    @FXML
    private TableColumn<Pedido, String> clientesCerrados;

    @FXML
    private TableColumn<Pedido, Double> totalCerrados;

    @FXML
    private Label visualizacionPedido;

    private ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    PedidoDAO pedidoDAO;
    private ObjectProperty<Pedido> pedido = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visualizacionPedido.setText("<- Seleccione un elemento");

        listarPedidosCerrados();

        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        idCerrados.setCellValueFactory(new PropertyValueFactory<>("numeroDePedido"));
        horaInicioCerrados.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        estadoCerrados.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getEstadoPedido().getEstado());
            }
        });
        clientesCerrados.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getComensal().getNombre());
            }
        });
        totalCerrados.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));

        tablaCerrados.setItems(listaPedidos);
        pedido.bind(tablaCerrados.getSelectionModel().selectedItemProperty());

    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae los pedidos en curso
     */
    private void listarPedidosCerrados(){
        Task<List<Pedido>> listTask = new Task<List<Pedido>>() {
            @Override
            protected List<Pedido> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                pedidoDAO = new PedidoDAO(conexionBDRestaurante);
                return pedidoDAO.getPedidos(5);
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tablaCerrados.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tablaCerrados.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaPedidos.setAll(listTask.getValue());
            tablaCerrados.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tablaCerrados,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tablaCerrados.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de los pedidos cerrados
     * y carga la informacion de dicho pedido seleccionado
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItems(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        Pedido pedido = tablaCerrados.getSelectionModel().getSelectedItem();
        if(pedido == null){
            return;
        }
        conexionBDRestaurante.conectarBDRestaurante();
        pedidoDAO = new PedidoDAO(conexionBDRestaurante);
        visualizacionPedido.setText(pedidoDAO.getSelectedItem(pedido.getNumeroDePedido()));
    }
}
