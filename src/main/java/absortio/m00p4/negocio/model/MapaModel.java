package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mapa")
public class MapaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="mapa_idmapa_seq")
    @SequenceGenerator(name="mapa_idmapa_seq", sequenceName="mapa_idmapa_seq", allocationSize=1)
    @Column(name = "idmapa")
    private Integer id;

    @Column(name = "imagen")
    private String imagen;
    @Column(name = "ancho")
    private Integer ancho;
    @Column(name = "largo")
    private Integer largo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "ruta")
    private String ruta;
    @Column(name = "identificador")
    private String identificador;
    @Column(name = "estado")
    private String estado;
    @Column(name = "puertax")
    private Integer puertax;
    @Column(name = "puertay")
    private Integer puertay;

    @OneToMany(mappedBy = "idMapa", cascade = CascadeType.ALL)
    private List<SectorAlmacenModel> sectorAlmacen= new ArrayList<>();

    public void addSectorAlmacen(SectorAlmacenModel sectorAlmacenModel) {
        sectorAlmacen.add(sectorAlmacenModel);
        sectorAlmacenModel.setIdMapa(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getAncho() {
        return ancho;
    }

    public void setAncho(Integer ancho) {
        this.ancho = ancho;
    }

    public Integer getLargo() {
        return largo;
    }

    public void setLargo(Integer largo) {
        this.largo = largo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPuertax() {
        return puertax;
    }

    public void setPuertax(Integer puertax) {
        this.puertax = puertax;
    }

    public Integer getPuertay() {
        return puertay;
    }

    public void setPuertay(Integer puertay) {
        this.puertay = puertay;
    }
}