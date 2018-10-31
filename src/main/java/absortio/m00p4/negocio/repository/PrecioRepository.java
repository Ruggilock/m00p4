package absortio.m00p4.negocio.repository;


import absortio.m00p4.negocio.model.PrecioModel;
import absortio.m00p4.negocio.model.ProductoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface PrecioRepository extends JpaRepository<PrecioModel, Integer> {


    PrecioModel getByProducto_IdAndEstadoLike(Integer idproducto, String estado);

    PrecioModel getById(Integer id);

    //PrecioModel findFirstByProductoOrderByFechaInicio(Integer id);
    //PrecioModel findTopByOrOrderByFechaFinDesc(Integer id);


    List<PrecioModel> getByProductoId(Integer id);


    PrecioModel findTopByProductoOrderByFechaFinDesc(Integer id);
    //List<PrecioModel> findAllByProductoIsStartingWith(Integer id);


}