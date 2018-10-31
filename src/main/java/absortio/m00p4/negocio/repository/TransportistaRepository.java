package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.IgvModel;
import absortio.m00p4.negocio.model.TransportistaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// AQUI SE HACEN LAS CONSULTAS QUERIES A LA BD

@Repository
public interface TransportistaRepository extends JpaRepository<TransportistaModel, Integer> {
    @Transactional
    void deleteByDocumentoIdentidad(String documentoIdentidad);
    @Transactional
    TransportistaModel getByDocumentoIdentidad(String documentoIdentidad);
    @Transactional
    TransportistaModel getById(Integer id);
    @Transactional
    TransportistaModel getByNombreCompleto(String nombreCompleto);
    @Transactional
    List<TransportistaModel> findAllByTelefono(String telefono);
    // Los campos en las queries dependen de como se vayan a llamar en PostgreSQL
    // Para update o delete se sigue lo sgte


    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "UPDATE transportista SET estado = ?2 WHERE (idtransportista = ?1)",nativeQuery = true)
    Integer updateEstadoById(Integer id, String estado);

    @Query(value = "SELECT * FROM transportista t " +
            "WHERE UPPER(t.nombres) LIKE CONCAT('%', UPPER(?1),'%') OR " +
            "UPPER(t.apellidopaterno) LIKE CONCAT('%', UPPER(?1),'%') OR " +
            "UPPER(t.apellidomaterno) LIKE CONCAT('%', UPPER(?1),'%') ", nativeQuery = true)
    List<TransportistaModel> buscarPorCualquierNombre(String nombre);

    @Query(value = "SELECT * FROM transportista t " +
            "WHERE UPPER(t.documentoidentidad) LIKE CONCAT('%', UPPER(?1),'%')", nativeQuery = true)
    List<TransportistaModel> buscarPorDocumento(String documento);

    @Query(value =  "SELECT * FROM transportista t " +
                    "WHERE (UPPER(t.nombres) LIKE CONCAT('%', UPPER(?1),'%') OR " +
                    "UPPER(t.apellidopaterno) LIKE CONCAT('%', UPPER(?1),'%') OR " +
                    "UPPER(t.apellidomaterno) LIKE CONCAT('%', UPPER(?1),'%')) " +
                    "AND UPPER(t.documentoidentidad) LIKE CONCAT('%', UPPER(?2),'%') " +
                    "AND UPPER(t.telefono) LIKE CONCAT('%', UPPER(?3),'%') ", nativeQuery = true)
    List<TransportistaModel> buscarPorTodosFiltros(String nombre, String documento, String telefono);


}
