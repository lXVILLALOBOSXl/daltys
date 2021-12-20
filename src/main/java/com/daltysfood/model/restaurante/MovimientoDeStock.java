package com.daltysfood.model.restaurante;

import org.exolab.castor.types.DateTime;

import java.time.LocalDateTime;

public class MovimientoDeStock {
    private Integer idMovimientoDeStock;
    private LocalDateTime fecha;
    private Integer stockAnterior;
    private Integer stockActual;
    private PedidoProducto pedidoProducto;
    private TipoDeMovimientoDeStock tipoDeMovimientoDeStock;
    private Producto producto;

    public MovimientoDeStock() {
    }

    public MovimientoDeStock(Integer idMovimientoDeStock, LocalDateTime fecha, Integer stockAnterior, Integer stockActual, PedidoProducto pedidoProducto, TipoDeMovimientoDeStock tipoDeMovimientoDeStock, Producto producto) {
        this.idMovimientoDeStock = idMovimientoDeStock;
        this.fecha = fecha;
        this.stockAnterior = stockAnterior;
        this.stockActual = stockActual;
        this.pedidoProducto = pedidoProducto;
        this.tipoDeMovimientoDeStock = tipoDeMovimientoDeStock;
        this.producto = producto;
    }

    public Integer getIdMovimientoDeStock() {
        return idMovimientoDeStock;
    }

    public void setIdMovimientoDeStock(Integer idMovimientoDeStock) {
        this.idMovimientoDeStock = idMovimientoDeStock;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Integer getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(Integer stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public PedidoProducto getPedidoProducto() {
        return pedidoProducto;
    }

    public void setPedidoProducto(PedidoProducto pedidoProducto) {
        this.pedidoProducto = pedidoProducto;
    }

    public TipoDeMovimientoDeStock getTipoDeMovimientoDeStock() {
        return tipoDeMovimientoDeStock;
    }

    public void setTipoDeMovimientoDeStock(TipoDeMovimientoDeStock tipoDeMovimientoDeStock) {
        this.tipoDeMovimientoDeStock = tipoDeMovimientoDeStock;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "MovimientoDeStock{" +
                "idMovimientoDeStock=" + idMovimientoDeStock +
                ", fecha=" + fecha +
                ", stockAnterior=" + stockAnterior +
                ", stockActual=" + stockActual +
                ", pedidoProducto=" + pedidoProducto +
                ", tipoDeMovimientoDeStock=" + tipoDeMovimientoDeStock +
                ", producto=" + producto +
                '}';
    }
}
