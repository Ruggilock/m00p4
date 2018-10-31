package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.DespachoModel;
import absortio.m00p4.negocio.model.RackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public interface DespachoRepository extends JpaRepository<DespachoModel, Integer> {
    DespachoModel findById(Integer id );

    ArrayList<DespachoModel>findByEstadoDespacho(String porConfirmar);

    @Query(value =  "SELECT DISTINCT * FROM despacho d " +
                    "WHERE UPPER(d.sobrenombre) LIKE CONCAT('%', UPPER(?1),'%') "+
                    "AND UPPER(d.estadodespacho) LIKE CONCAT('%', UPPER(?2),'%') " +
                    "AND (d.fecha >= to_date(?3, 'YYYY-MM-DD'))  ", nativeQuery = true)
    List<DespachoModel> buscarPorTodosFiltros(String sobrenombre, String estado, String fecha);


    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "DELETE FROM despacho WHERE (estadodespacho ="+"por atender"+" )",nativeQuery = true)
    void deleteAllByEstadoDespacho();



    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "DELETE FROM despacho d WHERE d.iddespacho = ?1",nativeQuery = true)
    void deleteDespachoByIdDespacho(Integer id);

    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "DELETE FROM detalledespacho i WHERE i.iddespacho = ?1",nativeQuery = true)
    void deleteDetalleDespachoByIdDespacho(Integer id);

    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "DELETE FROM ruta r WHERE r.iddespacho = ?1",nativeQuery = true)
    void deleteRutaByIdDespacho(Integer id);

    @Query(value = "SELECT DISTINCT idrutapaso FROM rutapaso WHERE idruta=?1",nativeQuery = true)
    List<Integer> findAllDistinctIdRutapasoByIdruta(Integer idruta);

    @Query(value = "SELECT DISTINCT idruta FROM ruta WHERE iddespacho=?1",nativeQuery = true)
    List<Integer> findAllDistinctIdRutaByIdDespacho(Integer iddespacho);

    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "DELETE FROM rutacoordenada r WHERE r.idrutapaso = ?1",nativeQuery = true)
    void deleteAllRutaCoordenadaByIdRutaPaso(Integer idrutapaso);

    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "DELETE FROM rutapaso r WHERE r.idrutapaso = ?1",nativeQuery = true)
    void deleteRutaPasoByIdRutaPaso(Integer idrutapaso);


}
