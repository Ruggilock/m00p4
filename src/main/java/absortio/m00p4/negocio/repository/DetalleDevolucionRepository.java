package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.DetalleDevolucionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleDevolucionRepository extends JpaRepository<DetalleDevolucionModel, Integer> {

    @Query(value="select * from detalledevolucion where devolucion = ?1 ", nativeQuery = true)
    public List<DetalleDevolucionModel> findAllDetalleDevolucionByIdDevolucion(Integer id);

}
