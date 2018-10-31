package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "precio")
public class PrecioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "precio_idprecio_seq")
    @SequenceGenerator(name = "precio_idprecio_seq", sequenceName = "precio_idprecio_seq", allocationSize = 1)
    @Column(name = "idprecio")
    private Integer id;

    @Column(name = "preciounitario")
    private Double precio;

    @Column(name = "fechaini")
    private Date fechaInicio;

    @Column(name = "fechafin")
    private Date fechaFin;

    @Column(name = "estado")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto")
    private ProductoModel producto= new ProductoModel ();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrecioModel )) return false;
        return id != null && id.equals(((PrecioModel) o).id);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public ProductoModel getProducto() {
        return producto;
    }

    public void setProducto(ProductoModel producto) {
        this.producto = producto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public PrecioModel(Double precio, Date fechaInicio, String estado, ProductoModel producto) {
        this.precio = precio;
        this.fechaInicio = fechaInicio;
        this.estado=estado;
        this.producto = producto;
    }

    public PrecioModel() {
    }
}
