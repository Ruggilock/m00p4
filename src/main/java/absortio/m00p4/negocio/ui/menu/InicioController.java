package absortio.m00p4.negocio.ui.menu;

import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.service.MapaService;
import absortio.m00p4.negocio.service.PermisoService;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class InicioController implements Initializable {

    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    @FXML
    Pane paneInicio;
    @Autowired
    PermisoService permisoService;
    @Autowired
    MapaService mapaService;
    @Autowired
    private ApplicationContext context;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void clickEmpleados(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("empleadosVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        String logMensaje = "El usuario " + usuarioModel.getNombres() + " ingresó a Empleados";
        LOGGERAUDIT.info(logMensaje);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/empleados/empleados.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);
    }

    @FXML
    public void clickClientes(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("clientesVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        String logMensaje = "El usuario " + usuarioModel.getNombres() + " ingresó a Clientes";
        LOGGERAUDIT.info(logMensaje);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/clientes/clientes.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);
    }

    @FXML
    public void clickRoles(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("rolesVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
             return;
        }
        String logMensaje="El usuario "+ usuarioModel.getNombres()+ " ingresó a Roles";
        LOGGERAUDIT.info(logMensaje);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/roles/roles.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);

    }

    @FXML
    public void clickVentas(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("proformasVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        String logMensaje="El usuario "+ usuarioModel.getNombres()+ " ingresó a Ventas";
        LOGGERAUDIT.info(logMensaje);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/ventas/ventas.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);
    }

    @FXML
    public void clickProductos(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("productosVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        String logMensaje="El usuario "+ usuarioModel.getNombres()+ " ingresó a Productos";
        LOGGERAUDIT.info(logMensaje);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/productos/productos.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);
    }

    @FXML
    public void clickAlmacen(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("almacenVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        String logMensaje="El usuario "+ usuarioModel.getNombres()+ " ingresó a Almacén";
        LOGGERAUDIT.info(logMensaje);
        FXMLLoader fxmlLoader;
        if (mapaService.findAllMapa().size() > 0) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/almacenes/almacenesTabPadre.fxml"));
        } else {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/almacenes/cargarMapa.fxml"));
        }
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);
    }

    @FXML
    public void clickDescuentos(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("descuentosVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        String logMensaje="El usuario "+ usuarioModel.getNombres()+ " ingresó a Descuentos";
        LOGGERAUDIT.info(logMensaje);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuento.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);

    }

    @FXML
    public void clickTransportistas(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("transportistasVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
             return;
        }
        String logMensaje="El usuario "+ usuarioModel.getNombres()+ " ingresó a Transportistas";
        LOGGERAUDIT.info(logMensaje);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);
    }

    @FXML
    public void clickFlete(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("fletesVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        String logMensaje="El usuario "+ usuarioModel.getNombres()+ " ingresó a Flete";
        LOGGERAUDIT.info(logMensaje);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/fletes/tabpane.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);

    }

    @FXML
    public void clickOperaciones(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("solmovVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/kardex.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);
    }

    @FXML
    public void clickDespacho(MouseEvent mouseEvent) throws IOException {
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("despachosVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/despacho.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);

    }

    @FXML
    public void clickDevolucion(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/devoluciones/devoluciones.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneInicio.getChildren().setAll(pane);
    }
}
