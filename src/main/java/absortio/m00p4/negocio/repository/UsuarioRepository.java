package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    Integer countByCorreoAndContrasena(String correo, String contrasena);
    UsuarioModel getByCorreoAndContrasena(String correo, String contrasena);

    @Transactional
    void deleteByDocumentoIdentidad(String documentoIdentidad);

    UsuarioModel getByDocumentoIdentidad(String documentoIdentidad);

    ArrayList<UsuarioModel> getByEstado(String estado);

    @Query(value = "SELECT * FROM  usuario u WHERE (u.nombrecompleto LIKE ?1%) AND " +
            " (u.documentoidentidad LIKE ?2%) AND (u.cargo LIKE ?3%) ",nativeQuery = true)
    ArrayList<UsuarioModel> findAllByNombresAndDocumentoIdentidadAndCargo(String nombres, String documentoIdentidad, Integer idcargo);

    @Query(value = "SELECT * FROM  usuario u WHERE (u.nombrecompleto LIKE ?1%) AND " +
            " (u.documentoidentidad LIKE ?2%)  ",nativeQuery = true)
    ArrayList<UsuarioModel> findAllByNombresAndDocumentoIdentidad(String nombres, String documentoIdentidad);

    @Query(value =  "SELECT * FROM usuario u " +
            "INNER JOIN rol r ON (u.idrol = r.idrol) " +
            "WHERE (UPPER(u.nombres) LIKE CONCAT('%', UPPER(?1),'%') OR " +
            "UPPER(u.apellidopaterno) LIKE CONCAT('%', UPPER(?1),'%') OR " +
            "UPPER(u.apellidomaterno) LIKE CONCAT('%', UPPER(?1),'%')) " +
            "AND UPPER(u.documentoidentidad) LIKE CONCAT('%', UPPER(?2),'%') " +
            "AND UPPER(r.nombre) LIKE CONCAT('%', UPPER(?3),'%') ", nativeQuery = true)
    List<UsuarioModel> buscarPorTodosFiltros(String nombre, String documento, String rol);

}
