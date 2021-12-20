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
import java.util.ArrayList;
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

public class VentasController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private GridPane tableResumen;

    @FXML
    private TableView<Pedido> tableVentas;

    @FXML
    private TableColumn<Pedido, LocalDate> tableCHoraInicio;

    @FXML
    private TableColumn<Pedido, LocalDate> tableCHoraCierre;

    @FXML
    private TableColumn<Pedido, String> tableCEstado;

    @FXML
    private TableColumn<Pedido, Integer> tableCMesa;

    @FXML
    private TableColumn<Pedido, String> tableCCamarero;

    @FXML
    private TableColumn<Pedido, String> tableCCliente;

    @FXML
    private TableColumn<Pedido, Double> tableCTotal;

    @FXML
    private Label visualizacionPedido;

    @FXML
    private Label labelTotalComensales;

    @FXML
    private Label labelPromedioPersona;

    @FXML
    private Label labelPromedioVenta;

    @FXML
    private Label labelTotalVentas;

    private ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    VentasDAO ventasDAO;
    PedidoDAO pedidoDAO;
    private ObjectProperty<Pedido> pedido = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listarVentas();

        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCHoraCierre.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        tableCHoraInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        tableCEstado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pedido,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pedido, String> param) {
                return new SimpleStringProperty(param.getValue().getEstadoPedido().getEstado());
            }
        });
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
        tableCTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));

        tableVentas.setItems(listaPedidos);
        pedido.bind(tableVentas.getSelectionModel().selectedItemProperty());

    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae las ventas
     */
    void listarVentas(){
        try {
            setInfo();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Task<List<Pedido>> listTask = new Task<List<Pedido>>() {
            @Override
            protected List<Pedido> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                ventasDAO = new VentasDAO(conexionBDRestaurante);
                return ventasDAO.getVentas();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableVentas.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableVentas.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaPedidos.setAll(listTask.getValue());
            tableVentas.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableVentas,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableVentas.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Muestra y pide el resumen de las ventas
     * @throws SQLException Si hubo problema al traer la infromacion
     */
    void setInfo() throws SQLException {
        conexionBDRestaurante.conectarBDRestaurante();
        ventasDAO = new VentasDAO(conexionBDRestaurante);
        List<Double> info = ventasDAO.getInfo();
        Double promedioVenta = info.get(0) / info.get(1);
        Double promedioPersona = info.get(0) / info.get(2);
        labelTotalVentas.setText(info.get(0).toString());
        labelPromedioPersona.setText(promedioPersona.toString());
        labelPromedioVenta.setText(promedioVenta.toString());
        labelTotalComensales.setText(info.get(2).toString());
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de las ventas
     * y carga la informacion de dicho pedido seleccionado
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        Pedido pedido = tableVentas.getSelectionModel().getSelectedItem();
        if(pedido == null){
            return;
        }
        conexionBDRestaurante.conectarBDRestaurante();
        pedidoDAO = new PedidoDAO(conexionBDRestaurante);
        visualizacionPedido.setText(pedidoDAO.getSelectedItem(pedido.getNumeroDePedido()));
    }


}
