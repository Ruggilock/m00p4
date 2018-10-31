package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.MotivoOperacionKardexModel;
import absortio.m00p4.negocio.repository.MotivoOperacionKardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotivoOperacionKardexService {
    private MotivoOperacionKardexRepository motivoOperacionKardexRepository;

    @Autowired
    public MotivoOperacionKardexService(MotivoOperacionKardexRepository motivoOperacionKardexRepository ){
        this.motivoOperacionKardexRepository = motivoOperacionKardexRepository;
    }
    public List<MotivoOperacionKardexModel> findAllByTipooperacion_Id(Integer id){
        return this.motivoOperacionKardexRepository.findAllByTipooperacion_Id(id);
    }
    public List<MotivoOperacionKardexModel> findAllByTipooperacion_Nombre(String id){
        return this.motivoOperacionKardexRepository.findAllByTipooperacion_Nombre(id);
    }

    public MotivoOperacionKardexModel getByNombreAndTipo_id(String nombre, Integer id){
        return motivoOperacionKardexRepository.getByNombreAndTipooperacion_Id(nombre, id);
    }

    public MotivoOperacionKardexModel getByNombre(String nombre){
        return motivoOperacionKardexRepository.getByNombre(nombre);
    }
}
