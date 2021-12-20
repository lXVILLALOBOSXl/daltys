package com.daltysfood.model.restaurante;

public class Cocina {
    private Integer idCocina;
    private String nombre;

    public Cocina() {
    }

    public Cocina(Integer idCocina, String nombre) {
        this.idCocina = idCocina;
        this.nombre = nombre;
    }

    public Integer getIdCocina() {
        return idCocina;
    }

    public void setIdCocina(Integer idCocina) {
        this.idCocina = idCocina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
