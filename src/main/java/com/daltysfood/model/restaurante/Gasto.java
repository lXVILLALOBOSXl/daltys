package com.daltysfood.model.restaurante;

import java.time.LocalDateTime;

public class Gasto {
    private Integer idGasto;
    private LocalDateTime fecha;
    private Double importe;
    private Integer numeroDeComprobante;
    private ArqueoDeCaja arqueoDeCaja;
    private String comentario;
    private CategoriaDeGasto categoriaDeGasto;
    private Proveedor proveedor;
    private MedioDePago medioDePago;

    public Gasto() {
    }

    public Gasto(Integer idGasto, LocalDateTime fecha, Double importe, Integer numeroDeComprobante, ArqueoDeCaja arqueoDeCaja, String comentario, CategoriaDeGasto categoriaDeGasto, Proveedor proveedor, MedioDePago medioDePago) {
        this.idGasto = idGasto;
        this.fecha = fecha;
        this.importe = importe;
        this.numeroDeComprobante = numeroDeComprobante;
        this.arqueoDeCaja = arqueoDeCaja;
        this.comentario = comentario;
        this.categoriaDeGasto = categoriaDeGasto;
        this.proveedor = proveedor;
        this.medioDePago = medioDePago;
    }

    public Integer getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(Integer idGasto) {
        this.idGasto = idGasto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Integer getNumeroDeComprobante() {
        return numeroDeComprobante;
    }

    public void setNumeroDeComprobante(Integer numeroDeComprobante) {
        this.numeroDeComprobante = numeroDeComprobante;
    }

    public ArqueoDeCaja getArqueoDeCaja() {
        return arqueoDeCaja;
    }

    public void setArqueoDeCaja(ArqueoDeCaja arqueoDeCaja) {
        this.arqueoDeCaja = arqueoDeCaja;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public CategoriaDeGasto getCategoriaDeGasto() {
        return categoriaDeGasto;
    }

    public void setCategoriaDeGasto(CategoriaDeGasto categoriaDeGasto) {
        this.categoriaDeGasto = categoriaDeGasto;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public MedioDePago getMedioDePago() {
        return medioDePago;
    }

    public void setMedioDePago(MedioDePago medioDePago) {
        this.medioDePago = medioDePago;
    }

    @Override
    public String toString() {
        return "Gasto{" +
                "idGasto=" + idGasto +
                ", fecha=" + fecha +
                ", importe=" + importe +
                ", numeroDeComprobante=" + numeroDeComprobante +
                ", arqueoDeCaja=" + arqueoDeCaja +
                ", comentario='" + comentario + '\'' +
                ", categoriaDeGasto=" + categoriaDeGasto +
                ", proveedor=" + proveedor +
                ", medioDePago=" + medioDePago +
                '}';
    }
}
