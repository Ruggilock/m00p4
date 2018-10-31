package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.MotivoOperacionKardexModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotivoOperacionKardexRepository extends JpaRepository<MotivoOperacionKardexModel, Integer> {

    List<MotivoOperacionKardexModel> findAllByTipooperacion_Id(Integer id);
    List<MotivoOperacionKardexModel> findAllByTipooperacion_Nombre(String id);
    MotivoOperacionKardexModel getByNombreAndTipooperacion_Id(String nombre, Integer id);
    MotivoOperacionKardexModel getByNombre(String nombre);
}
