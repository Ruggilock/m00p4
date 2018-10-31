package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
import absortio.m00p4.negocio.model.auxiliares.FleteDistrito;
import absortio.m00p4.negocio.repository.FleteDistritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FleteDistritoService {
    private FleteDistritoRepository fleteDistritoRepository;

    @Autowired
    public FleteDistritoService(FleteDistritoRepository fleteDistritoRepository) {
        this.fleteDistritoRepository = fleteDistritoRepository;
    }
    public FleteDistrito getById(Integer id){
        return this.fleteDistritoRepository.getById(id);
    }

    public List<FleteDistritoModel> findAllFleteDistrito() {
        return fleteDistritoRepository.findAll();
    }

    public List<FleteDistritoModel> findAllByDepartamentoAndProvinciaAndDistrito(String departamento, String provincia, String distrito) {
        return fleteDistritoRepository.findAllByDepartamentoAndProvinciaAndDistrito(departamento, provincia, distrito);
    }
    public FleteDistritoModel getByDepartamentoAndProvinciaAndDistritoAndEstadoLike(String departamento, String provincia, String distrito, String estado){
        return fleteDistritoRepository.getByDepartamentoAndProvinciaAndDistritoAndEstadoLike( departamento,  provincia,  distrito, estado);
    }

    public void delete(Integer id) {
        fleteDistritoRepository.delete(id);
    }

    public void save(FleteDistritoModel fleteDistritoModel) {
        fleteDistritoRepository.save(fleteDistritoModel);
    }

    @Transactional
    public Integer updateDepartamentoProvinciaDistritoCosto(Integer id,String departamento, String provincia, String distrito, Double costo) {
        return fleteDistritoRepository.updateDepartamentoProvinciaDistrito( id, departamento,  provincia,  distrito, costo);
    }

    public List<FleteDistritoModel> buscar(String departamento,  String provincia, String distrito,  Double costomin, Double costomax){
        return fleteDistritoRepository.buscar(departamento,provincia,distrito,costomin,costomax);
    }

}