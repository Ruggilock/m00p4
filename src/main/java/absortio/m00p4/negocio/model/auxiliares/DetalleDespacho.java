package absortio.m00p4.negocio.model.auxiliares;

import javafx.scene.control.TextField;


public class DetalleDespacho {

    private DetalleEntrega detalleEntrega;
    private Integer lote;
    private TextField cantidad;
    private Double peso;
    private Double volumen;
    private Integer iddetalleentrega;

    public DetalleEntrega getDetalleEntrega() {
        return detalleEntrega;
    }

    public void setDetalleEntrega(DetalleEntrega detalleEntrega) {
        this.detalleEntrega = detalleEntrega;
    }

    public Integer getLote() {
        return lote;
    }

    public void setLote(Integer lote) {
        this.lote = lote;
    }

    public TextField getCantidad() {
        return cantidad;
    }

    public void setCantidad(TextField cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getVolumen() {
        return volumen;
    }

    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    public Integer getIddetalleentrega() {
        return iddetalleentrega;
    }

    public void setIddetalleentrega(Integer iddetalleentrega) {
        this.iddetalleentrega = iddetalleentrega;
    }
}
