package com.daltysfood.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class DashboardController implements Initializable {
    @FXML
    private BorderPane root;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuRestauranteDashboard;

    @FXML
    private Menu menuMesas;

    @FXML
    private MenuItem menuRestaurante;

    @FXML
    private MenuItem menuTerrazaRestaurante;

    @FXML
    private Menu pedidosMostrador;

    @FXML
    private MenuItem pedidosPendientes;

    @FXML
    private MenuItem pedidosEnCurso;

    @FXML
    private MenuItem pedidosCerrados;

    @FXML
    private Menu menuVentasDashboard;

    @FXML
    private MenuItem menuVentas;

    @FXML
    private MenuItem menuMovimientosDeCaja;

    @FXML
    private MenuItem menuArqueoDeCaja;

    @FXML
    private MenuItem menuPropinas;

    @FXML
    private Menu menuGastosDashboard;

    @FXML
    private MenuItem menuGastos;

    @FXML
    private MenuItem menuCategoriaDeGastos;

    @FXML
    private Menu menuProductosDashboard;

    @FXML
    private MenuItem menuProductos;

    @FXML
    private MenuItem menuCategoriaDeProductos;

    @FXML
    private MenuItem menuControlDeStock;

    @FXML
    private MenuItem menuMovimientosDeStock;

    @FXML
    private MenuItem menuListasDePrecios;

    @FXML
    private Menu menuProveedoresDashboard;

    @FXML
    private MenuItem menuProveedores;

    @FXML
    private MenuItem menuCuentasCorrientes;

    @FXML
    private Menu menuReportesDashboard;

    @FXML
    private MenuItem menuReportes;

    @FXML
    private MenuItem menuConfiguracionSalon;

    @FXML
    private MenuItem menuConfiguracionTerraza;

    @FXML
    private Menu menuCuentaDashboard;

    @FXML
    private MenuItem menuCuenta;

    @FXML
    private MenuItem menuSalir;

    @FXML
    private Menu menuNotificaciones;

    @FXML
    private Menu menuAyuda;

    @FXML
    private MenuItem menuTutoriales;

    @FXML
    private MenuItem menuContacto;

    @FXML
    private JFXTabPane tabPane;

    private Tab tabRestaurante;
    private Tab tabTerraza;
    private Tab tabPendientes;
    private Tab tabEnCurso;
    private Tab tabCerrados;
    private Tab tabVentas;
    private Tab tabMovimientosDeCaja;
    private Tab tabArqueoDeCaja;
    private Tab tabPropinas;
    private Tab tabCategoriaDeGastos;
    private Tab tabGastos;
    private Tab tabCategoriaProductos;
    private Tab tabProductos;
    private Tab tabControlDeStock;
    private Tab tabMovimientosDeStock;
    private Tab tabProveedor;
    private Tab tabCuentaCorriente;
    private Tab tabConfiguracionGeneral;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void mostrarRestaurante(ActionEvent event) throws IOException {
        if (tabRestaurante == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/Restaurante.fxml"));
            tabRestaurante = new Tab("Restaurante", anchorPane);
            tabRestaurante.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabRestaurante.setStyle("-fx-background-color: #ff7c00;");
            tabRestaurante.setClosable(true);
            tabRestaurante.setOnClosed(event1 -> {
                tabRestaurante = null;
            });
            tabPane.getTabs().add(tabRestaurante);
        }
        tabPane.getSelectionModel().select(tabRestaurante);
    }

    @FXML
    void mostarCategoriaDeProductos(ActionEvent event) throws IOException {
        if (tabCategoriaProductos == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/CategoriaProductos.fxml"));
            tabCategoriaProductos = new Tab("Categoria de Productos", anchorPane);
            tabCategoriaProductos.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabCategoriaProductos.setStyle("-fx-background-color: #ff7c00;");
            tabCategoriaProductos.setClosable(true);
            tabCategoriaProductos.setOnClosed(event1 -> {
                tabCategoriaProductos = null;
            });
            tabPane.getTabs().add(tabCategoriaProductos);
        }
        tabPane.getSelectionModel().select(tabCategoriaProductos);

    }

    @FXML
    void mostarProductos(ActionEvent event) throws IOException {
        if (tabProductos == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/Productos.fxml"));
            tabProductos = new Tab("Productos", anchorPane);
            tabProductos.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabProductos.setStyle("-fx-background-color: #ff7c00;");
            tabProductos.setClosable(true);
            tabProductos.setOnClosed(event1 -> {
                tabProductos = null;
            });
            tabPane.getTabs().add(tabProductos);
        }
        tabPane.getSelectionModel().select(tabProductos);
    }

    @FXML
    void mostrarArqueoDeCaja(ActionEvent event) throws IOException {
        if (tabArqueoDeCaja == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/ArqueoDeCaja.fxml"));
            tabArqueoDeCaja = new Tab("Arqueo de Caja", anchorPane);
            tabArqueoDeCaja.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabArqueoDeCaja.setStyle("-fx-background-color: #ff7c00;");
            tabArqueoDeCaja.setClosable(true);
            tabArqueoDeCaja.setOnClosed(event1 -> {
                tabArqueoDeCaja = null;
            });
            tabPane.getTabs().add(tabArqueoDeCaja);
        }
        tabPane.getSelectionModel().select(tabArqueoDeCaja);

    }

    @FXML
    void mostrarCategoriaDeGastos(ActionEvent event) throws IOException {
        if (tabCategoriaDeGastos == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/CategoriaGasto.fxml"));
            tabCategoriaDeGastos = new Tab("Categoria de Gastos", anchorPane);
            tabCategoriaDeGastos.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabCategoriaDeGastos.setStyle("-fx-background-color: #ff7c00;");
            tabCategoriaDeGastos.setClosable(true);
            tabCategoriaDeGastos.setOnClosed(event1 -> {
                tabCategoriaDeGastos = null;
            });
            tabPane.getTabs().add(tabCategoriaDeGastos);
        }
        tabPane.getSelectionModel().select(tabCategoriaDeGastos);

    }

    @FXML
    void mostrarConfiguracionGeneral(ActionEvent event) throws IOException {
        if (tabConfiguracionGeneral == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/ConfiguracionGeneral.fxml"));
            tabConfiguracionGeneral = new Tab("Configuración General", anchorPane);
            tabConfiguracionGeneral.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabConfiguracionGeneral.setStyle("-fx-background-color: #ff7c00;");
            tabConfiguracionGeneral.setClosable(true);
            tabConfiguracionGeneral.setOnClosed(event1 -> {
                tabConfiguracionGeneral = null;
            });
            tabPane.getTabs().add(tabConfiguracionGeneral);
        }
        tabPane.getSelectionModel().select(tabConfiguracionGeneral);

    }

    @FXML
    void mostrarContacto(ActionEvent event) {

    }

    @FXML
    void mostrarControlDeStock(ActionEvent event) throws IOException {
        if (tabControlDeStock == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/ControlDeStock.fxml"));
            tabControlDeStock = new Tab("Control de Stock", anchorPane);
            tabControlDeStock.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabControlDeStock.setStyle("-fx-background-color: #ff7c00;");
            tabControlDeStock.setClosable(true);
            tabControlDeStock.setOnClosed(event1 -> {
                tabControlDeStock = null;
            });
            tabPane.getTabs().add(tabControlDeStock);
        }
        tabPane.getSelectionModel().select(tabControlDeStock);
    }

    @FXML
    void mostrarCuenta(ActionEvent event) {

    }

    @FXML
    void mostrarCuentasCorrientes(ActionEvent event) throws IOException {
        if (tabCuentaCorriente == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/CuentaCorriente.fxml"));
            tabCuentaCorriente = new Tab("Cuentas Corrientes", anchorPane);
            tabCuentaCorriente.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabCuentaCorriente.setStyle("-fx-background-color: #ff7c00;");
            tabCuentaCorriente.setClosable(true);
            tabCuentaCorriente.setOnClosed(event1 -> {
                tabCuentaCorriente = null;
            });
            tabPane.getTabs().add(tabCuentaCorriente);
        }
        tabPane.getSelectionModel().select(tabCuentaCorriente);

    }

    @FXML
    void mostrarGastos(ActionEvent event) throws IOException {
        if (tabGastos == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/Gasto.fxml"));
            tabGastos = new Tab("Gastos", anchorPane);
            tabGastos.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabGastos.setStyle("-fx-background-color: #ff7c00;");
            tabGastos.setClosable(true);
            tabGastos.setOnClosed(event1 -> {
                tabGastos = null;
            });
            tabPane.getTabs().add(tabGastos);
        }
        tabPane.getSelectionModel().select(tabGastos);

    }

    @FXML
    void mostrarListasDePrecios(ActionEvent event) {

    }

    @FXML
    void mostrarMesasSalonConfiguracion(ActionEvent event) {

    }

    @FXML
    void mostrarMesasTerrazaConfiguracion(ActionEvent event) {

    }


    @FXML
    void mostrarPedidosCerrados(ActionEvent event) throws IOException {
        if (tabCerrados == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/PedidosCerrados.fxml"));
            tabCerrados = new Tab("Cerrados", anchorPane);
            tabCerrados.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabCerrados.setStyle("-fx-background-color: #ff7c00;");
            tabCerrados.setClosable(true);
            tabCerrados.setOnClosed(event1 -> {
                tabCerrados = null;
            });
            tabPane.getTabs().add(tabCerrados);
        }
        tabPane.getSelectionModel().select(tabCerrados);
    }

    @FXML
    void mostrarPedidosEnCurso(ActionEvent event) throws IOException {
        if (tabEnCurso == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/PedidosEnCurso.fxml"));
            tabEnCurso = new Tab("En Curso", anchorPane);
            tabEnCurso.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabEnCurso.setStyle("-fx-background-color: #ff7c00;");
            tabEnCurso.setClosable(true);
            tabEnCurso.setOnClosed(event1 -> {
                tabEnCurso = null;
            });
            tabPane.getTabs().add(tabEnCurso);
        }
        tabPane.getSelectionModel().select(tabEnCurso);
    }

    @FXML
    void mostrarPedidosPendientes(ActionEvent event) throws IOException {
        if (tabPendientes == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/PedidosPendientes.fxml"));
            tabPendientes = new Tab("Pendientes", anchorPane);
            tabPendientes.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabPendientes.setStyle("-fx-background-color: #ff7c00;");
            tabPendientes.setClosable(true);
            tabPendientes.setOnClosed(event1 -> {
                tabPendientes = null;
            });
            tabPane.getTabs().add(tabPendientes);
        }
        tabPane.getSelectionModel().select(tabPendientes);
    }


    @FXML
    void mostrarMovimientosDeCaja(ActionEvent event) throws IOException {
        if (tabMovimientosDeCaja == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/MovimientosDeCaja.fxml"));
            tabMovimientosDeCaja = new Tab("Movimientos de caja", anchorPane);
            tabMovimientosDeCaja.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabMovimientosDeCaja.setStyle("-fx-background-color: #ff7c00;");
            tabMovimientosDeCaja.setClosable(true);
            tabMovimientosDeCaja.setOnClosed(event1 -> {
                tabMovimientosDeCaja = null;
            });
            tabPane.getTabs().add(tabMovimientosDeCaja);
        }
        tabPane.getSelectionModel().select(tabMovimientosDeCaja);

    }

    @FXML
    void mostrarMovimientosDeStock(ActionEvent event) throws IOException {
        if (tabMovimientosDeStock == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/MovimientosDeStock.fxml"));
            tabMovimientosDeStock = new Tab("Movimientos de Stock", anchorPane);
            tabMovimientosDeStock.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabMovimientosDeStock.setStyle("-fx-background-color: #ff7c00;");
            tabMovimientosDeStock.setClosable(true);
            tabMovimientosDeStock.setOnClosed(event1 -> {
                tabMovimientosDeStock = null;
            });
            tabPane.getTabs().add(tabMovimientosDeStock);
        }
        tabPane.getSelectionModel().select(tabMovimientosDeStock);

    }

    @FXML
    void mostrarPropinas(ActionEvent event) throws IOException {
        if (tabPropinas == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/Propinas.fxml"));
            tabPropinas = new Tab("Propinas", anchorPane);
            tabPropinas.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabPropinas.setStyle("-fx-background-color: #ff7c00;");
            tabPropinas.setClosable(true);
            tabPropinas.setOnClosed(event1 -> {
                tabPropinas = null;
            });
            tabPane.getTabs().add(tabPropinas);
        }
        tabPane.getSelectionModel().select(tabPropinas);

    }

    @FXML
    void mostrarProveedores(ActionEvent event) throws IOException {
        if (tabProveedor == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/Proveedor.fxml"));
            tabProveedor = new Tab("Restaurante", anchorPane);
            tabProveedor.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabProveedor.setStyle("-fx-background-color: #ff7c00;");
            tabProveedor.setClosable(true);
            tabProveedor.setOnClosed(event1 -> {
                tabProveedor = null;
            });
            tabPane.getTabs().add(tabProveedor);
        }
        tabPane.getSelectionModel().select(tabProveedor);

    }

    @FXML
    void mostrarReportes(ActionEvent event) {

    }

    @FXML
    void mostrarTerrazaRestaurante(ActionEvent event) throws IOException {
        if (tabTerraza == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/Terraza.fxml"));
            tabTerraza = new Tab("Terraza", anchorPane);
            tabTerraza.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabTerraza.setStyle("-fx-background-color: #ff7c00;");
            tabTerraza.setClosable(true);
            tabTerraza.setOnClosed(event1 -> {
                tabTerraza = null;
            });
            tabPane.getTabs().add(tabTerraza);
        }
        tabPane.getSelectionModel().select(tabTerraza);

    }

    @FXML
    void mostrarTutoriales(ActionEvent event) {

    }

    @FXML
    void mostrarVentas(ActionEvent event) throws IOException {
        if (tabVentas == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/Ventas.fxml"));
            tabVentas = new Tab("Ventas", anchorPane);
            tabVentas.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabVentas.setStyle("-fx-background-color: #ff7c00;");
            tabVentas.setClosable(true);
            tabVentas.setOnClosed(event1 -> {
                tabVentas = null;
            });
            tabPane.getTabs().add(tabVentas);
        }
        tabPane.getSelectionModel().select(tabVentas);
    }

    @FXML
    void salir(ActionEvent event) {

    }
}
