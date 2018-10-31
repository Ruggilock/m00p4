package absortio.m00p4.negocio.model;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "PermisoModel")
@Table(name = "permiso")
@Proxy(lazy = false)
public class PermisoModel {
    @Id
    @Column(name = "idpermiso")
    private Integer id;
    @NaturalId
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private String estado;

    @ManyToMany(mappedBy = "permisos")
    private List<RolModel> roles = new ArrayList<>();

    public List<RolModel> getRoles() {
        return roles;
    }

    public void setRoles(List<RolModel> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "PermisoModel{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", descripcion='" + getDescripcion() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermisoModel permisoModel = (PermisoModel) o;
        return Objects.equals(nombre, permisoModel.nombre);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

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
}
