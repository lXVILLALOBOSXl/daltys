package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CuentaCorrienteDAO {
    private ConexionBDRestaurante conexionBDRestaurante;
    private static final String SQL_INSERT = "INSERT INTO `CuentaCorriente` (`monto`, `comentario`, `estaPagado`, `fecha`, `idArqueoDeCaja`, `idMedioDePago`, `idProveedor`, `idTipoDeMovimiento`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE `CuentaCorriente` SET `estaPagado` = '1' WHERE `CuentaCorriente`.`idCuentaCorriente` = ?";

    public CuentaCorrienteDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public List<CuentaCorriente> getCuentasCorrientes() throws SQLException {
        List<CuentaCorriente> cuentasCorrientes = new ArrayList<>();
        CuentaCorriente cuentaCorriente = null;
        Proveedor proveedor = null;
        ArqueoDeCaja arqueoDeCaja = null;
        MedioDePago medioDePago = null;
        TipoDeMovimiento tipoDeMovimiento = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT CuentaCorriente.*, Proveedor.nombres, MedioDePago.nombre, TipoDeMovimiento.concepto FROM CuentaCorriente INNER JOIN Proveedor ON CuentaCorriente.idProveedor = Proveedor.idProveedor INNER JOIN MedioDePago ON CuentaCorriente.idMedioDePago = MedioDePago.idMedioDePago INNER JOIN TipoDeMovimiento ON CuentaCorriente.idTipoDeMovimiento = TipoDeMovimiento.idTipoDeMovimiento");

        while (resultSet.next()){
            cuentaCorriente = new CuentaCorriente();
            proveedor = new Proveedor();
            medioDePago = new MedioDePago();
            tipoDeMovimiento = new TipoDeMovimiento();
            arqueoDeCaja = new ArqueoDeCaja();
            proveedor.setIdProveedor(resultSet.getInt("idProveedor"));
            proveedor.setNombres(resultSet.getString("nombres"));
            medioDePago.setIdMedioDePago(resultSet.getInt("idMedioDePago"));
            medioDePago.setNombre(resultSet.getString("nombre"));
            tipoDeMovimiento.setIdTipoDeMovimiento(resultSet.getInt("idTipoDeMovimiento"));
            tipoDeMovimiento.setConcepto(resultSet.getString("concepto"));
            arqueoDeCaja.setIdArqueoDeCaja(resultSet.getInt("idArqueoDeCaja"));
            cuentaCorriente.setProveedor(proveedor);
            cuentaCorriente.setMedioDePago(medioDePago);
            cuentaCorriente.setTipoDeMovimiento(tipoDeMovimiento);
            cuentaCorriente.setArqueoDeCaja(arqueoDeCaja);
            cuentaCorriente.setIdCuentaCorriente(resultSet.getInt("idCuentaCorriente"));
            cuentaCorriente.setMonto(resultSet.getDouble("monto"));
            cuentaCorriente.setComentario(resultSet.getString("comentario"));
            cuentaCorriente.setEstaPagado(resultSet.getBoolean("estaPagado"));
            cuentaCorriente.setFecha(resultSet.getTimestamp("fecha", Calendar.getInstance()).toLocalDateTime());
            cuentasCorrientes.add(cuentaCorriente);
        }
        return cuentasCorrientes;

    }

    public List<Double> getResumen() throws SQLException {
        List<Double> resumen = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT SUM(CuentaCorriente.monto) AS balance, COUNT(CuentaCorriente.idCuentaCorriente) AS registros FROM CuentaCorriente WHERE estaPagado = 0");
        if(resultSet.next()){
            resumen = new ArrayList<>();
            resumen.add(resultSet.getDouble("balance"));
            resumen.add(resultSet.getDouble("registros"));
        }
        return resumen;
    }

    public int guardar(CuentaCorriente cuentaCorriente) throws SQLException{
        int affectedRows = 0;
        PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_INSERT);
        preparedStatement.setString(1,cuentaCorriente.getMonto().toString());
        preparedStatement.setString(2,cuentaCorriente.getComentario());
        String estaPagado = (cuentaCorriente.isEstaPagado()) ? "1" : "0";
        preparedStatement.setString(3,estaPagado);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        preparedStatement.setString(4,cuentaCorriente.getFecha().format(formatter));
        String arqueoDeCaja = (cuentaCorriente.getArqueoDeCaja() == null) ? null: cuentaCorriente.getArqueoDeCaja().getIdArqueoDeCaja().toString();
        preparedStatement.setString(5,arqueoDeCaja);
        preparedStatement.setString(6,cuentaCorriente.getMedioDePago().getIdMedioDePago().toString());
        preparedStatement.setString(7,cuentaCorriente.getProveedor().getIdProveedor().toString());
        preparedStatement.setString(8,"3");
        affectedRows = preparedStatement.executeUpdate();
        conexionBDRestaurante.cerrarBDRestaurante();
        return affectedRows;
    }

    public int deleteItem(CuentaCorriente cuentaCorriente) throws SQLException {
        int affectedRows = 0;
        PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_UPDATE);
        preparedStatement.setString(1,cuentaCorriente.getIdCuentaCorriente().toString());
        affectedRows = preparedStatement.executeUpdate();
        conexionBDRestaurante.cerrarBDRestaurante();
        return affectedRows;
    }
}
