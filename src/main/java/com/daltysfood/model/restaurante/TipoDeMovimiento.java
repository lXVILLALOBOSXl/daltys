package com.daltysfood.model.restaurante;

public class TipoDeMovimiento {
    private Integer idTipoDeMovimiento;
    private String concepto;

    public TipoDeMovimiento() { }

    public TipoDeMovimiento(Integer idTipoDeMovimiento) {
        this.idTipoDeMovimiento = idTipoDeMovimiento;
    }

    public TipoDeMovimiento(Integer idTipoDeMovimiento, String concepto) {
        this.idTipoDeMovimiento = idTipoDeMovimiento;
        this.concepto = concepto;
    }

    public Integer getIdTipoDeMovimiento() {
        return idTipoDeMovimiento;
    }

    public void setIdTipoDeMovimiento(Integer idTipoDeMovimiento) {
        this.idTipoDeMovimiento = idTipoDeMovimiento;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    @Override
    public String toString() {
        return concepto;
    }
}
