package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.RackModel;
import absortio.m00p4.negocio.model.SectorAlmacenModel;
import absortio.m00p4.negocio.service.SectorAlmacenService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RackRepository extends JpaRepository<RackModel, Integer> {
    RackModel findByIdentificador(String identificador);
    Collection<RackModel> getAllByIdSectorAlmacen (SectorAlmacenModel sectorAlmacen);
    List<RackModel> getByIdSectorAlmacen_Nombre(String nombre);
}
