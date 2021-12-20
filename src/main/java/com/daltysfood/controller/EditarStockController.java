package com.daltysfood.controller;

import animatefx.animation.Tada;
import com.daltysfood.dao.restaurante.CategoriaDeProductoDAO;
import com.daltysfood.dao.restaurante.MovimientoDeStockDAO;
import com.daltysfood.dao.restaurante.ProductoDAO;
import com.daltysfood.model.restaurante.MovimientoDeStock;
import com.daltysfood.model.restaurante.Producto;
import com.daltysfood.model.restaurante.TipoDeMovimiento;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class EditarStockController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField textFieldStock;

    @FXML
    private TextField textFieldStockMinimo;

    @FXML
    private TextArea textAreaComentario;

    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    ProductoDAO productoDAO;
    MovimientoDeStockDAO movimientoDeStockDAO;
    Producto producto;
    private int stock;
    private int stockMinimo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Verifica que los datos del formulario para modificar el producto sean correctos
     * @param actionEvent Click en boton guardar
     * @throws SQLException Si hubo algun problema al realizar el cambio en la bd
     */
    public void guardar(ActionEvent actionEvent) throws SQLException {
        if(textFieldStock.getText().isEmpty()){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese el Stock del producto").position(Pos.CENTER).showWarning();
            return;
        }

        int nuevoStock = stock;
        int nuevoStockMinimo = stockMinimo;

        try {
            nuevoStock = Integer.parseInt(textFieldStock.getText());
            nuevoStockMinimo = Integer.parseInt(textFieldStockMinimo.getText());
            if(nuevoStock < -1 || nuevoStockMinimo < -1){
                org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese valores positivos").position(Pos.CENTER).showWarning();
            }
            if((stock - nuevoStock) != 0 || (stockMinimo - nuevoStockMinimo) != 0){
                int guardar = 0;

                if((stockMinimo - nuevoStockMinimo) != 0) {
                    conexionBDRestaurante.conectarBDRestaurante();
                    productoDAO = new ProductoDAO(conexionBDRestaurante);
                    producto.setStockMinimo(nuevoStockMinimo);
                    guardar = productoDAO.guardar(producto);
                    if (guardar > 0) {
                        com.daltysfood.util.Metodos.closeEffect(root);
                    }
                    conexionBDRestaurante.cerrarBDRestaurante();
                }

                if((stock - nuevoStock) != 0 ) {
                    conexionBDRestaurante.conectarBDRestaurante();
                    movimientoDeStockDAO = new MovimientoDeStockDAO(conexionBDRestaurante);
                    guardar = movimientoDeStockDAO.agregarStockManual(producto, (nuevoStock - stock));
                    if (guardar > 0) {
                        com.daltysfood.util.Metodos.closeEffect(root);
                    }
                    conexionBDRestaurante.cerrarBDRestaurante();
                }
            }
        }catch (Exception ex){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese un numero valido").position(Pos.CENTER).showWarning();
        }

    }

    public Producto getProducto() {
        return producto;
    }

    /**
     * Almacena los datos iniciales con los que el producto esta
     * @param producto informacion del producto cuando se llama
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
        textFieldStock.setText(producto.getStock().toString());
        textFieldStockMinimo.setText(producto.getStockMinimo().toString());
        stock = Integer.parseInt(textFieldStock.getText());
        stockMinimo = Integer.parseInt(textFieldStockMinimo.getText());
    }
}
