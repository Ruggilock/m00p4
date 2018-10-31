package absortio.m00p4.negocio.model;


import javax.persistence.*;

@Entity
@Table(name = "ruta")
public class RutaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ruta_idruta_seq")
    @SequenceGenerator(name="ruta_idruta_seq", sequenceName="ruta_idruta_seq", allocationSize=1)
    @Column(name = "idruta")
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "iddespacho")
    private DespachoModel idDespacho;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RutaModel)) return false;
        return id != null && id.equals(((RutaModel) o).id);
    }
    @Column(name = "funcion_objetivo")
    private Double funcionObjetivo;
    @Column(name = "numero_viajes")
    private Integer numeroViajes;
    @Column(name = "peso_total")
    private Double pesoTotal;
    @Column(name = "volumen_total")
    private Double volumenTotal;
    @Column(name = "ruta")
    private String ruta;
    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DespachoModel getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(DespachoModel idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Double getFuncionObjetivo() {
        return funcionObjetivo;
    }

    public void setFuncionObjetivo(Double funcionObjetivo) {
        this.funcionObjetivo = funcionObjetivo;
    }

    public Integer getNumeroViajes() {
        return numeroViajes;
    }

    public void setNumeroViajes(Integer numeroViajes) {
        this.numeroViajes = numeroViajes;
    }

    public Double getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(Double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public Double getVolumenTotal() {
        return volumenTotal;
    }

    public void setVolumenTotal(Double volumenTotal) {
        this.volumenTotal = volumenTotal;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}