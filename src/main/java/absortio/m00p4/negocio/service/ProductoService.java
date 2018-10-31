package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.model.LoteModel;
import absortio.m00p4.negocio.model.PrecioModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.repository.PrecioRepository;
import absortio.m00p4.negocio.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductoService {

    private final static String INACTIVO="inactivo";
    private final static String ACTIVO="activo";

    private ProductoRepository productoRepository;
    
    @Autowired
    private PrecioService precioService;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ProductoModel getById(Integer id){
        return productoRepository.getById(id);
    }
    public void deleteProductoModelByCodigoBarras(String codigo) {
        ProductoModel producto =productoRepository.getByCodigoBarras ( codigo );
        producto.setEstado (INACTIVO);
        productoRepository.save(producto);
    }

    public List<ProductoModel> findAllProducto() {
        return productoRepository.findAllByEstadoNotLike (INACTIVO);
    }

    public ProductoModel save(ProductoModel productoModel) {
        productoModel.setEstado ( ACTIVO );
        return productoRepository.save ( productoModel );
    }

    public ProductoModel getByIdAndNombre(Integer id, String nombre) {
        return productoRepository.getByIdAndNombre ( id, nombre );
    }

    public ProductoModel obtenerProducto(String codigo) {
        return productoRepository.getByCodigoBarras ( codigo );
    }



    public List<ProductoModel> filtrarProductostodos(String nombre, String codigo, String marca, String modelo, CategoriaProductoModel categoria) {
        if (marca.equals ( "-Seleccione-" )) marca="";
        if (modelo.equals ( "-Seleccione-" )) modelo="";
        if (categoria!=null) {
            return productoRepository.findAllByCodigoBarrasNombreMarcaModeloCategoriaProducto ( codigo,nombre,marca,modelo,categoria.getId () );
        } else {
            return productoRepository.findAllByCodigoBarrasNombreMarcaModelo (  codigo,nombre,marca,modelo);
        }
    }

    public List<ProductoModel> filtrarNombre(String nombre){
        return productoRepository.findAllByNombreIsStartingWith(nombre);
    }

    public Double hallaPrecio(String codigo){
        return productoRepository.hallarPrecio(codigo);
    }
    public Double obtenerPrecio(String codigo){
        Double precioaux=0.0 ;
        ProductoModel producto =productoRepository.getByCodigoBarras ( codigo );
        for ( int i=0;i< producto.getPrecios ().size ();i++ ){
            if (producto.getPrecios ().get ( i ).getEstado ().equals ( ACTIVO )){
                return producto.getPrecios ().get ( i ).getPrecio ();
            }
        }
        return precioaux;
    }

    public Double getPrecio(String codigo){
        ProductoModel producto =productoRepository.getByCodigoBarras ( codigo );
        PrecioModel precio=precioService.getByProducto_IdAndEstadoLike ( producto.getId (),ACTIVO ) ;
        return precio.getPrecio ();
    }

    public PrecioModel actualizarPrecio (String codigo, Double precio){
        ProductoModel producto =productoRepository.getByCodigoBarras ( codigo );
        PrecioModel precioAnterior=precioService.getByProducto_IdAndEstadoLike ( producto.getId (),ACTIVO ) ;
        precioAnterior.setEstado ( INACTIVO );
        precioAnterior.setFechaFin ( new Date () );
        precioService.save ( precioAnterior );
        PrecioModel precioActual= new PrecioModel ( precio,new Date () , ACTIVO,producto);
       // precioService.save (precioActual);
        return precioActual ;
    }

    public int hallarIdByCodigoBarras(String codigo){
        return productoRepository.getByCodigoBarras(codigo).getId();
    }

    public  String hallarNombreCategoria(String codigo){
        return productoRepository.getByCodigoBarras(codigo).getCategoriaProducto().getNombre();
    }

    public Double buscarPrecioActualById(Integer id) { return productoRepository.buscarPorPrecioActual(id) ;}

    public ProductoModel hallarProductoXid(Integer id){
        return productoRepository.findOne(id);
    }
    public Integer hallarStockDisponibleById(Integer id) {
        Integer cantidad = (int) (long) productoRepository.hallarStockDisponibleById(id);
        return cantidad ;
    }

    public String hallarCodigoById(Integer id) {
        //return productoRepository.hallarCodigoById(id);
        return productoRepository.getOne(id).getCodigoBarras();
    }

    public List<ProductoModel> findAllConStockDisponible(){
        return productoRepository.findAllConStockDisponible();
    }

    public List<ProductoModel> findByAllFiltrosConStockDisponible(String codbarras, String nombre, String categoria, String marca, String modelo){
        return productoRepository.findByAllFiltrosConStockDisponible(codbarras, nombre, categoria, marca, modelo);
    }

    public List<ProductoModel> findByAllFiltros(String codbarras, String nombre, String categoria, String marca, String modelo){
        return productoRepository.findByAllFiltros(codbarras, nombre, categoria, marca, modelo);
    }

    public List<String> findAllMarca() {
        return productoRepository.findAllMarca();
    }

    public List<String> findAllModelo() {
        return productoRepository.findAllModelo();
    }


}
