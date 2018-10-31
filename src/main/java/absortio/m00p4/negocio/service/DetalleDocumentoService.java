package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.DetalleDocumentoModel;
import absortio.m00p4.negocio.model.DocumentoModel;
import absortio.m00p4.negocio.repository.DetalleDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleDocumentoService {
    private DetalleDocumentoRepository detalleDocumentoRepository;
    @Autowired
    public DetalleDocumentoService(DetalleDocumentoRepository detalleDocumentoRepository){
        this.detalleDocumentoRepository = detalleDocumentoRepository;
    }

    public List<DetalleDocumentoModel> findAllDetalleDocumento(){
        return detalleDocumentoRepository.findAll();
    }
    public ArrayList<DetalleDocumentoModel> findAllDetalleDocumentobyDocumento_Id(DocumentoModel idDocumento){
        return detalleDocumentoRepository.findAllByIdDocumento(idDocumento);
    }
    public void delete(Integer id){
        detalleDocumentoRepository.delete(id);
    }

    public void save( DetalleDocumentoModel detalleDocumentoModel){ detalleDocumentoRepository.save(detalleDocumentoModel);}



}
