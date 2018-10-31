package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "detalledocumento")
public class DetalleDocumentoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalledocumento_iddetalledocumento_seq")
    @SequenceGenerator(name = "detalledocumento_iddetalledocumento_seq", sequenceName = "detalledocumento_iddetalledocumento_seq", allocationSize = 1)
    @Column(name = "iddetalledocumento")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "iddocumento")
    private DocumentoModel idDocumento;

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


    @OneToMany(mappedBy = "idDetalleDocumento", cascade = CascadeType.MERGE)
    private List<DetalleEntregaModel> detalleEntregas= new ArrayList<>();


    public void addDetalleEntrega(DetalleEntregaModel detalleEntregaModel) {
        detalleEntregas.add(detalleEntregaModel);
        detalleEntregaModel.setIdDetalleDocumento(this);
    }


    public LoteModel getIdLote() {
        return idLote;
    }

    public void setIdLote(LoteModel idLote) {
        this.idLote = idLote;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DocumentoModel getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(DocumentoModel idDocumento) {
        this.idDocumento = idDocumento;
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

    public  void AsignarDetalleDocumento(DetalleDocumentoModel data){
        this.cantidad = data.cantidad;
        this.dU = data.dU;
        this.id = data.id;
        this.idDocumento = data.idDocumento;
        this.idLote = data.idLote;
        this.importe = data.importe;
    }

    public List<DetalleEntregaModel> getDetalleEntregas() {
        return detalleEntregas;
    }

    public void setDetalleEntregas(List<DetalleEntregaModel> detalleEntregas) {
        this.detalleEntregas = detalleEntregas;
    }
}
