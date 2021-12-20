package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PedidoDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public PedidoDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }


    /***
     * Consulta los pedidos pendientes y guarda los resultados de la transaccion
     * @return Lista de pedidos pendientes
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<Pedido> getPedidos() throws SQLException {

        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido = null;
        Mesa mesa = null;
        Comensal comensal = null;

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Pedido.numeroDePedido, Pedido.fechaInicio, Mesa.numeroDeMesa, Comensal.nombre, Pedido.montoTotal FROM Pedido INNER JOIN Comensal ON Pedido.idComensal = Comensal.idComensal INNER JOIN Mesa ON Mesa.numeroDeMesa = Comensal.idMesa INNER JOIN EstadoPedido ON Pedido.idEstadoPedido = EstadoPedido.idEstadoPedido WHERE EstadoPedido.idEstadoPedido = 1 ORDER BY Pedido.numeroDePedido");

        while (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            pedido = new Pedido();
            mesa = new Mesa();
            comensal = new Comensal();

            pedido.setNumeroDePedido(resultSet.getInt("numeroDePedido"));
            pedido.setFechaInicio(resultSet.getTimestamp("fechaInicio", Calendar.getInstance()).toLocalDateTime());
            mesa.setNumeroDeMesa(resultSet.getInt("numeroDeMesa"));
            comensal.setNombre(resultSet.getString("nombre"));
            pedido.setMontoTotal(resultSet.getDouble("montoTotal"));
            pedido.setMesa(mesa);
            pedido.setComensal(comensal);

            pedidos.add(pedido);

        }
        return pedidos;
    }

    /***
     * Consulta los pedidos cerrados o en curso y guarda los resultados de la transaccion
     * @param idEstadoPedido Segun su id el tipo de estado: en curso o cerrado
     * @return  Lista de pedidos cerrados o en curso
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<Pedido> getPedidos(int idEstadoPedido) throws SQLException {

        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido = null;
        EstadoPedido estadoPedido = null;
        Comensal comensal = null;

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Pedido.numeroDePedido, Pedido.fechaInicio, EstadoPedido.estado, Comensal.nombre, Pedido.montoTotal FROM Pedido INNER JOIN Comensal ON Pedido.idComensal = Comensal.idComensal INNER JOIN EstadoPedido ON Pedido.idEstadoPedido = EstadoPedido.idEstadoPedido WHERE EstadoPedido.idEstadoPedido = " + idEstadoPedido + " ORDER BY Pedido.numeroDePedido");

        while (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            pedido = new Pedido();
            estadoPedido = new EstadoPedido();
            comensal = new Comensal();

            pedido.setNumeroDePedido(resultSet.getInt("numeroDePedido"));
            pedido.setFechaInicio(resultSet.getTimestamp("fechaInicio", Calendar.getInstance()).toLocalDateTime());
            estadoPedido.setEstado(resultSet.getString("estado"));
            comensal.setNombre(resultSet.getString("nombre"));
            pedido.setMontoTotal(resultSet.getDouble("montoTotal"));
            pedido.setEstadoPedido(estadoPedido);
            pedido.setComensal(comensal);

            pedidos.add(pedido);

        }
        return pedidos;
    }

    /***
     * Consulta las propinas y guarda los resultados de la transaccion
     * @return  Lista de pedidos asociados a las propinas
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public List<Pedido> getPropinas() throws SQLException {

        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido = null;
        Comensal comensal = null;
        Trabajador trabajador = null;
        Mesa mesa = null;
        MedioDePago medioDePago = null;

        //El resutado de la transaccion depende del numero de pedidos en dicho estado de pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Pedido.fechaFin, Pedido.fechaInicio, Pedido.idMesa, Trabajador.nombres, Comensal.nombre, MedioDePago.nombre AS medioDePago, Pedido.montoTotal, Pedido.propina FROM Pedido INNER JOIN Mesa ON Mesa.numeroDeMesa = Pedido.idMesa INNER JOIN Trabajador ON Mesa.idTrabajador = Trabajador.idTrabajador INNER JOIN Comensal ON Pedido.idComensal = Comensal.idComensal INNER JOIN MedioDePago ON MedioDePago.idMedioDePago = Pedido.idMedioDePago WHERE Pedido.idEstadoPedido = 4 AND propina IS NOT NULL");

        while (resultSet.next()){
            //Almacenamos la informacion necesaria que vamos a mostrar en el pedido
            pedido = new Pedido();
            comensal = new Comensal();
            trabajador = new Trabajador();
            mesa = new Mesa();
            medioDePago = new MedioDePago();

            pedido.setFechaFin(resultSet.getTimestamp("fechaFin", Calendar.getInstance()).toLocalDateTime());
            pedido.setFechaInicio(resultSet.getTimestamp("fechaInicio", Calendar.getInstance()).toLocalDateTime());
            mesa.setNumeroDeMesa(resultSet.getInt("idMesa"));
            trabajador.setNombres(resultSet.getString("nombres"));
            mesa.setTrabajador(trabajador);
            pedido.setMesa(mesa);
            comensal.setNombre(resultSet.getString("nombre"));
            pedido.setComensal(comensal);
            medioDePago.setNombre(resultSet.getString("medioDePago"));
            pedido.setMedioDePago(medioDePago);
            pedido.setMontoTotal(resultSet.getDouble("montoTotal"));
            pedido.setPropina(resultSet.getDouble("propina"));

            pedidos.add(pedido);
        }
        return pedidos;
    }

    /***
     * Consulta la informacion de un pedido seleccionado
     * @param numeroDePedido Seleccionado
     * @return Informacion del pedido seleccionado
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public String getSelectedItem(int numeroDePedido) throws SQLException {

        //Almacena la informacion del pedido
        String informacionPedido = "";

        //Consulta para la informacion del pedido
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT Comensal.nombre, Mesa.numeroDeMesa, Pedido.numeroDePedido, Pedido.fechaInicio, Pedido.comentario, Producto.nombre AS nombreProducto, Producto.precio, Pedido_Producto.numeroDeProductos FROM Producto INNER JOIN Pedido_Producto ON Producto.idProducto = Pedido_Producto.idProducto INNER JOIN Pedido ON Pedido.numeroDePedido = Pedido_Producto.numeroDePedido INNER JOIN Comensal ON Pedido.idComensal = Comensal.idComensal INNER JOIN Mesa ON Comensal.idMesa = Mesa.numeroDeMesa WHERE Pedido.numeroDePedido = " + numeroDePedido  + " ;");

        //Si hubo un resultado
        if (resultSet.next()){
            //Almacenamos la informacion que se repite en el resultado de la consulta, 1 sola vez, ya que dicha consulta depende del numero de productos
            //que tiene dicho pedido
            informacionPedido += "Mesa: " + resultSet.getInt("numeroDeMesa") + "\n";
            informacionPedido += "Comensal: " + resultSet.getString("nombre") + "\n";
            informacionPedido += "Pedido: " + resultSet.getString("numeroDePedido") + "\n";
            informacionPedido += "Solicitado: " + resultSet.getTimestamp("fechaInicio", Calendar.getInstance()).toLocalDateTime() + "\n";
            //Nos regresamos al inicio del resultado de la consulta
            resultSet.beforeFirst();
            //Mientras haya mas resultados, quiere decir que hay mas productos
            while (resultSet.next()){
             //Almacenamos los productos
             informacionPedido += resultSet.getInt("numeroDeProductos") + " PZA " + resultSet.getString("nombreProducto") + " " + resultSet.getDouble("precio") + "\n";
            }
        }
        return informacionPedido;
    }

}
