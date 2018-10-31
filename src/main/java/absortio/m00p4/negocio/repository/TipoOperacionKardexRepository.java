package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.TipoOperacionKardexModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoOperacionKardexRepository extends JpaRepository<TipoOperacionKardexModel, Integer> {
    TipoOperacionKardexModel getByNombre(String nombre);
}
