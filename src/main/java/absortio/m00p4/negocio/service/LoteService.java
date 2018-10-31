package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.LoteModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.repository.BloqueRepository;
import absortio.m00p4.negocio.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class LoteService {
    private LoteRepository loteRepository;
    @Autowired
    public LoteService(LoteRepository loteRepository){
        this.loteRepository = loteRepository;
    }

    public List<LoteModel> findAllLote(){
        return loteRepository.findAll();
    }
    public void delete(Integer id){
        loteRepository.delete(id);
    }
    public void save( LoteModel loteModel){ loteRepository.save(loteModel);}

    public LoteModel findById(Integer id){
        return loteRepository.findById(id);
    }
    public LoteModel getById(Integer id){
        return loteRepository.getById(id);
    }

    public List<LoteModel> hallarLotesById (Integer id){
        return loteRepository.hallarLotesById(id);
    }

    @Transactional
    public Integer reduceStockDisponible(int idlote, Integer stockDisponibleActualizado){
        return loteRepository.salidaMerma(idlote,stockDisponibleActualizado);
    }

    @Transactional
    public Integer aumentaStockMerma(int idlote, Integer stockMermaActualizado){
        return loteRepository.aumentoMerma(idlote,stockMermaActualizado);
    }

//    public LoteModel findFirstByOrderByIdentificador(String identificador){
//        return loteRepository.findFirstByOrderByIdentificador(identificador);
//    }
//    public Collection<LoteModel> getAllByIdProducto(ProductoModel productoModel){
//        return loteRepository.getAllByIdProducto(productoModel);
//    }

}
