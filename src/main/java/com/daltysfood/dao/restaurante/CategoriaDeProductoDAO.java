package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.CategoriaDeGasto;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.Cocina;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDeProductoDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public CategoriaDeProductoDAO(ConexionBDRestaurante conexionBDRestaurante) {
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /**
     * Prepara y pide latransaccion para consultar una sola categoria de producto
     * @param idCategoriaDeProducto numero de categoria que se quiere consultar
     * @return informacion de la categoria consultada
     * @throws SQLException
     */
    public CategoriaDeProducto getCategoriaDeProducto(int idCategoriaDeProducto) throws SQLException {
        CategoriaDeProducto categoriaDeProducto = new CategoriaDeProducto();

        String sql = "SELECT * FROM CategoriaDeProducto WHERE CategoriaDeProducto.idCategoriaDeProducto = " + idCategoriaDeProducto + " ;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        if(resultSet.next()){
            categoriaDeProducto.setIdCategoriaDeProducto(resultSet.getInt("idCategoriaDeProducto"));
            categoriaDeProducto.setNombre(resultSet.getString("nombre"));
        }

        return categoriaDeProducto;
    }

    /***
     * Consulta las categorias de producto y guarda los resultados de la transaccion
     * @return Lista de categorias de productos
     * @throws SQLException Si hubo un fallo en la transaccion
     */
    public ObservableList<CategoriaDeProducto> getCategoriasDeProducto() throws SQLException {
        ObservableList<CategoriaDeProducto> categoriasDeProducto = FXCollections.observableArrayList();
        //List<CategoriaDeProducto> categoriasDeProducto = new ArrayList<>();
        CategoriaDeProducto categoriaDeProducto = null;
        Cocina cocina = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT CategoriaDeProducto.idCategoriaDeProducto, CategoriaDeProducto.nombre, Cocina.idCocina, Cocina.nombre AS nombreCocina, COUNT(Producto.idCategoriaDeProducto) AS numeroProductos FROM CategoriaDeProducto LEFT JOIN Cocina ON CategoriaDeProducto.idCocina = Cocina.idCocina LEFT JOIN Producto ON (CategoriaDeProducto.idCategoriaDeProducto = Producto.idCategoriaDeProducto) GROUP BY CategoriaDeProducto.idCategoriaDeProducto;");

        while (resultSet.next()){
            categoriaDeProducto = new CategoriaDeProducto();
            cocina = new Cocina();
            cocina.setIdCocina(resultSet.getInt("idCocina"));
            cocina.setNombre(resultSet.getString("nombreCocina"));
            categoriaDeProducto.setIdCategoriaDeProducto(resultSet.getInt("idCategoriaDeProducto"));
            categoriaDeProducto.setCocina(cocina);
            categoriaDeProducto.setNombre(resultSet.getString("nombre"));
            categoriaDeProducto.setNumeroDeProductos(resultSet.getInt("numeroProductos"));
            categoriasDeProducto.add(categoriaDeProducto);
        }

        return categoriasDeProducto;
    }

    /***
     *  Pide realizar la transaccion para eliminar cuando se realiza una peticion desde el controlador
     * @param idCategoriaDeProducto Identificador de categoria de producto
     * @return Si se puedo ejecutar la transaccion
     * @throws SQLException Si hubo algun error al ejecutar la transaccion
     */
    public boolean deleteItem(Integer idCategoriaDeProducto) throws SQLException {
        String sql = "DELETE FROM CategoriaDeProducto WHERE idCategoriaDeProducto = "+ idCategoriaDeProducto;
        return conexionBDRestaurante.guardarBDRestaurante(sql);
    }

    /***
     * Prepara y pide la transaccion para guardar o editar un nuevo registro de categoria de producto en la BD
     * @param categoriaDeProducto objeto que contiene la nueva informacion
     * @return Regsitros afectados
     * @throws SQLException SI hubo algun error al ejecutar la transaccion
     */
    public int guardar(CategoriaDeProducto categoriaDeProducto) throws SQLException {
        String sql = "";
        if(categoriaDeProducto.getCocina() != null && categoriaDeProducto.getIdCategoriaDeProducto() == null){
            sql = "INSERT INTO CategoriaDeProducto(\n"
                    + "	nombre, idCocina)\n"
                    + "	VALUES ('"+categoriaDeProducto.getNombre()+"' , '"+ categoriaDeProducto.getCocina().getIdCocina() +"');";
        }else if(categoriaDeProducto.getIdCategoriaDeProducto() == null){
            sql = "INSERT INTO CategoriaDeProducto(\n"
                    + "	nombre)\n"
                    + "	VALUES ('"+categoriaDeProducto.getNombre()+"');";
        }else{
            sql = "UPDATE CategoriaDeProducto SET "
                    + "	nombre = '" + categoriaDeProducto.getNombre() + "', idCocina = " + categoriaDeProducto.getCocina().getIdCocina()
                    + " WHERE idCategoriaDeProducto = " + categoriaDeProducto.getIdCategoriaDeProducto() + ";";
        }

        PreparedStatement pst = conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int insert = pst.executeUpdate();
        if (categoriaDeProducto.getIdCategoriaDeProducto() == null) {
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            insert = rs.getInt(1);
            rs.close();
        }
        return insert;
    }
}
