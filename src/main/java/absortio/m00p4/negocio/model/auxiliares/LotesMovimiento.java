package absortio.m00p4.negocio.model.auxiliares;

import javafx.scene.control.Button;

import java.util.Date;

public class LotesMovimiento {
    private Integer n;
    private Integer codigo;
    private String nombre;
    private Date fechaAdquisicion;
    private Date fechaVencimiento;
    private Button acciones;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Button getAcciones() {
        return acciones;
    }

    public void setAcciones(Button acciones) {
        this.acciones = acciones;
    }
}
