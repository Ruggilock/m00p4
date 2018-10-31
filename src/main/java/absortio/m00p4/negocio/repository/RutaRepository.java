package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.RutaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RutaRepository extends JpaRepository<RutaModel, Integer> {
    @Query(value = "select * from ruta where iddespacho = ?1", nativeQuery = true)
    List<RutaModel> findAllByIdDespacho(Integer id);

}
