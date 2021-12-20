package com.daltysfood.dao.restaurante;

import com.daltysfood.model.daltys.Cliente;
import com.daltysfood.model.daltys.Licencia;
import com.daltysfood.model.daltys.Restaurante;
import com.daltysfood.model.daltys.Usuario;
import com.daltysfood.util.ConexionBDDaltys;
import com.daltysfood.util.Sesion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;


/**
 * Gestiona las transferencias entre el Modelo de la clase Usuario y la conexion a la Base de datos,
 * es decir que es el medium, se encarga de pedir resutados y asignarlos al modelo mediante peticiones
 * que realiza a la base de datos
 */
public class UsuarioDAO {
    private ConexionBDDaltys conexionBDDaltys;

    /***
     * Se encarga de inicializar la conexion con la base de datos
     * @param conexionBDDaltys
     */
    public UsuarioDAO(ConexionBDDaltys conexionBDDaltys){ this.conexionBDDaltys = conexionBDDaltys; }

    /**
     * Se encarga de validar que el usuario exista y tenga una licencia valida, si es asi, guarda la informacion de la transaccion
     * al objeto modelo
     * @param nombreUsuario String nombre de usuario del fxml
     * @param contrasena String contraseña del fxml
     * @return Objeto modelo con la informacion necesaria
     * @throws SQLException Si no se pudo realizar la transaccion nos manda un error
     */
    public Usuario getUsuario(String nombreUsuario, String contrasena) throws SQLException {
        Usuario usuario = null;
        Licencia licencia = null;
        Restaurante restaurante = null;
        Cliente cliente = null;

        String sql = "SELECT Licencia.isValid, Restaurante.nombreRestaurante, Restaurante.imagen, Usuario.idUsuario, Cliente.nombreCliente FROM Cliente INNER JOIN Usuario ON Cliente.idCliente = Usuario.idCliente INNER JOIN Restaurante ON Cliente.idRestaurante = Restaurante.idRestaurante INNER JOIN Licencia ON Usuario.idlicencia = Licencia.idLicencia WHERE Usuario.nombreUsuario = '" + nombreUsuario + "' AND Usuario.contrasena = '" + contrasena + "'";
        ResultSet resultSet = conexionBDDaltys.consultarBDDaltys(sql);
        //si hubo algun resultado
        if(resultSet.next()){
            usuario = new Usuario();
            licencia = new Licencia();
            //Si el resultado arroja una licencia valida
            if(resultSet.getInt("isValid") == 1){
                cliente = new Cliente();
                restaurante = new Restaurante();
                licencia.setValid(true);
                restaurante.setNombreRestaurante(resultSet.getString("nombreRestaurante"));
                restaurante.setImagen(resultSet.getBytes("imagen"));
                cliente.setNombreCliente(resultSet.getString("nombreCliente"));
                cliente.setRestaurante(restaurante);
                usuario.setIdUsuario(resultSet.getInt("idUsuario"));
                usuario.setNombreUsuario(nombreUsuario);
                usuario.setConstrasena(contrasena);
                usuario.setLicencia(licencia);
                usuario.setCliente(cliente);
                //Ponemos al usuario como el de la sesion activa
                Sesion.getSesion(usuario);
            }else{
                licencia.setValid(false);
                usuario.setLicencia(licencia);
            }
        }
        return usuario;
    }
}
