package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

import java.util.ArrayList;
import java.util.Random;

public class CalculadorDistancia {
    private ProblemaDef problema;
    private Double alfa = 0.33;
    private Integer numIteraciones = 500;

    public CalculadorDistancia(ProblemaDef pdef) {
        problema = pdef;
    }

    public Double hallarDistancia(Integer punto1, Integer punto2, ArrayList<ArrayList<ArrayList<Coordenada>>> rutas) {
        Coordenada origen = new Coordenada(problema.getCoordenada(punto1).getX(), problema.getCoordenada(punto1).getY(), 0);
        Coordenada destino = new Coordenada(problema.getCoordenada(punto2).getX(), problema.getCoordenada(punto2).getY(), 0);

        if (origen.equals(destino)) {
            ArrayList<Coordenada> rutaSinMoverse = new ArrayList<Coordenada>();
            rutaSinMoverse.add(origen);
            rutas.get(punto1).set(punto2 - punto1 - 1, rutaSinMoverse);
            return Double.valueOf(Math.abs(problema.getCoordenada(punto1).getZ() - problema.getCoordenada(punto2).getZ()));
        }

        Random random = new Random();
        ArrayList<Coordenada> mejorRuta = null;

        if (origen.equals(destino)) {
            mejorRuta = new ArrayList<>();
            mejorRuta.add(origen);
            insertarRuta(rutas,punto1,punto2,mejorRuta);
            return Double.valueOf(Math.abs(problema.getCoordenada(punto1).getZ() - problema.getCoordenada(punto2).getZ()));
        }

        for (Integer i = 0; i < numIteraciones; i++) {
            ArrayList<Coordenada> ruta = new ArrayList<Coordenada>();
            boolean fallo = false;
            ruta.add(origen);
            Coordenada ultimoAnadido = origen;
            ArrayList<ArrayList<String>> mapaDibujadoCopia = copiarMapa();
            do {
                ArrayList<Coordenada> vecinos = new ArrayList<Coordenada>();
                ArrayList<Coordenada> rcl = new ArrayList<Coordenada>();
                for (Integer j = 0; j < 4; j++) { //0: arriba, 1: derecha, 2:abajo, 3:izquierda
                    Coordenada vecino = aplicarDireccion(ultimoAnadido, j);
                    if (esPasillo(vecino, mapaDibujadoCopia))
                        vecinos.add(vecino);
                }
                if (vecinos.isEmpty()) {// || (mejorRuta != null && ruta.size() > mejorRuta.size())) {
                    fallo = true;
                    break;
                } else if (vecinos.size() == 1) {
                    ruta.add(vecinos.get(0));
                    mapaDibujadoCopia.get(ultimoAnadido.getY()).set(ultimoAnadido.getX(), "x");
                    ultimoAnadido = vecinos.get(0);
                    continue;
                }
                Integer menorValorGreedy = hallarMenorValorGreedy(vecinos, destino); // El vecino que me acerca mas
                Integer mayorValorGreedy = hallarMayorValorGreedy(vecinos, destino); // EL vecino que me aleja mas
                for (Coordenada vecino : vecinos) {
                    if (vecino.manhattan(destino) <= menorValorGreedy + alfa * (mayorValorGreedy - menorValorGreedy)) {
                        rcl.add(vecino);
                    }
                }
                Coordenada vecinoTomado = rcl.get(random.nextInt(rcl.size()));
                ruta.add(vecinoTomado);
                mapaDibujadoCopia.get(ultimoAnadido.getY()).set(ultimoAnadido.getX(), "x");
                ultimoAnadido = vecinoTomado;

            } while (!ultimoAnadido.equals(destino));
            if (!fallo) {
                if (mejorRuta == null)
                    mejorRuta = ruta;
                else if (mejorRuta.size() > ruta.size()) {
                    mejorRuta = ruta;
                }
            } else if (i == (numIteraciones - 1) && mejorRuta == null)
                i--;
        }
        insertarRuta(rutas, punto1, punto2, mejorRuta);
        return Double.valueOf(mejorRuta.size() + (Math.abs(problema.getCoordenada(punto1).getZ() - problema.getCoordenada(punto2).getZ())));
    }

    public ArrayList<ArrayList<String>> copiarMapa() {
        ArrayList<ArrayList<String>> matrizOriginal = problema.obtenerMapa();
        ArrayList<ArrayList<String>> matrizCopia = new ArrayList<>();
        for (int i = 0; i < matrizOriginal.size(); i++) {
            matrizCopia.add(new ArrayList<>());
            for (int j = 0; j < matrizOriginal.get(i).size(); j++) {
                matrizCopia.get(i).add(matrizOriginal.get(i).get(j));
            }
        }
        return matrizCopia;
    }

    private void insertarRuta(ArrayList<ArrayList<ArrayList<Coordenada>>> rutas, Integer punto1, Integer punto2, ArrayList<Coordenada> ruta) {
        if (punto1 > punto2) {
            Integer swap = punto2;
            punto2 = punto1;
            punto1 = swap;
        }
        rutas.get(punto1).set(punto2 - punto1 - 1, ruta);
    }

    private Integer hallarMenorValorGreedy(ArrayList<Coordenada> vecinos, Coordenada destino) {
        Integer menorValorGreedy = vecinos.get(0).manhattan(destino);
        for (Integer i = 1; i < vecinos.size(); i++) {
            if (vecinos.get(i).manhattan(destino) < menorValorGreedy)
                menorValorGreedy = vecinos.get(i).manhattan(destino);
        }
        return menorValorGreedy;
    }

    private Integer hallarMayorValorGreedy(ArrayList<Coordenada> vecinos, Coordenada destino) {
        Integer mayorValorGreedy = vecinos.get(0).manhattan(destino);
        for (Integer i = 1; i < vecinos.size(); i++) {
            if (vecinos.get(i).manhattan(destino) > mayorValorGreedy)
                mayorValorGreedy = vecinos.get(i).manhattan(destino);
        }
        return mayorValorGreedy;
    }

    private Coordenada aplicarDireccion(Coordenada coord, Integer direccion) {
        Coordenada nuevaCoord = new Coordenada(coord.getX(), coord.getY(), coord.getZ());
        switch (direccion) {
            case 0:
                nuevaCoord.setY(nuevaCoord.getY() - 1);
                break;
            case 1:
                nuevaCoord.setX(nuevaCoord.getX() + 1);
                break;
            case 2:
                nuevaCoord.setY(nuevaCoord.getY() + 1);
                break;
            case 3:
                nuevaCoord.setX(nuevaCoord.getX() - 1);
                break;
        }
        return nuevaCoord;
    }

    private Boolean esPasillo(Coordenada coord, ArrayList<ArrayList<String>> mapaDibujadoCopia) {
        try {
            return (mapaDibujadoCopia.get(coord.getY()).get((coord.getX())).compareTo(" ") == 0);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
