package absortio.m00p4.negocio.model.algoritmo.algoritmosPrincipales;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.*;

import java.util.ArrayList;
import java.util.List;

public class AlgoritmoTabu {
    private static final int CASTIGO = 3;
    private static final int VECINOS = 14;
    private Double menor;
    private Solucion menorSolucion;
    private ProblemaDef problemaDef;
    private FuncionObjetivo funcionObjetivo;
    private ArrayList<ArrayList<Integer>> listaTabu;
    private ArrayList<ArrayList<Integer>> cambios;
    private ArrayList<Solucion> listaSoluciones;
    private List<Double> listaCostos;
    private Solucion solucionActual;
    private Grasp grasp;
    private Integer timespan = 5000; // 270000; // 4.5 minutos


    public AlgoritmoTabu(ArrayList<BloqueModel> pbloques, ArrayList<Double> ppesos, ArrayList<Double> pvolumenes, MapaModel pmapa, Double pmaxVolumenCarro, Double pmaxPesoCarro) {
        problemaDef = new ProblemaDef(pbloques, ppesos, pvolumenes, pmapa, pmaxVolumenCarro, pmaxPesoCarro,null,null);
        funcionObjetivo = new FuncionObjetivo(problemaDef);
        listaTabu = new ArrayList<ArrayList<Integer>>();
        cambios = new ArrayList<ArrayList<Integer>>();
        listaSoluciones = new ArrayList<Solucion>();
        listaCostos = new ArrayList<>();
        grasp = new Grasp(funcionObjetivo, problemaDef);
    }

    private void generarSolucionesPosibles(Solucion solucionActual) {
        int indice1, indice2, aux;
        int n = solucionActual.Tamano();
        listaCostos.clear();
        listaSoluciones.clear();
        for (int i = 0; i < 5; i++) {
            indice1 = 0;
            indice2 = 0;
            while (indice1 == indice2) {
                indice1 = (int) (Math.random() * n);
                indice2 = (int) (Math.random() * n);
            }
            if (indice1 > indice2) {
                aux = indice1;
                indice1 = indice2;
                indice2 = aux;
            }
            listaSoluciones.add(new Solucion(funcionObjetivo));
            for (int j = 0; j < solucionActual.Tamano(); j++) {
                listaSoluciones.get(i).anadirPunto(solucionActual.obtener(j));
            }

            if (listaSoluciones.get(i).obtener(indice1) < listaSoluciones.get(i).obtener(indice2)) {
                cambios.get(i).set(0, listaSoluciones.get(i).obtener(indice1));
                cambios.get(i).set(1, listaSoluciones.get(i).obtener(indice2));
            } else {
                cambios.get(i).set(0, listaSoluciones.get(i).obtener(indice2));
                cambios.get(i).set(1, listaSoluciones.get(i).obtener(indice1));
            }

            aux = listaSoluciones.get(i).obtener(indice1);
            listaSoluciones.get(i).set(indice1, listaSoluciones.get(i).obtener(indice2));
            listaSoluciones.get(i).set(indice2, aux);

            listaCostos.add(listaSoluciones.get(i).Puntuacion());
        }
    }

    private int hallaMenor() {
        Integer posicionMenor = 0;
        Double menorActual = listaCostos.get(posicionMenor);

        for (int i = 1; i < listaCostos.size(); i++) {
            if (menorActual > listaCostos.get(i) && listaCostos.get(i) != Double.valueOf(-1)) {
                menorActual = listaCostos.get(i);
                posicionMenor = i;
            }
        }
        menor = menorActual;
        if (menorActual < 0) posicionMenor = -1;
        return posicionMenor;
    }

    private void modificarListaTabu(int n, int ind1, int ind2) {

        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (listaTabu.get(i).get(j) > 0)
                    listaTabu.get(i).set(j, listaTabu.get(i).get(j) - 1);
            }
        }

        listaTabu.get(ind1 - 1).set(ind2 - 1, CASTIGO);
        listaTabu.get(ind2 - 1).set(ind1 - 1, listaTabu.get(ind2 - 1).get(ind1 - 1) + 1); // += 1;
    }

    private boolean esTabu(Solucion solucion, int n, Integer posicionMenor) {
        if (listaTabu.get(cambios.get(posicionMenor).get(0) - 1).get(cambios.get(posicionMenor).get(1) - 1) == 0) {  // no hay castigo
            agregarSolucionTabu(solucion, n, posicionMenor);
            return false;
        }
        return true;
    }

    private void aspiracion(Solucion solucion, int n, Double funcionObjetivoActual, Integer posicionMenor) {
        if (funcionObjetivoActual < menor) {  // se evade el castigo
            agregarSolucionTabu(solucion, n, posicionMenor);
        }
    }

    private void agregarSolucionTabu(Solucion solucion, int n, Integer posicionMenor) {
        Solucion nuevaSolucionActual = new Solucion(funcionObjetivo);
        for (int j = 0; j < n; j++) {
            nuevaSolucionActual.anadirPunto(listaSoluciones.get(posicionMenor).obtener(j));
        }
        modificarListaTabu(n, cambios.get(posicionMenor).get(0), cambios.get(posicionMenor).get(1));
        solucionActual = nuevaSolucionActual;
    }

    private void inicializarListasEstaticas(int n) {
        for (int i = 0; i < n; i++) {
            listaTabu.add(new ArrayList<Integer>());
            for (int j = 0; j < n; j++) {
                listaTabu.get(i).add(0);
            }
        }
        for (int i = 0; i < VECINOS; i++) {
            cambios.add(new ArrayList<Integer>());
            for (int j = 0; j < 2; j++) {
                cambios.get(i).add(-1);
            }
        }
    }

    private void actualizarMenorSolucion() {
        for (Solucion solucion : listaSoluciones) {
            if (solucion.Puntuacion() < menorSolucion.Puntuacion()) {
                menorSolucion = solucion;
            }
        }
        if (solucionActual.Puntuacion() < menorSolucion.Puntuacion()) {
            menorSolucion = solucionActual;
        }
    }

    public Solucion correrAlgoritmo() {
        Long startingTime = System.currentTimeMillis();
        Solucion solucionInicial = grasp.generarSolucion();
        int n = solucionInicial.Tamano();
        inicializarListasEstaticas(n);
        Double funcionObjetivoActual = solucionInicial.Puntuacion();
        menorSolucion = solucionInicial;
        solucionActual = solucionInicial;

        while (System.currentTimeMillis() - startingTime <= timespan) {
            generarSolucionesPosibles(solucionActual);
            actualizarMenorSolucion();
            Integer posicionMenor = hallaMenor();
            while (true) {
                if (!esTabu(solucionActual, n, posicionMenor)) break;
                aspiracion(solucionActual, n, funcionObjetivoActual, posicionMenor);
                if (!esTabu(solucionActual, n, posicionMenor)) break;
                listaCostos.set(posicionMenor, -1.0); //busca otro
                posicionMenor = hallaMenor();
                if (posicionMenor == -1) break;
            }

        }
        return menorSolucion;
    }
}

