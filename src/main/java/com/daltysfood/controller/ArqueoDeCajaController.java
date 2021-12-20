package com.daltysfood.controller;

import animatefx.animation.Tada;
import com.daltysfood.dao.restaurante.ArqueoDeCajaDAO;
import com.daltysfood.dao.restaurante.MovimientosDeCajaDAO;
import com.daltysfood.model.restaurante.ArqueoDeCaja;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ArqueoDeCajaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private Button buttonNuevoArqueo;

    @FXML
    private TableView<ArqueoDeCaja> tableArqueoDeCaja;

    @FXML
    private TableColumn<ArqueoDeCaja, Date> tableCApertura;

    @FXML
    private TableColumn<ArqueoDeCaja, Date> tableCCierre;

    @FXML
    private TableColumn<ArqueoDeCaja, Double> tableCSistema;

    @FXML
    private TableColumn<ArqueoDeCaja, Double> tableCUsuario;

    @FXML
    private TableColumn<ArqueoDeCaja, Double> tableCDiferencia;

    @FXML
    private TableColumn<ArqueoDeCaja, String> tableCEstado;

    @FXML
    private VBox informacionArqueo;

    @FXML
    private Label visualizacionArqueo;

    private static final Button buttonFinalizarArqueo = new Button("Finalizar Arqueo");
    private static HBox hboxComentario;
    private static HBox hboxEfectivo;
    private static Label labelComentario;
    private static Label labelEfectivo;
    private static TextField textEfectivo;
    private static TextArea textComentario;

    private NuevoArqueoController nuevoArqueoController;
    private Stage stageArqueo;

    private ObservableList<ArqueoDeCaja> listaArqueos = FXCollections.observableArrayList();
    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    ArqueoDeCajaDAO arqueoDeCajaDAO;
    private ObjectProperty<ArqueoDeCaja> arqueoDeCaja = new SimpleObjectProperty<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /**
         * Inicializamos los componentes que se usan cuando se selecciona un item de la
         * tabla
         */
        hboxComentario = new HBox();
        hboxEfectivo = new HBox();
        labelComentario = new Label("Comentario: \t");
        labelEfectivo = new Label("Efectivo*: \t");
        textEfectivo = new TextField();
        textComentario = new TextArea();
        hboxComentario.setPadding(new Insets(20,20,20,20));
        hboxEfectivo.setPadding(new Insets(20,20,20,20));
        textEfectivo.setMaxWidth(140);
        hboxEfectivo.getChildren().add(labelEfectivo);
        hboxEfectivo.getChildren().add(textEfectivo);
        textComentario.setMaxWidth(140);
        textComentario.setMaxHeight(100);
        hboxComentario.getChildren().add(labelComentario);
        hboxComentario.getChildren().add(textComentario);
        listarArqueos();
        /***
         *Se encarga de inicializar las columnas de las tablas y agregar la informacion
         */
        tableCApertura.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        tableCCierre.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        tableCSistema.setCellValueFactory(new PropertyValueFactory<>("sistema"));
        tableCUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        tableCDiferencia.setCellValueFactory(new PropertyValueFactory<>("diferencia"));
        tableCEstado.setCellValueFactory(new PropertyValueFactory<>("estaActiva"));

        tableArqueoDeCaja.setItems(listaArqueos);
        arqueoDeCaja.bind(tableArqueoDeCaja.getSelectionModel().selectedItemProperty());
    }

    /***
     * Se encarga de almacenar la informacion y crear los espacios correspondientes a la respuesta de la transaccion
     * que trae los movimientos de caja
     */
    void listarArqueos(){
        Task<List<ArqueoDeCaja>> listTask = new Task<List<ArqueoDeCaja>>() {
            @Override
            protected List<ArqueoDeCaja> call() throws Exception {
                conexionBDRestaurante.conectarBDRestaurante();
                arqueoDeCajaDAO = new ArqueoDeCajaDAO(conexionBDRestaurante);
                return arqueoDeCajaDAO.getArqueos();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBDRestaurante.cerrarBDRestaurante();
            tableArqueoDeCaja.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tableArqueoDeCaja.setPlaceholder(null);
            conexionBDRestaurante.cerrarBDRestaurante();
            listaArqueos.setAll(listTask.getValue());
            tableArqueoDeCaja.getColumns().forEach(productoTableColumn -> {
                com.daltysfood.util.Metodos.changeSizeOnColumn(productoTableColumn,tableArqueoDeCaja,-1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setStyle(" -fx-progress-color: #ff7c00;");
        tableArqueoDeCaja.setPlaceholder(progressIndicator);

        Thread thread = new Thread(listTask);
        thread.start();
    }

    /***
     * Se encarga de crear y mostrar una nueva ventana con un formulario para agregar un arqueo
     * Y carga la vista padre con la nueva infromacion
     * @param actionEvent Click en botón "nuevo arqueo"
     * @throws IOException Si no encuentra el archivo fxml
     */
    public void nuevoArqueo(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoArqueo.fxml"));
        AnchorPane ap = loader.load();
        nuevoArqueoController = loader.getController();
        Scene scene = new Scene(ap);
        stageArqueo = new Stage();
        stageArqueo.setScene(scene);
        stageArqueo.initOwner(root.getScene().getWindow());
        stageArqueo.initModality(Modality.WINDOW_MODAL);
        stageArqueo.initStyle(StageStyle.DECORATED);
        stageArqueo.setResizable(false);
        stageArqueo.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageArqueo.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageArqueo.setResizable(false);
        root.setEffect(new GaussianBlur(7.0));
        stageArqueo.showAndWait();
        listarArqueos();
    }

    /***
     * Se encarga de determinar cual es el elemento seleccionado en la tabla de los arqueos de caja
     * y carga la informacion de dicho arqueo seleccionado
     * @param mouseEvent Click en la tabla
     * @throws SQLException Si hubo un problema con la conexion
     */
    public void getItem(MouseEvent mouseEvent) throws SQLException {
        /**
         * Si ya existen los componentes de la vista lateral, los eliminamos
         */
        visualizacionArqueo.setText("");
        informacionArqueo.getChildren().remove(hboxEfectivo);
        informacionArqueo.getChildren().remove(hboxComentario);
        informacionArqueo.getChildren().remove(buttonFinalizarArqueo);


        ArqueoDeCaja arqueoDeCaja = tableArqueoDeCaja.getSelectionModel().getSelectedItem();
        conexionBDRestaurante.conectarBDRestaurante();
        arqueoDeCajaDAO = new ArqueoDeCajaDAO(conexionBDRestaurante);
        List<Double> montoMovimientos = arqueoDeCajaDAO.getMovimientos(arqueoDeCaja.getIdArqueoDeCaja());

        if(arqueoDeCaja == null){
            return;
        }
        Double egresos = (montoMovimientos.get(0) + montoMovimientos.get(1) + montoMovimientos.get(3))*-1;
        Double total = (arqueoDeCaja.getMontoInicial() + montoMovimientos.get(2) + egresos);

        String estado = "Abierto";
        String infoMovimiento = "";
        infoMovimiento += "Hora de Apertura: \t " + arqueoDeCaja.getFechaInicio() +  "\n";

        if(arqueoDeCaja.getEstaActiva()){
            infoMovimiento += "Estado: \t " + estado +  "\n";
        }else{
            estado = "Cerrado";
            infoMovimiento += "Estado: \t " + estado +  "\n";
            infoMovimiento += "Hora de cierre: \t " + arqueoDeCaja.getFechaFin() +  "\n";
            infoMovimiento += "Comentario: \t " + arqueoDeCaja.getComentario() +  "\n";
        }

        infoMovimiento += "\tSEGÚN SISTEMA" +  "\n";
        infoMovimiento += "MONTO INICIAL \t" + arqueoDeCaja.getMontoInicial() +  "\n";
        infoMovimiento += "INGRESOS \t" + montoMovimientos.get(2) +  "\n";
        infoMovimiento += "\tMovimientos de caja \t" + montoMovimientos.get(2) +  "\n";
        infoMovimiento += "EGRESOS \t" + egresos  +  "\n";
        infoMovimiento += "\tGastos \t" + montoMovimientos.get(0) +  "\n";
        infoMovimiento += "\tCuenta Corriente \t" + montoMovimientos.get(1) +  "\n";
        infoMovimiento += "\tMovimientos de caja \t" + montoMovimientos.get(3) +  "\n";
        infoMovimiento += "TOTAL \t" + total +  "\n";
        infoMovimiento += "\tSEGÚN USUARIO:" +  "\n";

        if(!arqueoDeCaja.getEstaActiva()){
            infoMovimiento += "\tEfectivo \t" + arqueoDeCaja.getUsuario() +  "\n";
            infoMovimiento += "\tTotal \t" + arqueoDeCaja.getUsuario() +  "\n";
            infoMovimiento += "DIFERENCIA \t" + arqueoDeCaja.getDiferencia() +  "\n";
            visualizacionArqueo.setText(infoMovimiento);
            return;
        }

        if(informacionArqueo.getChildren().contains(buttonFinalizarArqueo)){
            return;
        }else{
            /***
             * Si el boton no esta dentro de la vista, lo agregamos y le asignamos una funcion para cerrar el arqueo
             * seleccionado
             */
            informacionArqueo.getChildren().add(hboxEfectivo);
            informacionArqueo.getChildren().add(hboxComentario);
            informacionArqueo.getChildren().add(buttonFinalizarArqueo);
            visualizacionArqueo.setText(infoMovimiento);


            buttonFinalizarArqueo.setOnMouseClicked(e-> {

                if(textEfectivo.getText().isEmpty()){
                    new Tada(textEfectivo).play();
                    org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese el monto").position(Pos.CENTER).showWarning();
                    return;
                }else{
                    arqueoDeCaja.setUsuario(Double.parseDouble(textEfectivo.getText()));
                    Double diferencia = (total - arqueoDeCaja.getUsuario());
                    arqueoDeCaja.setDiferencia(diferencia);
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION, ("¿Desea Cerrar el arqueo? con: " + diferencia + " de diferencia"), ButtonType.YES, ButtonType.NO);
                    a.setHeaderText("Arqueo de caja: " + arqueoDeCaja.getIdArqueoDeCaja().toString());
                    if (a.showAndWait().get() == ButtonType.YES) {
                        conexionBDRestaurante.conectarBDRestaurante();
                        arqueoDeCajaDAO = new ArqueoDeCajaDAO(conexionBDRestaurante);
                        try {
                            if(arqueoDeCajaDAO.updateArqueo(arqueoDeCaja)){
                                informacionArqueo.getChildren().remove(hboxEfectivo);
                                informacionArqueo.getChildren().remove(hboxComentario);
                                informacionArqueo.getChildren().remove(buttonFinalizarArqueo);
                                textEfectivo.clear();
                                textComentario.clear();
                                listarArqueos();
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        this.conexionBDRestaurante.cerrarBDRestaurante();
                    }}
            });
        }

    }
}
