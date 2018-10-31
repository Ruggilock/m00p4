package absortio.m00p4.negocio.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "documento")
public class DocumentoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documento_iddocumento_seq")
    @SequenceGenerator(name = "documento_iddocumento_seq", sequenceName = "documento_iddocumento_seq", allocationSize = 1)
    @Column(name = "iddocumento")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idtipodocumento")
    private TipoDocumentoModel tipodocumento;

    @ManyToOne
    @JoinColumn(name = "idcliente")
    private ClienteModel cliente;

    @ManyToOne
    @JoinColumn(name = "idigv")
    private IgvModel igv;

    @ManyToOne
    @JoinColumn(name = "idmoneda")
    private MonedaModel moneda;

    @ManyToOne
    @JoinColumn(name = "idusuario")
    private UsuarioModel usuaio;

    @OneToMany(mappedBy = "idDocumento", cascade = CascadeType.ALL)
    private List<DetalleDocumentoModel> detalleDocumentos= new ArrayList<>();

    public void addDetalleDocumento(DetalleDocumentoModel detalleDocumentoModel) {
        detalleDocumentos.add(detalleDocumentoModel);
        detalleDocumentoModel.setIdDocumento(this);
    }


    @Column(name = "nodocumento")
    private String noDocumento;

    @Column(name = "fechaemision")
    private Date fechaEmision;

    @Column(name = "estado")
    private String estado;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "departamento")
    private String departamento;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "distrito")
    private String distrito;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "descuentototal")
    private Double descuentoTotal;

    @Column(name = "fletetotal")
    private Double fleteTotal;

    @Column(name = "montototal")
    private Double montoTotal;

    @Column(name = "numeroentregas")
    private Integer numeroEntregas;

    @ManyToOne
    /*
    @JoinColumns ({
            @JoinColumn (name = "idcapacidad", referencedColumnName = "idcapacidad", updatable = false, insertable = false),
            @JoinColumn (name = "iddistrito", referencedColumnName = "iddistrito", updatable = false, insertable = false)
    })*/

    private FleteModel flete;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoDocumentoModel getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(TipoDocumentoModel tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public IgvModel getIgv() {
        return igv;
    }

    public void setIgv(IgvModel igv) {
        this.igv = igv;
    }

    public MonedaModel getMoneda() {
        return moneda;
    }

    public void setMoneda(MonedaModel moneda) {
        this.moneda = moneda;
    }

    public UsuarioModel getUsuaio() {
        return usuaio;
    }

    public void setUsuaio(UsuarioModel usuaio) {
        this.usuaio = usuaio;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDescuentoTotal() {
        return descuentoTotal;
    }

    public void setDescuentoTotal(Double descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }

    public Double getFleteTotal() {
        return fleteTotal;
    }

    public void setFleteTotal(Double fleteTotal) {
        this.fleteTotal = fleteTotal;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Integer getNumeroEntregas() {
        return numeroEntregas;
    }

    public void setNumeroEntregas(Integer numeroEntregas) {
        this.numeroEntregas = numeroEntregas;
    }

    public FleteModel getFlete() {
        return flete;
    }

    public void setFlete(FleteModel flete) {
        this.flete = flete;
    }

    public String getNoDocumento() {
        return noDocumento;
    }

    public void setNoDocumento(String noDocumento) {
        this.noDocumento = noDocumento;
    }

    public List<DetalleDocumentoModel> getDetalleDocumentos() {
        return detalleDocumentos;
    }

    public void setDetalleDocumentos(List<DetalleDocumentoModel> detalleDocumentos) {
        this.detalleDocumentos = detalleDocumentos;
    }
}
