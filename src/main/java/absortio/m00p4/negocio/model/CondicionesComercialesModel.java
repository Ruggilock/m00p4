package absortio.m00p4.negocio.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "condicionescomerciales")
public class CondicionesComercialesModel {
    @Id
    @Column(name = "idcondicionescomerciales")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "fechaini")
    private Date fechaIni;

    @Column(name = "fechafin")
    private Date fechaFin;

    @Column(name = "codigo1")
    private Integer codigo1;

    @Column(name = "cantidad1")
    private Integer cantidad1;

    @Column(name = "codigo2")
    private Integer codigo2;

    @Column(name = "cantidad2")
    private Integer cantidad2;

    @Column(name = "porcentaje")
    private Double porcentaje;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "estado")
    private String estado;

    public CondicionesComercialesModel(){

        this.setCantidad1(0);
        this.cantidad2=0;
        this.categoria=" ";
        this.codigo1=0;
        this.codigo2=0;
        descripcion =" ";
        nombre=" ";
        estado = "activo";
    }
    /*@Override
    public String toString() {
        return "ClienteModel{" +
                "id=" + getId() +
                ", documentoIdentidad='" + documentoIdentidad + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", estado='" + estado + '\'' +
                ", grupo='" + grupo + '\'' +
                ", contacto='" + contacto + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                '}';
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getCodigo1() {
        return codigo1;
    }

    public void setCodigo1(Integer codigo1) {
        this.codigo1 = codigo1;
    }

    public Integer getCantidad1() {
        return cantidad1;
    }

    public void setCantidad1(Integer cantidad1) {
        this.cantidad1 = cantidad1;
    }

    public Integer getCodigo2() {
        return codigo2;
    }

    public void setCodigo2(Integer codigo2) {
        this.codigo2 = codigo2;
    }

    public Integer getCantidad2() {
        return cantidad2;
    }

    public void setCantidad2(Integer cantidad2) {
        this.cantidad2 = cantidad2;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
