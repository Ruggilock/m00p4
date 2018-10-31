package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.RutaCoordenadaModel;
import absortio.m00p4.negocio.repository.RutaCoordenadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RutaCoordenadaService {
    private RutaCoordenadaRepository rutaCoordenadaRepository;

    @Autowired
    public RutaCoordenadaService(RutaCoordenadaRepository rutaCoordenadaRepository){
        this.rutaCoordenadaRepository = rutaCoordenadaRepository;
    }

    public void save( RutaCoordenadaModel rutaCoordenadaModel){ rutaCoordenadaRepository.save(rutaCoordenadaModel);}

    public List<RutaCoordenadaModel> findAllByIdRutaPaso(Integer id){
        return rutaCoordenadaRepository.findAllByIdRutaPaso(id);
    }
}
