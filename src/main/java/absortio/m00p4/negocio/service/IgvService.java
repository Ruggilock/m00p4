package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.IgvModel;
import absortio.m00p4.negocio.repository.IgvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IgvService {
    private IgvRepository igvRepository;
    @Autowired
    public IgvService(IgvRepository igvRepository){
        this.igvRepository = igvRepository;
    }

    public List<IgvModel> findAllIgv(){
        return igvRepository.findAll();
    }
    public void delete(Integer id){
        igvRepository.delete(id);
    }
    public void save( IgvModel igvModel){ igvRepository.save(igvModel);}

    public IgvModel findByEstado(String estado){
        return igvRepository.findByEstado(estado);

    }

}
