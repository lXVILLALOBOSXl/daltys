package com.daltysfood.model.restaurante;

import java.time.LocalDateTime;

public class ArqueoDeCaja {
    private Integer idArqueoDeCaja;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Double usuario;
    private Double sistema;
    private Double montoInicial;
    private Double diferencia;
    private Boolean estaActiva;
    private String comentario;

    public ArqueoDeCaja() {
    }

    public ArqueoDeCaja(Integer idArqueoDeCaja, LocalDateTime fechaInicio, LocalDateTime fechaFin, Double usuario, Double sistema, Double montoInicial, Double diferencia, Boolean estaActiva, String comentario) {
        this.idArqueoDeCaja = idArqueoDeCaja;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.usuario = usuario;
        this.sistema = sistema;
        this.montoInicial = montoInicial;
        this.diferencia = diferencia;
        this.estaActiva = estaActiva;
        this.comentario = comentario;
    }

    public Integer getIdArqueoDeCaja() {
        return idArqueoDeCaja;
    }

    public void setIdArqueoDeCaja(Integer idArqueoDeCaja) {
        this.idArqueoDeCaja = idArqueoDeCaja;
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

    public Double getUsuario() {
        return usuario;
    }

    public void setUsuario(Double usuario) {
        this.usuario = usuario;
    }

    public Double getSistema() {
        return sistema;
    }

    public void setSistema(Double sistema) {
        this.sistema = sistema;
    }

    public Double getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(Double montoInicial) {
        this.montoInicial = montoInicial;
    }

    public Double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Double diferencia) {
        this.diferencia = diferencia;
    }

    public Boolean getEstaActiva() {
        return estaActiva;
    }

    public void setEstaActiva(Boolean estaActiva) {
        this.estaActiva = estaActiva;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "ArqueoDeCaja{" +
                "idArqueoDeCaja=" + idArqueoDeCaja +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", usuario=" + usuario +
                ", sistema=" + sistema +
                ", montoInicial=" + montoInicial +
                ", diferencia=" + diferencia +
                ", estaActiva=" + estaActiva +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}
