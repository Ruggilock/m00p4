package absortio.m00p4.negocio.model;

import javax.persistence.*;

@Entity
@Table(name = "rutapaso")
public class RutaPasoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ruta_idrutapaso_seq")
    @SequenceGenerator(name="ruta_idrutapaso_seq", sequenceName="ruta_idrutapaso_seq", allocationSize=1)
    @Column(name = "idrutapaso")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idruta")
    private RutaModel idRuta;

    @ManyToOne
    @JoinColumn(name = "idproducto")
    private ProductoModel idProducto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RutaPasoModel)) return false;
        return id != null && id.equals(((RutaPasoModel) o).id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RutaModel getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(RutaModel idRuta) {
        this.idRuta = idRuta;
    }

    public ProductoModel getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(ProductoModel idProducto) {
        this.idProducto = idProducto;
    }
}
