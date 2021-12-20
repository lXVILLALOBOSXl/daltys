package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.MovimientosDeCajaDAO;
import com.daltysfood.dao.restaurante.PedidoDAO;
import com.daltysfood.dao.restaurante.VentasDAO;
import com.daltysfood.model.restaurante.MovimientoDeCaja;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class MovimientosDeCajaController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private GridPane tableResumen;

    @FXML
    private Label labelBalance;

    @FXML
    private TableView<MovimientoDeCaja> tableMovimientosDeCaja;

    @FXML
    private TableColumn<MovimientoDeCaja, LocalDate> tableCFecha;

    @FXML
    private TableColumn<MovimientoDeCaja, Double> tableCMonto;

    @FXML
    private TableColumn<MovimientoDeCaja, String> tableCTipo;

    @FXML
    private TableColumn<MovimientoDeCaja, String> tableCComentario;

    @FXML
    private Label visualizacionMovimiento;

    @FXML
    private VBox informacionMovimiento;

    @FXML
    private Button buttonNuevoMovimiento;

    private static final Button buttonEliminarMovimiento = new Button("Eliminar");

    private NuevoMovimientoController nuevoMovimientoController;
    private Stage stageMovimiento;

    private ObservableList<MovimientoDeCaja> listaMovimientos = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    MovimientosDeCajaDAO movimientosDeCajaDAO;
    private ObjectProperty<MovimientoDeCaja> movimientoDeCaja = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listarMovimientos();

        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        tableCMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        tableCTipo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MovimientoDeCaja,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<MovimientoDeCaja, String> param) {
                return new SimpleStringProperty(param.getValue().getTipoDeMovimiento().getConcepto());
            }
        });
        tableCComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));

        tableMovimientosDeCaja.setItems(listaMovimientos);
        movimientoDeCaja.bind(tableMovimientosDeCaja.getSelectionModel().selectedItemProperty());

    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae los movimientos de caja
     */
    void listarMovimientos(){
        try {
            setInfo();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Task<List<MovimientoDeCaja>> listTask = new Task<List<MovimientoDeCaja>>() {
            @Override
            protected List<MovimientoDeCaja> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                movimientosDeCajaDAO = new MovimientosDeCajaDAO(conexionBDRestaurante);
                return movimientosDeCajaDAO.getMovimientos();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableMovimientosDeCaja.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableMovimientosDeCaja.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaMovimientos.setAll(listTask.getValue());
            tableMovimientosDeCaja.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableMovimientosDeCaja,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableMovimientosDeCaja.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Muestra y pide los egresos e ingresos de la caja, saca el balance
     * @throws SQLException Si hubo problema al traer la infromacion
     */
    void setInfo() throws SQLException {
        conexionBDRestaurante.conectarBDRestaurante();
        movimientosDeCajaDAO = new MovimientosDeCajaDAO(conexionBDRestaurante);
        List<Double> infoBalance = movimientosDeCajaDAO.getInfo();
        Double balance = infoBalance.get(1) - infoBalance.get(0);
        labelBalance.setText(balance.toString());
        conexionBDRestaurante.cerrarBDRestaurante();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de los movimientos de caja
     * y carga la informacion de dicho movimiento seleccionado
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(javafx.scene.input.MouseEvent mouseEvent) {
        MovimientoDeCaja movimientoDeCaja = tableMovimientosDeCaja.getSelectionModel().getSelectedItem();
        if(movimientoDeCaja == null){
            return;
        }
        String infoMovimiento = "";
        infoMovimiento += "Fecha\t " + movimientoDeCaja.getFecha() +  "\n";
        infoMovimiento += "Monto\t " + movimientoDeCaja.getMonto() +  "\n";
        infoMovimiento += "Tipo\t " + movimientoDeCaja.getTipoDeMovimiento().getConcepto() +  "\n";
        infoMovimiento += "Medio de pago\t " + movimientoDeCaja.getMedioDePago().getNombre() +  "\n";
        infoMovimiento += "Comentario\t " + movimientoDeCaja.getComentario() +  "\n";
        visualizacionMovimiento.setText(infoMovimiento);
        if(informacionMovimiento.getChildren().contains(buttonEliminarMovimiento)){
            return;
        }else{
            /***
             * Si el boton no esta dentro de la vista, lo agregamos y le asignamos una funcion para eliminar el pedido
             * seleccionado
             */
            informacionMovimiento.getChildren().add(buttonEliminarMovimiento);
            buttonEliminarMovimiento.setOnMouseClicked(e-> {
                conexionBDRestaurante.conectarBDRestaurante();
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar el movimiento?", ButtonType.YES, ButtonType.NO);
                a.setHeaderText("Movimiento de caja: " + movimientoDeCaja.getIdMovimientoDeCaja().toString());
                if (a.showAndWait().get() == ButtonType.YES) {
                    conexionBDRestaurante.conectarBDRestaurante();
                    movimientosDeCajaDAO = new MovimientosDeCajaDAO(conexionBDRestaurante);
                    try {
                        boolean delete = movimientosDeCajaDAO.deleteItem(movimientoDeCaja.getIdMovimientoDeCaja());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    listarMovimientos();
                    this.conexionBDRestaurante.cerrarBDRestaurante();
                }
            });
        }
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para agregar un movimiento de caja
     * Y carga la vista padre con la nueva infromacion
     * @param event Click en botón "nuevo movimiento"
     * @throws IOException Si no encuentra el archivo fxml
     */
    @FXML
    void nuevoMovimiento(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoMovimiento.fxml"));
        AnchorPane ap = loader.load();
        nuevoMovimientoController = loader.getController();
        Scene scene = new Scene(ap);
        stageMovimiento = new Stage();
        stageMovimiento.setScene(scene);
        stageMovimiento.initOwner(root.getScene().getWindow());
        stageMovimiento.initModality(Modality.WINDOW_MODAL);
        stageMovimiento.initStyle(StageStyle.DECORATED);
        stageMovimiento.setResizable(false);
        stageMovimiento.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageMovimiento.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageMovimiento.setResizable(false);
        root.setEffect(new GaussianBlur(7.0));
        stageMovimiento.showAndWait();
        listarMovimientos();
        try {
            setInfo();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
