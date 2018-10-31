package absortio.m00p4.negocio.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "fletedistrito")
public class FleteDistritoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="fletedistrito_idfletedistrito_seq")
    @SequenceGenerator(name="fletedistrito_idfletedistrito_seq", sequenceName="fletedistrito_idfletedistrito_seq", allocationSize=1)
    @Column(name = "idfletedistrito")
    private Integer id;

    @Column(name = "departamento")
    private String departamento;
    @Column(name = "provincia")
    private String provincia;
    @Column(name = "distrito")
    private String distrito;
    @Column(name = "costo")
    private Double costo;
    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "distrito", cascade = {CascadeType.MERGE})
    //@Fetch(FetchMode.SELECT)
    private List<FleteModel> fletes= new ArrayList<>();

    public void addCapacidad(FleteCapacidadModel tag) {
        FleteModel postTag = new FleteModel();
        postTag.setDistrito(this);
        postTag.setCapacidad(tag);
        postTag.setCosto((tag.getCosto()*0.5)+(this.getCosto()*0.5));
        postTag.setIdcapacidad(tag.getId());
        postTag.setIddistrito(this.getId());
        fletes.add(postTag);
        tag.getFletes().add(postTag);
    }

    public void removeCapacidad(FleteCapacidadModel tag) {
        for (Iterator<FleteModel> iterator = fletes.iterator();
             iterator.hasNext(); ) {
            FleteModel postTag = iterator.next();

            if (postTag.getDistrito().equals(this) &&
                    postTag.getCapacidad().equals(tag)) {
                iterator.remove();
                postTag.getCapacidad().getFletes().remove(postTag);
                postTag.setDistrito(null);
                postTag.setCapacidad(null);
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public List<FleteModel> getFletes() {
        return fletes;
    }

    public void setFletes(List<FleteModel> fletes) {
        this.fletes = fletes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        FleteDistritoModel post = (FleteDistritoModel) o;
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
