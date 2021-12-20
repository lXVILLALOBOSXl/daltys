package com.daltysfood.model.restaurante;

public class CategoriaDeGasto {
    private Integer idCategoriaDeGasto;
    private String nombre;
    private Boolean estaActiva;

    public CategoriaDeGasto() {
    }

    public CategoriaDeGasto(Integer idCategoriaDeGasto, String nombre, Boolean estaActiva) {
        this.idCategoriaDeGasto = idCategoriaDeGasto;
        this.nombre = nombre;
        this.estaActiva = estaActiva;
    }

    public Integer getIdCategoriaDeGasto() {
        return idCategoriaDeGasto;
    }

    public void setIdCategoriaDeGasto(Integer idCategoriaDeGasto) {
        this.idCategoriaDeGasto = idCategoriaDeGasto;
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
