package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.FacturaParcialModel;
import absortio.m00p4.negocio.repository.FacturaParcialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaParcialService {
    private FacturaParcialRepository facturaParcialRepository;
    @Autowired
    public FacturaParcialService(FacturaParcialRepository facturaParcialRepository){
        this.facturaParcialRepository = facturaParcialRepository;
    }

    public List<FacturaParcialModel> findAllFacturaParcial(){
        return facturaParcialRepository.findAll();
    }
    public void delete(Integer id){
        facturaParcialRepository.delete(id);
    }
    public void save( FacturaParcialModel facturaParcialModel){ facturaParcialRepository.save(facturaParcialModel);}

    public List<FacturaParcialModel> findAllByIdDocumentol(Integer idDocumento){
        return facturaParcialRepository.findAllByIdDocumento(idDocumento);
    }
    public  List<FacturaParcialModel> findAllByNoDocumento(String nodoc){
        return  facturaParcialRepository.findAllByNoDocumento(nodoc);
    }

    public FacturaParcialModel getByNoDocumento(String nodoc){
        return facturaParcialRepository.getByNoDocumento(nodoc);
    }

}
