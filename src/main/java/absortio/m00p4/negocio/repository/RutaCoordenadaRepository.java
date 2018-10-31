package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.RutaCoordenadaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaCoordenadaRepository extends JpaRepository<RutaCoordenadaModel, Integer> {
    @Query(value = "select * from rutacoordenada where idrutapaso = ?1", nativeQuery = true)
    List<RutaCoordenadaModel> findAllByIdRutaPaso(Integer id);
}
