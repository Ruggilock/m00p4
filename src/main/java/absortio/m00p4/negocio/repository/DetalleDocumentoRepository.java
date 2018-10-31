package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.DetalleDespachoModel;
import absortio.m00p4.negocio.model.DetalleDocumentoModel;
import absortio.m00p4.negocio.model.DocumentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface DetalleDocumentoRepository extends JpaRepository<DetalleDocumentoModel, Integer> {

    ArrayList<DetalleDocumentoModel> findAllByIdDocumento(DocumentoModel idDocumento);
}
