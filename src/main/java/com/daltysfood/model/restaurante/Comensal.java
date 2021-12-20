package com.daltysfood.model.restaurante;

public class Comensal {
    private Integer idComensal;
    private String nombre;
    private Mesa mesa;
    private Boolean estaActivo;

    public Comensal() {
    }

    public Comensal(Integer idComensal, String nombre, Mesa mesa, Boolean estaActivo) {
        this.idComensal = idComensal;
        this.nombre = nombre;
        this.mesa = mesa;
        this.estaActivo = estaActivo;
    }

    public Integer getIdComensal() {
        return idComensal;
    }

    public void setIdComensal(Integer idComensal) {
        this.idComensal = idComensal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }

    @Override
    public String toString() {
        return "Comensal{" +
                "idComensal=" + idComensal +
                ", nombre='" + nombre + '\'' +
                ", mesa=" + mesa +
                ", estaActivo=" + estaActivo +
                '}';
    }
}
