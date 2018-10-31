package absortio.m00p4.negocio.repository;


import absortio.m00p4.negocio.model.DocumentoModel;
import absortio.m00p4.negocio.service.DocumentoService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<DocumentoModel, Integer> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE DocumentoModel c SET c.noDocumento = :noDoc WHERE c.id = :idventa")
    Integer setNoDoc(@Param("idventa") int idventa, @Param("noDoc") String noDoc);
    DocumentoModel getByNoDocumento(String noDocumento);

    @Query(value =  "SELECT * FROM documento d " +
                    "INNER JOIN tipodocumento t ON (t.idtipodocumento = d.idtipodocumento) " +
                    "INNER JOIN cliente c ON (c.idcliente = d.idcliente) " +
                    "WHERE UPPER(c.nombres) LIKE CONCAT('%', UPPER(?1),'%') " +
                    "AND UPPER(d.nodocumento) LIKE CONCAT('%', UPPER(?2),'%') " +
                    "AND (d.fechaemision >= to_date(?3, 'YYYY-MM-DD') AND d.fechaemision <= to_date(?4, 'YYYY-MM-DD') ) " +
                    "AND UPPER(d.estado) LIKE CONCAT('%', UPPER(?5),'%') " +
                    "AND UPPER(t.nombre) LIKE CONCAT('%', UPPER(?6),'%') " +
                    "AND (d.montototal >= ?7 AND d.montototal <= ?8) ", nativeQuery = true)
    List<DocumentoModel> findByTodosFiltros(String cliente, String documento, String fechaIni, String fechaFin, String estado, String tipoDoc, Double cantIni, Double cantFin);


    DocumentoModel getByNoDocumentoAndEstado(String nodoc, String estado);

    DocumentoModel getById(Integer id);
}
