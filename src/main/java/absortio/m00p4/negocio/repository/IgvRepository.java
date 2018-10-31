package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.ClienteModel;
import absortio.m00p4.negocio.model.IgvModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IgvRepository extends JpaRepository<IgvModel, Integer> {
    IgvModel findByEstado(String estado);
}
