package com.daltysfood.controller;

import com.daltysfood.dao.restaurante.CategoriaDeProductoDAO;
import com.daltysfood.dao.restaurante.CocinaDAO;
import com.daltysfood.dao.restaurante.ProductoDAO;
import com.daltysfood.dao.restaurante.ProveedorDAO;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.Cocina;
import com.daltysfood.model.restaurante.Producto;
import com.daltysfood.model.restaurante.Proveedor;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class NuevoProductoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private ChoiceBox<CategoriaDeProducto> choiceBoxCategoria;

    @FXML
    private JFXTextField textFieldNombre;

    @FXML
    private JFXTextField textFieldPrecio;

    @FXML
    private JFXTextField textFieldCosto;

    @FXML
    private JFXTextField textFieldCodigo;

    @FXML
    private ChoiceBox<Cocina> choiceBoxCocina;

    @FXML
    private ChoiceBox<Proveedor> choiceBoxProveedor;

    @FXML
    private CheckBox checkBoxPermitir;

    @FXML
    private CheckBox checkBoxControl;

    @FXML
    private CheckBox checkBoxActivo;

    @FXML
    private JFXTextField textFieldDescripcion;

    @FXML
    private JFXTextField textFieldImagen;

    ConexionBDRestaurante conexionBDRestaurante = new ConexionBDRestaurante();
    ProveedorDAO proveedorDAO;
    CocinaDAO cocinaDAO;
    CategoriaDeProductoDAO categoriaDeProductoDAO;
    ProductoDAO productoDAO;
    Producto producto;

    /***
     * Pide y carga las llaves foraneas para que se puedan seleccionar al momento de crear un nuevo producto
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conexionBDRestaurante.conectarBDRestaurante();
        cocinaDAO = new CocinaDAO(conexionBDRestaurante);
        categoriaDeProductoDAO = new CategoriaDeProductoDAO(conexionBDRestaurante);
        productoDAO = new ProductoDAO(conexionBDRestaurante);
        proveedorDAO = new ProveedorDAO(conexionBDRestaurante);
        try {
            choiceBoxCocina.setItems(cocinaDAO.getCocinas());
            choiceBoxCategoria.setItems(categoriaDeProductoDAO.getCategoriasDeProducto());
            choiceBoxProveedor.setItems(proveedorDAO.getProveedoresActivos());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        conexionBDRestaurante.cerrarBDRestaurante();

    }

    /***
     * Verifica que los datos del formulario sean correctos y envia la informacion ingresada para
     * guardarlo en la base de datos
     * @param actionEvent Click en el boton guardar
     */
    public void guardar(ActionEvent actionEvent) {
        if(textFieldNombre.getText().isEmpty() || choiceBoxCategoria.getSelectionModel().isEmpty() || choiceBoxCategoria.getSelectionModel().isEmpty() || textFieldPrecio.getText().isEmpty()){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese los campos requeridos").position(Pos.CENTER).showWarning();
            return;
        }

        if(getProducto() == null){
            producto = new Producto();
        }

        producto.setNombre(textFieldNombre.getText());
        try{
            producto.setCategoriaDeProducto(choiceBoxCategoria.getValue());
            producto.setPrecio(Double.parseDouble(textFieldPrecio.getText()));
            if(textFieldCosto.getText().isEmpty()){
                producto.setCosto(0d);
            }else{
                producto.setCosto(Double.parseDouble(textFieldCosto.getText()));
            }
        }catch (Exception e){
            org.controlsfx.control.Notifications.create().title("Aviso").text("Campos de informacion invalidos").position(Pos.CENTER).showWarning();
            return;
        }
        producto.setCodigo(textFieldCodigo.getText());
        producto.setCocina(choiceBoxCocina.getValue());
        producto.setProveedor(choiceBoxProveedor.getValue());
        producto.setDescripcion(textFieldDescripcion.getText());
        producto.setImagen(textFieldImagen.getText());
        producto.setEstaActivo(checkBoxActivo.isSelected());
        producto.setControl(checkBoxControl.isSelected());
        producto.setPermitirVender(checkBoxPermitir.isSelected());

        conexionBDRestaurante.conectarBDRestaurante();
        productoDAO = new ProductoDAO(conexionBDRestaurante);

        try{
            int guardar = productoDAO.guardar(producto);
            if(guardar > 0){
                com.daltysfood.util.Metodos.closeEffect(root);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        conexionBDRestaurante.cerrarBDRestaurante();
    }

    public Producto getProducto() {
        return producto;
    }

    /**
     * Crga la informacion en el formulario del producto que se va editar
     * @param producto informacion del producto que se va a editar
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
        textFieldNombre.setText(producto.getNombre());
        choiceBoxCategoria.getSelectionModel().select(producto.getCategoriaDeProducto().getIdCategoriaDeProducto());
        textFieldPrecio.setText(producto.getPrecio().toString());
        if (producto.getCocina() != null){
            choiceBoxCocina.getSelectionModel().select(producto.getCocina().getIdCocina());
        }
        if (producto.getProveedor() != null){
            choiceBoxProveedor.getSelectionModel().select(producto.getProveedor().getIdProveedor());
        }
        if(producto.getCosto() != 0d){
            textFieldCosto.setText(producto.getCosto().toString());
        }
        if(producto.getCodigo() != ""){
            textFieldCodigo.setText(producto.getCodigo());
        }
        if(producto.getDescripcion() != ""){
            textFieldDescripcion.setText(producto.getDescripcion().toString());
        }
        if(producto.getImagen() != ""){
            textFieldImagen.setText(producto.getImagen());
        }
        if(producto.getPermitirVender()){
            checkBoxPermitir.setSelected(true);
        }
        if(producto.getEstaActivo()){
            checkBoxActivo.setSelected(true);
        }
        if(producto.getControl()){
            checkBoxControl.setSelected(true);
        }
    }
}
