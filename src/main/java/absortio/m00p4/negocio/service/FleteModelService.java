package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
import absortio.m00p4.negocio.repository.FleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FleteModelService {
    private FleteRepository fleteRepository;

    @Autowired
    public FleteModelService(FleteRepository fleteRepository) {
        this.fleteRepository = fleteRepository;
    }

    public List<FleteModel> findAllFlete() {
        return fleteRepository.findAll();
    }

    //public List<FleteModel> findAllByDepartamentoAndProvinciaAnd(String departamento, String provincia, String ) {
    //return fleteRepository.findAllByDepartamentoAndProvinciaAnd(departamento, provincia, );
    //}

    public void delete(Integer id) {
        fleteRepository.delete(id);
    }

    public void save(FleteModel fleteModel) {
        fleteRepository.save(fleteModel);
    }

    public List<FleteModel> findByDistrito_Id(Integer id) {
        return fleteRepository.findByDistrito_Id(id);
    }

    public List<FleteModel> findByCapacidad_Id(Integer id) {
        return fleteRepository.findByCapacidad_Id(id);
    }

    public FleteModel getByCapacidad_IdAndDistrito_Id(Integer idCapacidad, Integer idDistrito) {
        return fleteRepository.getByCapacidad_IdAndDistrito_Id(idCapacidad, idDistrito);
    }

    @Transactional
    public Integer updateCostoFletes(Double costo, FleteCapacidadModel capacidad, FleteDistritoModel distrito) {
        return fleteRepository.updateCostoFletes(costo, capacidad, distrito);

    }

}
