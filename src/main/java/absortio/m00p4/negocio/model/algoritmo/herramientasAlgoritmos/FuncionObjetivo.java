package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

public class FuncionObjetivo {

    private ListaDistancias distancias;
    private Double maxVolumenCarro;
    private Double maxPesoCarro;
    private ProblemaDef prob;

    public FuncionObjetivo(ProblemaDef pprob) {
        distancias = new ListaDistancias(pprob);
        maxPesoCarro = pprob.getMaxPesoCarro();
        maxVolumenCarro = pprob.getMaxVolumenCarro();
        prob = pprob;
    }

    public ListaDistancias getDistancias() {
        return distancias;
    }

    public Double calcular(Solucion solucion) {
        Double puntuacion = distancias.getDistancia(0, solucion.obtener(0));
        Double pesoActual = prob.getPeso(solucion.obtener(0));
        Double volumenActual = prob.getVolumen(solucion.obtener(0));

        for (Integer i = 0; i < solucion.Tamano() - 1; i++) {
            if (pesoActual + prob.getPeso(solucion.obtener(i + 1)) > maxPesoCarro || volumenActual + prob.getVolumen(solucion.obtener(i + 1)) > maxVolumenCarro) {
                puntuacion = puntuacion + distancias.getDistancia(0, solucion.obtener(i));
                puntuacion = puntuacion + distancias.getDistancia(0, solucion.obtener(i + 1));
                pesoActual = prob.getPeso(solucion.obtener(i+1));
                volumenActual = prob.getVolumen(solucion.obtener(i+1));
            } else {
                puntuacion = puntuacion + distancias.getDistancia(solucion.obtener(i), solucion.obtener(i + 1));
                pesoActual = pesoActual + prob.getPeso(solucion.obtener(i + 1));
                volumenActual = volumenActual + prob.getVolumen(solucion.obtener(i+1));
            }
        }
        puntuacion = puntuacion + distancias.getDistancia(0, solucion.obtener(solucion.Tamano() - 1));
        return puntuacion;
    }

    public ProblemaDef getProb() {
        return prob;
    }

    public void setProb(ProblemaDef prob) {
        this.prob = prob;
    }
}
