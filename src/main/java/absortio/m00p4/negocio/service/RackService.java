package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.RackModel;
import absortio.m00p4.negocio.model.SectorAlmacenModel;
import absortio.m00p4.negocio.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class RackService {
    private RackRepository rackRepository;
    @Autowired
    public RackService(RackRepository rackRepository){
        this.rackRepository = rackRepository;
    }

    public List<RackModel> findAllRack(){
        return rackRepository.findAll();
    }
    public void delete(Integer id){
        rackRepository.delete(id);
    }
    public void save( RackModel rackModel){ rackRepository.save(rackModel);}
    public RackModel findByIdentificador(String identificador) {

        return rackRepository.findByIdentificador(identificador);
    }
    public  Collection<RackModel> getAllByIdSectorAlmacen (SectorAlmacenModel sectorAlmacen){
        return rackRepository.getAllByIdSectorAlmacen(sectorAlmacen);
    }

    public List<RackModel>getByIdSectorAlmacen_Nombre(String nombre){
        return rackRepository.getByIdSectorAlmacen_Nombre(nombre);
    }

}
