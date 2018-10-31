package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.repository.CategoriaProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaProductoService {
    private CategoriaProductoRepository categoriaProductoRepository;

    @Autowired
    public CategoriaProductoService(CategoriaProductoRepository categoriaProductoRepository) {
        this.categoriaProductoRepository = categoriaProductoRepository;
    }

    public List<CategoriaProductoModel> findAllCategoriaProducto() {
        return categoriaProductoRepository.findAll ();
    }

    public CategoriaProductoModel obtenerCategoriaPorNombre(String nombre){
        return categoriaProductoRepository.getByNombre ( nombre );
    }

    public void delete(Integer id) {
        categoriaProductoRepository.delete ( id );
    }

    public void save(CategoriaProductoModel categoriaProductoModel) {
        categoriaProductoRepository.save ( categoriaProductoModel );
    }

    public CategoriaProductoModel obtenerCategoria(Integer id) {
        return categoriaProductoRepository.getOne ( id );
    }
}
