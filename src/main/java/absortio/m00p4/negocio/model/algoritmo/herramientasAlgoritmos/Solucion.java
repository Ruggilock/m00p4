package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Solucion {


    private ArrayList<Integer> puntosOrdenados;
    private Double funcionObjetivo = -1.0;
    private FuncionObjetivo calcFuncionObjetivo;
    private ProblemaDef prob;
    private ArrayList<ArrayList<ArrayList<String>>> rutas;
    private ArrayList<ArrayList<Integer>> pasos;
    private ArrayList<ArrayList<Coordenada>> rutasCoord;
    private ArrayList<ProductoModel> listaProductos;

    public Solucion(FuncionObjetivo pfo) {
        calcFuncionObjetivo = pfo;
        prob = pfo.getProb();
        puntosOrdenados = new ArrayList<>();
    }

    public void anadirPunto(Integer punto) {
        puntosOrdenados.add(punto);
    }

    public Integer obtener(Integer indice) {
        return puntosOrdenados.get(indice);
    }

    public Integer Tamano() {
        return puntosOrdenados.size();
    }

    public Double Puntuacion() {
        if (funcionObjetivo < 0) {
            return (funcionObjetivo = calcFuncionObjetivo.calcular(this));
        } else {
            return funcionObjetivo;
        }
    }

    public void Intercambiar(Integer indice1, Integer indice2) {
        Integer aux = puntosOrdenados.get(indice1);
        puntosOrdenados.set(indice1, puntosOrdenados.get(indice2));
        puntosOrdenados.set(indice2, aux);
    }

    public void set(Integer posicion, Integer valor) {
        if (posicion < puntosOrdenados.size())
            puntosOrdenados.set(posicion, valor);
    }

    public Integer remove(Integer posicion) {
        if (posicion < puntosOrdenados.size()) {
            return puntosOrdenados.remove((int) posicion);
        }
        return null;
    }

    public Integer IndiceDe(Integer valor) {
        return puntosOrdenados.indexOf(valor);
    }

    public ArrayList<ArrayList<String>> copiarMapa() {
        ArrayList<ArrayList<String>> matrizOriginal = prob.obtenerMapa();
        ArrayList<ArrayList<String>> matrizCopia = new ArrayList<>();
        for (int i = 0; i < matrizOriginal.size(); i++) {
            matrizCopia.add(new ArrayList<>());
            for (int j = 0; j < matrizOriginal.get(i).size(); j++) {
                matrizCopia.get(i).add(matrizOriginal.get(i).get(j));
            }
        }
        return matrizCopia;
    }

    public ArrayList<ArrayList<ArrayList<String>>> obtenerRutas() {
        if (rutas != null) return rutas;
        Double maxPesoCarro = prob.getMaxPesoCarro();
        Double maxVolumenCarro = prob.getMaxVolumenCarro();

        ArrayList<ArrayList<String>> rutaActual = copiarMapa();
        ArrayList<ArrayList<ArrayList<String>>> nuevasRutas = new ArrayList<>();
        ArrayList<ArrayList<Integer>> nuevosPasos = new ArrayList<>();
        Double pesoActual = prob.getPeso(obtener(0));
        Double volumenActual = prob.getVolumen(obtener(0));
        dibujarRuta(rutaActual, 0, obtener(0));
        nuevosPasos.add(new ArrayList<>());
        nuevosPasos.get(0).add(this.obtener(0));
        int numRuta = 0;
        for (int i = 0; i < Tamano() - 1; i++) {
            if (pesoActual + prob.getPeso(obtener(i + 1)) > maxPesoCarro || volumenActual + prob.getVolumen(obtener(i + 1)) > maxVolumenCarro) {
                dibujarRuta(rutaActual, obtener(i), 0);
                nuevasRutas.add(rutaActual);
                rutaActual = copiarMapa();
                dibujarRuta(rutaActual, 0, obtener(i + 1));
                pesoActual = prob.getPeso(obtener(i + 1));
                volumenActual = prob.getVolumen(obtener(i + 1));
                nuevosPasos.add(new ArrayList<>());
                numRuta++;
                nuevosPasos.get(numRuta).add(this.obtener(i + 1));
            } else {
                dibujarRuta(rutaActual, obtener(i), obtener(i + 1));
                nuevosPasos.get(numRuta).add(this.obtener(i + 1));
                pesoActual = pesoActual + prob.getPeso(obtener(i + 1));
                volumenActual = volumenActual + prob.getVolumen(obtener(i + 1));
            }
        }
        dibujarRuta(rutaActual, obtener(Tamano() - 1), 0);
        nuevasRutas.add(rutaActual);
        rutas = nuevasRutas;
        pasos = nuevosPasos;
        return rutas;
    }

    public String exportarExcel() throws IOException {
        String nombre = "";
        if (SystemSingleton.getInstance().getRute() == null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = directoryChooser.showDialog(null);
            SystemSingleton.getInstance().setRute(file.getPath());
        }
        if (SystemSingleton.getInstance().getRute() != null) {
            nombre = ExcelSolucion.generarExcelSolucion(this);
        }
        return nombre;
    }

    private void dibujarRuta(ArrayList<ArrayList<String>> mapa, Integer posicionInicial, Integer posicionFinal) {
        ArrayList<Coordenada> ruta = calcFuncionObjetivo.getDistancias().getRuta(posicionInicial, posicionFinal);
        for (Coordenada punto : ruta) {
            mapa.get(punto.getY()).set(punto.getX(), "o");
        }
    }

    public String serializar() {
        ArrayList<ArrayList<ArrayList<String>>> rutas = obtenerRutas();
        Integer numRutas = rutas.size();
        Integer altura = rutas.get(0).size();
        Integer ancho = rutas.get(0).get(0).size();
        String serializacion = numRutas.toString() + ";";
        for (int i = 0; i < numRutas; i++) {
            for (int j = 0; j < pasos.get(i).size(); j++) {
                serializacion += pasos.get(i).get(j) + obtenerCoordenada(pasos.get(i).get(j)).toString(); //+ ";";
            }
            serializacion += ";";
        }

        serializacion += altura.toString() + ";" + ancho.toString() + ";";

        for (int i = 0; i < numRutas; i++) {
            ArrayList<ArrayList<String>> rutaActual = rutas.get(i);

            for (int j = 0; j < altura; j++) {
                ArrayList<String> columna = rutaActual.get(j);
                for (int k = 0; k < ancho; k++) {
                    String celda = columna.get(k);
                    serializacion += celda;
                }
            }
            serializacion += ";";
        }
        return serializacion;
    }

    public ArrayList<ProductoModel> getListaProductos() {
        if (listaProductos == null) {
            listaProductos = new ArrayList<>();
            Double maxPesoCarro = prob.getMaxPesoCarro();
            Double maxVolumenCarro = prob.getMaxVolumenCarro();
            listaProductos.add(prob.getProducto(this.obtener(0)));
            Double pesoActual = prob.getPeso(obtener(0));
            Double volumenActual = prob.getVolumen(obtener(0));
            for (int i = 0; i < Tamano() - 1; i++) {
                if (pesoActual + prob.getPeso(obtener(i + 1)) > maxPesoCarro || volumenActual + prob.getVolumen(obtener(i + 1)) > maxVolumenCarro) {
                    listaProductos.add(prob.getProducto(0));
                    listaProductos.add(prob.getProducto(obtener(i + 1)));
                    pesoActual = prob.getPeso(obtener(i + 1));
                    volumenActual = prob.getVolumen(obtener(i + 1));
                } else {
                    listaProductos.add(prob.getProducto(obtener(i + 1)));
                    pesoActual = pesoActual + prob.getPeso(obtener(i + 1));
                    volumenActual = volumenActual + prob.getVolumen(obtener(i + 1));
                }
            }
            listaProductos.add(null);
        }
        return listaProductos;
    }

    public ArrayList<ArrayList<Coordenada>> getRutasCoord() {
        if (rutasCoord == null) {
            rutasCoord = new ArrayList<>();
            Double maxPesoCarro = prob.getMaxPesoCarro();
            Double maxVolumenCarro = prob.getMaxVolumenCarro();
            rutasCoord.add(calcFuncionObjetivo.getDistancias().getRuta(0, obtener(0)));
            Double pesoActual = prob.getPeso(obtener(0));
            Double volumenActual = prob.getVolumen(obtener(0));

            for (int i = 0; i < Tamano() - 1; i++) {
                if (pesoActual + prob.getPeso(obtener(i + 1)) > maxPesoCarro || volumenActual + prob.getVolumen(obtener(i + 1)) > maxVolumenCarro) {
                    rutasCoord.add(calcFuncionObjetivo.getDistancias().getRuta(obtener(i), 0));
                    rutasCoord.add(calcFuncionObjetivo.getDistancias().getRuta(0, obtener(i + 1)));
                    pesoActual = prob.getPeso(obtener(i + 1));
                    volumenActual = prob.getVolumen(obtener(i + 1));
                } else {
                    rutasCoord.add(calcFuncionObjetivo.getDistancias().getRuta(obtener(i), obtener(i + 1)));
                    pesoActual = pesoActual + prob.getPeso(obtener(i + 1));
                    volumenActual = volumenActual + prob.getVolumen(obtener(i + 1));
                }
            }
            rutasCoord.add(calcFuncionObjetivo.getDistancias().getRuta(obtener(Tamano() - 1), 0));
        }
        return rutasCoord;
    }

    public ArrayList<Integer> getPuntosOrdenados() {
        return puntosOrdenados;
    }

    public void setPuntosOrdenados(ArrayList<Integer> puntosOrdenados) {
        this.puntosOrdenados = puntosOrdenados;
    }

    public Double getFuncionObjetivo() {
        return funcionObjetivo;
    }

    public void setFuncionObjetivo(Double funcionObjetivo) {
        this.funcionObjetivo = funcionObjetivo;
    }

    public FuncionObjetivo getCalcFuncionObjetivo() {
        return calcFuncionObjetivo;
    }

    public void setCalcFuncionObjetivo(FuncionObjetivo calcFuncionObjetivo) {
        this.calcFuncionObjetivo = calcFuncionObjetivo;
    }

    public ArrayList<ArrayList<Integer>> getPasos() {
        return pasos;
    }

    public Coordenada obtenerCoordenada(Integer indice) {
        return prob.getCoordenada(indice);
    }
}
