package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CuentaCorrienteDAO;
import com.daltysfood.dao.restaurante.ProveedorDAO;
import com.daltysfood.model.restaurante.CuentaCorriente;
import com.daltysfood.model.restaurante.MovimientoDeCaja;
import com.daltysfood.model.restaurante.Proveedor;
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
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class CuentaCorrienteController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button buttonNuevoMovimiento;

    @FXML
    private GridPane tableResumen;

    @FXML
    private Label labelBalance;

    @FXML
    private Label labelRegistros;

    @FXML
    private TableView<CuentaCorriente> tableCuentasCorrientes;

    @FXML
    private TableColumn<CuentaCorriente, String> tableCProveedor;

    @FXML
    private TableColumn<CuentaCorriente, LocalDateTime> tableCFecha;

    @FXML
    private TableColumn<CuentaCorriente, Double> tableCMonto;

    @FXML
    private TableColumn<CuentaCorriente, Boolean> tableCEstaPagado;

    @FXML
    private VBox informacionMovimiento;

    @FXML
    private Label visualizacionMovimiento;

    private NuevaCuentaCorrienteController nuevaCuentaCorrienteController;
    private Stage stageCuentaCorriente;

    private ObservableList<CuentaCorriente> listaCuentaCorriente = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    CuentaCorrienteDAO cuentaCorrienteDAO;
    private static CuentaCorriente cuentaSeleccionada;
    private ObjectProperty<CuentaCorriente> cuentaCorriente = new SimpleObjectProperty<>();
    private static final Button buttonEliminar = new Button("Eliminar");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonEliminar.setOnAction(event -> {eliminarCuenta(); });
        listarCuentas();
        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCProveedor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCorriente,String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CuentaCorriente, String> param) {
                return new SimpleStringProperty(param.getValue().getProveedor().getNombres());
            }
        });
        tableCFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        tableCMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        tableCEstaPagado.setCellValueFactory(new PropertyValueFactory<>("estaPagado"));
        tableCEstaPagado.setCellFactory(param -> {
            return new TableCell<CuentaCorriente, Boolean>() {
                @Override
                protected void updateItem(Boolean value, boolean empty) {
                    super.updateItem(value, empty);
                    if(!empty || value!=null) {
                        setText(value.toString());
                        setAlignment(Pos.CENTER);
                        if (!value.booleanValue()) {
                            setStyle("-fx-background-color: #F44336; fx-font-weight: bold; -fx-text-fill: white; ");
                        } else {
                            setStyle("-fx-background-color: #4CAF50; fx-font-weight: bold; -fx-text-fill: white;");
                        }
                    } else {
                        setStyle(null);
                        setText(null);
                    }
                }
            };
        });
        tableCuentasCorrientes.setItems(listaCuentaCorriente);
        cuentaCorriente.bind(tableCuentasCorrientes.getSelectionModel().selectedItemProperty());
    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae las cuentas corrientes
     */
    private void listarCuentas() {
        Task<List<CuentaCorriente>> listTask = new Task<List<CuentaCorriente>>() {
            @Override
            protected List<CuentaCorriente> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                cuentaCorrienteDAO = new CuentaCorrienteDAO(conexionBDRestaurante);
                return cuentaCorrienteDAO.getCuentasCorrientes();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableCuentasCorrientes.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableCuentasCorrientes.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaCuentaCorriente.setAll(listTask.getValue());
            tableCuentasCorrientes.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableCuentasCorrientes,-1);
            });
        });

        try {
            conexionBDRestaurante.conectarBDRestaurante();
            cuentaCorrienteDAO = new CuentaCorrienteDAO(conexionBDRestaurante);
            labelBalance.setText(cuentaCorrienteDAO.getResumen().get(0).toString());
            labelRegistros.setText(cuentaCorrienteDAO.getResumen().get(1).toString());
            conexionBDRestaurante.cerrarBDRestaurante();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableCuentasCorrientes.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de las cuentas corrientes
     * y carga la informacion de dicha cuenta corriente
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(MouseEvent mouseEvent) {
        cuentaSeleccionada = tableCuentasCorrientes.getSelectionModel().getSelectedItem();
        if(cuentaSeleccionada == null){
            return;
        }
        String infoCuenta = "";
        infoCuenta += "Tipo\t " + cuentaSeleccionada.getTipoDeMovimiento().getConcepto() +  "\n";
        infoCuenta += "Proveedor\t " + cuentaSeleccionada.getProveedor().getNombres() +  "\n";
        infoCuenta += "Monto\t " + cuentaSeleccionada.getMonto() +  "\n";
        infoCuenta += "Fecha\t " + cuentaSeleccionada.getFecha() +  "\n";
        infoCuenta += "Medio de Pago\t " + cuentaSeleccionada.getMedioDePago().getNombre() +  "\n";
        String usarEnArqueo = (cuentaSeleccionada.getArqueoDeCaja() == null ) ? "No" : "Si";
        infoCuenta += "Usar en Arqueo\t " + usarEnArqueo +  "\n";
        visualizacionMovimiento.setText(infoCuenta);
        if(!informacionMovimiento.getChildren().contains(buttonEliminar) && !cuentaSeleccionada.isEstaPagado()){
            informacionMovimiento.getChildren().add(buttonEliminar);
        }
        if(informacionMovimiento.getChildren().contains(buttonEliminar) && cuentaSeleccionada.isEstaPagado()){
            informacionMovimiento.getChildren().remove(buttonEliminar);
        }
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para agregar una nueva cuenta corriente
     * Y carga la vista padre con la nueva infromacion
     * @param actionEvent Click en botón "nueva transaccion"
     * @throws IOException Si no encuentra el archivo fxml
     */
    public void nuevoMovimiento(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaCuentaCorriente.fxml"));
        AnchorPane ap = loader.load();
        nuevaCuentaCorrienteController = loader.getController();
        Scene scene = new Scene(ap);
        stageCuentaCorriente = new Stage();
        stageCuentaCorriente.setScene(scene);
        stageCuentaCorriente.initOwner(root.getScene().getWindow());
        stageCuentaCorriente.initModality(Modality.WINDOW_MODAL);
        stageCuentaCorriente.initStyle(StageStyle.DECORATED);
        stageCuentaCorriente.setResizable(false);
        stageCuentaCorriente.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageCuentaCorriente.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        root.setEffect(new GaussianBlur(7.0));
        stageCuentaCorriente.showAndWait();
        listarCuentas();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de las cuentas corrientes
     * y guarda la informacion del elemento que se desea eliminar
     */
    private void eliminarCuenta() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar la cuenta?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText("Cuenta a: " + cuentaSeleccionada.getProveedor().getNombres());
        if (a.showAndWait().get() == ButtonType.YES) {
            conexionBDRestaurante.cerrarBDRestaurante();
            conexionBDRestaurante.conectarBDRestaurante();
            cuentaCorrienteDAO = new CuentaCorrienteDAO(conexionBDRestaurante);
            try {
                int delete = cuentaCorrienteDAO.deleteItem(cuentaSeleccionada);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            conexionBDRestaurante.cerrarBDRestaurante();
            listarCuentas();
        }
    }
}
