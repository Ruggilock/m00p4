package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.DespachoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import absortio.m00p4.negocio.repository.DespachoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DespachoService {
    private DespachoRepository despachoRepository;
    @Autowired
    public DespachoService(DespachoRepository DespachoRepository){
        this.despachoRepository = DespachoRepository;
    }

    public List<DespachoModel> findAllDespacho(){
        return despachoRepository.findAll();
    }
    public void delete(Integer id){
        despachoRepository.delete(id);
    }
    public void save( DespachoModel DespachoModel){despachoRepository.save(DespachoModel);}
    public DespachoModel findById(Integer id){
        return despachoRepository.findById(id);
    }
    public void deleteEstadoDespacho(){
        despachoRepository.deleteAllByEstadoDespacho();
    }

    public ArrayList<DespachoModel>findByEstadoDespacho(String porConfirmar){
        return despachoRepository.findByEstadoDespacho(porConfirmar);
    }

    public List<DespachoModel> findByTodosFiltros(String sobrenombre, String estado, String fecha){
        return despachoRepository.buscarPorTodosFiltros(sobrenombre, estado, fecha);
    }

    public void deleteDespachoById(Integer idDespacho){

        List<Integer> listaIdRutas = new ArrayList<Integer>();
        listaIdRutas = despachoRepository.findAllDistinctIdRutaByIdDespacho(idDespacho);

        for (Integer idRuta : listaIdRutas){
            List<Integer> listaIdRutaPaso = new ArrayList<Integer>();
            listaIdRutaPaso = despachoRepository.findAllDistinctIdRutapasoByIdruta(idRuta);
            for (Integer idRutaPaso : listaIdRutaPaso){
                despachoRepository.deleteAllRutaCoordenadaByIdRutaPaso(idRutaPaso);
                despachoRepository.deleteRutaPasoByIdRutaPaso(idRutaPaso);
            }
        }


        // Borramos el resto
        despachoRepository.deleteRutaByIdDespacho(idDespacho);
        despachoRepository.deleteDetalleDespachoByIdDespacho(idDespacho);
        despachoRepository.deleteDespachoByIdDespacho(idDespacho);
    }



}
