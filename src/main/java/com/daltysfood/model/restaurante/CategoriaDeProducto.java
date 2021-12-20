package com.daltysfood.model.restaurante;

public class CategoriaDeProducto {
    private Integer idCategoriaDeProducto;
    private String nombre;
    private Cocina cocina;
    private Integer numeroDeProductos; /** **/

    public CategoriaDeProducto() {
    }

    public CategoriaDeProducto(Integer idCategoriaDeProducto, String nombre, Cocina cocina, Integer numeroDeProductos) {
        this.idCategoriaDeProducto = idCategoriaDeProducto;
        this.nombre = nombre;
        this.cocina = cocina;
        this.numeroDeProductos = numeroDeProductos;
    }

    public Integer getIdCategoriaDeProducto() {
        return idCategoriaDeProducto;
    }

    public void setIdCategoriaDeProducto(Integer idCategoriaDeProducto) {
        this.idCategoriaDeProducto = idCategoriaDeProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Cocina getCocina() {
        return cocina;
    }

    public void setCocina(Cocina cocina) {
        this.cocina = cocina;
    }

    public Integer getNumeroDeProductos() {
        return numeroDeProductos;
    }

    public void setNumeroDeProductos(Integer numeroDeProductos) {
        this.numeroDeProductos = numeroDeProductos;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
