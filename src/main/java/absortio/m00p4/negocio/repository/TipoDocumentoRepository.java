package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.ClienteModel;
import absortio.m00p4.negocio.model.TipoDocumentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumentoModel, Integer> {
    TipoDocumentoModel getByNombre(String tipodocumento);

    List<TipoDocumentoModel> findAllByEstado(String estado);
}
