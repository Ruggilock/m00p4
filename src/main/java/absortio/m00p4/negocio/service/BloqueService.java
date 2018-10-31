package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.RackModel;
import absortio.m00p4.negocio.repository.BloqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class BloqueService {
    private BloqueRepository bloqueRepository;
    @Autowired
    public BloqueService(BloqueRepository bloqueRepository){
        this.bloqueRepository = bloqueRepository;
    }

    public List<BloqueModel> findAllBloque(){
        return bloqueRepository.findAll();
    }
    public void delete(Integer id){
        bloqueRepository.delete(id);
    }
    public void save( BloqueModel bloqueModel){ bloqueRepository.save(bloqueModel);}
    public BloqueModel findFirstByOrderByIdentificador(String identificador){
        return bloqueRepository.findFirstByOrderByIdentificador(identificador);
    }
    public Collection<BloqueModel> getAllByIdRack(RackModel rackModel){
        return bloqueRepository.getAllByIdRack(rackModel);
    }

    public BloqueModel getById(Integer id){
        return bloqueRepository.getById(id);
    }

    public BloqueModel getFirstByEstado(String estado){
        return bloqueRepository.getFirstByEstado(estado);
    }

    public List<BloqueModel> findAllByEstado(String estado){
        return bloqueRepository.findAllByEstado(estado);
    }

    public List<BloqueModel> findAllByEstadoAndAndIdRack_IdSectorAlmacen_Id(String estado, Integer id){
        return bloqueRepository.findAllByEstadoAndAndIdRack_IdSectorAlmacen_Id(estado, id);
    }

    public List<BloqueModel> findAllByPosxAndPosy(Integer posX, Integer posY) {
        return bloqueRepository.findAllByPosxAndPosy(posX,posY);
    }

    @Transactional
    public Integer updateEstadoaOcupado( Integer id){
        return bloqueRepository.updateEstadoaOcupado(id);
    }

    @Transactional
    public  Integer updateEstadoaActivo(Integer id){return  bloqueRepository.updateEstadoaActivo(id);}

    public  List<BloqueModel> findAllByIdRack_IdSectorAlmacen_Nombre(String nombreAlmacen){
        return bloqueRepository.findAllByIdRack_IdSectorAlmacen_Nombre(nombreAlmacen);
    }
}
