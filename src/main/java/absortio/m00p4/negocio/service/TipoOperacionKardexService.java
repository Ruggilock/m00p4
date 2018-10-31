package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.TipoOperacionKardexModel;
import absortio.m00p4.negocio.repository.TipoOperacionKardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoOperacionKardexService {
    private TipoOperacionKardexRepository tipoOperacionKardexRepository;

    @Autowired
    public TipoOperacionKardexService(TipoOperacionKardexRepository rolRepository ){
        this.tipoOperacionKardexRepository = rolRepository;
    }

    public List<TipoOperacionKardexModel> findAllTipoOperacionKardex(){
        return tipoOperacionKardexRepository.findAll();
    }
    public void delete(Integer id){
        tipoOperacionKardexRepository.delete(id);
    }
    public TipoOperacionKardexModel save( TipoOperacionKardexModel rolModel){ return tipoOperacionKardexRepository.save(rolModel);}

    public TipoOperacionKardexModel getByNombre(String nombre){
        return tipoOperacionKardexRepository.getByNombre(nombre);
    }
}
