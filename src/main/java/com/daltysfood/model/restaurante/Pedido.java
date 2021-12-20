package com.daltysfood.model.restaurante;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Pedido {
    private Integer numeroDePedido;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoPedido estadoPedido;
    private Double montoTotal;
    private String comentario;
    private Double propina;
    private Integer atencion;
    private MedioDePago medioDePago;
    private Mesa mesa;
    private Comensal comensal;
    public static final DateTimeFormatter HOUR = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Pedido() {
    }

    public Pedido(Integer numeroDePedido, LocalDateTime fechaInicio, LocalDateTime fechaFin, EstadoPedido estadoPedido, Double montoTotal, String comentario, Double propina, Integer atencion, MedioDePago medioDePago, Mesa mesa, Comensal comensal) {
        this.numeroDePedido = numeroDePedido;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estadoPedido = estadoPedido;
        this.montoTotal = montoTotal;
        this.comentario = comentario;
        this.propina = propina;
        this.atencion = atencion;
        this.medioDePago = medioDePago;
        this.mesa = mesa;
        this.comensal = comensal;
    }

    public Integer getNumeroDePedido() {
        return numeroDePedido;
    }

    public void setNumeroDePedido(Integer numeroDePedido) {
        this.numeroDePedido = numeroDePedido;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Double getPropina() {
        return propina;
    }

    public void setPropina(Double propina) {
        this.propina = propina;
    }

    public Integer getAtencion() {
        return atencion;
    }

    public void setAtencion(Integer atencion) {
        this.atencion = atencion;
    }

    public MedioDePago getMedioDePago() {
        return medioDePago;
    }

    public void setMedioDePago(MedioDePago medioDePago) {
        this.medioDePago = medioDePago;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Comensal getComensal() {
        return comensal;
    }

    public void setComensal(Comensal comensal) {
        this.comensal = comensal;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "numeroDePedido=" + numeroDePedido +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estadoPedido=" + estadoPedido +
                ", montoTotal=" + montoTotal +
                ", comentario='" + comentario + '\'' +
                ", propina=" + propina +
                ", atencion=" + atencion +
                ", medioDePago=" + medioDePago +
                ", mesa=" + mesa +
                ", comensal=" + comensal +
                '}';
    }
}
