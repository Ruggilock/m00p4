package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.RutaPasoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaPasoRepository extends JpaRepository<RutaPasoModel, Integer> {
    @Query(value = "select * from rutapaso where idruta = ?1", nativeQuery = true)
    List<RutaPasoModel> findAllByIdRuta(Integer id);
}
