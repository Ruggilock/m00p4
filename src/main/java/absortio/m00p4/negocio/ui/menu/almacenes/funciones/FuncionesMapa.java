package absortio.m00p4.negocio.ui.menu.almacenes.funciones;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.RackModel;
import absortio.m00p4.negocio.model.SectorAlmacenModel;
import absortio.m00p4.negocio.service.BloqueService;
import absortio.m00p4.negocio.service.RackService;
import absortio.m00p4.negocio.service.SectorAlmacenService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
@Component
public class FuncionesMapa {
    @Autowired
    SectorAlmacenService sectorAlmacenService;
    @Autowired
    RackService rackService;
    @Autowired
    BloqueService bloqueService;

    private ArrayList<ArrayList<String>> mapaDibujado;

    public void crearMatriz(MapaModel mapaModel) {

        mapaDibujado= new ArrayList<>();
        for (int i = 0; i < mapaModel.getLargo(); i++) {
            ArrayList<String> fila = new ArrayList<>();
            for (int j = 0; j < mapaModel.getAncho(); j++) {
                if (i == 0 || i == mapaModel.getLargo() - 1) {
                    fila.add("=");
                } else if (j == 0 || j == mapaModel.getAncho() - 1) {
                    fila.add("=");
                } else fila.add(" ");
            }
            mapaDibujado.add(fila);
        }
    }

    public void llenarMapa(MapaModel mapaModel) {
        Collection<SectorAlmacenModel> almacens = sectorAlmacenService.getAllByIdMapa(mapaModel);
        for (int i = 0; i < almacens.size(); i++) {
            for (SectorAlmacenModel almacen : almacens) {
                Collection<RackModel> racks = rackService.getAllByIdSectorAlmacen(almacen);
                for (RackModel rack : racks) {
                    Collection<BloqueModel> bloques = bloqueService.getAllByIdRack(rack);
                    for (BloqueModel bloque : bloques) {
                        mapaDibujado.get(bloque.getPosY()).set(bloque.getPosX(), "r");
                    }
                }
            }
        }
        SystemSingleton.getInstance().setMapa(mapaModel);
    }

    public void printMatriz(MapaModel mapaModel){
        for (int i = 0; i < mapaModel.getLargo() ; i++) {
            for (int j = 0; j < mapaModel.getAncho(); j++) {
                System.out.print(mapaDibujado.get(i).get(j));
            }
            System.out.println(" ");
        }
    }




    public ArrayList<ArrayList<String>> getMapaDibujado() {
        return mapaDibujado;
    }

    public void setMapaDibujado(ArrayList<ArrayList<String>> mapaDibujado) {
        this.mapaDibujado = mapaDibujado;
    }
}
