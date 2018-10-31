package absortio.m00p4.negocio.ui.menu.modal;

import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.service.UsuarioService;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;


@Component
public class cambioClaveController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(cambioClaveController.class);
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;
    @FXML
    private Button botonAceptar;
    @FXML
    private PasswordField passwordFieldAnterior;
    @FXML
    private PasswordField passwordFieldNueva;
    @FXML
    private PasswordField passwordFieldConfirmar;
    @FXML
    private Label labelMensaje;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ApplicationContext context;
    UsuarioModel usuarioModel;


    @FXML
    private void buttonAceptar(Event event) throws IOException {


        if (passwordFieldAnterior.getText().equals(usuarioModel.getContrasena())) {
            if (passwordFieldNueva.getText().equals(passwordFieldConfirmar.getText())) {
                usuarioModel.setContrasena(passwordFieldNueva.getText());
                Date date = new Date();
                usuarioModel.setFechaUltimaConexion(new java.sql.Date(date.getTime()));
                usuarioService.save(usuarioModel);
                LOGGERAUDIT.info("El usuario "+usuarioModel.getNombres()+"realizo cambio de clave");
                Alert dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
                dialogoAlerta.setTitle("Cambio de clave exitoso");
                dialogoAlerta.setHeaderText(null);
                dialogoAlerta.setContentText("Se realizo el cambio de clave exitosamente");
                dialogoAlerta.initStyle(StageStyle.UTILITY);
                dialogoAlerta.show();

                ((Node) (event.getSource())).getScene().getWindow().hide();
            } else {
                Validador.mostrarDialogError("No coinciden la nueva contraseña con la confirmacion");
            }
        } else {
            Validador.mostrarDialogError("No coinciden la nueva contraseña con la confirmacion");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        if(usuarioModel.getFechaUltimaConexion()!=null){
            labelMensaje.setText("Ingrese su nueva clave.");
        }
    }


}


