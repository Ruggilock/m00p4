package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.DespachoModel;
import absortio.m00p4.negocio.model.DetalleDespachoModel;
import absortio.m00p4.negocio.model.auxiliares.DetalleDespacho;
import absortio.m00p4.negocio.repository.DetalleDespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleDespachoService {
    private DetalleDespachoRepository detalleDespachoRepository;

    @Autowired
    public DetalleDespachoService(DetalleDespachoRepository detalleDespachoRepository) {
        this.detalleDespachoRepository = detalleDespachoRepository;
    }

    public List<DetalleDespachoModel> findAllDetalleDespacho() {
        return detalleDespachoRepository.findAll();
    }

    public void delete(Integer id) {
        detalleDespachoRepository.delete(id);
    }

    public void save(DetalleDespachoModel detalleDespachoModel) {
        detalleDespachoRepository.save(detalleDespachoModel);
    }

    public List<DetalleDespachoModel> findAllDetalleDespachoByIdDespacho(Integer id) {
        return detalleDespachoRepository.findAllDetalleDespachoByIdDespacho(id);
    }

    public List<DetalleDespachoModel> findAllByIdDespacho(DespachoModel despachoModel) {
        return detalleDespachoRepository.findAllByIdDespacho(despachoModel);
    }

    public void deletePorAtender(Integer idDespacho) {
        detalleDespachoRepository.updateEstadoDetalleDespacho(idDespacho);
    }

    public DetalleDespachoModel getById(Integer id) {
        return detalleDespachoRepository.getById(id);
    }
}
