package absortio.m00p4.negocio.model.auxiliares;

import java.util.Date;


public class DetalleEntrega {
    private Integer id;         // idDetalleEntrega
    private Integer lote;       // idLote
    private Date fechaLimite;
    private Integer cantidad;
    private Integer resta;
    private String estado;

    public Integer getLote() {
        return lote;
    }

    public void setLote(Integer lote) {
        this.lote = lote;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getResta() {
        return resta;
    }

    public void setResta(Integer resta) {
        this.resta = resta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
