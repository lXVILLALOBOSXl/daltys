package com.daltysfood.dao.restaurante;

import com.daltysfood.model.restaurante.CategoriaDeGasto;
import com.daltysfood.model.restaurante.CategoriaDeProducto;
import com.daltysfood.model.restaurante.MedioDePago;
import com.daltysfood.util.ConexionBDRestaurante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDeGastoDAO {
    private ConexionBDRestaurante conexionBDRestaurante;

    public CategoriaDeGastoDAO(ConexionBDRestaurante conexionBDRestaurante){
        this.conexionBDRestaurante = conexionBDRestaurante;
    }

    /***
     * Hace una peticion para consultar las categorias de gastos
     * @return categorias de gastos
     * @throws SQLException si hubo algun problema con la transaccion
     */
    public List<CategoriaDeGasto> getCategoriasDeGasto() throws SQLException {
        List<CategoriaDeGasto> categoriasDeGasto = new ArrayList<>();
        CategoriaDeGasto categoriaDeGasto = null;
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante("SELECT * FROM CategoriaDeGasto;");

        while (resultSet.next()){
            categoriaDeGasto = new CategoriaDeGasto();
            categoriaDeGasto.setIdCategoriaDeGasto(resultSet.getInt("idCategoriaDeGasto"));
            categoriaDeGasto.setNombre(resultSet.getString("nombre"));
            categoriaDeGasto.setEstaActiva(resultSet.getBoolean("estaActiva"));
            categoriasDeGasto.add(categoriaDeGasto);
        }

        return categoriasDeGasto;
    }

    /***
     * Se encarga de hacer la peticion con la informacion necesaria para agregar una nueva categoria de gastos o a la base de datos
     * @param categoriaDeGasto nuevo elemento que se va agregar
     * @return rows afectadas
     * @throws SQLException si hhubo algun problema en la transaccion
     */
    public int guardar(CategoriaDeGasto categoriaDeGasto) throws SQLException {
        int estado = (categoriaDeGasto.getEstaActiva() == true) ? 1 : 0;
        String sql = "INSERT INTO CategoriaDeGasto(\n"
                + "	nombre, estaActiva)\n"
                + "	VALUES ('"+ categoriaDeGasto.getNombre() +"' , '"+ estado +"');";

        this.conexionBDRestaurante.getConexionBDRestaurante().setAutoCommit(false);

        PreparedStatement pst = this.conexionBDRestaurante.getConexionBDRestaurante().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        if (pst.executeUpdate() > 0) {
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int idCategoria = rs.getInt(1);
            this.conexionBDRestaurante.getConexionBDRestaurante().commit();
            return idCategoria;
        }

        return 0;
    }

    /***
     * Prepara y pide la transaccion para obtener las categorias de gasto activas
     * @return Lista con categorias activas
     * @throws SQLException Si hubo un error al procesar la transaccion
     */
    public ObservableList<CategoriaDeGasto> getCategoriasActivas() throws SQLException {
        CategoriaDeGasto categoriaDeGasto = null;
        ObservableList<CategoriaDeGasto> categoriaDeGastosActivas = FXCollections.observableArrayList();

        String sql = "SELECT * FROM CategoriaDeGasto WHERE estaActiva = 1;";
        ResultSet resultSet = conexionBDRestaurante.consultarBDRestaurante(sql);

        while(resultSet.next()){
            categoriaDeGasto = new CategoriaDeGasto();
            categoriaDeGasto.setIdCategoriaDeGasto(resultSet.getInt("idCategoriaDeGasto"));
            categoriaDeGasto.setNombre(resultSet.getString("nombre"));
            categoriaDeGastosActivas.add(categoriaDeGasto);
        }

        return  categoriaDeGastosActivas;
    }
}
