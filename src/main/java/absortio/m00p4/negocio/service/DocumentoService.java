package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.DocumentoModel;
import absortio.m00p4.negocio.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DocumentoService {
    private DocumentoRepository documentoRepository;

    @Autowired
    public DocumentoService(DocumentoRepository documentoRepository) {
        this.documentoRepository = documentoRepository;
    }

        public DocumentoModel save(DocumentoModel documento){
            return documentoRepository.save ( documento );
        }

        @Transactional
        public void setNoDoc(Integer id, String tipodoc){
            documentoRepository.setNoDoc(id, tipodoc.substring(0,3).toUpperCase()+"-"+id.toString());
        }


    public DocumentoModel getByNoDocumento(String noDocumento) {
        return documentoRepository.getByNoDocumento(noDocumento);
    }

    public List<DocumentoModel> obtenerVentas() {
        return documentoRepository.findAll();
    }

    public List<DocumentoModel> findByTodosFiltros(String cliente, String documento, String fechaIni, String fechaFin, String estado, String tipoDoc, Double cantIni, Double cantFin){
            return documentoRepository.findByTodosFiltros(cliente, documento, fechaIni, fechaFin, estado, tipoDoc, cantIni, cantFin);
    }

    public DocumentoModel getByNoDocumentoAndEstado(String nodoc, String estado) {
        return documentoRepository.getByNoDocumentoAndEstado(nodoc,estado);
    }

    public  DocumentoModel getById(Integer id){
        return  documentoRepository.getById(id);
    }
}
