package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.RutaModel;
import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.repository.RutaRepository;
import absortio.m00p4.negocio.repository.TransportistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
// Aqui enlazamos los campos externos contra los queries de la BD

@Service
public class RutaService {
    private RutaRepository rutaRepository;
    @Autowired
    public RutaService(RutaRepository rutaRepository){
        this.rutaRepository = rutaRepository;
    }

    public void save( RutaModel rutaModel){ rutaRepository.save(rutaModel);}

    public List<RutaModel> findByIdDespacho(Integer id){
        return rutaRepository.findAllByIdDespacho(id);
    }


}



