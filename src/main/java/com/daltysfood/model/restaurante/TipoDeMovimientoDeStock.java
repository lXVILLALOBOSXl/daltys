package com.daltysfood.model.restaurante;

public class TipoDeMovimientoDeStock {
    private Integer idTipoDeMovimientoDeStock;
    private String nombreMovimientoDeStock;

    public TipoDeMovimientoDeStock() {
    }

    public TipoDeMovimientoDeStock(Integer idTipoDeMovimientoDeStock, String nombreMovimientoDeStock) {
        this.idTipoDeMovimientoDeStock = idTipoDeMovimientoDeStock;
        this.nombreMovimientoDeStock = nombreMovimientoDeStock;
    }

    public Integer getIdTipoDeMovimientoDeStock() {
        return idTipoDeMovimientoDeStock;
    }

    public void setIdTipoDeMovimientoDeStock(Integer idTipoDeMovimientoDeStock) {
        this.idTipoDeMovimientoDeStock = idTipoDeMovimientoDeStock;
    }

    public String getNombreMovimientoDeStock() {
        return nombreMovimientoDeStock;
    }

    public void setNombreMovimientoDeStock(String nombreMovimientoDeStock) {
        this.nombreMovimientoDeStock = nombreMovimientoDeStock;
    }

    @Override
    public String toString() {
        return "TipoDeMovimientoDeStock{" +
                "idTipoDeMovimientoDeStock=" + idTipoDeMovimientoDeStock +
                ", nombreMovimientoDeStock='" + nombreMovimientoDeStock + '\'' +
                '}';
    }
}
