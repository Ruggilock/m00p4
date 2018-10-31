package absortio.m00p4.negocio.model;


import javax.persistence.*;

@Entity
@Table(name = "detallefacturaparcialmodel")
public class DetalleFacturaParcialModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detallefacturaparcial_iddetallefacturaparcial_seq")
    @SequenceGenerator(name = "detallefacturaparcial_iddetallefacturaparcial_seq", sequenceName = "detallefacturaparcial_iddetallefacturaparcial_seq", allocationSize = 1)

    @Column(name = "iddetallefacturaparcial")
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "idfacturaparcialmodel")
    private FacturaParcialModel facturaParcialModel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleFacturaParcialModel )) return false;
        return id != null && id.equals(((DetalleFacturaParcialModel) o).id);
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
    @Column(name ="idlote")
    private Integer lote;
    @Column(name = "iddetalledespacho" )
    private Integer idDetalleDespacho;





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FacturaParcialModel getFacturaParcialModel() {
        return facturaParcialModel;
    }

    public void setFacturaParcialModel(FacturaParcialModel facturaParcialModel) {
        this.facturaParcialModel = facturaParcialModel;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getLote() {
        return lote;
    }

    public void setLote(Integer lote) {
        this.lote = lote;
    }

    public Integer getIdDetalleDespacho() {
        return idDetalleDespacho;
    }

    public void setIdDetalleDespacho(Integer idDetalleDespacho) {
        this.idDetalleDespacho = idDetalleDespacho;
    }
}