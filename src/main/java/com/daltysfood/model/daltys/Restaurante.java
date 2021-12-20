package com.daltysfood.model.daltys;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Restaurante {
    private Integer idRestaurante;
    private String nombreRestaurante;
    private final ObjectProperty<byte[]> imagen = new SimpleObjectProperty<>();

    public Integer getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(Integer idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public byte[] getImagen() {
        return imagen.get();
    }

    public ObjectProperty<byte[]> imagenProperty() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen.set(imagen);
    }
}

