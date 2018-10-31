package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.FacturaParcialModel;
import absortio.m00p4.negocio.model.IgvModel;
import absortio.m00p4.negocio.model.ProductoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaParcialRepository extends JpaRepository<FacturaParcialModel, Integer> {

    @Query(value = "SELECT * FROM facturaparcialmodel f WHERE (f.iddocumento = ?1)",nativeQuery = true)
    List<FacturaParcialModel> findAllByIdDocumento(Integer idDocumento);


    List<FacturaParcialModel> findAllByNoDocumento(String nodoc);

    FacturaParcialModel getByNoDocumento(String nodoc);
}
