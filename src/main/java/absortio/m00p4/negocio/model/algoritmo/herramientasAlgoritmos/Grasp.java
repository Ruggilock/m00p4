package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grasp {

    static final int VECINOS = 20;// setear respecto al tamaño de
    private FuncionObjetivo funcionObjetivo;
    private ProblemaDef problema;
    private Double alfa = 0.2;

    public Grasp(FuncionObjetivo pfuncionObjetivo, ProblemaDef pproblemaDef) {
        funcionObjetivo = pfuncionObjetivo;
        problema = pproblemaDef;
    }


    private int seRepite(int num, ArrayList<ArrayList<Integer>> matCantT, int iMat, Solucion solucion, int index) {
        int repite = 0;

        for (int i = 0; i < iMat; i++) {
            if (num == matCantT.get(0).get(i)) {
                repite = 1;
                break;
            }
        }

        for (int i = 1; i < index; i++) {
            if (num == solucion.obtener(i)) {
                repite = 1;
                break;
            }
        }

        return repite;
    }

    private Double calcularBeneficio(Solucion solucion, int index, int num, Double beneficio) {

        int menor = 0, mayor = 0;
        if (solucion.obtener(index - 1) > num) {
            menor = num;
            mayor = solucion.obtener(index - 1);
        } else {
            mayor = num;
            menor = solucion.obtener(index - 1);
        }
        return beneficio + funcionObjetivo.getDistancias().getDistancia(menor, mayor) + funcionObjetivo.getDistancias().getDistancia(0, num); //+ hallarDistanciaCxC(menor, mayor) + hallarDistanciaCxC(0, num);
    }

    private int hallarPosMenor(ArrayList<ArrayList<Integer>> matCantT, int k) {
        int menorActual = matCantT.get(1).get(0), pos = 0;
        for (int i = 1; i < k; i++) {
            if (matCantT.get(1).get(i) < menorActual) {
                menorActual = matCantT.get(1).get(i);
                pos = i;
            }
        }
        return pos;
    }

    private void inicializarMatrizEstatica(ArrayList<ArrayList<Integer>> matriz, Integer numFilas, Integer numColumnas) {
        for (Integer i = 0; i < numFilas; i++) {
            matriz.add(new ArrayList<Integer>());
            for (Integer j = 0; j < numColumnas; j++) {
                matriz.get(i).add(-1);
            }
        }
    }

    private void rellenarCandidatos(ArrayList<ArrayList<Integer>> matCandidatos, Solucion solucion,
                                    int cantC, int index, Double beneficio) {

        int k = cantC - index + 1;
        ArrayList<ArrayList<Integer>> matCantT = new ArrayList<>();

        inicializarMatrizEstatica(matCantT, 2, k);

        int iMat = 0;
        for (int num = 1; num <= cantC; num++) { //calcula todos los candidatos posibles con su beneficio
            if (seRepite(num, matCantT, iMat, solucion, index) == 0) {
                matCantT.get(0).set(iMat, num);
                matCantT.get(1).set(iMat, calcularBeneficio(solucion, index, num, beneficio).intValue()); //se halla su beneficio
                iMat++;
            }
            if (k == iMat) break; // si ya lleno la lista
        }

        Double puntuacionMenor = hallarMenorBeneficio(matCantT);
        Double puntuacionMayor = hallarMayorBeneficio(matCantT);
        for (Integer i = 0; i < k; i++) {
            if (matCantT.get(1).get(i) <= (puntuacionMenor + (alfa*(puntuacionMayor-puntuacionMenor)))) {
                matCandidatos.get(0).add(matCantT.get(0).get(i));
                matCandidatos.get(1).add(matCantT.get(1).get(i));
            }
        }

    }

    private Double hallarMenorBeneficio(ArrayList<ArrayList<Integer>> matCantT) {
        Integer menorBeneficio = matCantT.get(1).get(0);
        for (Integer i = 1; i < matCantT.get(1).size(); i++) {
            if (matCantT.get(1).get(1) < menorBeneficio)
                menorBeneficio = matCantT.get(1).get(1);
        }
        return Double.valueOf(menorBeneficio);
    }

    private Double hallarMayorBeneficio(ArrayList<ArrayList<Integer>> matCantT) {
        Integer mayorBeneficio = matCantT.get(1).get(0);
        for (Integer i = 1; i < matCantT.get(1).size(); i++) {
            if (matCantT.get(1).get(1) > mayorBeneficio)
                mayorBeneficio = matCantT.get(1).get(1);
        }
        return Double.valueOf(mayorBeneficio);
    }

    private void funcionGreedy(Solucion solucion, int cantC, int index, Double beneficio) {

        int eleccion = 0;

        ArrayList<ArrayList<Integer>> matCandidatos = new ArrayList<>();

        matCandidatos.add(new ArrayList<Integer>());
        matCandidatos.add(new ArrayList<Integer>());

        rellenarCandidatos(matCandidatos, solucion, cantC, index, beneficio);

        eleccion = (new Random()).nextInt(matCandidatos.get(1).size());
        solucion.anadirPunto(matCandidatos.get(0).get(eleccion));
    }

    private Solucion faseConstructiva(int cantC) {

        Solucion solucion = new Solucion(funcionObjetivo);
        solucion.anadirPunto(0);

        Double beneficio = 0.0;

        for (int index = 1; index <= cantC; index++) {
            funcionGreedy(solucion, cantC, index, beneficio);
        }

        solucion.remove(0);

        return solucion;
    }

    private void generarVecinos(Solucion solucion, ArrayList<Solucion> listSol, List<Double> listFO) { //, int[][] cambios) {
        int indice1, indice2, aux;
        int n = solucion.Tamano();

        listFO.clear();
        for (int i = 0; i < VECINOS; i++) {
            indice1 = 0;
            indice2 = 0;
            while (indice1 == indice2) {
                Random random = new Random();
                indice1 = (int) (random.nextInt(n - 1)) + 1;
                indice2 = (int) (random.nextInt(n - 1)) + 1;
            }
            if (indice1 > indice2) {
                aux = indice1;
                indice1 = indice2;
                indice2 = aux;
            }

            listSol.add(new Solucion(funcionObjetivo));

            for (int j = 0; j < solucion.Tamano(); j++) {
                listSol.get(i).anadirPunto(solucion.obtener(j));
            }

            aux = listSol.get(i).obtener(indice1);
            listSol.get(i).set(indice1, listSol.get(i).obtener(indice2));
            listSol.get(i).set(indice2, aux);

            listFO.add(listSol.get(i).Puntuacion());
        }

    }

    private Integer hallaMenor(List<Double> lista) {
        Double menorActual = lista.get(0);
        Integer posicionMenor = 0;
        for (int i = 1; i < lista.size(); i++) {
            if (lista.get(i) < menorActual) {
                menorActual = lista.get(i);
                posicionMenor = i;
            }
        }
        return posicionMenor;
    }

    private Solucion faseBusqueda(Solucion solucion) {

        int n = solucion.Tamano();
        ArrayList<Solucion> listSol = new ArrayList<Solucion>();
        List<Double> listFO = new ArrayList<Double>();

        generarVecinos(solucion, listSol, listFO);
        Double FOsolucion = solucion.Puntuacion();

        int posicionMenor = hallaMenor(listFO);
        Double menorFO = listFO.get(posicionMenor);

        if (menorFO < FOsolucion)
            return listSol.get(posicionMenor);
        else
            return solucion;
    }

    public Solucion generarSolucion() {
        Solucion solucion = faseConstructiva(problema.numPuntos() - 1);
        return solucion; //faseBusqueda(solucion);
    }

    private void imprimir2(List<Integer> lista) {
        for (int i = 0; i < lista.size(); i++) {
            System.out.print(lista.get(i));
        }

    }
}