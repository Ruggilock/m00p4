package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
import absortio.m00p4.negocio.model.auxiliares.FleteDistrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FleteDistritoRepository extends JpaRepository<FleteDistritoModel, Integer> {
    List<FleteDistritoModel> findAllByDepartamentoAndProvinciaAndDistrito(String departamento, String provincia, String distrito);

    FleteDistritoModel getByDepartamentoAndProvinciaAndDistritoAndEstadoLike(String departamento, String provincia, String distrito,String estado);

    FleteDistrito getById(Integer id);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FleteDistritoModel c SET c.departamento = :departamento, c.provincia = :provincia, c.distrito = :distrito, c.costo = :costo WHERE c.id = :fleteDistritoId")
    Integer updateDepartamentoProvinciaDistrito(@Param("fleteDistritoId") int fleteDistritoId, @Param("departamento") String departamento, @Param("provincia") String provincia, @Param("distrito") String distrito, @Param("costo") Double Costo);

    @Modifying(clearAutomatically = true)
    @Query("select t from FleteDistritoModel t WHERE (:departamento is null or t.departamento = :departamento) and (:provincia is null or t.provincia = :provincia) and (:distrito is null or t.distrito = :distrito) and (:costomin is null or (t.costo >= :costomin) ) and (:costomax is null or (t.costo<= :costomax))")
    List<FleteDistritoModel> buscar(@Param("departamento") String departamento, @Param("provincia") String provincia, @Param("distrito") String distrito, @Param("costomin") Double costomin, @Param("costomax") Double costomax);


    //@Modifying(clearAutomatically = true)
    //@Query("select b.id from FleteDistritoModel b JOIN b.fletes c where c. = :groupName ")
    //Integer updateDepartamentoProvinciaDistrito(@Param("fleteDistritoId") int fleteDistritoId, @Param("departamento") String departamento, @Param("provincia") String provincia,, @Param("distrito") String distrito);


}
