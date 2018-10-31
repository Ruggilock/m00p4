package absortio.m00p4.negocio.ui.menu.roles;

import absortio.m00p4.negocio.model.PermisoModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.auxiliares.Rol;
import absortio.m00p4.negocio.service.PermisoService;
import absortio.m00p4.negocio.service.RolService;
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
import java.util.HashMap;
import java.util.ResourceBundle;

@Component
public class RolesVerController implements Initializable {
    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");

    @FXML
    Pane paneVerRol;

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
    HashMap<String, CheckBox> checkBoxMap;
    @Autowired
    private ApplicationContext context;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rolModel = new RolModel();
        agruparCheckBoxs();

    }

    @FXML
    public void cargarData(Rol rolseleccionado) {
        rolModel = rolService.getByIdAndNombre(rolseleccionado.getId(), rolseleccionado.getNombre());
        textFieldNombre.setText(rolseleccionado.getNombre());
        textAreaDescripcion.setText(rolseleccionado.getDescripcion());
        for (int i = 0; i < rolModel.getPermisos().size(); i++) {
            PermisoModel permisoModel = rolModel.getPermisos().get(i);
            checkBoxMap.get(permisoModel.getNombre()).setSelected(true);
        }
    }

    @FXML
    void buttonAceptar(MouseEvent event) throws IOException {

        Pane paneParent = (Pane) paneVerRol.getParent();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/roles/roles.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {
        Pane paneParent = (Pane) paneVerRol.getParent();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/roles/roles.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    void agruparCheckBoxs() {
        checkBoxMap = new HashMap<String, CheckBox>();
        checkBoxMap.put("clientesVer", clientesVer);
        checkBoxMap.put("clientesCrear", clientesCrear);
        checkBoxMap.put("clientesEditar", clientesEditar);
        checkBoxMap.put("clientesEliminar", clientesEliminar);

        checkBoxMap.put("empleadosVer", empleadosVer);
        checkBoxMap.put("empleadosCrear", empleadosCrear);
        checkBoxMap.put("empleadosEditar", empleadosEditar);
        checkBoxMap.put("empleadosEliminar", empleadosEliminar);

        checkBoxMap.put("rolesVer", rolesVer);
        checkBoxMap.put("rolesCrear", rolesCrear);
        checkBoxMap.put("rolesEditar", rolesEditar);
        checkBoxMap.put("rolesEliminar", rolesEliminar);

        checkBoxMap.put("proformasVer", proformasVer);
        checkBoxMap.put("proformasCrear", proformasCrear);
        checkBoxMap.put("proformasEditar", proformasEditar);
        checkBoxMap.put("proformasEliminar", proformasEliminar);

        checkBoxMap.put("productosVer", productosVer);
        checkBoxMap.put("productosCrear", productosCrear);
        checkBoxMap.put("productosEditar", productosEditar);
        checkBoxMap.put("productosEliminar", productosEliminar);

        checkBoxMap.put("almacenVer", almacenVer);
        checkBoxMap.put("almacenCrear", almacenCrear);
        checkBoxMap.put("almacenEditar", almacenEditar);
        checkBoxMap.put("almacenEliminar", almacenEliminar);

        checkBoxMap.put("descuentosVer", descuentosVer);
        checkBoxMap.put("descuentosCrear", descuentosCrear);
        checkBoxMap.put("descuentosEditar", descuentosEditar);
        checkBoxMap.put("descuentosEliminar", descuentosEliminar);

        checkBoxMap.put("transportistasVer", transportistasVer);
        checkBoxMap.put("transportistasCrear", transportistasCrear);
        checkBoxMap.put("transportistasEditar", transportistasEditar);
        checkBoxMap.put("transportistasEliminar", transportistasEliminar);

        checkBoxMap.put("solmovVer", solmovVer);
        checkBoxMap.put("solmovCrear", solmovCrear);
        checkBoxMap.put("solmovEditar", solmovEditar);
        checkBoxMap.put("solmovEliminar", solmovEliminar);

        checkBoxMap.put("despachosVer", despachosVer);
        checkBoxMap.put("despachosCrear", despachosCrear);
        checkBoxMap.put("despachosEditar", despachosEditar);
        checkBoxMap.put("despachosEliminar", despachosEliminar);

        checkBoxMap.put("kardexVer", kardexVer);

        checkBoxMap.put("fletesVer", fletesVer);
        checkBoxMap.put("fletesCrear", fletesCrear);
        checkBoxMap.put("fletesEditar", fletesEditar);
        checkBoxMap.put("fletesEliminar", fletesEliminar);

    }

}
