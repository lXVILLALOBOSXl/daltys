package com.daltysfood.model.restaurante;

public class EstadoPedido {
    private Integer idEstadoPedido;
    private String estado;

    public EstadoPedido() {
    }

    public EstadoPedido(Integer idEstadoPedido, String estado) {
        this.idEstadoPedido = idEstadoPedido;
        this.estado = estado;
    }

    public Integer getIdEstadoPedido() {
        return idEstadoPedido;
    }

    public void setIdEstadoPedido(Integer idEstadoPedido) {
        this.idEstadoPedido = idEstadoPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "EstadoPedido{" +
                "idEstadoPedido=" + idEstadoPedido +
                ", estado='" + estado + '\'' +
                '}';
    }
}
