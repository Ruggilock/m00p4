package absortio.m00p4.negocio.model.auxiliares;

public class UnidadTransporte {
    private Integer n;
    private Integer id;
    private Integer idTransportista;
    private String nombreTransportista;
    private String placa;
    private String modelo;
    private String marca;
    private String unidadVolumen;
    private Float volumenSoportado;
    private String unidadPeso;
    private Float pesoSoportado;
    private String estado;


    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
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

    public Float getVolumenSoportado() {
        return volumenSoportado;
    }

    public void setVolumenSoportado(Float volumenSoportado) {
        this.volumenSoportado = volumenSoportado;
    }

    public String getUnidadPeso() {
        return unidadPeso;
    }

    public void setUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
    }

    public Float getPesoSoportado() {
        return pesoSoportado;
    }

    public void setPesoSoportado(Float pesoSoportado) {
        this.pesoSoportado = pesoSoportado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
