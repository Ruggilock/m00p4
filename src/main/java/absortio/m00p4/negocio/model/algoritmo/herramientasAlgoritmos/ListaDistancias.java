package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

import java.util.ArrayList;

public class ListaDistancias {

    private ArrayList<ArrayList<Double>> distancias;
    private ArrayList<ArrayList<ArrayList<Coordenada>>> rutas;
    private Integer dimension;
    //private AAsterisco aasterisco;
    private CalculadorDistancia calculadorDistancia;

    public ListaDistancias(ProblemaDef prob) {
        dimension = prob.numPuntos();
        //aasterisco = new AAsterisco(prob);
        calculadorDistancia = new CalculadorDistancia(prob);
        distancias = new ArrayList<>();
        for (Integer i = 0; i < dimension - 1; i++) {
            distancias.add(new ArrayList<>());
            for (Integer j = i; j < dimension - 1; j++) {
                distancias.get(i).add(-1.0);
            }
        }
        rutas = new ArrayList<>();
        for (Integer i = 0; i < dimension - 1; i++) {
            rutas.add(new ArrayList<>());
            for (Integer j = i; j < dimension - 1; j++) {
                rutas.get(i).add(new ArrayList<>());
            }
        }
    }

    private void SetDistancia(Integer punto1, Integer punto2, Double distancia) {
        if (punto1 > punto2) {
            Integer swap = punto2;
            punto2 = punto1;
            punto1 = swap;
        }

        distancias.get(punto1).set(punto2 - punto1 - 1, distancia);
    }

    public ArrayList<Coordenada> getRuta(Integer puntoInicio, Integer puntoFin) {
        Integer punto1, punto2;

        if (puntoInicio.equals(puntoFin)) return null;
        else if (puntoInicio > puntoFin) {
            punto1 = puntoFin;
            punto2 = puntoInicio;
        } else {
            punto1 = puntoInicio;
            punto2 = puntoFin;
        }

        Double distancia = distancias.get(punto1).get(punto2 - punto1 - 1);
        if (distancia < 0) {
            //SetDistancia(punto1, punto2, aasterisco.hallarDistancia(punto1, punto2, false, null));
            SetDistancia(punto1, punto2, calculadorDistancia.hallarDistancia(punto1, punto2, rutas));
        }

        if (punto2.equals(puntoInicio)) { // Si se realizo swap al inicio
            ArrayList<Coordenada> rutaInvertida = new ArrayList<Coordenada>();
            ArrayList<Coordenada> rutaOriginal = rutas.get(punto1).get(punto2 - punto1 - 1);
            for (int i = rutaOriginal.size() - 1; i >= 0; i--) {
                rutaInvertida.add(rutaOriginal.get(i));
            }
            return rutaInvertida;
        } else return rutas.get(punto1).get(punto2 - punto1 - 1);

    }

    public Double getDistancia(Integer punto1, Integer punto2) {
        if (punto1.equals(punto2)) return 0.0;

        if (punto1 > punto2) {
            Integer swap = punto2;
            punto2 = punto1;
            punto1 = swap;
        }

        Double distancia = distancias.get(punto1).get(punto2 - punto1 - 1);
        if (distancia < 0) {
            //SetDistancia(punto1, punto2, aasterisco.hallarDistancia(punto1, punto2, false, null));
            SetDistancia(punto1, punto2, calculadorDistancia.hallarDistancia(punto1, punto2, rutas));
        }
        return distancias.get(punto1).get(punto2 - punto1 - 1);
    }
}
