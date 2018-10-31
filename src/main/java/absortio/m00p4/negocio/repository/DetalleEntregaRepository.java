package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.DetalleDespachoModel;
import absortio.m00p4.negocio.model.DetalleDocumentoModel;
import absortio.m00p4.negocio.model.DetalleEntregaModel;
import absortio.m00p4.negocio.model.ProductoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleEntregaRepository extends JpaRepository<DetalleEntregaModel, Integer> {

    DetalleEntregaModel getById(Integer id);

 List<DetalleEntregaModel> findAllByIdDetalleDocumento(DetalleDocumentoModel iddetalledocumento);


    @Query(value = "SELECT * FROM  detalleentrega d WHERE (d.estadoentrega LIKE ?1%)",nativeQuery = true)
    List<DetalleEntregaModel> findAllByEstadoEntrega(String estado);



}
