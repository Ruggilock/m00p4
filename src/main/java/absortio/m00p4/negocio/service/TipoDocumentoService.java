package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.TipoDocumentoModel;
import absortio.m00p4.negocio.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDocumentoService {
    private TipoDocumentoRepository tipoDocumentoRepository;
    @Autowired
    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository){
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public List<TipoDocumentoModel> findAllTipoDocumento(){

        return tipoDocumentoRepository.findAll();
    }
    public void delete(Integer id){
        tipoDocumentoRepository.delete(id);
    }
    public void save( TipoDocumentoModel tipoDocumentoModel){ tipoDocumentoRepository.save(tipoDocumentoModel);}
    public TipoDocumentoModel getTipodocumento(String tipoDocumento){
        return tipoDocumentoRepository.getByNombre(tipoDocumento);
    }

    public List<TipoDocumentoModel> findAllByEstado (String estado){
        return tipoDocumentoRepository.findAllByEstado(estado);
    }

}
