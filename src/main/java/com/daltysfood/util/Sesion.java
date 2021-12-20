package com.daltysfood.util;

import com.daltysfood.model.daltys.Usuario;

/**
 * Gestiona los usuarios con la sesion activos
 */

public class Sesion {
    private static Usuario usuario;

    /**
     * Retorna el usuario que esta en la sesion
     * @param usuario Por si inicia sesion por primera vez
     * @return Usuario que esta en la sesion
     */
    public static  Usuario getSesion(Usuario usuario){
        if(Sesion.usuario == null){
            Sesion.usuario = usuario;
        }
        return Sesion.usuario;
    }
}
