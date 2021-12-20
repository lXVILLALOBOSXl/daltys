package com.daltysfood.model.restaurante;

public class EstadoDeMesa {
    private Integer idEstadoDeMesa;
    private String estado;

    public EstadoDeMesa() {
    }

    public EstadoDeMesa(Integer idEstadoDeMesa, String estado) {
        this.idEstadoDeMesa = idEstadoDeMesa;
        this.estado = estado;
    }

    public Integer getIdEstadoDeMesa() {
        return idEstadoDeMesa;
    }

    public void setIdEstadoDeMesa(Integer idEstadoDeMesa) {
        this.idEstadoDeMesa = idEstadoDeMesa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "EstadoDeMesa{" +
                "idEstadoDeMesa=" + idEstadoDeMesa +
                ", estado='" + estado + '\'' +
                '}';
    }
}
