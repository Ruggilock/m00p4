package absortio.m00p4.negocio.repository;


import absortio.m00p4.negocio.model.RolModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<RolModel, Integer> {
    RolModel getByIdAndNombre(Integer id, String nombre);
    List<RolModel> findAllByNombreIsStartingWith(String nombre);
    RolModel getByNombre(String nombre);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE RolModel c SET c.nombre = :nombre, c.descripcion = :descripcion  WHERE c.id = :rolId")
    Integer updateNombreAndDescripcion(@Param("rolId") int rolId, @Param("nombre") String nombre, @Param("descripcion") String descripcion);

    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "UPDATE rol SET estado = ?2 WHERE (idrol = ?1)",nativeQuery = true)
    Integer updateEstadoById(Integer id, String estado);
}
