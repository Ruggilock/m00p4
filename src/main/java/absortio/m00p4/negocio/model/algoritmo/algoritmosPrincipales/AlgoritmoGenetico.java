package absortio.m00p4.negocio.model.algoritmo.algoritmosPrincipales;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.*;
import absortio.m00p4.negocio.service.KardexService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import com.sun.javafx.scene.layout.region.Margins;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.Random;

public class AlgoritmoGenetico {

    private ProblemaDef problema;
    private FuncionObjetivo funcionObjetivo;
    private Double radioCruce = 0.7;
    private Double radioMutacion = 0.001;
    private Double radioElitismo = 0.05;
    private Integer tamanoPoblacion = 100;
    private Integer timespan = 5000; // 270000; // 4.5 minutos
    private Grasp grasp;

    public AlgoritmoGenetico(ArrayList<BloqueModel> pbloques, ArrayList<Double> ppesos, ArrayList<Double> pvolumenes, Double pmaxVolumenCarro, Double pmaxPesoCarro, ArrayList<ArrayList<String>> mapa, KardexService kardexService) {
        MapaModel pmapa= SystemSingleton.getInstance().getMapa();
        problema = new ProblemaDef(pbloques, ppesos, pvolumenes, pmapa, pmaxVolumenCarro, pmaxPesoCarro,mapa, kardexService);
        funcionObjetivo = new FuncionObjetivo(problema);
        grasp = new Grasp(funcionObjetivo, problema);
    }

    private Solucion generarSolucionAleatoria() {
        ArrayList<Integer> solucion = new ArrayList<>();
        Integer cantidadPuntos = problema.numPuntos() - 1;
        for (Integer i = 1; i <= cantidadPuntos; i++) {
            solucion.add(i);
        }
        Random random = new Random();
        for (Integer i = cantidadPuntos - 1; i > 0; i--) {
            Integer j = random.nextInt(i + 1);
            Integer aux = solucion.get(i);
            solucion.set(i, solucion.get(j));
            solucion.set(j, aux);
        }
        Solucion solucionObj = new Solucion(funcionObjetivo);
        for (Integer i = 0; i < solucion.size(); i++) {
            solucionObj.anadirPunto(solucion.get(i));
        }
        return solucionObj;
    }

    private ArrayList<Solucion> generarPoblacionInicial(Integer cantidadSoluciones) {
        ArrayList<Solucion> poblacionInicial = new ArrayList<>();
        for (Integer i = 0; i < cantidadSoluciones; i++) {
            if (problema.numPuntos() > 50)
                poblacionInicial.add(generarSolucionAleatoria());
            else
                poblacionInicial.add(grasp.generarSolucion());
        }
        return poblacionInicial;
    }

    private Solucion seleccionar(ArrayList<Solucion> soluciones) {
        Double seleccionAleatoria = Double.valueOf(Math.random());
        Double sumaPuntuacionesTotal = 0.0;
        for (Solucion solucion : soluciones) {
            sumaPuntuacionesTotal = sumaPuntuacionesTotal + solucion.Puntuacion();
        }
        Double sumaPuntuacionesTemp = 0.0;
        for (Solucion solucion : soluciones) {
            sumaPuntuacionesTemp = sumaPuntuacionesTemp + solucion.Puntuacion();
            if (sumaPuntuacionesTemp / sumaPuntuacionesTotal > seleccionAleatoria) {
                return solucion;
            }
        }
        return null; //no deberia llegar aca :O
    }

    private ArrayList<Solucion> cruce(Solucion solucion1, Solucion solucion2) {
        Solucion hijo1 = new Solucion(funcionObjetivo);
        Solucion hijo2 = new Solucion(funcionObjetivo);
        Random random = new Random();

        Integer puntoIntercambio1 = random.nextInt(solucion1.Tamano());
        Integer puntoIntercambio2 = random.nextInt(solucion1.Tamano());

        if (puntoIntercambio1 > puntoIntercambio2) {
            Integer aux = puntoIntercambio1;
            puntoIntercambio1 = puntoIntercambio2;
            puntoIntercambio2 = aux;
        }

        ArrayList<Integer> hijo1puntos = new ArrayList<>(solucion1.Tamano());
        ArrayList<Integer> hijo2puntos = new ArrayList<>(solucion1.Tamano());

        for (Integer i = 0; i < puntoIntercambio1; i++) {
            hijo1puntos.add(-1);
            hijo2puntos.add(-1);
        }
        for (Integer i = puntoIntercambio1; i <= puntoIntercambio2; i++) {
            hijo1puntos.add(solucion1.obtener(i));
            hijo2puntos.add(solucion2.obtener(i));
        }
        for (Integer i = puntoIntercambio2 + 1; i < solucion1.Tamano(); i++) {
            hijo1puntos.add(-1);
            hijo2puntos.add(-1);
        }

        for (Integer i = 0; i < solucion1.Tamano(); i++) {
            if (i == puntoIntercambio1) {
                i = puntoIntercambio2;
                continue;
            }
            Integer valorOriginal = solucion2.obtener(i);
            Integer valorAInsertar = valorOriginal;
            while (true) {
                if (hijo1puntos.contains(valorAInsertar)) {
                    valorAInsertar = hijo2puntos.get(hijo1puntos.indexOf(valorAInsertar));
                } else {
                    hijo1puntos.set(i, valorAInsertar);
                    hijo2puntos.set(solucion1.IndiceDe(valorAInsertar), valorOriginal);
                    break;
                }
            }
        }

        ArrayList<Solucion> hijos = new ArrayList<>();

        for (Integer i = 0; i < hijo1puntos.size(); i++) {
            hijo1.anadirPunto(hijo1puntos.get(i));
            hijo2.anadirPunto(hijo2puntos.get(i));
        }

        hijos.add(hijo1);
        hijos.add(hijo2);

        return hijos;

    }

