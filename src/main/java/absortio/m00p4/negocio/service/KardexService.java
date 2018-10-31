package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.KardexModel;
import absortio.m00p4.negocio.model.auxiliares.Kardex;
import absortio.m00p4.negocio.repository.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KardexService {
    private KardexRepository kardexRepository;

    @Autowired
    public KardexService(KardexRepository kardexRepository) {
        this.kardexRepository = kardexRepository;
    }

    public List<KardexModel> findAllKardex() {
        return kardexRepository.findAll();
    }

    public void delete(Integer id) {
        kardexRepository.delete(id);
    }

    public KardexModel save(KardexModel kardexModel) {
        return kardexRepository.save(kardexModel);
    }

    public List<Integer> findAllDistinctForLote(Integer id) {
        return kardexRepository.findAllDistinctForLote(id);
    }

    public List<KardexModel> findOperacion(Integer id) {
        return kardexRepository.findOperacion(id);
    }

    public List<KardexModel> findAllByMotivooperacion_Tipooperacion_NombreAndMotivooperacion_Nombre(String tipooperacion, String motivooperacion) {
        return kardexRepository.findAllByMotivooperacion_Tipooperacion_NombreAndMotivooperacion_Nombre(tipooperacion, motivooperacion);
    }

    public List<KardexModel> findByTodosFiltros(String codbarras, String prod, String tipo, String motivo, String sector, String rack, String fechaIni, String fechaFin, Integer cantIni, Integer cantFin) {
        if (tipo.compareTo("-Seleccione-") == 0) tipo = "";
        if (motivo.compareTo("-Seleccione-") == 0) motivo = "";
        if (sector.compareTo("-Seleccione-") == 0) sector = "";
        if (rack.compareTo("-Seleccione-") == 0) rack = "";

        return kardexRepository.buscarPorTodosFiltros(codbarras, prod, tipo, motivo, sector, rack, fechaIni, fechaFin, cantIni, cantFin);
    }

    public List<KardexModel> findAllOrdenados(){
        return kardexRepository.findAllOrdenados();
    }

    public Integer hallarStockByLoteYBloque(Integer idLote, Integer idBloque){
        Integer stockFinal = (int) (long) kardexRepository.hallarStockByLoteYBloque(idLote, idBloque);
        return stockFinal ;
    }


    public List<KardexModel> findByTodosFiltros(String lote, String codbarras, String producto, String categoria, String marca, String modelo, String seccion, String rack) {
        if (categoria.compareTo("-Seleccione-") == 0) categoria = "";
        if (marca.compareTo("-Seleccione-") == 0) marca = "";
        if (modelo.compareTo("-Seleccione-") == 0) modelo = "";
        if (seccion.compareTo("-Seleccione-") == 0) seccion = "";
        if (rack.compareTo("-Seleccione-") == 0) rack = "";

        return kardexRepository.buscarPorTodosFiltrosV2(lote, codbarras, producto, categoria, marca, modelo, seccion, rack);
    }
    public List<Integer> findAllDistinctforLoteByAlmacen(Integer idlote, String nombreAlmacen){
        return kardexRepository.findAllDistinctforLoteByAlmacen(idlote,nombreAlmacen);
    }

    public List<String>findAllAlmacenesWithLote(Integer idlote){
        return  kardexRepository.findAllAlmacenesWithLote(idlote);
    }

    public KardexModel findLoteInBloque(Integer idbloque){
        return kardexRepository.findHistoricoLoteInBloque(idbloque).get(0);
    }
}
