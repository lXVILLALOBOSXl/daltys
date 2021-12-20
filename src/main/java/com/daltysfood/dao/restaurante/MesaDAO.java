package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.*;
import com.daltysfood.util.ConexionBDDaltys;
import com.daltysfood.util.ConexionBDRestaurante;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MesaDAO {
    private ConexionBDRestaurante conexionBDRestaurante;
    private static final String SQL_UPDATE = "UPDATE `Mesa` SET `idTrabajador` = ? WHERE `numeroDeMesa` = ?";

    public MesaDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /***
     * Se encarga de hacer una transaccion a la bd para traer y organizar la informacion que se desea mostrar en la mesa
     * @param numeroMesa de la cual se quiere saber la transaccion
     * @return informacion de mesa de manera organizada
     * @throws SQLException si la transaccion es incorrecta
     */
    public String getMesa(int numeroMesa) throws SQLException {
        //Se encarga de almacenar la informacion necesaria de la mesa
        String informacionMesa = "";

        //Mesa: Se utilizan para almacenar unicamente de la mesq
        Mesa mesa = new Mesa();
        Trabajador trabajador = new Trabajador();
        EstadoDeMesa estadoDeMesa = new EstadoDeMesa();

        //Se encarga de almacenar comensales NO REPETIDOS, mediante su ID
        LinkedHashMap<Integer, Comensal> comensales = new LinkedHashMap<>();
        Comensal comensal = new Comensal();

        //Se encarga de almacenar pedidos con sus respectivos productos NO REPETIDOS, mediante su Numero de Pedido
        LinkedHashMap<Integer, List<Producto>> pedidos = new LinkedHashMap<>();

        //Almacena los distintos productos que se encuentran en un pedido
        List<Producto> productos = new ArrayList<>();
        Producto producto = new Producto();

        //Almacena el numero de items de un producto
        List<PedidoProducto> pedidoProductos = new ArrayList<>();
        PedidoProducto pedidoProducto = new PedidoProducto();

        String sql = "SELECT Mesa.numeroDeMesa, Mesa.numeroDePersonas, EstadoDeMesa.estado, Comensal.nombre, Pedido.numeroDePedido, Producto.nombre AS nombreProducto, Producto.precio, Pedido_Producto.numeroDeProductos, Trabajador.nombres, Comensal.idComensal FROM Pedido_Producto INNER JOIN Pedido ON Pedido_Producto.numeroDePedido = Pedido.numeroDePedido INNER JOIN Comensal ON Pedido.idComensal = Comensal.idComensal INNER JOIN Mesa ON Comensal.idMesa = Mesa.numeroDeMesa INNER JOIN Producto ON Pedido_Producto.idProducto = Producto.idProducto INNER JOIN EstadoDeMesa ON Mesa.idEstadoDeMesa = EstadoDeMesa.idEstadoDeMesa INNER JOIN Trabajador ON Mesa.idTrabajador = Trabajador.idTrabajador WHERE Mesa.numeroDeMesa = " + numeroMesa + " AND Comensal.estaActivo = 1 ORDER BY Comensal.idComensal, Pedido.numeroDePedido;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);
        //El resultado de la consulta arroja campos repetidos porque depende de los productos que se encuentren dentro de los distintos pedidos, es por eso que utilizo
        //LinkedHashMaps y hago el siguiente algoritmo:

        //Si hubo un resultado a la transaccion
        if (resultSet.next()) {

            //cuentan el numero de personas que hay en una mesa
            int numeroPersona = 1;

            //Almacenamos la informacion SOLO DE LA MESA del resultado consulta
            trabajador.setNombres(resultSet.getString("nombres"));
            estadoDeMesa.setEstado(resultSet.getString("estado"));
            mesa.setTrabajador(trabajador);
            mesa.setEstadoDeMesa(estadoDeMesa);
            mesa.setNumeroDePersonas(resultSet.getInt("numeroDePersonas"));
            informacionMesa += "Mesa " + numeroMesa + ": \n";
            informacionMesa += "Encargado: " + mesa.getTrabajador().getNombres() + "\n";
            informacionMesa += "Estado: " + mesa.getEstadoDeMesa().getEstado() + "\n";
            informacionMesa += "Numero de Personas: " + mesa.getNumeroDePersonas() + "\n";

            //Si existio resultado de la transaccion, quiere decir que al menos hay un pedido, por ende productos y comensal.
            //Lo almacanemos para poder hacer comparaciones mediante sus respectivas keys
            pedidos.put(resultSet.getInt("numeroDePedido"), new ArrayList<Producto>());
            comensal.setNombre(resultSet.getString("nombre"));
            comensales.put(resultSet.getInt("idComensal"), comensal);

            //Guardamos y organizamos la informacion del primer comensal
            informacionMesa += "Comensal " + numeroPersona + ": " + comensales.get(resultSet.getInt("idComensal")).getNombre() + "\n";

            //Iteramos por el numero de productos que se pidieron en sus respectivos pedidos
            resultSet.beforeFirst();
            while (resultSet.next()) {

                //Guardamos la informacion del primer producto
                producto.setNombre(resultSet.getString("nombreProducto"));
                producto.setPrecio(resultSet.getDouble("precio"));
                pedidoProducto.setNumeroDeProductos(resultSet.getInt("numeroDeProductos"));

                //Si el producto en el que estamos iterando pertenece al mismo pedido y dicho pedido al mismo comnesal
                if (comensales.containsKey(resultSet.getInt("idComensal"))) {

                    //Si el producto en el que estamos iterando pertenece al mismo pedido
                    if (pedidos.containsKey(resultSet.getInt("numeroDePedido"))) {

                        //Solo agregamos dicho producto al pedido perteneciente al comensal
                        pedidos.get(resultSet.getInt("numeroDePedido")).add(producto);
                        pedidoProductos.add(pedidoProducto);
                        producto = new Producto();
                        pedidoProducto = new PedidoProducto();

                    //Si el producto ya no pertenece al mismo pedido pero si al mismo comensal
                    } else {

                        //Borramos los productos del anterior pedido y agregamos el producto en el que estamos iterando
                        productos = new ArrayList<>();
                        productos.add(producto);

                        //Agregamos el nuevo pedido con el nuevo producto perteneciente a un mismo comensal
                        pedidos.put(resultSet.getInt("numeroDePedido"), productos);
                    }

                //Si el producto sobre el que estamos iterando ya no pertenece al mismo comensal
                } else {

                    //Organizamos y guardamos la informacion de los pedidos del anterior comensal
                    //Guardamos los numeros de pedido del comensal anterior en una lista
                    List keys = new ArrayList(pedidos.keySet());
                    //Iteramos sobre los pedidos que tiene el anterior comensal
                    for (int i = 0; i < keys.size(); i++) {
                        informacionMesa += "Pedido " + (i + 1) + ": " + keys.get(i) + "\n";
                        //Iteramos sobre los produtos que tienen los pedidos del anterior comensal
                        for (int x = 0; x < pedidos.get(keys.get(i)).size(); x++) {
                            informacionMesa +=  pedidoProductos.get(x).getNumeroDeProductos() + " PZA " + pedidos.get(keys.get(i)).get(x).getNombre() + " $" + pedidos.get(keys.get(i)).get(x).getPrecio() + "\n";
                        }
                    }

                    //agregamos el nuevo comensal
                    comensal.setNombre(resultSet.getString("nombre"));
                    comensales.put(resultSet.getInt("idComensal"), comensal);
                    informacionMesa += "Comensal " + (++numeroPersona) + ": " + comensales.get(resultSet.getInt("idComensal")).getNombre() + "\n";
                    //Limpiamos los pedidos del anterior comensal
                    pedidos = new LinkedHashMap<>();
                    
                    //Borramos los productos del anterior pedido y agregamos el producto en el que estamos iterando
                    productos = new ArrayList<>();
                    productos.add(producto);

                    pedidoProductos = new ArrayList<>();
                    pedidoProductos.add(pedidoProducto);

                    //Agregamos el nuevo pedido con el nuevo producto perteneciente a un mismo comensal
                    pedidos.put(resultSet.getInt("numeroDePedido"), productos);
                }
            }
            //Almacenamos los pedidos del ultimo comensal
            List keys = new ArrayList(pedidos.keySet());
            for (int i = 0; i < keys.size(); i++) {
                informacionMesa += "Pedido " + (i + 1) + ": " + keys.get(i) + "\n";
                for (int x = 0; x < pedidos.get(keys.get(i)).size(); x++) {
                    informacionMesa +=  pedidoProductos.get(x).getNumeroDeProductos() + " PZA " + pedidos.get(keys.get(i)).get(x).getNombre() + " $" + pedidos.get(keys.get(i)).get(x).getPrecio() + "\n";
                }
            }
            //si la mesa aun no tiene un pedido en estado de encargado, mostramos solo la informacion necesaria
        }else{
            sql = "SELECT Mesa.numeroDeMesa, Mesa.numeroDePersonas, EstadoDeMesa.estado,Trabajador.nombres FROM Mesa INNER JOIN EstadoDeMesa ON Mesa.idEstadoDeMesa = EstadoDeMesa.idEstadoDeMesa INNER JOIN Trabajador ON Mesa.idTrabajador = Trabajador.idTrabajador WHERE Mesa.numeroDeMesa = " + numeroMesa + " ;";
            resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);
            if(resultSet.next()){
                //Almacenamos la informacion SOLO DE LA MESA del resultado consulta
                trabajador.setNombres(resultSet.getString("nombres"));
                estadoDeMesa.setEstado(resultSet.getString("estado"));
                mesa.setTrabajador(trabajador);
                mesa.setEstadoDeMesa(estadoDeMesa);
                mesa.setNumeroDePersonas(resultSet.getInt("numeroDePersonas"));
                informacionMesa += "Mesa " + numeroMesa + ": \n";
                informacionMesa += "Encargado: " + mesa.getTrabajador().getNombres() + "\n";
                informacionMesa += "Estado: " + mesa.getEstadoDeMesa().getEstado() + "\n";
                informacionMesa += "Numero de Personas: " + mesa.getNumeroDePersonas() + "\n";
            }else{
                informacionMesa += "Error \n";
            }

        }

        //System.out.println(informacionMesa);
        return informacionMesa;
    }


    public int asignarEncargado(Integer mesa,Trabajador trabajador) throws SQLException {
        int affectedRows = 0;
        PreparedStatement preparedStatement = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(SQL_UPDATE);
        preparedStatement.setString(1,trabajador.getIdTrabajador().toString());
        preparedStatement.setString(2,mesa.toString());
        affectedRows = preparedStatement.executeUpdate();
        conexionBDRestaurante.cerrarBDRestaurante();
        return affectedRows;
    }
}