    private void mutacion(ArrayList<Solucion> soluciones) {
        Random random = new Random();
        for (Solucion solucion : soluciones) {
            if (radioMutacion.compareTo(Double.valueOf(Math.random())) >= 0) {
                Integer puntoIntercambio1 = random.nextInt(solucion.Tamano());
                Integer puntoIntercambio2 = random.nextInt(solucion.Tamano());
                solucion.Intercambiar(puntoIntercambio1, puntoIntercambio2);
            }
        }
    }

    private ArrayList<Solucion> elitismo(ArrayList<Solucion> soluciones) {
        Integer cantidadElite = ((Double) Math.floor(radioElitismo * (tamanoPoblacion))).intValue();
        ArrayList<Solucion> solucionesElite = new ArrayList<>();
        for (Integer i = 0; i < cantidadElite && soluciones.size() > 0; i++) {
            Solucion solucionElite = obtenerMejorSolucion(soluciones);
            soluciones.remove(solucionElite);
            solucionesElite.add(solucionElite);
        }
        return solucionesElite;
    }

    private ArrayList<Solucion> limpieza(ArrayList<Solucion> soluciones, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            soluciones.remove(obtenerPeorSolucion(soluciones));
        }
        return soluciones;
    }

    private Solucion obtenerMejorSolucion(ArrayList<Solucion> soluciones) {
        Solucion solucion = soluciones.get(0);
        Double menorPuntuacion = solucion.Puntuacion();
        for (Integer i = 1; i < soluciones.size(); i++) {
            if (soluciones.get(i).Puntuacion() < menorPuntuacion) {
                solucion = soluciones.get(i);
                menorPuntuacion = solucion.Puntuacion();
            }
        }
        return solucion;
    }

    private Solucion obtenerPeorSolucion(ArrayList<Solucion> soluciones) {
        Solucion solucion = soluciones.get(0);
        Double mayorPuntuacion = solucion.Puntuacion();
        for (Integer i = 1; i < soluciones.size(); i++) {
            if (soluciones.get(i).Puntuacion() > mayorPuntuacion) {
                solucion = soluciones.get(i);
                mayorPuntuacion = solucion.Puntuacion();
            }
        }
        return solucion;
    }

    public Solucion correrAlgoritmo() {
        Long startingTime = System.currentTimeMillis();
        ArrayList<Solucion> soluciones = generarPoblacionInicial(tamanoPoblacion);
        Solucion mejorSolucion = obtenerMejorSolucion(soluciones);
        Double menorPuntuacion = mejorSolucion.Puntuacion();
        ArrayList<Solucion> nuevasSoluciones = new ArrayList<>();

        while (System.currentTimeMillis() - startingTime <= timespan) {
            nuevasSoluciones.clear();
            ArrayList<Solucion> antiguasSoluciones = new ArrayList<>();
            antiguasSoluciones.addAll(soluciones);
            while (soluciones.size() >= 2) {
                Solucion padre1 = seleccionar(soluciones);
                soluciones.remove(padre1);
                Solucion padre2 = seleccionar(soluciones);
                soluciones.remove(padre2);
                if (radioCruce >= Math.random()) {
                    ArrayList<Solucion> hijos = cruce(padre1, padre2);
                    nuevasSoluciones.add(hijos.get(0));
                    nuevasSoluciones.add(hijos.get(1));
                } else {
                    nuevasSoluciones.add(padre1);
                    nuevasSoluciones.add(padre2);
                }
            }
            Solucion mejorSolucionActual = obtenerMejorSolucion(nuevasSoluciones);
            if (mejorSolucionActual.Puntuacion() < menorPuntuacion) {
                mejorSolucion = mejorSolucionActual;
                menorPuntuacion = mejorSolucion.Puntuacion();
            }
            mutacion(nuevasSoluciones);
            mejorSolucionActual = obtenerMejorSolucion(nuevasSoluciones);
            if (mejorSolucionActual.Puntuacion() < menorPuntuacion) {
                mejorSolucion = mejorSolucionActual;
                menorPuntuacion = mejorSolucion.Puntuacion();
            }
            ArrayList<Solucion> elite = elitismo(antiguasSoluciones);
            nuevasSoluciones.addAll(elite);
            nuevasSoluciones = limpieza(nuevasSoluciones, elite.size());
            soluciones.clear();
            soluciones.addAll(nuevasSoluciones);
        }
        return mejorSolucion;
    }
}
