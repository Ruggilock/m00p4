package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rack")
public class RackModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="rack_idrack_seq")
    @SequenceGenerator(name="rack_idrack_seq", sequenceName="rack_idrack_seq", allocationSize=1)
    @Column(name = "idrack")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idsectoralmacen")
    private SectorAlmacenModel idSectorAlmacen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RackModel )) return false;
        return id != null && id.equals(((RackModel) o).id);
    }

    @OneToMany(mappedBy = "idRack", cascade = CascadeType.ALL)
    private List<BloqueModel> idBloque= new ArrayList<>();

    public void addBloque(BloqueModel bloqueModel) {
        idBloque.add(bloqueModel);
        bloqueModel.setIdRack(this);
    }

    @Column(name = "orientacion")
    private String orientacion;

    @Column(name ="tamanio")
    private Integer tamanio;

    @Column(name = "posxini")
    private Integer posXIni;

    @Column(name = "posxfin")
    private Integer posXFin;

    @Column(name = "posyini")
    private Integer posYIni;

    @Column(name = "posyfin")
    private Integer posYFin;

    @Column(name = "pisos")
    private Integer pisos;

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

    public SectorAlmacenModel getIdSectorAlmacen() {
        return idSectorAlmacen;
    }

    public void setIdSectorAlmacen(SectorAlmacenModel idSectorAlmacen) {
        this.idSectorAlmacen = idSectorAlmacen;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }

    public Integer getTamanio() {
        return tamanio;
    }

    public void setTamanio(Integer tamanio) {
        this.tamanio = tamanio;
    }

    public Integer getPosXIni() {
        return posXIni;
    }

    public void setPosXIni(Integer posXIni) {
        this.posXIni = posXIni;
    }

    public Integer getPosXFin() {
        return posXFin;
    }

    public void setPosXFin(Integer posXFin) {
        this.posXFin = posXFin;
    }

    public Integer getPosYIni() {
        return posYIni;
    }

    public void setPosYIni(Integer posYIni) {
        this.posYIni = posYIni;
    }

    public Integer getPosYFin() {
        return posYFin;
    }

    public void setPosYFin(Integer posYFin) {
        this.posYFin = posYFin;
    }

    public Integer getPisos() {
        return pisos;
    }

    public void setPisos(Integer pisos) {
        this.pisos = pisos;
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