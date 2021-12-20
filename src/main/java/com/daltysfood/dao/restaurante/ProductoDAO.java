package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductoDAO {
    private ConexionBDRestaurante conexionBDRestaurante;
    private static final String SQL_INSERT = "INSERT INTO Producto (nombre, precio, costo, descripcion, codigo, imagen, estaActivo, permitirVender, control, idCategoriaDeProducto, idCocina, idProveedor, stock, stockMinimo) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE `Producto` SET `nombre` = ?, `precio` = ?, `costo` = ?, `descripcion` = ?, `codigo` = ?, `imagen` = ?, `estaActivo` = ?, `permitirVender` = ?, `control` = ?, `stock` = ?, `stockMinimo` = ?, `idCategoriaDeProducto` = ?, `idCocina` = ?, `idProveedor` = ? WHERE `Producto`.`idProducto` = ?";


    public ProductoDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /**
     * Prepara y pide la transaccion para consultar un solo producto
     * @param idDeProducto numero de producto que se quiere consultar
     * @return informacion del producto consultado
     * @throws SQLException
     */
    public Producto getProductoMesa(int idDeProducto) throws SQLException {
        Producto producto = new Producto();

        String sql = "SELECT * FROM Producto WHERE Producto.idProducto =" + idDeProducto + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if (resultSet.next()){
            producto.setNombre(resultSet.getString("nombre"));
            producto.setPrecio(resultSet.getDouble("precio"));
        }

        return producto;
    }

    /***
     * Prepara y pide ejecutar la transaccion para consultar los productos segun en la categoria que se encuentren
     * @param nombreCategoria categoria de la que se desea saber los productos que la conforman
     * @return Lista de productos asociados a una categoria
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<Producto> getProductsByCategory(String nombreCategoria) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        Producto producto = null;
        CategoriaDeProducto categoriaDeProducto = null;
        Proveedor proveedor = null;
        Cocina cocina = null;

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Producto.idProducto, Producto.codigo, Producto.stock, Producto.stockMinimo, Producto.descripcion, Producto.nombre, Producto.precio, Producto.costo, Producto.control, Producto.permitirVender, Producto.estaActivo, CategoriaDeProducto.idCategoriaDeProducto, CategoriaDeProducto.nombre as c, Cocina.idCocina, Cocina.nombre AS co, Proveedor.idProveedor, Proveedor.nombres AS p FROM Producto LEFT JOIN CategoriaDeProducto ON Producto.idCategoriaDeProducto = CategoriaDeProducto.idCategoriaDeProducto LEFT JOIN Cocina ON Producto.idCocina = Cocina.idCocina LEFT JOIN Proveedor ON Producto.idProveedor = Proveedor.idProveedor WHERE CategoriaDeProducto.nombre = '" + nombreCategoria + "'");

        while (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            producto = new Producto();
            categoriaDeProducto = new CategoriaDeProducto();
            proveedor = new Proveedor();
            cocina = new Cocina();
            categoriaDeProducto.setNombre(resultSet.getString("c"));
            categoriaDeProducto.setIdCategoriaDeProducto(resultSet.getInt("idCategoriaDeProducto"));
            proveedor.setNombres(resultSet.getString("p"));
            proveedor.setIdProveedor(resultSet.getInt("idProveedor"));
            cocina.setNombre(resultSet.getString("co"));
            cocina.setIdCocina(resultSet.getInt("idCocina"));
            producto.setCategoriaDeProducto(categoriaDeProducto);
            producto.setProveedor(proveedor);
            producto.setCocina(cocina);
            producto.setStock(resultSet.getInt("stock"));
            producto.setStockMinimo(resultSet.getInt("stockMinimo"));
            producto.setPrecio(resultSet.getDouble("precio"));
            producto.setCosto(resultSet.getDouble("costo"));
            producto.setPermitirVender(resultSet.getBoolean("permitirVender"));
            producto.setEstaActivo(resultSet.getBoolean("estaActivo"));
            producto.setNombre(resultSet.getString("nombre"));
            producto.setCodigo(resultSet.getString("codigo"));
            producto.setControl(resultSet.getBoolean("control"));
            producto.setDescripcion(resultSet.getString("descripcion"));
            producto.setIdProducto(resultSet.getInt("idProducto"));
            productos.add(producto);
        }
        return productos;
    }

    /***
     * Prepara y pide la transaccion para guardar o editar un nuevo producto en la BD
     * @param producto objeto que contiene la nueva informacion
     * @return Regsitros afectados
     * @throws SQLException SI hubo algun error al ejecutar la transaccion
     */
    public int guardar(Producto producto) throws SQLException{
        int affectedRows = 0;
        if(producto.getIdProducto() == null){
            PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_INSERT);
            preparedStatement.setString(1,producto.getNombre());
            preparedStatement.setString(2,producto.getPrecio().toString());
            preparedStatement.setString(3,producto.getCosto().toString());
            preparedStatement.setString(4,producto.getDescripcion());
            preparedStatement.setString(5,producto.getCodigo());
            preparedStatement.setString(6,producto.getImagen());
            String activo = (producto.getEstaActivo()) ? "1" : "0";
            String permitir = (producto.getPermitirVender()) ? "1" : "0";
            String control = (producto.getControl()) ? "1" : "0";
            preparedStatement.setString(7,activo);
            preparedStatement.setString(8,permitir);
            preparedStatement.setString(9,control);
            preparedStatement.setString(10,producto.getCategoriaDeProducto().getIdCategoriaDeProducto().toString());
            if(producto.getCocina() == null || producto.getCocina().getIdCocina() == 0){
                preparedStatement.setString(11,null);
            }else{
                preparedStatement.setString(11,producto.getCocina().getIdCocina().toString());
            }
            if(producto.getProveedor() == null || producto.getProveedor().getIdProveedor() == 0){
                preparedStatement.setString(12,null);
            }else{
                preparedStatement.setString(12,producto.getProveedor().getIdProveedor().toString());
            }
            preparedStatement.setString(13,"0");
            preparedStatement.setString(14,"0");
            affectedRows = preparedStatement.executeUpdate();
            conexionBDRestaurante.cerrarBDRestaurante();
            return affectedRows;
        }else{
            PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1,producto.getNombre());
            preparedStatement.setString(2,producto.getPrecio().toString());
            preparedStatement.setString(3,producto.getCosto().toString());
            preparedStatement.setString(4,producto.getDescripcion());
            preparedStatement.setString(5,producto.getCodigo());
            preparedStatement.setString(6,producto.getImagen());
            String activo = (producto.getEstaActivo()) ? "1" : "0";
            String permitir = (producto.getPermitirVender()) ? "1" : "0";
            String control = (producto.getControl()) ? "1" : "0";
            preparedStatement.setString(7,activo);
            preparedStatement.setString(8,permitir);
            preparedStatement.setString(9,control);
            preparedStatement.setString(10,producto.getStock().toString());
            preparedStatement.setString(11,producto.getStockMinimo().toString());
            preparedStatement.setString(12,producto.getCategoriaDeProducto().getIdCategoriaDeProducto().toString());
            if(producto.getCocina() == null || producto.getCocina().getIdCocina() == 0){
                preparedStatement.setString(13,null);
            }else{
                preparedStatement.setString(13,producto.getCocina().getIdCocina().toString());
            }
            if(producto.getCocina() == null || producto.getProveedor().getIdProveedor() == 0){
                preparedStatement.setString(14,null);
            }else{
                preparedStatement.setString(14,producto.getProveedor().getIdProveedor().toString());
            }
            preparedStatement.setString(15,producto.getIdProducto().toString());
            affectedRows = preparedStatement.executeUpdate();
            conexionBDRestaurante.cerrarBDRestaurante();
            return affectedRows;
        }
    }

    /***
     * Consulta todos los productos que estan en la bd
     * @return Lista de productos
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<Producto> getProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        Producto producto = null;
        CategoriaDeProducto categoriaDeProducto = null;
        Proveedor proveedor = null;
        Cocina cocina = null;

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Producto.idProducto, Producto.codigo, Producto.stock, Producto.stockMinimo, Producto.descripcion, Producto.nombre, Producto.precio, Producto.costo, Producto.control, Producto.permitirVender, Producto.estaActivo, CategoriaDeProducto.idCategoriaDeProducto, CategoriaDeProducto.nombre as c, Cocina.idCocina, Cocina.nombre AS co, Proveedor.idProveedor, Proveedor.nombres AS p FROM Producto LEFT JOIN CategoriaDeProducto ON Producto.idCategoriaDeProducto = CategoriaDeProducto.idCategoriaDeProducto LEFT JOIN Cocina ON Producto.idCocina = Cocina.idCocina LEFT JOIN Proveedor ON Producto.idProveedor = Proveedor.idProveedor; ");

        while (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            producto = new Producto();
            categoriaDeProducto = new CategoriaDeProducto();
            proveedor = new Proveedor();
            cocina = new Cocina();
            categoriaDeProducto.setNombre(resultSet.getString("c"));
            categoriaDeProducto.setIdCategoriaDeProducto(resultSet.getInt("idCategoriaDeProducto"));
            proveedor.setNombres(resultSet.getString("p"));
            proveedor.setIdProveedor(resultSet.getInt("idProveedor"));
            cocina.setNombre(resultSet.getString("co"));
            cocina.setIdCocina(resultSet.getInt("idCocina"));
            producto.setCategoriaDeProducto(categoriaDeProducto);
            producto.setProveedor(proveedor);
            producto.setCocina(cocina);
            producto.setStock(resultSet.getInt("stock"));
            producto.setStockMinimo(resultSet.getInt("stockMinimo"));
            producto.setPrecio(resultSet.getDouble("precio"));
            producto.setCosto(resultSet.getDouble("costo"));
            producto.setPermitirVender(resultSet.getBoolean("permitirVender"));
            producto.setEstaActivo(resultSet.getBoolean("estaActivo"));
            producto.setNombre(resultSet.getString("nombre"));
            producto.setCodigo(resultSet.getString("codigo"));
            producto.setControl(resultSet.getBoolean("control"));
            producto.setDescripcion(resultSet.getString("descripcion"));
            producto.setIdProducto(resultSet.getInt("idProducto"));
            productos.add(producto);
        }
        return productos;
    }
}
