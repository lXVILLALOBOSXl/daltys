package com.daltysfood.model.restaurante;

public class EstadoDeTrabajador {
    private Integer idEstadoDeTrabajador;
    private String estado;

    public EstadoDeTrabajador() {
    }

    public EstadoDeTrabajador(Integer idEstadoDeTrabajador, String estado) {
        this.idEstadoDeTrabajador = idEstadoDeTrabajador;
        this.estado = estado;
    }

    public Integer getIdEstadoDeTrabajador() {
        return idEstadoDeTrabajador;
    }

    public void setIdEstadoDeTrabajador(Integer idEstadoDeTrabajador) {
        this.idEstadoDeTrabajador = idEstadoDeTrabajador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "EstadoDeTrabajador{" +
                "idEstadoDeTrabajador=" + idEstadoDeTrabajador +
                ", estado='" + estado + '\'' +
                '}';
    }
}
