package absortio.m00p4.negocio.ui.menu.roles;

import absortio.m00p4.negocio.model.PermisoModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.service.PermisoService;
import absortio.m00p4.negocio.service.RolService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class RolesNuevoController implements Initializable {

    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    @FXML
    Pane paneNuevoRol;
    @FXML
    TextField textFieldNombre;
    @FXML
    TextArea textAreaDescripcion;
    @FXML
    CheckBox clientesVer;
    @FXML
    CheckBox clientesCrear;
    @FXML
    CheckBox clientesEditar;
    @FXML
    CheckBox clientesEliminar;
    @FXML
    CheckBox empleadosVer;
    @FXML
    CheckBox empleadosCrear;
    @FXML
    CheckBox empleadosEditar;
    @FXML
    CheckBox empleadosEliminar;
    @FXML
    CheckBox rolesVer;
    @FXML
    CheckBox rolesCrear;
    @FXML
    CheckBox rolesEditar;
    @FXML
    CheckBox rolesEliminar;
    @FXML
    CheckBox proformasVer;
    @FXML
    CheckBox proformasCrear;
    @FXML
    CheckBox proformasEditar;
    @FXML
    CheckBox proformasEliminar;
    @FXML
    CheckBox productosVer;
    @FXML
    CheckBox productosCrear;
    @FXML
    CheckBox productosEditar;
    @FXML
    CheckBox productosEliminar;
    @FXML
    CheckBox almacenVer;
    @FXML
    CheckBox almacenCrear;
    @FXML
    CheckBox almacenEditar;
    @FXML
    CheckBox almacenEliminar;
    @FXML
    CheckBox descuentosVer;
    @FXML
    CheckBox descuentosCrear;
    @FXML
    CheckBox descuentosEditar;
    @FXML
    CheckBox descuentosEliminar;
    @FXML
    CheckBox transportistasVer;
    @FXML
    CheckBox transportistasCrear;
    @FXML
    CheckBox transportistasEditar;
    @FXML
    CheckBox transportistasEliminar;
    @FXML
    CheckBox fletesVer;
    @FXML
    CheckBox fletesCrear;
    @FXML
    CheckBox fletesEditar;
    @FXML
    CheckBox fletesEliminar;
    @FXML
    CheckBox solmovVer;
    @FXML
    CheckBox solmovCrear;
    @FXML
    CheckBox solmovEditar;
    @FXML
    CheckBox solmovEliminar;
    @FXML
    CheckBox despachosVer;
    @FXML
    CheckBox despachosCrear;
    @FXML
    CheckBox despachosEditar;
    @FXML
    CheckBox despachosEliminar;
    @FXML
    CheckBox kardexVer;
    @Autowired
    RolService rolService;

    @Autowired
    PermisoService permisoService;

    RolModel rolModel;
    @Autowired
    private ApplicationContext context;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rolModel = new RolModel();
    }

    @FXML
    void buttonAceptar(MouseEvent event) throws IOException {
        if (!camposValidos()) return;

        //aca hacer el save
        rolModel.setNombre(textFieldNombre.getText());
        rolModel.setDescripcion(textAreaDescripcion.getText());
        rolModel.setEstado("activo");

        configureCheckBox(clientesCrear);
        configureCheckBox(clientesEditar);
        configureCheckBox(clientesEliminar);
        configureCheckBox(clientesVer);
        configureCheckBox(empleadosCrear);
        configureCheckBox(empleadosEditar);
        configureCheckBox(empleadosEliminar);
        configureCheckBox(empleadosVer);
        configureCheckBox(rolesCrear);
        configureCheckBox(rolesEditar);
        configureCheckBox(rolesEliminar);
        configureCheckBox(rolesVer);
        configureCheckBox(proformasCrear);
        configureCheckBox(proformasEditar);
        configureCheckBox(proformasEliminar);
        configureCheckBox(proformasVer);
        configureCheckBox(productosCrear);
        configureCheckBox(productosEditar);
        configureCheckBox(productosEliminar);
        configureCheckBox(productosVer);
        configureCheckBox(almacenCrear);
        configureCheckBox(almacenEditar);
        configureCheckBox(almacenEliminar);
        configureCheckBox(almacenVer);
        configureCheckBox(descuentosCrear);
        configureCheckBox(descuentosEditar);
        configureCheckBox(descuentosEliminar);
        configureCheckBox(descuentosVer);
        configureCheckBox(transportistasCrear);
        configureCheckBox(transportistasEditar);
        configureCheckBox(transportistasEliminar);
        configureCheckBox(transportistasVer);
        configureCheckBox(fletesVer);
        configureCheckBox(fletesCrear);
        configureCheckBox(fletesEditar);
        configureCheckBox(fletesEliminar);
        configureCheckBox(solmovCrear);
        configureCheckBox(solmovEditar);
        configureCheckBox(solmovEliminar);
        configureCheckBox(solmovVer);
        configureCheckBox(despachosCrear);
        configureCheckBox(despachosEditar);
        configureCheckBox(despachosEliminar);
        configureCheckBox(despachosVer);
        configureCheckBox(kardexVer);
        //save en tabla rol y en el cruce
        try {
            RolModel rol = rolService.save(rolModel);
            String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "ha creado el rol "+rol.getNombre();
            LOGGERAUDIT.info(logMensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Pane paneParent = (Pane) paneNuevoRol.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/roles/roles.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    private boolean camposValidos() {
        if (textFieldNombre.getText().equals("") || textAreaDescripcion.getText().equals("")) {
            Validador.mostrarDialogError("¡Deben llenarse todos los campos!");
            return false;
        }
        return true;
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {
        Pane paneParent = (Pane) paneNuevoRol.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/roles/roles.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    private void configureCheckBox(CheckBox checkBox) {
        if (checkBox.isSelected()) {
            PermisoModel permiso = permisoService.getByNombre(checkBox.getId());
            rolModel.addPermiso(permiso);
        }
    }
}
