package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.CuentaCorriente;
import com.daltysfood.model.restaurante.MedioDePago;
import com.daltysfood.model.restaurante.Proveedor;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {
    private ConexionBDRestaurante conexionBDRestaurante;
    private static final String SQL_INSERT = "INSERT INTO `Proveedor` (`nombres`, `apellidoPaterno`, `apellidoMaterno`, `correo`, `telefono`, `rfc`, `calle`, `numeroExterior`, `numeroInterior`, `codigoPostal`, `estaActivo`, `comentario`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE `Proveedor` SET `nombres` = ?, `apellidoPaterno` = ?, `apellidoMaterno` = ?, `correo` = ?, `telefono` = ?, `rfc` = ?, `calle` = ?, `numeroExterior` = ?, `numeroInterior` = ?, `codigoPostal` = ?, `estaActivo` = ?, `comentario` = ? WHERE `Proveedor`.`idProveedor` = ?";
    private static final String SQL_DELETE = "DELETE FROM `Proveedor` WHERE `Proveedor`.`idProveedor` = ?";


    public ProveedorDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /**
     * Preparay pide la transaccion para consultar y guardar la informacion de los proveedores que
     * se encuentran activos
     * @return Lista con informacion de los proveedores activos
     * @throws SQLException Si hubo un error al ejecutar la transaccion
     */
    public ObservableList<Proveedor> getProveedoresActivos() throws SQLException {
        Proveedor proveedor = null;
        ObservableList<Proveedor> proveedoresActivos = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Proveedor WHERE estaActivo = 1;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        while (resultSet.next()){
            proveedor = new Proveedor();
            proveedor.setIdProveedor(resultSet.getInt("idProveedor"));
            proveedor.setNombres(resultSet.getString("nombres"));
            proveedoresActivos.add(proveedor);
        }

        return proveedoresActivos;
    }

    /***
     * Consulta todos los proveedores que estan en la bd
     * @return Lista de proveedores
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<Proveedor> getProveedores() throws SQLException {
        List<Proveedor> proveedores = new ArrayList<>();
        Proveedor proveedor = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT SUM(CuentaCorriente.monto) AS saldo, Proveedor.* FROM Proveedor LEFT JOIN CuentaCorriente ON Proveedor.idProveedor = CuentaCorriente.idProveedor GROUP BY Proveedor.idProveedor");

        while (resultSet.next()){
            proveedor = new Proveedor();
            proveedor.setSaldo(resultSet.getDouble("saldo"));
            proveedor.setIdProveedor(resultSet.getInt("idProveedor"));
            proveedor.setNombres(resultSet.getString("nombres"));
            proveedor.setApellidoMaterno(resultSet.getString("apellidoMaterno"));
            proveedor.setApellidoPaterno(resultSet.getString("apellidoPaterno"));
            proveedor.setCorreo(resultSet.getString("correo"));
            proveedor.setTelefono(resultSet.getString("telefono"));
            proveedor.setRfc(resultSet.getString("rfc"));
            proveedor.setCalle(resultSet.getString("calle"));
            proveedor.setNumeroExterior(resultSet.getString("numeroExterior"));
            proveedor.setNumeroInterior(resultSet.getString("numeroInterior"));
            proveedor.setCodigoPostal(resultSet.getString("codigoPostal"));
            proveedor.setComentario(resultSet.getString("comentario"));
            proveedor.setEstaActivo(resultSet.getBoolean("estaActivo"));
            proveedores.add(proveedor);
        }
        return proveedores;
    }

    /**
     * Prepara y pide la transaccion para eliminar proveedor en la BD
     * @param idProveedor Proveedor que se desea eliminar
     * @return Regsitros afectados
     * @throws SQLException SI hubo algun error al ejecutar la transaccion
     */
    public int deleteItem(Integer idProveedor) throws SQLException {
        int affectedRows = 0;
        PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_DELETE);
        preparedStatement.setString(1,idProveedor.toString());
        affectedRows = preparedStatement.executeUpdate();
        conexionBDRestaurante.cerrarBDRestaurante();
        return affectedRows;
    }

    /***
     * Prepara y pide la transaccion para guardar o editar un nuevo proveedor en la BD
     * @param proveedor objeto que contiene la nueva informacion
     * @return Regsitros afectados
     * @throws SQLException SI hubo algun error al ejecutar la transaccion
     */
    public int guardar(Proveedor proveedor) throws SQLException {
        int affectedRows = 0;
        if(proveedor.getIdProveedor() == null){
            PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_INSERT);
            preparedStatement.setString(1,proveedor.getNombres());
            preparedStatement.setString(2,proveedor.getApellidoPaterno());
            preparedStatement.setString(3,proveedor.getApellidoMaterno());
            preparedStatement.setString(4,proveedor.getCorreo());
            preparedStatement.setString(5,proveedor.getTelefono());
            preparedStatement.setString(6,proveedor.getRfc());
            preparedStatement.setString(7,proveedor.getCalle());
            preparedStatement.setString(8,proveedor.getNumeroExterior());
            preparedStatement.setString(9,proveedor.getNumeroInterior());
            preparedStatement.setString(10,proveedor.getCodigoPostal());
            String activo = (proveedor.getEstaActivo()) ? "1" : "0";
            preparedStatement.setString(11,activo);
            preparedStatement.setString(12,proveedor.getComentario());
            affectedRows = preparedStatement.executeUpdate();
            conexionBDRestaurante.cerrarBDRestaurante();
            return affectedRows;
        }else{
            PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1,proveedor.getNombres());
            preparedStatement.setString(2,proveedor.getApellidoPaterno());
            preparedStatement.setString(3,proveedor.getApellidoMaterno());
            preparedStatement.setString(4,proveedor.getCorreo());
            preparedStatement.setString(5,proveedor.getTelefono());
            preparedStatement.setString(6,proveedor.getRfc());
            preparedStatement.setString(7,proveedor.getCalle());
            preparedStatement.setString(8,proveedor.getNumeroExterior());
            preparedStatement.setString(9,proveedor.getNumeroInterior());
            preparedStatement.setString(10,proveedor.getCodigoPostal());
            String activo = (proveedor.getEstaActivo()) ? "1" : "0";
            preparedStatement.setString(11,activo);
            preparedStatement.setString(12,proveedor.getComentario());
            preparedStatement.setString(13,proveedor.getIdProveedor().toString());
            affectedRows = preparedStatement.executeUpdate();
            conexionBDRestaurante.cerrarBDRestaurante();
            return affectedRows;
        }
        //return affectedRows;
    }
}
