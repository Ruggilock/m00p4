package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.ClienteModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.auxiliares.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {
    @Transactional
    void deleteByDocumentoIdentidad(String documentoIdentidad);

    ClienteModel getByDocumentoIdentidad(String documentoIdentidad);

    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "UPDATE cliente SET estado = ?2 WHERE (UPPER(documentoidentidad) = UPPER(?1))",nativeQuery = true)
    Integer updateEstadoById(String documento, String estado);

    @Query(value =  "SELECT * FROM cliente c " +
            "WHERE UPPER(c.nombres) LIKE CONCAT('%', UPPER(?1),'%') " +
            "AND UPPER(c.documentoidentidad) LIKE CONCAT('%', UPPER(?2),'%') ", nativeQuery = true)
    List<ClienteModel> buscarPorTodosFiltros(String nombre, String documento);

}
