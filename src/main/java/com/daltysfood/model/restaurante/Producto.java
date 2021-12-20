package com.daltysfood.model.restaurante;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Producto {
    private Integer idProducto;
    private String nombre;
    private Double precio;
    private Double costo;
    private String descripcion;
    private String imagen;
    private Boolean estaActivo;
    private Boolean permitirVender;
    private Boolean control;
    private Integer stock;
    private Integer stockMinimo;
    private CategoriaDeProducto categoriaDeProducto;
    private Cocina cocina;
    private Proveedor proveedor;
    private String codigo;

    public Producto() {

    }

    public Producto(Integer idProducto, String nombre, Double precio, Double costo, String descripcion, String imagen, Boolean estaActivo, Boolean permitirVender, Boolean control, Integer stock, Integer stockMinimo, CategoriaDeProducto categoriaDeProducto, Cocina cocina, Proveedor proveedor, String codigo) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.costo = costo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.estaActivo = estaActivo;
        this.permitirVender = permitirVender;
        this.control = control;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.categoriaDeProducto = categoriaDeProducto;
        this.cocina = cocina;
        this.proveedor = proveedor;
        this.codigo = codigo;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }


    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }

    public Boolean getPermitirVender() {
        return permitirVender;
    }

    public void setPermitirVender(Boolean permitirVender) {
        this.permitirVender = permitirVender;
    }

    public Boolean getControl() {
        return control;
    }

    public void setControl(Boolean control) {
        this.control = control;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public CategoriaDeProducto getCategoriaDeProducto() {
        return categoriaDeProducto;
    }

    public void setCategoriaDeProducto(CategoriaDeProducto categoriaDeProducto) {
        this.categoriaDeProducto = categoriaDeProducto;
    }

    public Cocina getCocina() {
        return cocina;
    }

    public void setCocina(Cocina cocina) {
        this.cocina = cocina;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", costo=" + costo +
                ", descripcion='" + descripcion + '\'' +
                ", imagen=" + imagen +
                ", estaActivo=" + estaActivo +
                ", permitirVender=" + permitirVender +
                ", control=" + control +
                ", stock=" + stock +
                ", stockMinimo=" + stockMinimo +
                ", categoriaDeProducto=" + categoriaDeProducto +
                ", cocina=" + cocina +
                ", proveedor=" + proveedor +
                '}';
    }
}
