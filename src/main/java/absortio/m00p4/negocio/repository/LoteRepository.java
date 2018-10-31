package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.LoteModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.RackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<LoteModel, Integer> {
    //LoteModel findFirstByOrderByIdentificador(String identificador);
    //Collection<LoteModel> getAllByIdProducto(ProductoModel rackModel);

    LoteModel findById(Integer id);
    LoteModel getById(Integer id);


    @Query(value =  "SELECT * FROM lote l " +
                    "WHERE l.idproducto = ?1 AND l.stock_disponible >0" +
                    "ORDER BY l.fecha_adquisicion ASC NULLS LAST ", nativeQuery = true)
    List<LoteModel> hallarLotesById(Integer id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE LoteModel c SET c.stockDisponible = :stockDisponibleActualizado WHERE c.id = :idlote")
    Integer salidaMerma(@Param("idlote") int idlote, @Param("stockDisponibleActualizado") Integer stockDisponibleActualizado);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE LoteModel c SET c.stockMerma = :stockMermaActualizado WHERE c.id = :idlote")
    Integer aumentoMerma(@Param("idlote") int idlote, @Param("stockMermaActualizado") Integer stockMermaActualizado);

    List<LoteModel> findAllByProducto_Id(Integer id);
}
