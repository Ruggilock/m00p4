package absortio.m00p4.negocio.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "kardex")
public class KardexModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="kardex_idkardex_seq")
    @SequenceGenerator(name="kardex_idkardex_seq", sequenceName="kardex_idkardex_seq", allocationSize=1)
    @Column(name = "idkardex")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idlote")
    private LoteModel lote;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "idbloque")
    private BloqueModel bloque;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "idmotivooperacion")
    private MotivoOperacionKardexModel motivooperacion;

    @Column(name = "fecha")
    private Date fechaoperacion;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "stockfisico")
    private Integer stockfisico;

    @Column(name="descripcion")
    private  String descripcion;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LoteModel getLote() {
        return lote;
    }

    public void setLote(LoteModel lote) {
        this.lote = lote;
    }

    public BloqueModel getBloque() {
        return bloque;
    }

    public void setBloque(BloqueModel bloque) {
        this.bloque = bloque;
    }

    public MotivoOperacionKardexModel getMotivooperacion() {
        return motivooperacion;
    }

    public void setMotivooperacion(MotivoOperacionKardexModel motivooperacion) {
        this.motivooperacion = motivooperacion;
    }

    public Date getFechaoperacion() {
        return fechaoperacion;
    }

    public void setFechaoperacion(Date fechaoperacion) {
        this.fechaoperacion = fechaoperacion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getStockfisico() {
        return stockfisico;
    }

    public void setStockfisico(Integer stockfisico) {
        this.stockfisico = stockfisico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        KardexModel that = (KardexModel) o;
        return Objects.equals(lote, that.lote) &&
                Objects.equals(bloque, that.bloque) &&
                Objects.equals(motivooperacion, that.motivooperacion) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lote, bloque,motivooperacion, id);
    }
    @Override
    public String toString() {
        return "";
    }

}
