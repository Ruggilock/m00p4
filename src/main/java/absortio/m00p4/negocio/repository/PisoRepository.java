package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.PisoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PisoRepository extends JpaRepository<PisoModel, Integer> {
    PisoModel getById(Integer id);
}
