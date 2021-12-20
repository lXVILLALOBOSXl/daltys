package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.ArqueoDeCaja;
import com.daltysfood.model.restaurante.MedioDePago;
import com.daltysfood.model.restaurante.MovimientoDeCaja;
import com.daltysfood.model.restaurante.TipoDeMovimiento;
import com.daltysfood.util.ConexionBDRestaurante;
import org.exolab.castor.types.DateTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ArqueoDeCajaDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public ArqueoDeCajaDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /***
     * Consulta los arqueos de caja y guarda los resultados de la transaccion
     * @return Lista de arqueos de caja
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<ArqueoDeCaja> getArqueos() throws SQLException {
        List<ArqueoDeCaja> arqueosDeCaja = new ArrayList<>();
        ArqueoDeCaja arqueoDeCaja = null;
        LocalDateTime fechaFin;

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT * FROM ArqueoDeCaja");

        while (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            arqueoDeCaja = new ArqueoDeCaja();
            arqueoDeCaja.setIdArqueoDeCaja(resultSet.getInt("idArqueoDeCaja"));
            arqueoDeCaja.setFechaInicio(resultSet.getTimestamp("fechaInicio", Calendar.getInstance()).toLocalDateTime());
            fechaFin = resultSet.getTimestamp("fechaFin") == null ? null : resultSet.getTimestamp("fechaFin", Calendar.getInstance()).toLocalDateTime();
            arqueoDeCaja.setFechaFin(fechaFin);
            arqueoDeCaja.setUsuario(resultSet.getDouble("usuario"));
            arqueoDeCaja.setSistema(resultSet.getDouble("sistema"));
            arqueoDeCaja.setDiferencia(resultSet.getDouble("diferencia"));
            arqueoDeCaja.setMontoInicial(resultSet.getDouble("montoInicial"));
            arqueoDeCaja.setEstaActiva(resultSet.getBoolean("estaActiva"));
            arqueosDeCaja.add(arqueoDeCaja);
        }
        return arqueosDeCaja;
    }

    /***
     * Consulta el unico arqueo que se encuantra activo para poder asociarlo con los movimientos
     * que se usan en el arqueo
     * @return arqueo activo
     * @throws SQLException si hubo algun problema con la transaccion
     */
    public ArqueoDeCaja getArqueoActivo() throws SQLException {
        ArqueoDeCaja arqueoDeCaja = null;
        LocalDateTime fechaFin;

        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT * FROM ArqueoDeCaja where estaActiva = 1");

        if (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            arqueoDeCaja = new ArqueoDeCaja();
            arqueoDeCaja.setIdArqueoDeCaja(resultSet.getInt("idArqueoDeCaja"));
            arqueoDeCaja.setFechaInicio(resultSet.getTimestamp("fechaInicio", Calendar.getInstance()).toLocalDateTime());
            fechaFin = resultSet.getTimestamp("fechaFin") == null ? null : resultSet.getTimestamp("fechaFin", Calendar.getInstance()).toLocalDateTime();
            arqueoDeCaja.setFechaFin(fechaFin);
            arqueoDeCaja.setUsuario(resultSet.getDouble("usuario"));
            arqueoDeCaja.setSistema(resultSet.getDouble("sistema"));
            arqueoDeCaja.setDiferencia(resultSet.getDouble("diferencia"));
            arqueoDeCaja.setEstaActiva(resultSet.getBoolean("estaActiva"));
        }
        return arqueoDeCaja;
    }

    /***
     * Hace una peticion para consultar los movimeintos relacionados con el arqueo de caja
     * @param idArqueoDeCaja identificador del arqueo seleccionado
     * @return montos de movimientos relacionados con el arqueo de caja seleccionado
     * @throws SQLException si hubo algun problema con la transaccion
     */
    public List<Double> getMovimientos(int idArqueoDeCaja) throws SQLException {
        List<Double> montosDeMovimientos = new ArrayList<>();

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT (SELECT SUM(importe) FROM Gasto WHERE Gasto.idArqueoDeCaja = " + idArqueoDeCaja + ") AS gastos, (SELECT SUM(monto) FROM CuentaCorriente WHERE CuentaCorriente.idArqueoDeCaja = " + idArqueoDeCaja + ") AS cuentaCorriente, (SELECT SUM(monto) FROM MovimientosDeCaja WHERE MovimientosDeCaja.idArqueoDeCaja = " + idArqueoDeCaja + " AND MovimientosDeCaja.idTipoDeMovimiento = 2) AS movimientosDeCajaIngreso, (SELECT SUM(monto) FROM MovimientosDeCaja WHERE MovimientosDeCaja.idArqueoDeCaja = " + idArqueoDeCaja + " AND MovimientosDeCaja.idTipoDeMovimiento = 1) AS movimientosDeCajaEgresos FROM DUAL");

        if (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            montosDeMovimientos.add(resultSet.getDouble("gastos"));
            montosDeMovimientos.add(resultSet.getDouble("cuentaCorriente"));
            montosDeMovimientos.add(resultSet.getDouble("movimientosDeCajaIngreso"));
            montosDeMovimientos.add(resultSet.getDouble("movimientosDeCajaEgresos"));
        }
        return montosDeMovimientos;
    }

    /***
     * Realiza la peticion para actualizar el arqueo de caja con la informacion del
     * arqueo que se quiere cerrar
     * @param arqueoDeCaja arqueo que se quiere cerrar
     * @return verdadero si se pudo realizar la transaccion
     * @throws SQLException Si no se pudo realizar la transaccion
     */
    public boolean updateArqueo(ArqueoDeCaja arqueoDeCaja) throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String sql = "UPDATE ArqueoDeCaja\n"
                + "	SET fechaFin = '" +LocalDateTime.now().format(formatter) + "'," + "usuario = '" + arqueoDeCaja.getUsuario() + "'," + "sistema = '" + arqueoDeCaja.getSistema() + "'," + "diferencia = '" + arqueoDeCaja.getDiferencia() + "'," + "estaActiva = '" + 0 + "'" +"\n"
                + "	WHERE idArqueoDeCaja = " + arqueoDeCaja.getIdArqueoDeCaja() + ";" ;

        PreparedStatement pst = this.conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(sql);

        if (pst.executeUpdate() > 0) {
            return true;
        }
        return false;
    }

    /***
     * Se encarga de hacer la peticion con la informacion necesaria para agregar un nuevo arqueo a la base de datos
     * @param arqueoDeCaja nuevo elemento que se va agregar
     * @return rows afectadas
     * @throws SQLException si hhubo algun problema en la transaccion
     */
    public int guardar(ArqueoDeCaja arqueoDeCaja) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String sql = "INSERT INTO ArqueoDeCaja(\n"
                + "	fechaInicio, sistema, montoInicial, estaActiva)\n"
                + "	VALUES ('"+arqueoDeCaja.getFechaInicio().format(formatter)+"' , '"+ arqueoDeCaja.getSistema() +"' , '"+ arqueoDeCaja.getMontoInicial()+"' , '"+ 1 +"');";

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
}
