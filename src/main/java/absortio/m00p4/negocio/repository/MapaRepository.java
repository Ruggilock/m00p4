package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.RolModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapaRepository extends JpaRepository<MapaModel, Integer> {
    MapaModel findFirstByOrderById();
}
