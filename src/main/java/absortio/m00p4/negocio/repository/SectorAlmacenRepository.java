package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.SectorAlmacenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SectorAlmacenRepository extends JpaRepository<SectorAlmacenModel, Integer> {
    @Query(value="select * from sectorAlmacen where nombre like ?1% and tipo like ?2%",nativeQuery = true)
    List<SectorAlmacenModel> findAllByNombreAndTipo(String nombre, String tipo);

    SectorAlmacenModel findFirstByOrderById();
    SectorAlmacenModel getByIdentificador(String identificador);
    Collection<SectorAlmacenModel> getAllByIdMapa(MapaModel mapaModel);

    SectorAlmacenModel getByNombre(String id);
}
