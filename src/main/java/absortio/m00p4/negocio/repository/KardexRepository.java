package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.KardexModel;
import absortio.m00p4.negocio.model.ProductoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KardexRepository extends JpaRepository<KardexModel,Integer>{
    //@Query("SELECT DISTINCT KardexModel.bloque FROM KardexModel")
    //List<BloqueModel> findAllBloquesDistinct();

    @Query(value = "SELECT DISTINCT idbloque FROM  kardex WHERE idlote=?1",nativeQuery = true)
    List<Integer> findAllDistinctForLote(Integer id);

    @Query(value = "SELECT * FROM kardex t WHERE t.idbloque=?1 ORDER BY t.fecha",nativeQuery = true)
    List<KardexModel> findOperacion(Integer id);

    List<KardexModel> findAllByMotivooperacion_Tipooperacion_NombreAndMotivooperacion_Nombre(String tipooperacion, String motivooperacion);

    @Query(value =  "SELECT DISTINCT * FROM kardex k " +
                    "INNER JOIN lote l ON (l.idlote = k.idlote) " +
                    "INNER JOIN producto p ON (l.idproducto = p.idproducto) " +
                    "INNER JOIN bloque b ON (b.idbloque = k.idbloque) " +
                    "INNER JOIN rack r ON (r.idrack = b.idrack) " +
                    "INNER JOIN sectoralmacen s ON (r.idsectoralmacen = s.idsectoralmacen) " +
                    "INNER JOIN motivooperacion m ON (m.idmotivooperacion = k.idmotivooperacion) "+
                    "INNER JOIN tipooperacion t ON (t.idtipooperacion = m.idtipooperacion) " +
                    "WHERE UPPER(p.codigobarras) LIKE CONCAT('%', UPPER(?1),'%') "+
                    "AND UPPER(p.nombre) LIKE CONCAT('%', UPPER(?2),'%') " +
                    "AND UPPER(t.nombre) LIKE CONCAT('%', UPPER(?3),'%') " +
                    "AND UPPER(m.nombre) LIKE CONCAT('%', UPPER(?4),'%') " +
                    "AND UPPER(s.nombre) LIKE CONCAT('%', UPPER(?5),'%') " +
                    "AND UPPER(r.identificador) LIKE CONCAT('%', UPPER(?6),'%') "+
                    "AND (k.fecha >= to_date(?7, 'YYYY-MM-DD') AND k.fecha <= to_date(?8, 'YYYY-MM-DD') ) " +
                    "AND (k.cantidad >= ?9 AND k.cantidad <= ?10) ", nativeQuery = true)
    List<KardexModel> buscarPorTodosFiltros(String codbarras, String prod, String tipo, String motivo, String sector, String rack, String fechaIni, String fechaFin, Integer cantIni, Integer cantFin);

    @Query(value =  "SELECT * FROM kardex k " +
                    "ORDER BY k.idlote, k.idbloque ASC ",nativeQuery = true)
    List<KardexModel> findAllOrdenados();

    @Query(value =  "SELECT ingreso.cantidad - salida.cantidad " +
                    "FROM (SELECT COALESCE(SUM(k.cantidad), 0) as cantidad FROM kardex k " +
                          "INNER JOIN motivooperacion m ON (k.idmotivooperacion = m.idmotivooperacion) " +
                          "INNER JOIN tipooperacion t ON (t.idtipooperacion = m.idtipooperacion) " +
                          "WHERE LOWER(t.nombre) = 'ingreso' " +
                          "AND k.idlote = ?1 AND k.idbloque = ?2) as ingreso, "+

 	                      "(SELECT COALESCE(SUM(k.cantidad), 0) as cantidad FROM kardex k " +
                          "INNER JOIN motivooperacion m ON (k.idmotivooperacion = m.idmotivooperacion) " +
                          "INNER JOIN tipooperacion t ON (t.idtipooperacion = m.idtipooperacion) " +
                          "WHERE LOWER(t.nombre) = 'salida' " +
                          "AND k.idlote = ?1 AND k.idbloque = ?2) as salida ",nativeQuery = true)
    Long hallarStockByLoteYBloque(Integer idLote, Integer idBloque);


    @Query(value =  "SELECT DISTINCT * FROM kardex k " +
                    "INNER JOIN lote l ON (l.idlote = k.idlote) " +
                    "INNER JOIN producto p ON (l.idproducto = p.idproducto) " +
                    "INNER JOIN categoriaproducto c ON (c.idcategoriaproducto = p.idcategoriaproducto) " +
                    "INNER JOIN bloque b ON (b.idbloque = k.idbloque) " +
                    "INNER JOIN rack r ON (r.idrack = b.idrack) " +
                    "INNER JOIN sectoralmacen s ON (r.idsectoralmacen = s.idsectoralmacen) " +
                    "WHERE UPPER(l.observacion) LIKE CONCAT('%', UPPER(?1),'%') "+
                    "AND UPPER(p.codigobarras) LIKE CONCAT('%', UPPER(?2),'%') " +
                    "AND UPPER(p.nombre) LIKE CONCAT('%', UPPER(?3),'%') " +
                    "AND UPPER(c.nombre) LIKE CONCAT('%', UPPER(?4),'%') " +
                    "AND UPPER(p.marca) LIKE CONCAT('%', UPPER(?5),'%') " +
                    "AND UPPER(p.modelo) LIKE CONCAT('%', UPPER(?6),'%') " +
                    "AND UPPER(s.nombre) LIKE CONCAT('%', UPPER(?7),'%') " +
                    "AND UPPER(r.identificador) LIKE CONCAT('%', UPPER(?8),'%') ", nativeQuery = true)
    List<KardexModel> buscarPorTodosFiltrosV2(String lote, String codbarras, String producto, String categoria, String marca, String modelo, String seccion, String rack);

    @Query(value = "SELECT DISTINCT k.idbloque FROM kardex k " +
            "INNER JOIN bloque b ON (b.idbloque = k.idbloque) "+
            "INNER JOIN rack r ON (r.idrack = b.idrack) "+
            "INNER JOIN sectoralmacen s ON (r.idsectoralmacen = s.idsectoralmacen) " +
            "WHERE UPPER(s.nombre) LIKE CONCAT('%', UPPER(?2),'%') " +
            "AND k.idlote = ?1 ",nativeQuery = true)
    List<Integer>findAllDistinctforLoteByAlmacen(Integer idlote, String nombreAlmacen);


    @Query(value = "SELECT DISTINCT s.nombre FROM kardex k "+
            "INNER JOIN bloque b ON (b.idbloque = k.idbloque) "+
            "INNER JOIN rack r ON (r.idrack = b.idrack) "+
            "INNER JOIN sectoralmacen s ON (r.idsectoralmacen = s.idsectoralmacen) "+
            "WHERE k.idlote = ?1 AND k.stockfisico > 0", nativeQuery = true)
    List<String>findAllAlmacenesWithLote(Integer idlote);

    @Query(value = "SELECT * " +
            "FROM kardex k " +
            "WHERE k.idbloque= ?1 " +
            "ORDER BY fecha DESC; ", nativeQuery = true)
    List<KardexModel>findHistoricoLoteInBloque(Integer idbloque);

}
