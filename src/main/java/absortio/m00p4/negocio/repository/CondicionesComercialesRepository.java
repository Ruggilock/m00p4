package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.CondicionesComercialesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CondicionesComercialesRepository extends JpaRepository<CondicionesComercialesModel, Integer> {

    //Integer countByCorreoAndContrasena(String correo,String contrasena);
    //CondicionesComercialesModel getByCorreoAndContrasena(String correo,String contrasena);
    @Override
    CondicionesComercialesModel findOne(Integer integer);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE CondicionesComercialesModel c SET c.categoria = :categoria, " +
                                                "c.descripcion = :descripcion,  " +
                                                "c.cantidad1 = :cantidad1,"+
                                                "c.cantidad2 = :cantidad2,"+
                                                "c.codigo1 = :codigo1,"+
                                                "c.codigo2 = :codigo2,"+
                                                "c.fechaFin = :fechaFin,"+
                                                "c.fechaIni = :fechaIni,"+
                                                "c.porcentaje = :porcentaje,"+
                                                "c.tipo = :tipo "+
                                                "WHERE c.id = :condicionId")
    Integer upadate(@Param("condicionId") int condicionId, @Param("categoria") int categoria, @Param("descripcion") String descripcion, @Param("cantidad1") int cantidad1, @Param("cantidad2") int cantidad2, @Param("codigo2") int codigo2, @Param("codigo1") int codigo1, @Param("fechaFin") String fechaFin, @Param("fechaIni") String fechaIni, @Param("porcentaje") float porcentaje, @Param("tipo") String tipo);

    @Query(value = "select  * from condicionescomerciales order by tipo asc ", nativeQuery = true)
    List<CondicionesComercialesModel> findAllOrdenado();
}
