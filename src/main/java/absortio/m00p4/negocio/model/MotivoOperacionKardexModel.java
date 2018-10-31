package absortio.m00p4.negocio.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "motivooperacion")
public class MotivoOperacionKardexModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="motivooperacion_idmotivooperacion_seq")
    @SequenceGenerator(name="motivooperacion_idmotivooperacion_seq", sequenceName="motivooperacion_idmotivooperacion_seq", allocationSize=1)
    @Column(name = "idmotivooperacion")
    private Integer id;

    @Column(name="nombre")
    private String nombre;

    @Column(name="descripcion")
    private String descripcion;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "idtipooperacion")
    private TipoOperacionKardexModel tipooperacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TipoOperacionKardexModel getTipooperacion() {
        return tipooperacion;
    }

    public void setTipooperacion(TipoOperacionKardexModel tipooperacion) {
        this.tipooperacion = tipooperacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MotivoOperacionKardexModel that = (MotivoOperacionKardexModel) o;
        return Objects.equals(tipooperacion, that.tipooperacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipooperacion, tipooperacion);
    }
    @Override
    public String toString() {
        return "";
    }
}
