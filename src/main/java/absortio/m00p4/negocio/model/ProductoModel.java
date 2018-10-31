package absortio.m00p4.negocio.model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "producto")
public class ProductoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="producto_idproducto_seq")
    @SequenceGenerator(name="producto_idproducto_seq", sequenceName="producto_idproducto_seq", allocationSize=1)
    @Column(name = "idproducto")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idcategoriaproducto")
    private CategoriaProductoModel categoriaProducto;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigobarras", unique = true)
    private String codigoBarras;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "marca")
    private String marca;
    @Column(name = "modelo")
    private String modelo;
    @Column(name = "unidadpeso")
    private String unidadPeso;
    @Column(name = "peso")
    private Double peso;
    @Column(name = "unidadvolumen")
    private String unidadVolumen;
    @Column(name = "volumen")
    private Double volumen;
    @Column(name = "unidadventa")
    private String unidadVenta;
    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "producto",
            cascade = CascadeType.MERGE)
    private List<LoteModel> lotes = new ArrayList<>();


    public void addLote(LoteModel loteModel){
        lotes.add(loteModel);
        loteModel.setProducto(this);
    }

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<PrecioModel> precios;
    public void addPrecio(PrecioModel precio) {
        precios.add(precio);
        precio.setProducto (this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoriaProductoModel getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(CategoriaProductoModel categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getUnidadPeso() {
        return unidadPeso;
    }

    public void setUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getUnidadVolumen() {
        return unidadVolumen;
    }

    public void setUnidadVolumen(String unidadVolumen) {
        this.unidadVolumen = unidadVolumen;
    }

    public Double getVolumen() {
        return volumen;
    }

    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    public String getUnidadVenta() {
        return unidadVenta;
    }

    public void setUnidadVenta(String unidadVenta) {
        this.unidadVenta = unidadVenta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<PrecioModel> getPrecios() {
        return precios;
    }

    public void setPrecios(List<PrecioModel> precios) {
        this.precios = precios;
    }

    public ProductoModel() {
        this.precios = new ArrayList<> (  );
    }


}
