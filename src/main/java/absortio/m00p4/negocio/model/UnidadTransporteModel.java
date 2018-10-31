package absortio.m00p4.negocio.model;

import javax.persistence.*;

@Entity
@Table(name = "unidadtransporte")      // Se coloca entre "" el nombre de la tabla en la BD
public class UnidadTransporteModel {

    // Creamos un id consecutivo; para ello, debemos asegurarnos que en BD este marcado como SERIAL
    //https://stackoverflow.com/questions/41791802/autoincrement-id-postgresql-and-spring-boot-data-jpa
    @Id
    @Column(name = "idunidadtransporte")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "placa")
    private String placa;
    @Column(name = "modelo")
    private String modelo;
    @Column(name = "marca")
    private String marca;
    @Column(name = "unidadvolumen")
    private String unidadVolumen;
    @Column(name = "volumensoportado")
    private float volumenSoportado;
    @Column(name = "unidadpeso")
    private String unidadPeso;
    @Column(name = "pesosoportado")
    private float pesoSoportado;
    @Column(name = "estado")
    private String estado;


    @ManyToOne
    @JoinColumn(name = "idtransportista")
    private TransportistaModel transportista;       // La columna "idtransporte" nos va a servir para encontrar el transportista

// ------------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getUnidadVolumen() {
        return unidadVolumen;
    }

    public void setUnidadVolumen(String unidadVolumen) {
        this.unidadVolumen = unidadVolumen;
    }

    public float getVolumenSoportado() {
        return volumenSoportado;
    }

    public void setVolumenSoportado(float volumenSoportado) {
        this.volumenSoportado = volumenSoportado;
    }

    public String getUnidadPeso() {
        return unidadPeso;
    }

    public void setUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
    }

    public float getPesoSoportado() {
        return pesoSoportado;
    }

    public void setPesoSoportado(float pesoSoportado) {
        this.pesoSoportado = pesoSoportado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TransportistaModel getTransportista() {
        return transportista;
    }

    public void setTransportista(TransportistaModel transportista) {
        this.transportista = transportista;
    }
}