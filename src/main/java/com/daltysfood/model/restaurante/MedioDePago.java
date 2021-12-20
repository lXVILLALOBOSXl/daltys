package com.daltysfood.model.restaurante;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class MedioDePago {
    private Integer idMedioDePago;
    private String nombre;
    private Boolean estaActiva;

    public MedioDePago() {
    }

    public MedioDePago(Integer idMedioDePago, String nombre, Boolean estaActiva) {
        this.idMedioDePago = idMedioDePago;
        this.nombre = nombre;
        this.estaActiva = estaActiva;
    }

    public Integer getIdMedioDePago() {
        return idMedioDePago;
    }

    public void setIdMedioDePago(Integer idMedioDePago) {
        this.idMedioDePago = idMedioDePago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstaActiva() {
        return estaActiva;
    }

    public void setEstaActiva(Boolean estaActiva) {
        this.estaActiva = estaActiva;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
