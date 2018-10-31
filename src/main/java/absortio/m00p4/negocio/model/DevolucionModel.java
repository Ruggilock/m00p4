package absortio.m00p4.negocio.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "devolucion")
public class DevolucionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "devolucion_iddevolucion_seq")
    @SequenceGenerator(name = "devolucion_iddevolucion_seq", sequenceName = "devolucion_iddevolucion_seq", allocationSize = 1)
    @Column(name = "iddevolucion")
    private Integer id;

    @Column
    private String nodevolucion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fechaemision")
    private Date fechaEmision;

    @ManyToOne
    @JoinColumn(name = "iddocumento")
    private FacturaParcialModel documento;

    @OneToMany(mappedBy = "devolucion", cascade = CascadeType.MERGE)
    private List<DetalleDevolucionModel> detallesdev= new ArrayList<>();

    public void addDetalleDevolucion(DetalleDevolucionModel detalleDevolucionModel) {
        detallesdev.add(detalleDevolucionModel);
        detalleDevolucionModel.setDevolucion(this);
    }

    @Column(name = "subtotal")
    private  Double subtotal;

    @Column(name = "impuestos")
    private  Double impuestos;

    @Column(name = "descuentos")
    private  Double descuentos;

    @Column(name="total")
    private  Double total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNodevolucion() {
        return nodevolucion;
    }

    public void setNodevolucion(String nodevolucion) {
        this.nodevolucion = nodevolucion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public FacturaParcialModel getDocumento() {
        return documento;
    }

    public void setDocumento(FacturaParcialModel documento) {
        this.documento = documento;
    }

    public List<DetalleDevolucionModel> getDetallesdev() {
        return detallesdev;
    }

    public void setDetallesdev(List<DetalleDevolucionModel> detallesdev) {
        this.detallesdev = detallesdev;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Double impuestos) {
        this.impuestos = impuestos;
    }

    public Double getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(Double descuentos) {
        this.descuentos = descuentos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
