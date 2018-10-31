package absortio.m00p4.negocio.ui.login;

import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.service.UsuarioService;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


@Component
public class LoginController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;
    @FXML
    private Button buttonFxUsuario;
    @FXML
    private TextField loginFxUsuario;
    @FXML
    private PasswordField loginFxPassword;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ApplicationContext context;

    @FXML
    private void clickButtonLogin(Event event) throws IOException {
        UsuarioModel usuarioModel = usuarioService.getByCorreoAndContrasena(loginFxUsuario.getText(), loginFxPassword.getText());
        if (usuarioModel != null) {
            UsuarioSingleton.getInstance().setUsuarioModel(usuarioModel);
            if (usuarioModel.getFechaUltimaConexion() == null) {
                Stage dialog = new Stage();
                FXMLLoader fxmlLoad = new FXMLLoader(getClass().getResource("/fxml/menu/modal/cambioClave.fxml"));
                fxmlLoad.setControllerFactory(context::getBean);
                Parent root = fxmlLoad.load();
                dialog.setScene(new Scene(root));
                dialog.setTitle("Cambio de Contraseña");
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(buttonFxUsuario.getScene().getWindow());
                dialog.showAndWait();
                dialog.close();
            } else {
                Alert dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
                dialogoAlerta.setTitle("Bienvenido");
                dialogoAlerta.setHeaderText(null);
                dialogoAlerta.initStyle(StageStyle.DECORATED);
                dialogoAlerta.setContentText("Bienvenido al Sistema Moppa " + usuarioModel.getNombres());
                Optional<ButtonType> result = dialogoAlerta.showAndWait();
            }
            ((Node) (event.getSource())).getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/menuform.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("MoppaInterface");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.setHeight(679.0);
            stage.setWidth(1005.0);
            stage.show();
            LOGGERAUDIT.info("El usuario " + usuarioModel.getNombres() + " ingreso al sistema");

        } else {
            LOGGER.error("Error en Logueo: Usuario o contraseña incorrecta.");
            Validador.mostrarDialogError("Usuario o contraseña incorrecta. Intente nuevamente");
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
