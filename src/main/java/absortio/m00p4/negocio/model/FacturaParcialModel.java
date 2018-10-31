package absortio.m00p4.negocio.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "facturaparcialmodel")
public class FacturaParcialModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "facturaparcial_idfacturaparcial_seq")
    @SequenceGenerator(name = "facturaparcial_idfacturaparcial_seq", sequenceName = "facturaparcial_idfacturaparcial_seq", allocationSize = 1)
    @Column(name = "idfacturaparcialmodel")
    private Integer id;

    @Column(name = "iddocumento")
    private Integer idDocumento;


    @OneToMany(mappedBy = "facturaParcialModel", cascade = CascadeType.ALL)
    private List<DetalleFacturaParcialModel> detalleFacturaParcialModels = new ArrayList<>();

    public void addFacturaParcial(DetalleFacturaParcialModel detalleFacturaParcialModel) {
        detalleFacturaParcialModels.add(detalleFacturaParcialModel);
        detalleFacturaParcialModel.setFacturaParcialModel(this);
    }

    @Column(name="subtotal")
    private Double subTotal;
    @Column(name="impuesto")
    private Double impuestos;
    @Column(name="flete")
    private Double Flete;
    @Column(name="descuento")
    private Double descuento;
    @Column(name="total")
    private Double total;
    @Column(name="estado")
    private String estado;
    @Column(name="nodocumento")
    private String noDocumento;
    @Column(name="fecha")
    private Date fecha;







    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(Double impuestos) {
        this.impuestos = impuestos;
    }

    public Double getFlete() {
        return Flete;
    }

    public void setFlete(Double flete) {
        Flete = flete;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<DetalleFacturaParcialModel> getDetalleFacturaParcialModels() {
        return detalleFacturaParcialModels;
    }

    public void setDetalleFacturaParcialModels(List<DetalleFacturaParcialModel> detalleFacturaParcialModels) {
        this.detalleFacturaParcialModels = detalleFacturaParcialModels;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getNoDocumento() {
        return noDocumento;
    }

    public void setNoDocumento(String noDocumento) {
        this.noDocumento = noDocumento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}