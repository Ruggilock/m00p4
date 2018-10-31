package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categoriaproducto")
public class CategoriaProductoModel {
    @Id
    @Column(name = "idcategoriaproducto")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;


    @OneToMany(mappedBy = "categoriaProducto", cascade = CascadeType.ALL)
    private List<ProductoModel> productos;

    @Column(name = "estado")
    private String estado;

    @Override
    public String toString() {
        return "CategoriaProductoModel{" +
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
}