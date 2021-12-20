package com.daltysfood.dao.restaurante;

import com.daltysfood.model.daltys.Usuario;
import com.daltysfood.model.restaurante.Pedido;
import com.daltysfood.model.restaurante.PedidoProducto;
import com.daltysfood.model.restaurante.Producto;
import com.daltysfood.util.ConexionBDRestaurante;
import com.daltysfood.util.Sesion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoProductoDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public PedidoProductoDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    public List<Producto> getPedidoProducto(int idPedido) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        List<Integer> idDeProducto = new ArrayList<>();

        String sql = "SELECT * FROM Pedido_Producto WHERE Pedido_Producto.numeroDePedido =" + idPedido + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        while(resultSet.next()){
            idDeProducto.add(resultSet.getInt("idProducto"));
        }

        for (int i = 0; i < idDeProducto.size(); i++){
            Producto producto = new ProductoDAO(conexionBDRestaurante).getProductoMesa(idDeProducto.get(i));
            productos.add(producto);
        }

        return productos;
    }


}
