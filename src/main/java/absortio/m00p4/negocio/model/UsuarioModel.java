package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.sql.Date;

@Entity(name = "UsuarioModel")
@Table(name = "usuario")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_idusuario_seq")
    @SequenceGenerator(name = "usuario_idusuario_seq", sequenceName = "usuario_idusuario_seq", allocationSize = 1)
    @Column(name = "idusuario")
    private Integer id;

    @Column(name = "documentoidentidad")
    private String documentoIdentidad;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidopaterno")
    private String apellidoPaterno;
    @Column(name = "apellidomaterno")
    private String apellidoMaterno;
    @Column(name = "correo")
    private String correo ;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "fechanacimiento")
    private Date fechaNacimiento;
    @Column(name = "contrasena")
    private String contrasena;
    @Column(name = "fechaultimaconexion")
    private Date fechaUltimaConexion;
    @Column(name = "genero")
    private String genero;
    @Column(name = "nombrecompleto")
    private String nombreCompleto;
    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "idrol")
    private RolModel idRol;

    public RolModel getIdRol() {
        return idRol;
    }

    public void setIdRol(RolModel idRol) {
        this.idRol = idRol;
    }

    @Override
    public String toString() {
        return "UsuarioModel{" +
                "id=" + id +
                ", documentoIdentidad='" + documentoIdentidad + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", contrasena='" + contrasena + '\'' +
                ", fechaUltimaConexion=" + fechaUltimaConexion +
                ", genero='" + genero + '\'' +
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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Date getFechaUltimaConexion() {
        return fechaUltimaConexion;
    }

    public void setFechaUltimaConexion(Date fechaUltimaConexion) {
        this.fechaUltimaConexion = fechaUltimaConexion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
