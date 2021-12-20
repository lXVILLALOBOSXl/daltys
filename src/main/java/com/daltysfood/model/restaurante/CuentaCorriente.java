package com.daltysfood.model.restaurante;


import java.time.LocalDateTime;

public class CuentaCorriente {
    private Integer idCuentaCorriente;
    private Double monto;
    private String comentario;
    private Boolean estaPagado;
    private ArqueoDeCaja arqueoDeCaja;
    private MedioDePago medioDePago;
    private Proveedor proveedor;
    private TipoDeMovimiento tipoDeMovimiento;
    private LocalDateTime fecha;

    public CuentaCorriente() {
    }

    public CuentaCorriente(Integer idCuentaCorriente, Double monto, String comentario, Boolean estaPagado, ArqueoDeCaja arqueoDeCaja, MedioDePago medioDePago, Proveedor proveedor, TipoDeMovimiento tipoDeMovimiento, LocalDateTime fecha) {
        this.idCuentaCorriente = idCuentaCorriente;
        this.monto = monto;
        this.comentario = comentario;
        this.estaPagado = estaPagado;
        this.arqueoDeCaja = arqueoDeCaja;
        this.medioDePago = medioDePago;
        this.proveedor = proveedor;
        this.tipoDeMovimiento = tipoDeMovimiento;
        this.fecha = fecha;
    }

    public Integer getIdCuentaCorriente() {
        return idCuentaCorriente;
    }

    public void setIdCuentaCorriente(Integer idCuentaCorriente) {
        this.idCuentaCorriente = idCuentaCorriente;
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

    public Boolean isEstaPagado() {
        return estaPagado;
    }

    public void setEstaPagado(Boolean estaPagado) {
        this.estaPagado = estaPagado;
    }

    public ArqueoDeCaja getArqueoDeCaja() {
        return arqueoDeCaja;
    }

    public void setArqueoDeCaja(ArqueoDeCaja arqueoDeCaja) {
        this.arqueoDeCaja = arqueoDeCaja;
    }

    public MedioDePago getMedioDePago() {
        return medioDePago;
    }

    public void setMedioDePago(MedioDePago medioDePago) {
        this.medioDePago = medioDePago;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public TipoDeMovimiento getTipoDeMovimiento() {
        return tipoDeMovimiento;
    }

    public void setTipoDeMovimiento(TipoDeMovimiento tipoDeMovimiento) {
        this.tipoDeMovimiento = tipoDeMovimiento;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "CuentaCorriente{" +
                "idCuentaCorriente=" + idCuentaCorriente +
                ", monto=" + monto +
                ", comentario='" + comentario + '\'' +
                ", estaPagado=" + estaPagado +
                ", arqueoDeCaja=" + arqueoDeCaja +
                ", medioDePago=" + medioDePago +
                ", proveedor=" + proveedor +
                ", tipoDeMovimiento=" + tipoDeMovimiento +
                ", fecha=" + fecha +
                '}';
    }
}
