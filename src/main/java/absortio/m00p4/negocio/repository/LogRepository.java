package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.LogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// AQUI SE HACEN LAS CONSULTAS QUERIES A LA BD

@Repository
public interface LogRepository extends JpaRepository<LogModel, Integer> {

    @Transactional
    LogModel getByBd(String bd);

    // Para update o delete se sigue lo sgte
    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "UPDATE log SET usuario = ?2, bd = 'java' WHERE (idlog = ?1)",nativeQuery = true)
    Integer updateUsuarioAndBDById(Integer id, String usuario);

    @Query(value =  "SELECT COALESCE(COUNT(l.bd),0) FROM log l " +
            "WHERE l.bd = ?1 ", nativeQuery = true)
    Integer hallarCantidadUsuariosByBD(String bd);

}
