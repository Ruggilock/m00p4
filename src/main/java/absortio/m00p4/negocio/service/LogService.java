package absortio.m00p4.negocio.service;

import absortio.m00p4.negocio.model.LogModel;
import absortio.m00p4.negocio.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// Aqui enlazamos los campos externos contra los queries de la BD

@Service
public class LogService {
    private LogRepository logRepository;
    @Autowired
    public LogService(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    public LogModel getByBD(String bd) { return logRepository.getByBd(bd); }
    public void delete(Integer id){
        logRepository.delete(id);
    }
    public void save( LogModel logModel){ logRepository.save(logModel);}


    public Integer hallarCantidadUsuariosByBD(String bd){
        return logRepository.hallarCantidadUsuariosByBD(bd);
    }

    public Integer updateUsuarioAndBDById(Integer id, String usuario) { return logRepository.updateUsuarioAndBDById(id, usuario) ;}


}



