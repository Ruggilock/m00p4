package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FleteRepository extends JpaRepository<FleteModel, Integer>{
    List<FleteModel> findByDistrito_Id(Integer id);
    List<FleteModel> findByCapacidad_Id(Integer id);

    FleteModel getByCapacidad_IdAndDistrito_Id(Integer idCapacidad, Integer idDistrito);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE FleteModel c SET c.costo = :costo WHERE c.capacidad = :capacidad AND c.distrito = :distrito")
    Integer updateCostoFletes(@Param("costo") Double costo, @Param("capacidad") FleteCapacidadModel capacidad, @Param("distrito") FleteDistritoModel distrito);

//    @Modifying(clearAutomatically = true)
//    @Query("select t from FleteModel t WHERE (:departamento is null or t. = :departamento) and (:provincia is null or t. = :provincia) and (:distrito is null or t. = :distrito) and (:costomin is null or (t.costo >= :costomin) ) and (:costomax is null or (t.costo<= :costomax))")
//    List<FleteModel> buscar(@Param("departamento") String departamento, @Param("provincia") String provincia, @Param("distrito") String distrito, @Param("costomin") Double costomin, @Param("costomax") Double costomax);

}
