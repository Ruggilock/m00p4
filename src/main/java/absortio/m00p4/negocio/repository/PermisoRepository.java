package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.PermisoModel;
import absortio.m00p4.negocio.model.RolModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermisoRepository extends JpaRepository<PermisoModel, Integer> {
    PermisoModel getByNombre(String nombre);
    @Query(value = "select * from permiso where idpermiso = any(select idpermiso from rol_permiso where idrol = ?1) ", nativeQuery = true)
    List<PermisoModel> findAllByIdRol(Integer idrol);
}

