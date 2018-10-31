package absortio.m00p4.negocio.model;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "RolModel")
@Table(name = "rol")
@Proxy(lazy = false)
public class RolModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="rol_idrol_seq")
    @SequenceGenerator(name="rol_idrol_seq", sequenceName="rol_idrol_seq", allocationSize=1)
    @Column(name = "idrol")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "idRol", cascade = CascadeType.ALL)
    private List<UsuarioModel> usuarioModels= new ArrayList<>();

    public void addUsuarioModel(UsuarioModel usuarioModel) {
        usuarioModels.add(usuarioModel);
        usuarioModel.setIdRol(this);
    }



    @ManyToMany
    @JoinTable(name = "rol_permiso",
            joinColumns = @JoinColumn(name = "idrol"),
            inverseJoinColumns = @JoinColumn(name = "idpermiso")
    )
    @Fetch(FetchMode.SELECT)
    private List<PermisoModel> permisos = new ArrayList<PermisoModel>();

    public List<PermisoModel> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<PermisoModel> permisos) {
        this.permisos = permisos;
    }

    public void addPermiso(PermisoModel permisoModel) {
        permisos.add(permisoModel);
        //permisoModel.getRoles().add(this);

    }

    public void removePermiso(PermisoModel permisoModel) {
        permisos.remove(permisoModel);
        //permisoModel.getRoles().remove(this);
    }

    @Override
    public String toString() {
        return "RolModel{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolModel)) return false;
        return id != null && id.equals(((RolModel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}