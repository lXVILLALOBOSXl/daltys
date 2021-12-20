package com.daltysfood.util;

import com.daltysfood.model.daltys.Usuario;

import java.sql.*;

/***
 * Se encarga de realizar la conexion con la base de datos del restaurante que hizo el login
 * implemneta metodos para ejecutar consultas, cerrar conexion y guardar lo cambios en
 * la base de datos
 */
public class ConexionBDRestaurante{
    private Connection connection;
    private Statement statement;
    private static String JDBC_URL;
    private static String JDBC_USER;
    private static String JDBC_PASS;

    public ConexionBDRestaurante() {
    }

    /***
     * Se encarga de complementar e iniciar los datos necesarios para acceder a la base de datos del restaurante que hizo el login
     * debido al estandar que se tiene en daltysfood para generar bases de datos de cada restaurante
     * @param usuario Datos de usuario que hizo el login
     */
    public ConexionBDRestaurante(Usuario usuario) {
        ConexionBDRestaurante.JDBC_URL = ("jdbc:mysql://156.67.72.201:3306/u696248240_" + usuario.getNombreUsuario() + "?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true") ;
        ConexionBDRestaurante.JDBC_USER = ("u696248240_" + usuario.getNombreUsuario() );
        /*ConexionBDRestaurante.JDBC_URL = ("jdbc:mysql://156.67.72.201:3306/u696248240_" + usuario.getNombreUsuario().substring(6,(usuario.getNombreUsuario().length()))  + "?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true") ;
        ConexionBDRestaurante.JDBC_USER = ("u696248240_" + usuario.getNombreUsuario().substring(6,(usuario.getNombreUsuario().length())));*/
        ConexionBDRestaurante.JDBC_PASS = usuario.getConstrasena();
    }

    /***
     * @return Conexion que pertenece al objeto que llamo el metodo
     */
    public Connection getConexionBDRestaurante() {
        return connection;
    }

    /***
     * Intenta realizar la conexion a la base de datos y asigna al objeto que llamo al metodo dicha conexion
     * Si no logra la conexion, arroja un error
     */
    public void conectarBDRestaurante(){
        try {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER,JDBC_PASS);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException throwables) {
            throwables.printStackTrace(System.out);
        }
    }

    /***
     * Realiza las transacciones y almacena el resultado de la consulta
     * @param sql Transaccion
     * @return Resultado de la Transaccion en un objeto ResultSet
     * @throws SQLException Si no puede ejecutar la transaccion
     */
    public ResultSet consultarBDRestaurante(String sql) throws SQLException {
        ResultSet resultSet = null;
        resultSet = statement.executeQuery(sql);
        return resultSet;
    }

    /***
     * Guarda los cambios hechos por una transaccion
     * @param sql Transaccion
     * @return Si se pudieron realizar los cambios
     * @throws SQLException Si no puede ejecutar la transaccion
     */
    public boolean guardarBDRestaurante(String sql) throws SQLException {
        return (statement.executeUpdate(sql) > 0);
    }

    /***
     * Intenta cerrar la conexion a la base de datos del objeto que lo mande a llamar
     * si no puede cerrar dicha conexion, notifica el error
     */
    public void cerrarBDRestaurante(){
        try {
            if(connection != null){
                connection.close();
            }
            if (statement != null){
                statement.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace(System.out);
        }
    }
}
