package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import org.hibernate.dialect.Ingres9Dialect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Integer> {
    ProductoModel getByIdAndNombre(Integer id, String nombre);

    List<ProductoModel> findAllByNombreIsStartingWith(String nombre);

    ProductoModel getById(Integer id);
    ProductoModel getByCodigoBarras(String codigo);

    List<ProductoModel> findAllByEstadoNotLike(String estado);

    @Transactional
    void deleteProductoModelByCodigoBarras(String codigo);

    @Query(value = "SELECT * FROM  producto p WHERE (p.codigobarras LIKE ?1%) AND " +
            " (p.nombre LIKE ?2%) AND (p.marca LIKE ?3%) AND  (p.modelo LIKE ?4%)",nativeQuery = true)
    List<ProductoModel> findAllByCodigoBarrasNombreMarcaModelo(String codigo, String nombre, String marca, String modelo);

    @Query(value = "SELECT * FROM  producto p WHERE (p.codigobarras LIKE ?1%) AND " +
            " (p.nombre LIKE ?2%) AND (p.marca LIKE ?3%) AND  (p.modelo LIKE ?4% )AND (idcategoriaproducto=?5)",nativeQuery = true)
    List<ProductoModel> findAllByCodigoBarrasNombreMarcaModeloCategoriaProducto(String codigo, String nombre, String marca, String modelo, Integer idCategoria);

    @Query(value = "select h.preciounitario from producto p,historialPrecio h " +
            "where (p.codigobarras like ?1%) and " +
            " (p.idproducto like h.idproducto)  " +
            " order by h.fechaini desc " +
            " limit 1;",nativeQuery = true)
    Double hallarPrecio(String codigoBarras);


    @Query(value =  "SELECT DISTINCT pr.preciounitario FROM producto p " +
            "INNER JOIN precio pr ON (p.idproducto = pr.producto) " +
            "WHERE p.idproducto = ?1 AND pr.estado = 'activo' ", nativeQuery = true)
    Double buscarPorPrecioActual(Integer id);

    @Query(value =  "SELECT COALESCE(SUM(l.stock_disponible),0) FROM lote l " +
                    "WHERE l.idproducto = ?1 ", nativeQuery = true)
    Long hallarStockDisponibleById(Integer id);

    @Query(value =  "SELECT COALESCE (p.codigobarras,'noencontrado') FROM producto p " +
                    "WHERE p.idproducto = ?1 ", nativeQuery = true)
    String hallarCodigoById(Integer id);

    @Query(value =  "SELECT * FROM producto p, " +
                    "(SELECT SUM(l.stock_disponible) as suma, l.idproducto as idproducto " +
                    "FROM lote l GROUP BY l.idproducto " +
                    "HAVING SUM(l.stock_disponible) >0) as aux " +
                    "WHERE aux.suma > 0 AND p.idproducto = aux.idproducto ", nativeQuery = true)
    List<ProductoModel> findAllConStockDisponible();

    @Query(value =  "SELECT DISTINCT * FROM producto p, " +
            "(SELECT SUM(l.stock_disponible) as suma, l.idproducto as idproducto " +
            "FROM lote l GROUP BY l.idproducto " +
            "HAVING SUM(l.stock_disponible) >0) as aux, " +
            "categoriaproducto c "+
            "WHERE aux.suma > 0 AND p.idproducto = aux.idproducto " +
            "AND p.idcategoriaproducto = c.idcategoriaproducto " +
            "AND UPPER(p.codigobarras) LIKE CONCAT('%', UPPER(?1),'%') " +
            "AND UPPER(p.nombre) LIKE CONCAT('%', UPPER(?2),'%')  " +
            "AND UPPER(c.nombre) LIKE CONCAT('%', UPPER(?3),'%') " +
            "AND UPPER(p.marca) LIKE CONCAT('%', UPPER(?4),'%') " +
            "AND UPPER(p.modelo) LIKE CONCAT('%', UPPER(?5),'%') ", nativeQuery = true)
    List<ProductoModel> findByAllFiltrosConStockDisponible(String codbarras, String nombre, String categoria, String marca, String modelo);


    @Query(value =  "SELECT DISTINCT * FROM producto p " +
                    "INNER JOIN categoriaproducto c ON (p.idcategoriaproducto = c.idcategoriaproducto) " +
                    "WHERE UPPER(p.codigobarras) LIKE CONCAT('%', UPPER(?1),'%') " +
                    "AND UPPER(p.nombre) LIKE CONCAT('%', UPPER(?2),'%')  " +
                    "AND UPPER(c.nombre) LIKE CONCAT('%', UPPER(?3),'%') " +
                    "AND UPPER(p.marca) LIKE CONCAT('%', UPPER(?4),'%') " +
                    "AND UPPER(p.modelo) LIKE CONCAT('%', UPPER(?5),'%') ", nativeQuery = true)
    List<ProductoModel> findByAllFiltros(String codbarras, String nombre, String categoria, String marca, String modelo);

    @Query(value = "select distinct marca from producto ", nativeQuery = true)
    List<String> findAllMarca();

    @Query(value = "select distinct modelo from producto ", nativeQuery = true)
    List<String> findAllModelo();


}
