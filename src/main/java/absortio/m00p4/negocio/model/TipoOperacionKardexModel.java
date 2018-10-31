package absortio.m00p4.negocio.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tipooperacion")
public class TipoOperacionKardexModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="tipooperacion_idtipooperacion_seq")
    @SequenceGenerator(name="tipooperacion_idtipooperacion_seq", sequenceName="tipooperacion_idtipooperacion_seq", allocationSize=1)
    @Column(name = "idtipooperacion")
    private Integer id;

    @Column(name="nombre")
    private String nombre;

    @OneToMany(mappedBy = "tipooperacion", cascade = {CascadeType.MERGE})
    private List<MotivoOperacionKardexModel> motivos=new ArrayList<>();

    //@OneToMany(mappedBy = "tipooperacion", cascade = {CascadeType.MERGE})
    //@Fetch(FetchMode.SELECT)
    //private List<KardexModel> kardexs= new ArrayList<>();


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

    public List<MotivoOperacionKardexModel> getMotivos() {
        return motivos;
    }

    public void setMotivos(List<MotivoOperacionKardexModel> motivos) {
        this.motivos = motivos;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        TipoOperacionKardexModel post = (TipoOperacionKardexModel) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "";
    }
}
