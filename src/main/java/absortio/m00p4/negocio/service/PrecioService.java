package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.PrecioModel;
import absortio.m00p4.negocio.repository.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecioService {

    private final static String INACTIVO = "inactivo";
    private final static String ACTIVO = "activo";

    private PrecioRepository precioRepository;

    @Autowired
    public PrecioService(PrecioRepository precioRepository) {
        this.precioRepository = precioRepository;
    }

    public PrecioModel save(PrecioModel precioModel) {
        return precioRepository.save ( precioModel );
    }

    public PrecioModel obtenerPrecioId(Integer id){
        return precioRepository.getById ( id );
    }

    PrecioModel getByProducto_IdAndEstadoLike(Integer idproducto, String estado){
        return precioRepository.getByProducto_IdAndEstadoLike ( idproducto,estado ) ;
    }

    public List<PrecioModel> findAll(){
        return precioRepository.findAll();
    }

    public List<PrecioModel> hallarPrecio(Integer id){
        return precioRepository.getByProductoId(id);
        //return precioRepository.findTopByProductoOrderByFechaFinDesc(id);
    }
}
