package absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos;

import absortio.m00p4.negocio.model.PermisoModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Optional;

public class Validador {
    public static boolean soloLetrasONumeros(String cadena) {
        for (char c : cadena.toCharArray()) {
            if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c != ' ')
                return false;
        }
        return true;
    }

    public static boolean soloNumeros(String cadena) {
        for (char c : cadena.toCharArray()) {
            if (!Character.isDigit(c) && c != ' ') return false;
        }
        return true;
    }

    public static boolean soloLetras(String cadena) {
        for (char c : cadena.toCharArray()) {
            if (!Character.isAlphabetic(c) && c != ' ') return false;
        }
        return true;
    }

    public static boolean formatoCorreo(String cadena) {
        if (!(cadena.contains("@") && cadena.contains(".")) //No contiene arroba ni punto
                || cadena.indexOf('@') == 0 //Empieza por arroba
                || !soloLetrasONumeros(cadena.toString().replace('@', 'a').replace('.', 'a')))
            return false;
        else return true;
    }

    public static boolean formatoTelefono(String cadena) {
        for (char c : cadena.toCharArray()) {
            if (!Character.isDigit(c) && c != '(' && c != ')' && c != '-' && c != ' ') return false;
        }
        return true;
    }

    public static void mostrarDialogError(String textoDialog) {
        Alert dialogoAlerta = new Alert(Alert.AlertType.ERROR);
        dialogoAlerta.setTitle("¡Error!");
        dialogoAlerta.setHeaderText(null);
        dialogoAlerta.initStyle(StageStyle.UTILITY);
        dialogoAlerta.setContentText(textoDialog);
        Optional<ButtonType> result = dialogoAlerta.showAndWait();
    }

    public static boolean tienePermiso(String nombrePermiso, List<PermisoModel> permisos) {
        for (PermisoModel permiso : permisos) {
            if (permiso.getNombre().equals(nombrePermiso))
                return true;
        }
        return false;
    }

}
