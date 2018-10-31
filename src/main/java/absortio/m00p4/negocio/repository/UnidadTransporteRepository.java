package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.UnidadTransporteModel;
import absortio.m00p4.negocio.model.auxiliares.UnidadTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// AQUI SE HACEN LAS CONSULTAS QUERIES A LA BD

@Repository
public interface UnidadTransporteRepository extends JpaRepository<UnidadTransporteModel, Integer> {
    @Transactional
    void deleteByPlaca(String placa);

    @Transactional
    UnidadTransporteModel getByPlaca(String placa);

    @Transactional
    UnidadTransporteModel getById(Integer id);
    //@Transactional
    //TransportistaModel getByIdTransportista(Integer idTransportista);

    // Los campos en las queries dependen de como se vayan a llamar en PostgreSQL
    // Para update o delete se sigue lo sgte
    @Modifying
    @Transactional(readOnly=false)
    @Query(value = "UPDATE unidadtransporte SET estado = ?2 WHERE (idunidadtransporte = ?1)",nativeQuery = true)
    Integer updateEstadoById(Integer id, String estado);



}
