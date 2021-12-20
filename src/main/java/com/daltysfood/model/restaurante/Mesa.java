package com.daltysfood.model.restaurante;

import java.util.List;

public class Mesa {
    private Integer numeroDeMesa;
    private Integer numeroDePersonas;
    private EstadoDeMesa estadoDeMesa;
    private Trabajador trabajador;
    private Area area;

    public Mesa() {
    }

    public Mesa(Integer numeroDeMesa, Integer numeroDePersonas, EstadoDeMesa estadoDeMesa, Trabajador trabajador, Area area) {
        this.numeroDeMesa = numeroDeMesa;
        this.numeroDePersonas = numeroDePersonas;
        this.estadoDeMesa = estadoDeMesa;
        this.trabajador = trabajador;
        this.area = area;
    }

    public Integer getNumeroDeMesa() {
        return numeroDeMesa;
    }

    public void setNumeroDeMesa(Integer numeroDeMesa) {
        this.numeroDeMesa = numeroDeMesa;
    }

    public Integer getNumeroDePersonas() {
        return numeroDePersonas;
    }

    public void setNumeroDePersonas(Integer numeroDePersonas) {
        this.numeroDePersonas = numeroDePersonas;
    }

    public EstadoDeMesa getEstadoDeMesa() {
        return estadoDeMesa;
    }

    public void setEstadoDeMesa(EstadoDeMesa estadoDeMesa) {
        this.estadoDeMesa = estadoDeMesa;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Mesa{" +
                "numeroDeMesa=" + numeroDeMesa +
                ", numeroDePersonas=" + numeroDePersonas +
                ", estadoDeMesa=" + estadoDeMesa +
                ", trabajador=" + trabajador +
                ", area=" + area +
                '}';
    }
}
