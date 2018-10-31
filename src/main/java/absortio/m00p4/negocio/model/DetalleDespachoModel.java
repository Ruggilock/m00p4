package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "detalledespacho")
public class DetalleDespachoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="detalledespacho_iddetalledespacho_seq")
    @SequenceGenerator(name="detalledespacho_iddetalledespacho_seq", sequenceName="detalledespacho_iddetalledespacho_seq", allocationSize=1)
    @Column(name = "iddetalledespacho")
    private Integer id;

    
    @ManyToOne
    @JoinColumn(name = "iddetalleentrega")
    private DetalleEntregaModel iddetalleEntrega;

    @ManyToOne
    @JoinColumn(name = "iddespacho")
    private DespachoModel idDespacho;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleDespachoModel )) return false;
        return id != null && id.equals(((DetalleDespachoModel) o).id);
    }

    @Column(name ="cantidadatendida")
    private Integer cantidadAtendida;

    @Column(name ="estado")
    private String estado;

    @Column(name = "estadodespacho")
    private String estadoDespacho;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DetalleEntregaModel getIddetalleEntrega() {
        return iddetalleEntrega;
    }

    public void setIddetalleEntrega(DetalleEntregaModel iddetalleEntrega) {
        this.iddetalleEntrega = iddetalleEntrega;
    }

    public DespachoModel getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(DespachoModel idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Integer getCantidadAtendida() {
        return cantidadAtendida;
    }

    public void setCantidadAtendida(Integer cantidadAtendida) {
        this.cantidadAtendida = cantidadAtendida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoDespacho() {
        return estadoDespacho;
    }

    public void setEstadoDespacho(String estadoDespacho) {
        this.estadoDespacho = estadoDespacho;
    }
}