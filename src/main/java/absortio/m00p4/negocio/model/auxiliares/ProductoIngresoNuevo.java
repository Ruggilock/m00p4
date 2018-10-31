package absortio.m00p4.negocio.model.auxiliares;

import javafx.scene.control.Button;

public class ProductoIngresoNuevo {
    private Integer id;
    private Integer n;
    private Integer codigo;
    private String nombre;
    private String marca;
    private String modelo;
    private Button seleccionar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Button getSeleccionar() {
        return seleccionar;
    }

    public void setSeleccionar(Button seleccionar) {
        this.seleccionar = seleccionar;
    }
}
