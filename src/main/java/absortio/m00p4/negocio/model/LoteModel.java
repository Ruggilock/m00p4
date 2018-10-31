package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lote")
public class LoteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lote_idlote_seq")
    @SequenceGenerator(name = "lote_idlote_seq", sequenceName = "lote_idlote_seq", allocationSize = 1)
    @Column(name = "idlote")
    private Integer id;

    @OneToMany(mappedBy = "idLote", cascade = CascadeType.ALL)
    private List<DetalleDocumentoModel> detalleDocumentos= new ArrayList<>();

    public void addSectorAlmacen(DetalleDocumentoModel detalleDocumentoModel) {
        detalleDocumentos.add(detalleDocumentoModel);
        detalleDocumentoModel.setIdLote(this);
    }


    @ManyToOne
    @JoinColumn(name = "idproducto")
    private ProductoModel producto;

    @Column(name = "fechaVencimiento")
    private Date fechaVencimiento;

    @Column(name = "fechaAdquisicion")
    private Date fechaAdquisicion;

    @Column(name = "stockAdquirido")
    private Integer stockAdquirido;

    @Column(name = "stockComprometido")
    private Integer stockComprometido;

    @Column(name = "stockDisponible")
    private Integer stockDisponible;

    @Column(name="stockMerma")
    private Integer stockMerma;

    @Column(name = "observacion")
    private String observacion;


    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "lote", cascade = {CascadeType.ALL})
    //@Fetch(FetchMode.SELECT)
    private List<KardexModel> operaciones= new ArrayList<>();

    public void addBloquetoNuevoLote(BloqueModel tag, Integer cantidad, MotivoOperacionKardexModel motivo) {
        KardexModel postTag = new KardexModel();
        postTag.setBloque(tag);
        postTag.setLote(this);
        postTag.setCantidad(cantidad);
        postTag.setFechaoperacion(new Date());
        postTag.setMotivooperacion(motivo);
        postTag.setStockfisico(cantidad);
        postTag.setDescripcion("");
        operaciones.add(postTag);
        tag.getOperaciones().add(postTag);
    }

    public void addBloquetoLote(BloqueModel tag, Integer cantidad, MotivoOperacionKardexModel motivo, String desc) {
        KardexModel postTag = new KardexModel();
        postTag.setBloque(tag);
        postTag.setLote(this);
        postTag.setCantidad(cantidad);
        postTag.setFechaoperacion(new Date());
        postTag.setMotivooperacion(motivo);
        postTag.setStockfisico(cantidad);
        postTag.setDescripcion(desc);
        operaciones.add(postTag);
        tag.getOperaciones().add(postTag);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public Integer getStockAdquirido() {
        return stockAdquirido;
    }

    public void setStockAdquirido(Integer stockAdquirido) {
        this.stockAdquirido = stockAdquirido;
    }

    public Integer getStockComprometido() {
        return stockComprometido;
    }

    public void setStockComprometido(Integer stockComprometido) {
        this.stockComprometido = stockComprometido;
    }

    public Integer getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<KardexModel> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<KardexModel> operaciones) {
        this.operaciones = operaciones;
    }

    public ProductoModel getProducto() {
        return producto;
    }

    public void setProducto(ProductoModel producto) {
        this.producto = producto;
    }

    public Integer getStockMerma() {
        return stockMerma;
    }

    public void setStockMerma(Integer stockMerma) {
        this.stockMerma = stockMerma;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        LoteModel post = (LoteModel) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LoteModel{" +
                "id=" + id +
                ", producto=" + producto +
                ", fechaVencimiento=" + fechaVencimiento +
                ", fechaAdquisicion=" + fechaAdquisicion +
                ", stockAdquirido=" + stockAdquirido +
                ", stockComprometido=" + stockComprometido +
                ", stockDisponible=" + stockDisponible +
                ", stockMerma=" + stockMerma +
                ", observacion='" + observacion + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}