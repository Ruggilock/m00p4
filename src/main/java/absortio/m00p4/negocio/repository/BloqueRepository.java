package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.IgvModel;
import absortio.m00p4.negocio.model.RackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BloqueRepository extends JpaRepository<BloqueModel, Integer> {

    BloqueModel findFirstByOrderByIdentificador(String identificador);
    Collection<BloqueModel> getAllByIdRack(RackModel rackModel);
    BloqueModel getById(Integer id);
    BloqueModel getFirstByEstado(String estado);
    List<BloqueModel> findAllByEstado(String estado);
    List<BloqueModel> findAllByEstadoAndAndIdRack_IdSectorAlmacen_Id(String estado, Integer id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE BloqueModel c SET c.estado = 'ocupado' WHERE c.id = :id")
    Integer updateEstadoaOcupado(@Param("id") Integer id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE BloqueModel c SET c.estado = 'activo' WHERE c.id = :id")
    Integer updateEstadoaActivo(@Param("id") Integer id);

    List<BloqueModel> findAllByIdRack_IdSectorAlmacen_Nombre(String nombreAlmacen);

    @Query(value = "select * from bloque where posx = ?1 and posy = ?2 ", nativeQuery = true)
    List<BloqueModel> findAllByPosxAndPosy(Integer posX, Integer posY);
}
