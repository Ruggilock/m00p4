package absortio.m00p4.negocio.model.auxiliares;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;

public class Entrega {
    private Integer n;
    private String codigo;
    private String nombre;
    private Integer cantPedida;
    private Integer cantidadEntrega;
    private Date fechaEntrega;
    private String departamento;
    private String provincia;
    private String distrito;
    private String direccion;
    private Spinner<Integer> spinner;

    public Entrega() {
        spinner= new Spinner<> (  ) ;
        SpinnerValueFactory spinnerValueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory ( 0,1000,0 );
        spinner.setEditable ( true );
        spinner.setValueFactory ( spinnerValueFactory );
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantPedida() {
        return cantPedida;
    }

    public void setCantPedida(Integer cantPedida) {
        this.cantPedida = cantPedida;
    }

    public Integer getCantidadEntrega() {
        return cantidadEntrega;
    }

    public void setCantidadEntrega(Integer cantidadEntrega) {
        this.cantidadEntrega = cantidadEntrega;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public Spinner<Integer> getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner<Integer> spinner) {
        this.spinner = spinner;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
