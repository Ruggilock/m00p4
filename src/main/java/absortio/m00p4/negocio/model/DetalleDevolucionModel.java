package absortio.m00p4.negocio.model;

import javax.persistence.*;

@Entity
@Table(name = "detalledevolucion")
public class DetalleDevolucionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalledevolucion_iddetalledevolucion_seq")
    @SequenceGenerator(name = "detalledevolucion_iddetalledevolucion_seq", sequenceName = "detalledevolucion_iddetalledevolucion_seq", allocationSize = 1)
    @Column(name = "iddetalledevolucion")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "devolucion")
    private DevolucionModel devolucion;

    @ManyToOne
    @JoinColumn(name = "idlote")
    private LoteModel idLote;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "pU")
    private Double pU;

    @Column(name = "dU")
    private Double dU;

    @Column(name = "importe")
    private Double importe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DevolucionModel getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(DevolucionModel devolucion) {
        this.devolucion = devolucion;
    }

    public LoteModel getIdLote() {
        return idLote;
    }

    public void setIdLote(LoteModel idLote) {
        this.idLote = idLote;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getpU() {
        return pU;
    }

    public void setpU(Double pU) {
        this.pU = pU;
    }

    public Double getdU() {
        return dU;
    }

    public void setdU(Double dU) {
        this.dU = dU;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }
}
