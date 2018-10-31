package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.service.KardexService;
import absortio.m00p4.negocio.service.MapaService;
import absortio.m00p4.negocio.ui.menu.almacenes.funciones.FuncionesMapa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

public class ProblemaDef {

    private HashMap<Integer, Coordenada> coordenadas;
    private HashMap<Integer, ProductoModel> productos;
    private HashMap<Integer, Coordenada> coordProductos;
    private ArrayList<ArrayList<String>> mapa;
    private ArrayList<Double> pesos;
    private ArrayList<Double> volumenes;
    private Double maxVolumenCarro;
    private Double maxPesoCarro;

    public ProblemaDef(ArrayList<BloqueModel> bloques, ArrayList<Double> ppesos, ArrayList<Double> pvolumenes, MapaModel pmapa, Double pmaxVolumenCarro, Double pmaxPesoCarro,ArrayList<ArrayList<String>> mapa, KardexService pkardexService) {
        pesos=new ArrayList<>();
        volumenes=new ArrayList<>();
        coordenadas = new HashMap<>();
        productos = new HashMap<>();
        coordProductos = new HashMap<>();
        coordenadas.put(0, new Coordenada(pmapa.getPuertax(),pmapa.getPuertay(),0));
        coordProductos.put(0,null);
        productos.put(0, null);
//        creadorMapa.crearMatriz(pmapa);
        this.mapa = mapa;
        for (Integer i = 0; i < bloques.size(); i++) {
            Integer posX = bloques.get(i).getPosX();
            Integer posY = bloques.get(i).getPosY();
            if (bloques.get(i).getIdRack().getOrientacion().compareTo("V") == 0) {
                if (mapa.get(posY).get(posX + 1).compareTo(" ") == 0) {
                    posX++;
                } else if (mapa.get(posY).get(posX - 1).compareTo(" ") == 0) {
                    posX--;
                } else return;
            } else if (bloques.get(i).getIdRack().getOrientacion().compareTo("H") == 0) {
                if (mapa.get(posY + 1).get(posX).compareTo(" ") == 0) {
                    posY++;
                } else if (mapa.get(posY - 1).get(posX).compareTo(" ") == 0) {
                    posY--;
                } else return;
            } else return;

            coordenadas.put(i + 1, new Coordenada(posX, posY, bloques.get(i).getPosZ()));
            coordProductos.put(i+1,new Coordenada(bloques.get(i).getPosX(), bloques.get(i).getPosY(),0));
            productos.put(i+1,pkardexService.findLoteInBloque(bloques.get(i).getId()).getLote().getProducto());
        }
        pesos.add(0, null);
        pesos.addAll(ppesos);
        volumenes.add(0, null);
        volumenes.addAll(pvolumenes);
        maxVolumenCarro = pmaxVolumenCarro;
        maxPesoCarro = pmaxPesoCarro;
    }

    public Integer numPuntos() {
        return coordenadas.size();
    }

    public Coordenada getCoordenada(Integer index) {
        return coordenadas.get(index);
    }

    public Double getPeso(Integer index) {
        return pesos.get(index);
    }

    public Double getVolumen(Integer index) {
        return volumenes.get(index);
    }

    public ArrayList<ArrayList<String>> obtenerMapa() {
        return mapa;
    }

    public Double getMaxVolumenCarro() {
        return maxVolumenCarro;
    }

    public Double getMaxPesoCarro() {
        return maxPesoCarro;
    }

    public ProductoModel getProducto(Integer index) { return productos.get(index); }

    public Coordenada getCoordProducto(Integer index) {
        return coordProductos.get(index);
    }
}
