package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bloque")
public class BloqueModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="bloque_idbloque_seq")
    @SequenceGenerator(name="bloque_idbloque_seq", sequenceName="bloque_idbloque_seq", allocationSize=1)
    @Column(name = "idbloque")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idrack")
    private RackModel idRack;

    @ManyToOne
    @JoinColumn(name = "idpiso")
    private PisoModel piso;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BloqueModel )) return false;
        return id != null && id.equals(((BloqueModel) o).id);
    }

/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        BloqueModel post = (BloqueModel) o;
        return Objects.equals(id, post.id);
    }
*/

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "posx")
    private Integer posX;

    @Column(name = "posy")
    private Integer posY;

    @Column(name = "posz")
    private Integer posZ;

    @Column(name = "unidadvolumen")
    private String  unidadVolumen;

    @Column(name = "volumendisponible")
    private Double volumenDisponible;

    @Column(name = "unidadpeso")
    private String unidadPeso;

    @Column(name = "pesodisponible")
    private Double pesoDisponible;

    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "bloque", cascade = {CascadeType.MERGE})
    //@Fetch(FetchMode.SELECT)
    private List<KardexModel> operaciones= new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RackModel getIdRack() {
        return idRack;
    }

    public void setIdRack(RackModel idRack) {
        this.idRack = idRack;
    }

    public PisoModel getPiso() {
        return piso;
    }

    public void setPiso(PisoModel piso) {
        this.piso = piso;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Integer getPosX() {
        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public Integer getPosZ() {
        return posZ;
    }

    public void setPosZ(Integer posZ) {
        this.posZ = posZ;
    }

    public String getUnidadVolumen() {
        return unidadVolumen;
    }

    public void setUnidadVolumen(String unidadVolumen) {
        this.unidadVolumen = unidadVolumen;
    }

    public Double getVolumenDisponible() {
        return volumenDisponible;
    }

    public void setVolumenDisponible(Double volumenDisponible) {
        this.volumenDisponible = volumenDisponible;
    }

    public String getUnidadPeso() {
        return unidadPeso;
    }

    public void setUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
    }

    public Double getPesoDisponible() {
        return pesoDisponible;
    }

    public void setPesoDisponible(Double pesoDisponible) {
        this.pesoDisponible = pesoDisponible;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<KardexModel> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<KardexModel> operaciones) {
        this.operaciones = operaciones;
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