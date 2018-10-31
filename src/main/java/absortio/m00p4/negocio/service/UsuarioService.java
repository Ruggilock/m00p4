package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.auxiliares.Cliente;
import absortio.m00p4.negocio.repository.UsuarioRepository;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    public UsuarioSingleton usuarioSingleton;
    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }


    public List<UsuarioModel> findAllUsuario(){
        return usuarioRepository.findAll();
    }

    public void delete(Integer id){
        usuarioRepository.delete(id);
    }

    public void save( UsuarioModel usuarioModel){ usuarioRepository.save(usuarioModel);}

    public Integer countByCorreoAndContrasena(String correo,String contrasena){
        return usuarioRepository.countByCorreoAndContrasena(correo,contrasena);
    }

    public UsuarioModel getByCorreoAndContrasena(String correo, String contrasena) {
        return usuarioRepository.getByCorreoAndContrasena(correo,contrasena);

    }

    public void deleteByDocumentoIdentidad(String documentoIdentidad){
        usuarioRepository.deleteByDocumentoIdentidad(documentoIdentidad);
    }

    public UsuarioModel getByDocumentoIdentidad(String documentoIdentidad) {
        return usuarioRepository.getByDocumentoIdentidad(documentoIdentidad);
    }
    public ArrayList<UsuarioModel> getByEstado(String estado) {
        return usuarioRepository.getByEstado(estado);
    }

    public ArrayList<UsuarioModel> findAllByNombresAndDocumentoIdentidadAndCargo(String nombres, String documentoIdentidad, RolModel cargo) {
        if (cargo!= null) {
            return usuarioRepository.findAllByNombresAndDocumentoIdentidadAndCargo(nombres, documentoIdentidad, cargo.getId());
        }else
            return usuarioRepository.findAllByNombresAndDocumentoIdentidad(nombres, documentoIdentidad);

    }

    public List<UsuarioModel> findByTodosFiltros(String nombre, String documento, String rol){
        return usuarioRepository.buscarPorTodosFiltros(nombre, documento, rol);
    }
}


