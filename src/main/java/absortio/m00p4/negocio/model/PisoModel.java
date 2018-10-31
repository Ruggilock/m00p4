package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "piso")
public class PisoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="piso_idpiso_seq")
    @SequenceGenerator(name="piso_idpiso_seq", sequenceName="piso_idpiso_seq", allocationSize=1)
    @Column(name = "idpiso")
    private Integer id;

    @OneToMany(mappedBy = "piso", cascade = CascadeType.ALL)
    private List<BloqueModel> bloque= new ArrayList<>();

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "maxpeso")
    private Double maxPeso;

    @Column(name = "maxvolumen")
    private Double maxVolumen;

    @Column(name = "estado")
    private String estado;


    public void addBloque(BloqueModel bloqueModel) {
        bloque.add(bloqueModel);
        bloqueModel.setPiso(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getMaxPeso() {
        return maxPeso;
    }

    public void setMaxPeso(Double maxPeso) {
        this.maxPeso = maxPeso;
    }

    public Double getMaxVolumen() {
        return maxVolumen;
    }

    public void setMaxVolumen(Double maxVolumen) {
        this.maxVolumen = maxVolumen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}