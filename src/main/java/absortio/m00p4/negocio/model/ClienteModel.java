package absortio.m00p4.negocio.model;


import javax.persistence.*;

@Entity
@Table(name = "cliente")
public class ClienteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cliente_idcliente_seq")
    @SequenceGenerator(name="cliente_idcliente_seq", sequenceName="cliente_idcliente_seq", allocationSize=1)
    @Column(name = "idcliente")
    private Integer id;

    @Column(name = "documentoidentidad", unique = true)
    private String documentoIdentidad;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "correo")
    private String correo;
    @Column(name = "estado")
    private String estado;

    @Column(name = "contacto")
    private String contacto;
    @Column(name = "tipodocumento")
    private String tipoDocumento;

    @Override
    public String toString() {
        return "ClienteModel{" +
                "id=" + id +
                ", documentoIdentidad='" + documentoIdentidad + '\'' +
                ", nombres='" + nombres + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", estado='" + estado + '\'' +
                ", contacto='" + contacto + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTipoDocumento() { return tipoDocumento; }

    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
}