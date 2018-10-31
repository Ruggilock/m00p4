package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.RackModel;
import absortio.m00p4.negocio.model.SectorAlmacenModel;
import absortio.m00p4.negocio.repository.MapaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class MapaService {
    private MapaRepository mapaRepository;
    @Autowired
    public MapaService(MapaRepository mapaRepository){
        this.mapaRepository = mapaRepository;
    }


    public List<MapaModel> findAllMapa(){
        return mapaRepository.findAll();
    }
    public void delete(Integer id){
        mapaRepository.delete(id);
    }
    public void save( MapaModel mapaModel){ mapaRepository.save(mapaModel);}

    public MapaModel findFirstByOrderById() {
        return mapaRepository.findFirstByOrderById();
    }



}
