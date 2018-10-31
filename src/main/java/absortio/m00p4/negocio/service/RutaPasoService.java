package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.RutaPasoModel;
import absortio.m00p4.negocio.repository.RutaPasoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RutaPasoService {
    private RutaPasoRepository rutaPasoRepository;

    @Autowired
    public RutaPasoService(RutaPasoRepository rutaPasoRepository){
        this.rutaPasoRepository = rutaPasoRepository;
    }

    public void save( RutaPasoModel rutaPasoModel){ rutaPasoRepository.save(rutaPasoModel);}

    public List<RutaPasoModel> findAllByRutaId(Integer id) {
        return rutaPasoRepository.findAllByIdRuta(id);
    }
}
