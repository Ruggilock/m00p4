package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sectoralmacen")
public class SectorAlmacenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="sectoralmacen_idsectoralmacen_seq")
    @SequenceGenerator(name="sectoralmacen_idsectoralmacen_seq", sequenceName="sectoralmacen_idsectoralmacen_seq", allocationSize=1)
    @Column(name = "idsectoralmacen")
    private Integer id;

    @OneToMany(mappedBy = "idSectorAlmacen", cascade = CascadeType.ALL)
    private List<RackModel> idrack= new ArrayList<>();

    public void addRack(RackModel rackModel) {
        idrack.add(rackModel);
        rackModel.setIdSectorAlmacen(this);
    }


    @ManyToOne
    @JoinColumn(name = "idmapa")
    private MapaModel idMapa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SectorAlmacenModel )) return false;
        return id != null && id.equals(((SectorAlmacenModel) o).id);
    }




    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "posxini")
    private Integer posXIni;

    @Column(name = "posyini")
    private Integer posYIni;

    @Column(name = "posxfin")
    private Integer posXFin;

    @Column(name = "posyfin")
    private Integer posYFin;

    @Column(name = "orientacion")
    private String orientacion;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "estado")
    private String estado;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MapaModel getIdMapa() {
        return idMapa;
    }

    public void setIdMapa(MapaModel idMapa) {
        this.idMapa = idMapa;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getPosXIni() {
        return posXIni;
    }

    public void setPosXIni(Integer posXIni) {
        this.posXIni = posXIni;
    }

    public Integer getPosYIni() {
        return posYIni;
    }

    public void setPosYIni(Integer posYIni) {
        this.posYIni = posYIni;
    }

    public Integer getPosXFin() {
        return posXFin;
    }

    public void setPosXFin(Integer posXFin) {
        this.posXFin = posXFin;
    }

    public Integer getPosYFin() {
        return posYFin;
    }

    public void setPosYFin(Integer posYFin) {
        this.posYFin = posYFin;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
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
}