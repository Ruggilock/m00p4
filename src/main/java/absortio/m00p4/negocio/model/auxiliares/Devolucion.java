package absortio.m00p4.negocio.model.auxiliares;

import java.util.Date;

public class Devolucion {
    private  Integer n;
    private String noDocDevolucion;
    private String noDocVenta;
    private Date fecha;
    private String descripcion;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getNoDocDevolucion() {
        return noDocDevolucion;
    }

    public void setNoDocDevolucion(String noDocDevolucion) {
        this.noDocDevolucion = noDocDevolucion;
    }

    public String getNoDocVenta() {
        return noDocVenta;
    }

    public void setNoDocVenta(String noDocVenta) {
        this.noDocVenta = noDocVenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
