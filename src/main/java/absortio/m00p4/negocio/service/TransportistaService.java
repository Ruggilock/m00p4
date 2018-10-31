package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.repository.TransportistaRepository;
import absortio.m00p4.negocio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
// Aqui enlazamos los campos externos contra los queries de la BD

@Service
public class TransportistaService {
    private TransportistaRepository transportistaRepository;
    @Autowired
    public TransportistaService(TransportistaRepository transportistaRepository){
        this.transportistaRepository = transportistaRepository;
    }

    public List<TransportistaModel> findAllTransportista(){
        return transportistaRepository.findAll();

    }
    public void delete(Integer id){
        transportistaRepository.delete(id);
    }
    public void save( TransportistaModel transportistaModel){ transportistaRepository.save(transportistaModel);}

    public void deleteByDocumentoIdentidad(String documentoIdentidad){
        transportistaRepository.deleteByDocumentoIdentidad(documentoIdentidad);
    }

    public TransportistaModel getByDocumentoIdentidad(String documentoIdentidad) {
        return transportistaRepository.getByDocumentoIdentidad(documentoIdentidad);
    }

    public TransportistaModel getById(Integer id){
        return  transportistaRepository.getById(id);
    }

    public TransportistaModel getByNombreCompleto(String nombreCompleto){
        return transportistaRepository.getByNombreCompleto(nombreCompleto);
    }

    public List<TransportistaModel> findByCualquierNombre(String nombre){
        return transportistaRepository.buscarPorCualquierNombre(nombre);
    }

    public List<TransportistaModel> findByDocumento(String documento){
        return transportistaRepository.buscarPorDocumento(documento);
    }

    public List<TransportistaModel> findByTelefono(String telefono){
        return transportistaRepository.findAllByTelefono(telefono);
    }

    public List<TransportistaModel> findByTodosFiltros(String nombre, String documento, String telefono){
        return transportistaRepository.buscarPorTodosFiltros(nombre, documento, telefono);
    }

    public Integer updateEstadoById(Integer id, String estado) { return transportistaRepository.updateEstadoById(id, estado) ;}


}



