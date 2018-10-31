package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "detalleentrega")
public class DetalleEntregaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalleentrega_iddetalleentrega_seq")
    @SequenceGenerator(name = "detalleentrega_iddetalleentrega_seq", sequenceName = "detalleentrega_iddetalleentrega_seq", allocationSize = 1)
    @Column(name = "iddetalleentrega")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "iddetalledocumento")
    private DetalleDocumentoModel idDetalleDocumento;

   @OneToMany(mappedBy = "iddetalleEntrega", cascade = CascadeType.ALL)
    private List<DetalleDespachoModel> detalleDespachos= new ArrayList<>();

    public void addDetalleDespacho(DetalleDespachoModel detalleDespachoModel) {
        detalleDespachos.add(detalleDespachoModel);
        detalleDespachoModel.setIddetalleEntrega(this);
    }

    @Column
    private Integer cantidadtotal;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleEntregaModel )) return false;
        return id != null && id.equals(((DetalleEntregaModel) o).id);
    }

    @Column(name = "fechaentrega")
    private Date fechaEntrega;

    @Column(name = "cantidadentregar")
    private Integer cantidadEntregar;

    @Column(name = "estado")
    private String estado;

    @Column(name = "estadoentrega")
    private String estadoEntrega;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Integer getCantidadEntregar() {
        return cantidadEntregar;
    }

    public void setCantidadEntregar(Integer cantidadEntregar) {
        this.cantidadEntregar = cantidadEntregar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public DetalleDocumentoModel getIdDetalleDocumento() {
        return idDetalleDocumento;
    }

    public void setIdDetalleDocumento(DetalleDocumentoModel idDetalleDocumento) {
        this.idDetalleDocumento = idDetalleDocumento;
    }

    public String getEstadoEntrega() {
        return estadoEntrega;
    }

    public void setEstadoEntrega(String estadoEntrega) {
        this.estadoEntrega = estadoEntrega;
    }

    public Integer getCantidadtotal() {
        return cantidadtotal;
    }

    public void setCantidadtotal(Integer cantidadtotal) {
        this.cantidadtotal = cantidadtotal;
    }
}