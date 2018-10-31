package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.DetalleFacturaParcialModel;
import absortio.m00p4.negocio.model.IgvModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.ArrayList;

@Repository
public interface DetalleFacturaParcialRepository extends JpaRepository<DetalleFacturaParcialModel, Integer> {

    @Query(value = "select * from detallefacturaparcialmodel where idfacturaparcialmodel = ?1 ", nativeQuery = true)
    List<DetalleFacturaParcialModel> findAllByIdFacturaParcial(Integer idFacturaParcial);

    ArrayList<DetalleFacturaParcialModel> findAllByFacturaParcialModel_NoDocumento(String nodoc);
}
