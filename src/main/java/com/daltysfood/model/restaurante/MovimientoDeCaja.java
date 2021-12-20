package com.daltysfood.model.restaurante;

import java.time.LocalDateTime;

public class MovimientoDeCaja {
    private Integer idMovimientoDeCaja;
    private LocalDateTime fecha;
    private Double monto;
    private String comentario;
    private MedioDePago medioDePago;
    private TipoDeMovimiento tipoDeMovimiento;
    private ArqueoDeCaja arqueoDeCaja;

    public MovimientoDeCaja() {}

    public MovimientoDeCaja(Integer idMovimientoDeCaja, LocalDateTime fecha, Double monto, String comentario, MedioDePago medioDePago, TipoDeMovimiento tipoDeMovimiento, ArqueoDeCaja arqueoDeCaja) {
        this.idMovimientoDeCaja = idMovimientoDeCaja;
        this.fecha = fecha;
        this.monto = monto;
        this.comentario = comentario;
        this.medioDePago = medioDePago;
        this.tipoDeMovimiento = tipoDeMovimiento;
        this.arqueoDeCaja = arqueoDeCaja;
    }

    public Integer getIdMovimientoDeCaja() {
        return idMovimientoDeCaja;
    }

    public void setIdMovimientoDeCaja(Integer idMovimientoDeCaja) {
        this.idMovimientoDeCaja = idMovimientoDeCaja;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public MedioDePago getMedioDePago() {
        return medioDePago;
    }

    public void setMedioDePago(MedioDePago medioDePago) {
        this.medioDePago = medioDePago;
    }

    public TipoDeMovimiento getTipoDeMovimiento() {
        return tipoDeMovimiento;
    }

    public void setTipoDeMovimiento(TipoDeMovimiento tipoDeMovimiento) {
        this.tipoDeMovimiento = tipoDeMovimiento;
    }

    public ArqueoDeCaja getArqueoDeCaja() {
        return arqueoDeCaja;
    }

    public void setArqueoDeCaja(ArqueoDeCaja arqueoDeCaja) {
        this.arqueoDeCaja = arqueoDeCaja;
    }

    @Override
    public String toString() {
        return "MovimientoDeCaja{" +
                "idMovimientoDeCaja=" + idMovimientoDeCaja +
                ", fecha=" + fecha +
                ", monto=" + monto +
                ", comentario='" + comentario + '\'' +
                ", medioDePago=" + medioDePago +
                ", tipoDeMovimiento=" + tipoDeMovimiento +
                ", arqueoDeCaja=" + arqueoDeCaja +
                '}';
    }
}
