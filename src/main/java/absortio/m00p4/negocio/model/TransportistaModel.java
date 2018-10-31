package absortio.m00p4.negocio.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transportista")      // Se coloca entre "" el nombre de la tabla en la BD
public class TransportistaModel {

    // Creamos un id consecutivo; para ello, debemos asegurarnos que en BD este marcado como SERIAL
    //https://stackoverflow.com/questions/41791802/autoincrement-id-postgresql-and-spring-boot-data-jpa
    @Id
    @Column(name = "idtransportista")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "documentoidentidad")
    private String documentoIdentidad;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidopaterno")
    private String apellidoPaterno;
    @Column(name = "apellidomaterno")
    private String apellidoMaterno;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "nombrecompleto")
    private String nombreCompleto;
    @Column(name = "tipodocumento")
    private String tipoDocumento;
    @Column(name = "estado")
    private String estado;



    @OneToMany(mappedBy = "idTransportista", cascade = CascadeType.ALL)
    private List<DespachoModel> despachos= new ArrayList<>();

    public void addDespacho(DespachoModel despachoModel) {
        despachos.add(despachoModel);
        despachoModel.setIdTransportista(this);
    }

    @Override
    public String toString() {
        return "TransportistaModel{" +
                //"id=" + id +
                ", documentoIdentidad='" + documentoIdentidad + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", telefono='" + telefono + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
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

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipoDocumento() { return tipoDocumento; }

    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }
}