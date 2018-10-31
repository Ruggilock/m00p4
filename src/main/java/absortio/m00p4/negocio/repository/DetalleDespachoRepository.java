package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.DespachoModel;
import absortio.m00p4.negocio.model.DetalleDespachoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface DetalleDespachoRepository extends JpaRepository<DetalleDespachoModel, Integer> {

    @Query(value =  "SELECT * FROM detalledespacho d " +
            "WHERE d.iddespacho = ?1 ", nativeQuery = true)
    List<DetalleDespachoModel> findAllDetalleDespachoByIdDespacho(Integer id);

    List<DetalleDespachoModel> findAllByIdDespacho(DespachoModel despachoModel);

    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "DELETE  FROM detalledespacho WHERE (iddespacho = ?1 )",nativeQuery = true)
    void updateEstadoDetalleDespacho(Integer idDespacho);

    DetalleDespachoModel getById(Integer id);

}
