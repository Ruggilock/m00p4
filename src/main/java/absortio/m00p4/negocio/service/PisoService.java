package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.PisoModel;
import absortio.m00p4.negocio.repository.PisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PisoService {
    private PisoRepository pisoRepository;
    @Autowired
    public PisoService(PisoRepository pisoRepository){
        this.pisoRepository = pisoRepository;
    }

    public List<PisoModel> findAllPiso(){
        return pisoRepository.findAll();
    }
    public void delete(Integer id){
        pisoRepository.delete(id);
    }
    public void save( PisoModel pisoModel){ pisoRepository.save(pisoModel);}
    public PisoModel getById(Integer id){
        return pisoRepository.getById(id);
    }


}
