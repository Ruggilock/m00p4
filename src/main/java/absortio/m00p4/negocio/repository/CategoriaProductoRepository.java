package absortio.m00p4.negocio.repository;

import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProductoModel, Integer> {

    @Override
    CategoriaProductoModel getOne(Integer integer);

    @Transactional
    CategoriaProductoModel getByNombre(String categoria);


}
