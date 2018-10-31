package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.DevolucionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevolucionRepository extends JpaRepository<DevolucionModel, Integer> {
    DevolucionModel getByNodevolucion(String nodevolucion);

    List<DevolucionModel> findAllByDocumento_Id(Integer docid);
}
