package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.CondicionesComercialesModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.repository.CondicionesComercialesRepository;
import absortio.m00p4.negocio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CondicionesComercialesService {

    private CondicionesComercialesRepository condicion;

    @Autowired
    public CondicionesComercialesService(CondicionesComercialesRepository condicionesComercialesRepository){
        this.condicion = condicionesComercialesRepository;
    }

    public List<CondicionesComercialesModel> findAllCondicion(){
        return condicion.findAll();

    }


    public void save( CondicionesComercialesModel condicion){
        this.condicion.save(condicion);
    }

    public void delete(Integer id){
        condicion.delete(id);
    }

    public CondicionesComercialesModel getById(Integer id){
        return condicion.findOne(id);
    }

    @Transactional
    public Integer updateNombreAndDescripcion(int condicionId,int categoria,String descripcion,int cantidad1,int cantidad2,
                                              int codigo2,int codigo1,String fechaFin , String fechaIni,float porcentaje,String tipo){
        return condicion.upadate(condicionId,categoria,descripcion,cantidad1,cantidad2,codigo2,codigo1,fechaFin,fechaIni,porcentaje,tipo);
    }

    public List<CondicionesComercialesModel> findAllOrdenado() {
        return condicion.findAllOrdenado();
    }

}
