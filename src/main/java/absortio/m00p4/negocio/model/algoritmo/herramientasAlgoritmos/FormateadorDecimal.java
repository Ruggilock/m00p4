package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

import java.text.DecimalFormat;

public class FormateadorDecimal {

    public static String formatear(Double valor) {
        DecimalFormat formateador = new DecimalFormat("#.##");
        String numeroFormateado = formateador.format(valor);
        return numeroFormateado.replace(',', '.');
    }

}
