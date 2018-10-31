package absortio.m00p4.negocio.model.auxiliares;

import javafx.scene.control.Button;

import java.util.Date;

public class Despacho {
    private Integer id;
    private Integer n;
    private String sobreNombre;
    private Date fecha;
    private Double volumen;
    private Double peso;
    private String transportista;
    private String archivo;
    private String estado;
    private Button buttonAgregarRuta;
    private Button confirmar;

    public Integer getN() {
        return n;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getSobreNombre() {
        return sobreNombre;
    }

    public void setSobreNombre(String sobreNombre) {
        this.sobreNombre = sobreNombre;
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

    public String getTransportista() {
        return transportista;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
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

    public Button getButtonAgregarRuta() {
        return buttonAgregarRuta;
    }

    public void setButtonAgregarRuta(Button buttonAgregarRuta) {
        this.buttonAgregarRuta = buttonAgregarRuta;
    }

    public Button getConfirmar() {
        return confirmar;
    }

    public void setConfirmar(Button confirmar) {
        this.confirmar = confirmar;
    }
}
