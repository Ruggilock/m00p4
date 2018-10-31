package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.DetalleDevolucionModel;
import absortio.m00p4.negocio.repository.DetalleDevolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleDevolucionService {

    private DetalleDevolucionRepository detalleDevolucionRepository;
    @Autowired
    public DetalleDevolucionService(DetalleDevolucionRepository detalleDevolucionRepository){
        this.detalleDevolucionRepository = detalleDevolucionRepository;
    }
    public List<DetalleDevolucionModel> findAllDetalleDevolucion(){
        return detalleDevolucionRepository.findAll();
    }

    public DetalleDevolucionModel save(DetalleDevolucionModel devolucionModel){
        return detalleDevolucionRepository.save ( devolucionModel );
    }

    public List<DetalleDevolucionModel> findAllDetalleDevolucionByIdDevolucion(Integer id) {
        return detalleDevolucionRepository.findAllDetalleDevolucionByIdDevolucion(id);
    }
}
