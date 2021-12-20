package com.daltysfood.model.restaurante;

public class PedidoProducto {
    private Integer idPedidoProducto;
    private Pedido pedido;
    private Producto producto;
    private Integer numeroDeProductos;

    public PedidoProducto() { }

    public PedidoProducto(Integer idPedidoProducto, Pedido pedido, Producto producto, Integer numeroDeProductos) {
        this.idPedidoProducto = idPedidoProducto;
        this.pedido = pedido;
        this.producto = producto;
        this.numeroDeProductos = numeroDeProductos;
    }

    public Integer getIdPedidoProducto() {
        return idPedidoProducto;
    }

    public void setIdPedidoProducto(Integer idPedidoProducto) {
        this.idPedidoProducto = idPedidoProducto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getNumeroDeProductos() {
        return numeroDeProductos;
    }

    public void setNumeroDeProductos(Integer numeroDeProductos) {
        this.numeroDeProductos = numeroDeProductos;
    }

    @Override
    public String toString() {
        return "PedidoProducto{" +
                "idPedidoProducto=" + idPedidoProducto +
                ", pedido=" + pedido +
                ", producto=" + producto +
                ", numeroDeProductos=" + numeroDeProductos +
                '}';
    }


}
