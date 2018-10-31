package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.SectorAlmacenModel;
import absortio.m00p4.negocio.repository.SectorAlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SectorAlmacenService {
    private SectorAlmacenRepository sectorAlmacenRepository;
    @Autowired
    public SectorAlmacenService(SectorAlmacenRepository sectorAlmacenRepository){
        this.sectorAlmacenRepository = sectorAlmacenRepository;
    }

    public List<SectorAlmacenModel> findAllSectorAlmacen(){
        return sectorAlmacenRepository.findAll();
    }
    public void delete(Integer id){
        sectorAlmacenRepository.delete(id);
    }
    public void save( SectorAlmacenModel sectorAlmacenModel){ sectorAlmacenRepository.save(sectorAlmacenModel);}

    public List<SectorAlmacenModel> filtrarAlmacenesTodos(String nombre, String tipo){
        return sectorAlmacenRepository.findAllByNombreAndTipo(nombre, tipo);
    }
    public SectorAlmacenModel findByIdentificador(String identificador) {

        return sectorAlmacenRepository.getByIdentificador(identificador);


    }
    public Collection<SectorAlmacenModel> getAllByIdMapa(MapaModel mapaModel){
        return sectorAlmacenRepository.getAllByIdMapa(mapaModel);
    }

    public SectorAlmacenModel getByNombre(String id){
        return sectorAlmacenRepository.getByNombre(id);
    }

}
