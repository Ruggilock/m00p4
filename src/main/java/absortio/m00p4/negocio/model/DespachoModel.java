package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "despacho")
public class DespachoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="despacho_iddespacho_seq")
    @SequenceGenerator(name="despacho_iddespacho_seq", sequenceName="despacho_iddespacho_seq", allocationSize=1)
    @Column(name = "iddespacho")
    private Integer id;

    @OneToMany(mappedBy = "idDespacho", cascade = CascadeType.ALL)
    private List<DetalleDespachoModel> detalleDespachos= new ArrayList<>();

    public void addDetalleDespacho(DetalleDespachoModel detalleDespacho) {
        detalleDespachos.add(detalleDespacho);
        detalleDespacho.setIdDespacho(this);
    }

    @OneToMany(mappedBy = "idDespacho", cascade = CascadeType.ALL)
    private List<RutaModel> rutaModels= new ArrayList<>();

    public void addRuta(RutaModel ruta) {
        rutaModels.add(ruta);
        ruta.setIdDespacho(this);
    }

    @ManyToOne
    @JoinColumn(name = "idtransportista")
    private TransportistaModel idTransportista;

    @Column(name="sobrenombre")
    private String sobreNombre;
    @Column(name="archivo")
    private String archivo;
    @Column(name="estado")
    private String estado;
    @Column(name="estadodespacho")
    private String estadoDespacho;
    @Column(name ="fecha")
    private Date fecha;
    @Column(name ="volumen")
    private Double volumen;
    @Column(name ="peso")
    private Double peso;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DespachoModel)) return false;
        return id != null && id.equals(((DespachoModel) o).id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getVolumen() {
        return volumen;
    }

    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public TransportistaModel getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(TransportistaModel idTransportista) {
        this.idTransportista = idTransportista;
    }

    public String getSobreNombre() {
        return sobreNombre;
    }

    public void setSobreNombre(String sobreNombre) {
        this.sobreNombre = sobreNombre;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoDespacho() {
        return estadoDespacho;
    }

    public void setEstadoDespacho(String estadoDespacho) {
        this.estadoDespacho = estadoDespacho;
    }

    public List<DetalleDespachoModel> getDetalleDespachos() {
        return detalleDespachos;
    }

}