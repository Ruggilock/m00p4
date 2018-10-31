package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.PermisoModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolService {
    private RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository ){
        this.rolRepository = rolRepository;
    }

    public List<RolModel> findAllRol(){
        return rolRepository.findAll();
    }
    public List <RolModel> findAllByNombreIsStartingWith(String nombre) { return rolRepository.findAllByNombreIsStartingWith(nombre);}
    public void delete(Integer id){
        rolRepository.delete(id);
    }
    public RolModel save( RolModel rolModel){ return rolRepository.save(rolModel);}
    public RolModel getByIdAndNombre(Integer id, String nombre){
        return rolRepository.getByIdAndNombre(id, nombre);
    }
    @Transactional
    public Integer updateNombreAndDescripcion(Integer id, String nombre, String descripcion){
        return rolRepository.updateNombreAndDescripcion(id,nombre,descripcion);
    }
    public RolModel getByNombre( String nombre){
        return rolRepository.getByNombre( nombre);
    }

    public Integer updateEstadoById(Integer id, String estado) { return rolRepository.updateEstadoById(id, estado) ;}




}
