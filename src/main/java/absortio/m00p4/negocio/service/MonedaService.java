package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.MonedaModel;
import absortio.m00p4.negocio.repository.MonedaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MonedaService {
    private MonedaRepository monedaRepository;
    @Autowired
    public MonedaService(MonedaRepository monedaRepository){
        this.monedaRepository = monedaRepository;
    }

    public List<MonedaModel> findAllMoneda(){

        return monedaRepository.findAll();
    }
    public void delete(Integer id){
        monedaRepository.delete(id);
    }
    public void save( MonedaModel monedaModel){ monedaRepository.save(monedaModel);}

    public MonedaModel findByNombre(String nombre){
        return monedaRepository.findByNombre(nombre);
    }

}
