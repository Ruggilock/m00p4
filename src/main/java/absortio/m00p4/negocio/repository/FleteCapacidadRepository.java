package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.auxiliares.FleteCapacidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FleteCapacidadRepository extends JpaRepository<FleteCapacidadModel, Integer> {
    List<FleteCapacidadModel> findAllByPesoMaxIsGreaterThanAndPesoMinIsLessThanEqual(Double peso, Double peso2);
    List<FleteCapacidadModel> findAllByVolumenMaxIsGreaterThanAndVolumenMinIsLessThanEqual(Double peso, Double peso2);
    List<FleteCapacidadModel> findAllByPesoMaxAndPesoMinAndVolumenMaxAndVolumenMin(Double pesomax, Double pesomin, Double volmax, Double volmin);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE FleteCapacidadModel  c SET c.costo = :costo, c.pesoMax = :pesomax, c.pesoMin = :pesomin, c.volumenMax = :volmax, c.volumenMin = :volmin, c.unidadPeso = :umpeso, c.unidadvolumen = :umvol WHERE c.id = :fleteCapacidadId")
    Integer updateTodo(@Param("fleteCapacidadId") int fleteCapacidadId, @Param("umpeso") String umpeso, @Param("pesomax") Double pesomax, @Param("pesomin") Double pesomin, @Param("umvol") String umvol, @Param("volmax") Double volmax, @Param("volmin") Double volmin, @Param("costo") Double costo);

    @Modifying(clearAutomatically = true)
    @Query("select t from FleteCapacidadModel t WHERE (:pesomin is null or t.pesoMin >= :pesomin) and (:pesomax is null or t.pesoMax <= :pesomax) and (:volmin is null or t.volumenMin >= :volmin) and (:volmax is null or t.volumenMax <= :volmax)")
    List<FleteCapacidadModel> buscar( @Param("pesomin") Double pesomin, @Param("pesomax") Double pesomax, @Param("volmin") Double volmin, @Param("volmax") Double volmax);

    @Query(value =  "SELECT  * FROM fletecapacidad f " +
            "WHERE (f.pesomin <= ?1) AND (?1 <=f.pesomax)  AND  (f.volumenmin <= ?2 )AND (?2 <= f.volumenmax )" +
            "AND (f.estado= 'activo') ", nativeQuery = true)
    FleteCapacidadModel obtenerFleteCapacidad(Double peso, Double volumen);
}
