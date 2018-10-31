package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.ClienteModel;
import absortio.m00p4.negocio.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public ClienteModel getByDocumentoIdentidad(String documentoIdentidad) {
        return clienteRepository.getByDocumentoIdentidad(documentoIdentidad);
    }

    public void deleteByDocumentoIdentidad(String documentoIdentidad){
        clienteRepository.deleteByDocumentoIdentidad(documentoIdentidad);
    }

    public List<ClienteModel> findAll(){
        return clienteRepository.findAll();
    }

    public void delete(Integer id){
        clienteRepository.delete(id);
    }

    public void save( ClienteModel clienteModel){ clienteRepository.save(clienteModel);}

    public Integer updateEstadoById(String documento, String estado) { return clienteRepository.updateEstadoById(documento, estado) ;}

    public  List<ClienteModel> findByTodosFiltros(String nombres, String documento) { return clienteRepository.buscarPorTodosFiltros(nombres, documento);}
}
