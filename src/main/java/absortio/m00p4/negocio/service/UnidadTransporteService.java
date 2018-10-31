package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.UnidadTransporteModel;
import absortio.m00p4.negocio.model.auxiliares.UnidadTransporte;
import absortio.m00p4.negocio.repository.TransportistaRepository;
import absortio.m00p4.negocio.repository.UnidadTransporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadTransporteService {
    private UnidadTransporteRepository unidadTransporteRepository;

    @Autowired
    public UnidadTransporteService(UnidadTransporteRepository unidadTransporteRepository){
        this.unidadTransporteRepository = unidadTransporteRepository;
    }

    public List<UnidadTransporteModel> findAllUnidadTransporte(){
        return unidadTransporteRepository.findAll();
    }

    public void delete(Integer id){
        unidadTransporteRepository.delete(id);
    }
    public void save( UnidadTransporteModel unidadTransporteModel){ unidadTransporteRepository.save(unidadTransporteModel);}

    public void deleteByPlaca(String placa){
        unidadTransporteRepository.deleteByPlaca(placa);
    }

    public UnidadTransporteModel getByPlaca(String placa) {
        return unidadTransporteRepository.getByPlaca(placa);
    }

    public UnidadTransporteModel getById(Integer id){ return  unidadTransporteRepository.getById(id); }

    public Integer updateEstadoById(Integer id, String estado) { return unidadTransporteRepository.updateEstadoById(id, estado) ;}

    //@Modifying(clearAutomatically = true)
    //@Query("UPDATE RolModel c SET c.nombre = :nombre, c.descripcion = :descripcion  WHERE c.id = :rolId")
    //Integer updateNombreAndDescripcion(@Param("rolId") int rolId, @Param("nombre") String nombre, @Param("descripcion") String descripcion);

//    public TransportistaModel getByIdTransportista(Integer idTransportista){
//        //TransportistaModel transportistaModel = new TransportistaModel();
//        return unidadTransporteRepository.getByIdTransportista(idTransportista);
//    }
}



