package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.DetalleDocumentoModel;
import absortio.m00p4.negocio.model.DetalleEntregaModel;
import absortio.m00p4.negocio.repository.DetalleEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleEntregaService {
    private DetalleEntregaRepository detalleEntregaRepository;
    @Autowired
    public DetalleEntregaService(DetalleEntregaRepository detalleEntregaRepository){
        this.detalleEntregaRepository = detalleEntregaRepository;
    }

    public List<DetalleEntregaModel> findAllDetalleEntrega(){
        return detalleEntregaRepository.findAll();
    }

    public List<DetalleEntregaModel> findAllByEstadoEntrega(String estado) {
        return detalleEntregaRepository.findAllByEstadoEntrega(estado);
    }

    public void delete(Integer id){
        detalleEntregaRepository.delete(id);
    }
    public DetalleEntregaModel save( DetalleEntregaModel detalleEntregaModel){ return detalleEntregaRepository.save(detalleEntregaModel);}

    public DetalleEntregaModel getById(Integer id){
        return detalleEntregaRepository.getById(id);
    };

    public List<DetalleEntregaModel> findAllByIdDetalleDocumento(DetalleDocumentoModel iddetalle){
        return detalleEntregaRepository.findAllByIdDetalleDocumento(iddetalle);
    }

    public List<DetalleEntregaModel> findAllByIdDetalleDocumento2(DetalleDocumentoModel iddetalledocumento){
        return     detalleEntregaRepository.findAllByIdDetalleDocumento( iddetalledocumento);

    }

}
