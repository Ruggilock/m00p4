package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.auxiliares.FleteCapacidad;
import absortio.m00p4.negocio.repository.FleteCapacidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FleteCapacidadService {
    private FleteCapacidadRepository fleteCapacidadRepository;
    @Autowired
    public FleteCapacidadService(FleteCapacidadRepository fleteCapacidadRepository){
        this.fleteCapacidadRepository = fleteCapacidadRepository;
    }

    public List<FleteCapacidadModel> findAllFleteCapacidad(){
        return  fleteCapacidadRepository.findAll();
    }
    public void delete(Integer id){
        fleteCapacidadRepository.delete(id);
    }
    public void save( FleteCapacidadModel fleteCapacidadModel){ fleteCapacidadRepository.save(fleteCapacidadModel);}

    public List<FleteCapacidadModel> findByPesoMaxIsGreaterThanAndPesoMinIsLessThanEqual(Double peso, Double peso2){
        return fleteCapacidadRepository.findAllByPesoMaxIsGreaterThanAndPesoMinIsLessThanEqual(peso, peso2);
    }
    public List<FleteCapacidadModel> findByVolumenMaxIsGreaterThanAndVolumenMinIsLessThanEqual(Double peso, Double peso2){
        return fleteCapacidadRepository.findAllByVolumenMaxIsGreaterThanAndVolumenMinIsLessThanEqual(peso, peso2);
    }
    public List<FleteCapacidadModel> findAllByPesoMaxAndPesoMinAndVolumenMaxAndVolumenMin(Double pesomax, Double pesomin, Double volmax, Double volmin){
        return fleteCapacidadRepository.findAllByPesoMaxAndPesoMinAndVolumenMaxAndVolumenMin(pesomax, pesomin, volmax, volmin);
    }
    @Transactional
    public void updateTodo(Integer id, String umpeso, Double pesomax, Double pesomin, String umvol, Double volmax, Double volmin, Double costo){
        fleteCapacidadRepository.updateTodo(id, umpeso, pesomax, pesomin, umvol, volmax, volmin,costo);
    }

    public List<FleteCapacidadModel> buscar(Double pesomin, Double pesomax,  Double volmin,  Double volmax){
        return fleteCapacidadRepository.buscar(pesomin,pesomax,volmin,volmax);
    }

    public  FleteCapacidadModel obtenerFleteCapacidad(Double peso, Double volumen){
        return fleteCapacidadRepository.obtenerFleteCapacidad(peso,volumen);
    }

}
