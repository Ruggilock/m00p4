package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.PermisoModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.repository.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermisoService {
    private PermisoRepository permisoRepository;

    @Autowired
    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    public List<PermisoModel> findAllPermiso() {
        return permisoRepository.findAll();
    }

    public void delete(Integer id) {
        permisoRepository.delete(id);
    }

    public void save(PermisoModel permisoModel) {
        permisoRepository.save(permisoModel);
    }

    public PermisoModel getByNombre(String nombre) {
        return permisoRepository.getByNombre(nombre);
    }

    public List<PermisoModel> findAllPermisoByIdRol(Integer idrol) {
        return permisoRepository.findAllByIdRol(idrol);
    }

}
