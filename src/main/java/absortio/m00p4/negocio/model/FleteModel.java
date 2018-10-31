package absortio.m00p4.negocio.model;

import absortio.m00p4.negocio.model.auxiliares.Flete;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "fletedistancia_capacidad")
@IdClass(FleteIdModel.class)
public class FleteModel implements Serializable{

    @Column(name = "costo")
    private Double costo;

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    @Id
    private Integer idcapacidad;
    @Id
    private Integer iddistrito;

    //@Id
    @ManyToOne
    @JoinColumn(name = "capacidad_id")
    private FleteCapacidadModel capacidad;

    @ManyToOne
    @JoinColumn(name="distrito_id")
    private  FleteDistritoModel distrito;

    public FleteCapacidadModel getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(FleteCapacidadModel capacidad) {
        this.capacidad = capacidad;
    }

    public FleteDistritoModel getDistrito() {
        return distrito;
    }

    public void setDistrito(FleteDistritoModel distrito) {
        this.distrito = distrito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FleteModel that = (FleteModel) o;
        return Objects.equals(distrito, that.distrito) &&
                Objects.equals(capacidad, that.capacidad);
    }
    @Override
    public int hashCode() {
        return Objects.hash(distrito, capacidad);
    }

    @Override
    public String toString() {
        return "";
    }


    public Integer getIdcapacidad() {
        return idcapacidad;
    }

    public void setIdcapacidad(Integer idcapacidad) {
        this.idcapacidad = idcapacidad;
    }

    public Integer getIddistrito() {
        return iddistrito;
    }

    public void setIddistrito(Integer iddistrito) {
        this.iddistrito = iddistrito;
    }
}
