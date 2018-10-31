package absortio.m00p4.negocio.model;

import javax.persistence.*;

@Entity
@Table(name = "rutacoordenada")
public class RutaCoordenadaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ruta_idrutacoordenada_seq")
    @SequenceGenerator(name="ruta_idrutacoordenada_seq", sequenceName="ruta_idrutacoordenada_seq", allocationSize=1)
    @Column(name = "idrutacoordenada")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idrutapaso")
    private RutaPasoModel idRutaPaso;

    @Column(name = "coordenadax")
    private Integer coordenadaX;

    @Column(name = "coordenaday")
    private Integer coordenadaY;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RutaCoordenadaModel)) return false;
        return id != null && id.equals(((RutaCoordenadaModel) o).id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RutaPasoModel getIdRutaPaso() {
        return idRutaPaso;
    }

    public void setIdRutaPaso(RutaPasoModel idRutaPaso) {
        this.idRutaPaso = idRutaPaso;
    }

    public Integer getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(Integer coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public Integer getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(Integer coordenadaY) {
        this.coordenadaY = coordenadaY;
    }
}
