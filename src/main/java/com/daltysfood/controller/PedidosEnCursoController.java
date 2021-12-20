package com.daltysfood.controller;
import com.daltysfood.dao.restaurante.PedidoDAO;
import com.daltysfood.model.restaurante.Pedido;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PedidosEnCursoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private TableView<Pedido> tablaEnCurso;

    @FXML
    private TableColumn<Pedido, Double> idEnCurso;

    @FXML
    private TableColumn<Pedido, Date> horaInicioEnCurso;

    @FXML
    private TableColumn<Pedido, String> estadoEnCurso;

    @FXML
    private TableColumn<Pedido, String> clientesEnCurso;

    @FXML
    private TableColumn<Pedido, Double> totalEnCurso;

    @FXML
    private Label visualizacionPedido;

    private ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    PedidoDAO pedidoDAO;
    private ObjectProperty<Pedido> pedido = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listarPedidosEnCurso();
        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        idEnCurso.setCellValueFactory(new PropertyValueFactory<>("numeroDePedido"));
        horaInicioEnCurso.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        estadoEnCurso.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getEstadoPedido().getEstado());
            }
        });
        clientesEnCurso.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getComensal().getNombre());
            }
        });
        totalEnCurso.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));

        tablaEnCurso.setItems(listaPedidos);
        pedido.bind(tablaEnCurso.getSelectionModel().selectedItemProperty());

    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae los pedidos en curso
     */
    void listarPedidosEnCurso(){
        Task<List<Pedido>> listTask = new Task<List<Pedido>>() {
            @Override
            protected List<Pedido> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                pedidoDAO = new PedidoDAO(conexionBDRestaurante);
                return pedidoDAO.getPedidos(3);
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tablaEnCurso.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tablaEnCurso.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaPedidos.setAll(listTask.getValue());
            tablaEnCurso.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tablaEnCurso,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tablaEnCurso.setPlaceholder(progressIndicator);

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
        Pedido pedido = tablaEnCurso.getSelectionModel().getSelectedItem();
        if(pedido == null){
            return;
        }
        conexionBDRestaurante.conectarBDRestaurante();
        pedidoDAO = new PedidoDAO(conexionBDRestaurante);
        visualizacionPedido.setText(pedidoDAO.getSelectedItem(pedido.getNumeroDePedido()));
    }
}
