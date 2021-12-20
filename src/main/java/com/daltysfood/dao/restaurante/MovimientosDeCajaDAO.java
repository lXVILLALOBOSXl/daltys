package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MovimientosDeCajaDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public MovimientosDeCajaDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /***
     * Consulta ls movimientos de caja y guarda los resultados de la transaccion
     * @return Lista de movimientos de caja
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<MovimientoDeCaja> getMovimientos() throws SQLException {

        List<MovimientoDeCaja> movimientoDeCajas = new ArrayList<>();
        MovimientoDeCaja movimientoDeCaja = null;
        TipoDeMovimiento tipoDeMovimiento = null;
        MedioDePago medioDePago = null;
        LocalDateTime fecha = null;

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT MovimientosDeCaja.idMovimientoDeCaja, MovimientosDeCaja.fecha, MovimientosDeCaja.monto, TipoDeMovimiento.concepto, MovimientosDeCaja.comentario, MedioDePago.nombre FROM MovimientosDeCaja INNER JOIN TipoDeMovimiento ON MovimientosDeCaja.idTipoDeMovimiento = TipoDeMovimiento.idTipoDeMovimiento INNER JOIN MedioDePago ON MovimientosDeCaja.idTipoDeMovimiento = TipoDeMovimiento.idTipoDeMovimiento GROUP BY MovimientosDeCaja.idMovimientoDeCaja");

        while (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            movimientoDeCaja = new MovimientoDeCaja();
            tipoDeMovimiento = new TipoDeMovimiento();
            medioDePago = new MedioDePago();
            movimientoDeCaja.setIdMovimientoDeCaja(resultSet.getInt("idMovimientoDeCaja"));
            movimientoDeCaja.setFecha(resultSet.getTimestamp("fecha", Calendar.getInstance()).toLocalDateTime());
            movimientoDeCaja.setMonto(resultSet.getDouble("monto"));
            movimientoDeCaja.setComentario(resultSet.getString("comentario"));
            tipoDeMovimiento.setConcepto(resultSet.getString("concepto"));
            medioDePago.setNombre(resultSet.getString("nombre"));
            movimientoDeCaja.setMedioDePago(medioDePago);
            movimientoDeCaja.setTipoDeMovimiento(tipoDeMovimiento);
            movimientoDeCajas.add(movimientoDeCaja);
        }
        return movimientoDeCajas;
    }

    /***
     * Consulta los egresos e ingresos para el balance
     * @return Arreglo con la infromacion consultada
     * @throws SQLException Si hubo algun problema en la transaccion
     */
    public List<Double> getInfo() throws SQLException{
        List<Double> infoBalance = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT (SELECT SUM(monto) FROM MovimientosDeCaja WHERE MovimientosDeCaja.idTipoDeMovimiento = 1) AS egresos, (SELECT SUM(monto) FROM MovimientosDeCaja WHERE MovimientosDeCaja.idTipoDeMovimiento = 2) AS ingresos");
        if(resultSet.next()){
            infoBalance = new ArrayList<>();
            infoBalance.add(resultSet.getDouble("egresos"));
            infoBalance.add(resultSet.getDouble("ingresos"));
        }
        return infoBalance;
    }

    /***
     * Prepara y pide la transaccion para guardar un nuevo registro de movimientos de caja en la BD
     * @param movimientoDeCaja objeto que contiene la nueva informacion
     * @return Regsitros afectados
     * @throws SQLException SI hubo algun error al ejecutar la transaccion
     */
    public int guardar(MovimientoDeCaja movimientoDeCaja) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String sql = "";

        if(movimientoDeCaja.getArqueoDeCaja() != null) {
            sql = "INSERT INTO MovimientosDeCaja(\n"
                    + "	monto, fecha, comentario, idMedioDePago, idTipoDeMovimiento, idArqueoDeCaja)\n"
                    + "	VALUES ('" + movimientoDeCaja.getMonto() + "' , '" + movimientoDeCaja.getFecha().format(formatter) + "' , '" + movimientoDeCaja.getComentario() + "' , '" + movimientoDeCaja.getMedioDePago().getIdMedioDePago() + "' , '" + movimientoDeCaja.getTipoDeMovimiento().getIdTipoDeMovimiento() + "' , '" + movimientoDeCaja.getArqueoDeCaja().getIdArqueoDeCaja() + "');";
        }else{
            sql = "INSERT INTO MovimientosDeCaja(\n"
                    + "	monto, fecha, comentario, idMedioDePago, idTipoDeMovimiento)\n"
                    + "	VALUES ('" + movimientoDeCaja.getMonto() + "' , '" + movimientoDeCaja.getFecha().format(formatter) + "' , '" + movimientoDeCaja.getComentario() + "' , '" + movimientoDeCaja.getMedioDePago().getIdMedioDePago() + "' , '" + movimientoDeCaja.getTipoDeMovimiento().getIdTipoDeMovimiento() + "');";
        }
        this.conexionBDRestaurante.getConexionBDRestaurante().setAutoCommit(false);

        PreparedStatement pst = this.conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        if (pst.executeUpdate() > 0) {
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int idventa = rs.getInt(1);
            this.conexionBDRestaurante.getConexionBDRestaurante().commit();
            return idventa;
        }

        return 0;
    }

    /***
     *  Pide realizar la transaccion para eliminar cuando se realiza una peticion desde el controlador
     * @param idMovimientoDeCaja Identificador de movimiento de caja
     * @return Si se puedo ejecutar la transaccion
     * @throws SQLException Si hubo algun error al ejecutar la transaccion
     */
    public boolean deleteItem(Integer idMovimientoDeCaja) throws SQLException {
        String sql = "DELETE FROM MovimientosDeCaja WHERE idMovimientoDeCaja ="+idMovimientoDeCaja;
        return conexionBDRestaurante.guardarBDRestaurante(sql);
    }
}
