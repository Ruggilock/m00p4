package absortio.m00p4.negocio.model;



import absortio.m00p4.negocio.model.auxiliares.FleteDistrito;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Cascade;

//import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.Double;
import java.util.*;

@Entity
@Table(name = "fletecapacidad")
public class FleteCapacidadModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fletecapacidad_idfletecapacidad_seq")
    @SequenceGenerator(name = "fletecapacidad_idfletecapacidad_seq", sequenceName = "fletecapacidad_idfletecapacidad_seq", allocationSize = 1)
    @Column(name = "idfletecapacidad")
    private Integer id;
    @Column(name = "unidadvolumen")
    private String unidadvolumen;
    @Column(name = "volumenmin")
    private Double volumenMin;
    @Column(name = "volumenmax")
    private Double volumenMax;
    @Column(name = "unidadpeso")
    private String unidadPeso;
    @Column(name = "pesomin")
    private Double pesoMin;
    @Column(name = "pesomax")
    private Double pesoMax;
    @Column(name = "costo")
    private Double costo;
    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "capacidad", cascade = CascadeType.MERGE)
    //@Fetch(FetchMode.SELECT)
    private List<FleteModel> fletes = new ArrayList<>();

    public void addDistrito(FleteDistritoModel tag) {
        FleteModel postTag = new FleteModel();
        postTag.setCapacidad(this);
        postTag.setDistrito(tag);
        postTag.setCosto((tag.getCosto()*0.5)+(this.getCosto()*0.5));
        postTag.setIdcapacidad(this.getId());
        postTag.setIddistrito(tag.getId());
        fletes.add(postTag);
        tag.getFletes().add(postTag);
    }

    public void removeDistrito(FleteDistritoModel tag) {
        for (Iterator<FleteModel> iterator = fletes.iterator();
             iterator.hasNext(); ) {
            FleteModel postTag = iterator.next();

            if (postTag.getCapacidad().equals(this) &&
                    postTag.getDistrito().equals(tag)) {
                iterator.remove();
                postTag.getDistrito().getFletes().remove(postTag);
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

    public String getUnidadvolumen() {
        return unidadvolumen;
    }

    public void setUnidadvolumen(String unidadvolumen) {
        this.unidadvolumen = unidadvolumen;
    }

    public Double getVolumenMin() {
        return volumenMin;
    }

    public void setVolumenMin(Double volumenMin) {
        this.volumenMin = volumenMin;
    }

    public Double getVolumenMax() {
        return volumenMax;
    }

    public void setVolumenMax(Double volumenMax) {
        this.volumenMax = volumenMax;
    }

    public String getUnidadPeso() {
        return unidadPeso;
    }

    public void setUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
    }

    public Double getPesoMin() {
        return pesoMin;
    }

    public void setPesoMin(Double pesoMin) {
        this.pesoMin = pesoMin;
    }

    public Double getPesoMax() {
        return pesoMax;
    }

    public void setPesoMax(Double pesoMax) {
        this.pesoMax = pesoMax;
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
        if (o == null || getClass() != o.getClass()) return false;
        FleteCapacidadModel tag = (FleteCapacidadModel) o;
        return Objects.equals(id, tag.id);
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