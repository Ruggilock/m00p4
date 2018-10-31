package absortio.m00p4.negocio.model.auxiliares;

import javafx.scene.control.Button;

public class ClienteVentas {
    private Integer n;
    private Integer id;
    private String documentoIdentidad;
    private String nombre;
    private String correo;
    private String telefono;
    private Button accion;

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

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Button getAccion() {
        return accion;
    }

    public void setAccion(Button accion) {
        this.accion = accion;
    }
}
