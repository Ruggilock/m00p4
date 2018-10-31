package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

import java.util.ArrayList;
import java.util.HashMap;

public class AAsterisco {
    private ProblemaDef problema;

    public AAsterisco(ProblemaDef pdef) {
        problema = pdef;
    }

    public Double valorH(Coordenada punto1, Coordenada punto2) {
        return Double.valueOf(punto1.manhattan(punto2));
    }

    public Double hallarDistancia(Integer punto1, Integer punto2, boolean crearMapa, ArrayList<ArrayList<String>> rutaTrazada) {
        Coordenada origen = new Coordenada(problema.getCoordenada(punto1).getX(), problema.getCoordenada(punto1).getY(), 0);
        Coordenada destino = new Coordenada(problema.getCoordenada(punto2).getX(), problema.getCoordenada(punto2).getY(), 0);
        ArrayList<Coordenada> revisados = new ArrayList<>(); // closed
        ArrayList<Coordenada> conocidos = new ArrayList<>(); // open
        conocidos.add(origen);
        HashMap<Coordenada, Coordenada> caminoOrigen = new HashMap<>();
        HashMap<Coordenada, Double> valorG = new HashMap<>();
        valorG.put(origen, 0.0);
        HashMap<Coordenada, Double> valorF = new HashMap<>();
        valorF.put(origen, valorH(origen, destino));
        while (conocidos.size() != 0) {
            Coordenada actual = obtenerMenorValorF(conocidos, valorF);
            if (actual.equals(destino)) {
                if (crearMapa) return trazarMapa(actual, caminoOrigen, rutaTrazada);
                else return calcularDistanciaFinal(actual, caminoOrigen) + Math.abs(problema.getCoordenada(punto1).getZ() - problema.getCoordenada(punto2).getZ());
            }
            conocidos.remove(actual);
            revisados.add(actual);
            for (Integer i = 0; i < 4; i++) { //0: arriba, 1: derecha, 2:abajo, 3:izquierda
                Coordenada vecino = aplicarDireccion(actual, i);
                if (noEsPasillo(vecino) || revisados.contains(vecino))
                    continue;
                if (!conocidos.contains(vecino))
                    conocidos.add(vecino);
                Double posibleValorG = valorG.get(actual) + 1;
                if (valorG.containsKey(vecino) && posibleValorG >= valorG.get(vecino))
                    continue;
                caminoOrigen.put(vecino, actual);
                valorG.put(vecino, posibleValorG);
                valorF.put(vecino, posibleValorG + valorH(vecino, destino));
            }
        }

        return -1.0;
    }

    private Coordenada obtenerMenorValorF(ArrayList<Coordenada> conocidos, HashMap<Coordenada, Double> valorF) {
        Coordenada minCoord = conocidos.get(0);
        Double minValF = valorF.get(minCoord);
        for (Integer i = 1; i < conocidos.size(); i++) {
            if (valorF.get(conocidos.get(i)) < minValF) {
                minCoord = conocidos.get(i);
                minValF = valorF.get(minCoord);
            }
        }
        return minCoord;
    }

    private Boolean noEsPasillo(Coordenada coord) {
        try {
            return (problema.obtenerMapa().get(coord.getY()).get((coord.getX())).compareTo(" ") != 0);
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
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

    private Double calcularDistanciaFinal(Coordenada actual, HashMap<Coordenada, Coordenada> caminoOrigen) {
        Integer distancia = 0;
        while (caminoOrigen.containsKey(actual)) {
            actual = caminoOrigen.get(actual);
            distancia++;
        }
        return Double.valueOf(distancia);
    }

    private Double trazarMapa(Coordenada actual, HashMap<Coordenada, Coordenada> caminoOrigen, ArrayList<ArrayList<String>> rutaTrazada) {
        Integer distancia = 0;
        rutaTrazada.get(actual.getY()).set(actual.getX(), "X");
        while (caminoOrigen.containsKey(actual)) {
            actual = caminoOrigen.get(actual);
            rutaTrazada.get(actual.getY()).set(actual.getX(), "X");
            distancia++;
        }
        return Double.valueOf(distancia);
    }


}
