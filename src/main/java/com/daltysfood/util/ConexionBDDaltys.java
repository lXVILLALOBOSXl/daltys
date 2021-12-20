package com.daltysfood.util;

import java.sql.*;

/***
 * Se encarga de realizar la conexion con la base de datos de la empresa (Dalty's Food)
 * implemneta metodos para ejecutar consultas, cerrar conexion y guardar lo cambios en
 * la base de datos
 */
public class ConexionBDDaltys {
    private Connection connection;
    private Statement statement;
    private static final String JDBC_URL = "jdbc:mysql://156.67.72.201:3306/u696248240_ClientesDaltys?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "u696248240_admin";
    private static final String JDBC_PASS = "Daltysfood2021";

    public ConexionBDDaltys() { }

    /***
     * @return Conexion que pertenece al objeto que llamo el metodo
     */
    public Connection getConexionBDDaltys() {
        return connection;
    }

    /***
     * Intenta realizar la conexion a la base de datos y asigna al objeto que llamo al metodo dicha conexion
     * Si no logra la conexion, arroja un error
     */
    public void conectarBDDaltys(){
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
    public ResultSet consultarBDDaltys(String sql) throws SQLException {
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
    public boolean guardarBDDaltys(String sql) throws SQLException {
        return (statement.executeUpdate(sql) > 0);
    }

    /***
     * Intenta cerrar la conexion a la base de datos del objeto que lo mande a llamar
     * si no puede cerrar dicha conexion, notifica el error
     */
    public void cerrarBDDaltys(){
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
