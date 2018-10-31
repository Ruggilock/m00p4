package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.DetalleFacturaParcialModel;
import absortio.m00p4.negocio.repository.DetalleFacturaParcialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleFacturaParcialService {
    private DetalleFacturaParcialRepository detalleFacturaParcialRepository;
    @Autowired
    public DetalleFacturaParcialService(DetalleFacturaParcialRepository detalleFacturaParcialRepository){
        this.detalleFacturaParcialRepository = detalleFacturaParcialRepository;
    }

    public List<DetalleFacturaParcialModel> findAllDetalleFacturaParcialByIdFacturaParcial(Integer idFacturaParcial){
        return detalleFacturaParcialRepository.findAllByIdFacturaParcial(idFacturaParcial);
    }

    public List<DetalleFacturaParcialModel> findAllDetalleFacturaParcial(){
        return detalleFacturaParcialRepository.findAll();
    }
    public void delete(Integer id){
        detalleFacturaParcialRepository.delete(id);
    }
    public void save( DetalleFacturaParcialModel detalleFacturaParcialModel){ detalleFacturaParcialRepository.save(detalleFacturaParcialModel);}

    public ArrayList<DetalleFacturaParcialModel> findAllByFacturaParcialModel_NoDocumento(String nodoc){
        return detalleFacturaParcialRepository.findAllByFacturaParcialModel_NoDocumento(nodoc);
    }
}
