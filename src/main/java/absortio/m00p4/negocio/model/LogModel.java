package absortio.m00p4.negocio.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log")      // Se coloca entre "" el nombre de la tabla en la BD
public class LogModel {

    // Creamos un id consecutivo; para ello, debemos asegurarnos que en BD este marcado como SERIAL
    //https://stackoverflow.com/questions/41791802/autoincrement-id-postgresql-and-spring-boot-data-jpa
    @Id
    @Column(name = "idlog")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha")
    private Date fecha;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "tabla")
    private String tabla;
    @Column(name = "idtabla")
    private String idtabla;
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "bd")
    private String bd;


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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getIdtabla() {
        return idtabla;
    }

    public void setIdtabla(String idtabla) {
        this.idtabla = idtabla;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getBd() {
        return bd;
    }

    public void setBd(String bd) {
        this.bd = bd;
    }
}