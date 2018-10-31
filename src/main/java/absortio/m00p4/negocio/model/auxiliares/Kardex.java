package absortio.m00p4.negocio.model.auxiliares;

import javax.persistence.criteria.CriteriaBuilder;

public class Kardex {
    private Integer n;
    private String bloque;
    private Integer cantidad;
    private String codbarras;
    private Integer lote;
    private String fecha;
    private String motivo;
    private String nombreproducto;
    private String identificadorrack;
    private String seccionalmacen;
    private String tipooperacion;

    public String getTipooperacion() {
        return tipooperacion;
    }

    public void setTipooperacion(String tipooperacion) {
        this.tipooperacion = tipooperacion;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodbarras() {
        return codbarras;
    }

    public void setCodbarras(String codbarras) {
        this.codbarras = codbarras;
    }

    public Integer getLote() {
        return lote;
    }

    public void setLote(Integer lote) {
        this.lote = lote;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }

    public String getIdentificadorrack() {
        return identificadorrack;
    }

    public void setIdentificadorrack(String identificadorrack) {
        this.identificadorrack = identificadorrack;
    }

    public String getSeccionalmacen() {
        return seccionalmacen;
    }

    public void setSeccionalmacen(String seccionalmacen) {
        this.seccionalmacen = seccionalmacen;
    }
}









