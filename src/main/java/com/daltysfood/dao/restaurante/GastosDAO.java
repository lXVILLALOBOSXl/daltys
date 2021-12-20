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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GastosDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public GastosDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /***
     * Consulta los gastos y guarda los resultados de la transaccion
     * @return Lista de gastos
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<Gasto> getGastos() throws SQLException {

        List<Gasto> gastos = new ArrayList<>();
        Gasto gasto = null;
        MedioDePago medioDePago = null;
        CategoriaDeGasto categoriaDeGasto = null;
        Proveedor proveedor = null;
        ArqueoDeCaja arqueoDeCaja = null;

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Gasto.idGasto, Gasto.fecha, Gasto.importe, Gasto.numeroDeComprobante, Gasto.comentario, MedioDePago.nombre AS medioDePagoNombre, CategoriaDeGasto.nombre, Proveedor.nombres, ArqueoDeCaja.estaActiva FROM Gasto INNER JOIN MedioDePago on Gasto.idMedioDePago = MedioDePago.idMedioDePago LEFT JOIN CategoriaDeGasto ON Gasto.idCategoriaDeGastos = CategoriaDeGasto.idCategoriaDeGasto INNER JOIN Proveedor ON Gasto.idProveedor = Proveedor.idProveedor LEFT JOIN ArqueoDeCaja ON Gasto.idArqueoDeCaja = ArqueoDeCaja.idArqueoDeCaja");

        while (resultSet.next()){
            gasto = new Gasto();
            medioDePago = new MedioDePago();
            categoriaDeGasto = new CategoriaDeGasto();
            proveedor = new Proveedor();
            arqueoDeCaja = new ArqueoDeCaja();
            gasto.setIdGasto(resultSet.getInt("idGasto"));
            gasto.setFecha(resultSet.getTimestamp("fecha", Calendar.getInstance()).toLocalDateTime());
            gasto.setImporte(resultSet.getDouble("importe"));
            gasto.setNumeroDeComprobante(resultSet.getInt("numeroDeComprobante"));
            gasto.setComentario(resultSet.getString("comentario"));
            medioDePago.setNombre(resultSet.getString("medioDePagoNombre"));
            categoriaDeGasto.setNombre(resultSet.getString("nombre"));
            proveedor.setNombres(resultSet.getString("nombres"));
            arqueoDeCaja.setEstaActiva(resultSet.getBoolean("estaActiva"));
            gasto.setMedioDePago(medioDePago);
            gasto.setCategoriaDeGasto(categoriaDeGasto);
            gasto.setProveedor(proveedor);
            gasto.setArqueoDeCaja(arqueoDeCaja);
            gastos.add(gasto);
        }


        return gastos;
    }

    /***
     * Consulta la informacion (registros y suma de los gastos) para el encabezado de la tabla
     * @return Arreglo con la infromacion consultada
     * @throws SQLException Si hubo algun problema en la transaccion
     */
    public List<Double> getInfo() throws SQLException{
        List<Double> infoGastos = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT (SELECT SUM(importe) FROM Gasto) AS total, (SELECT COUNT(*) FROM Gasto) AS registros");
        if(resultSet.next()){
            infoGastos = new ArrayList<>();
            infoGastos.add(resultSet.getDouble("total"));
            infoGastos.add(resultSet.getDouble("registros"));
        }
        return infoGastos;
    }

    /***
     *  Pide realizar la transaccion cuando se realiza una peticion desde el controlador
     * @param idGasto Identificador de gasto seleccionado
     * @return Si se puedo ejecutar la transaccion
     * @throws SQLException Si hubo algun error al ejecutar la transaccion
     */
    public boolean deleteItem(Integer idGasto) throws SQLException {
        String sql = "DELETE FROM Gasto WHERE idGasto ="+idGasto;
        return conexionBDRestaurante.guardarBDRestaurante(sql);
    }

    /***
     * Prepara y pide la transaccion para guardar un nuevo registro de gastos en la BD
     * @param gasto objeto que contiene la nueva informacion
     * @return Regsitros afectados
     * @throws SQLException SI hubo algun error al ejecutar la transaccion
     */
    public int guardar(Gasto gasto) throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql = "";

        if(gasto.getArqueoDeCaja() == null || gasto.getCategoriaDeGasto() == null){
            if(gasto.getArqueoDeCaja() == null && gasto.getCategoriaDeGasto() == null){
                sql = "INSERT INTO Gasto(\n"
                        + "	importe, fecha, comentario, idMedioDePago, idProveedor, numeroDeComprobante)\n"
                        + "	VALUES ('"+gasto.getImporte()+"' , '"+ gasto.getFecha().format(formatter) +"' , '"+ gasto.getComentario()+"' , '"+ gasto.getMedioDePago().getIdMedioDePago()+"' , '"+ gasto.getProveedor().getIdProveedor()+"' , '"+ gasto.getNumeroDeComprobante() +"');";
            }else if(gasto.getCategoriaDeGasto() == null){
                sql = "INSERT INTO Gasto(\n"
                        + "	importe, fecha, comentario, idMedioDePago, idProveedor, idArqueoDeCaja, numeroDeComprobante)\n"
                        + "	VALUES ('"+gasto.getImporte()+"' , '"+ gasto.getFecha().format(formatter) +"' , '"+ gasto.getComentario()+"' , '"+ gasto.getMedioDePago().getIdMedioDePago()+"' , '"+ gasto.getProveedor().getIdProveedor()+"' , '"+ gasto.getArqueoDeCaja().getIdArqueoDeCaja()+"' , '"+ gasto.getNumeroDeComprobante() +"');";
            }else if (gasto.getArqueoDeCaja() == null){
                sql = "INSERT INTO Gasto(\n"
                        + "	importe, fecha, comentario, idMedioDePago, idProveedor, idCategoriaDeGastos, numeroDeComprobante)\n"
                        + "	VALUES ('"+gasto.getImporte()+"' , '"+ gasto.getFecha().format(formatter) +"' , '"+ gasto.getComentario()+"' , '"+ gasto.getMedioDePago().getIdMedioDePago()+"' , '"+ gasto.getProveedor().getIdProveedor()+"' , '"+ gasto.getCategoriaDeGasto().getIdCategoriaDeGasto() +"', '"+ gasto.getNumeroDeComprobante() +"');";
            }
        }else{
            sql = "INSERT INTO Gasto(\n"
                    + "	importe, fecha, comentario, idMedioDePago, idProveedor, idCategoriaDeGastos, idArqueoDeCaja, numeroDeComprobante)\n"
                    + "	VALUES ('"+gasto.getImporte()+"' , '"+ gasto.getFecha().format(formatter) +"' , '"+ gasto.getComentario()+"' , '"+ gasto.getMedioDePago().getIdMedioDePago()+"' , '"+ gasto.getProveedor().getIdProveedor()+"' , '"+ gasto.getCategoriaDeGasto().getIdCategoriaDeGasto() +"', '"+ gasto.getArqueoDeCaja().getIdArqueoDeCaja()+"' , '"+ gasto.getNumeroDeComprobante() +"');";
        }

        this.conexionBDRestaurante.getConexionBDRestaurante().setAutoCommit(false);

        PreparedStatement pst = this.conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        if (pst.executeUpdate() > 0) {
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int idGasto = rs.getInt(1);
            this.conexionBDRestaurante.getConexionBDRestaurante().commit();
            return idGasto;
        }

        return 0;

    }
}
