package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.DevolucionModel;
import absortio.m00p4.negocio.repository.DevolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DevolucionService {
    private DevolucionRepository devolucionRepository;
    @Autowired
    public DevolucionService(DevolucionRepository devolucionRepository){
        this.devolucionRepository = devolucionRepository;
    }
    public List<DevolucionModel> findAllDevolucion(){
        return devolucionRepository.findAll();
    }

    public DevolucionModel save(DevolucionModel devolucionModel){
        return devolucionRepository.save ( devolucionModel );
    }

    public DevolucionModel getByNodevolucion(String nodevolucion){
        return devolucionRepository.getByNodevolucion(nodevolucion);
    }

    public List<DevolucionModel> findAllDevolucionByDocumento(Integer docid){
        return devolucionRepository.findAllByDocumento_Id(docid);
    }

}
