package absortio.m00p4.negocio.ui.menu;

import absortio.m00p4.M00p4Application;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.SQLReader;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class MenuController implements Initializable {
    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    private static final Logger LOGGER = LogManager.getLogger(MenuController.class);
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    MapaService mapaService;
    @Autowired
    PermisoService permisoService;
    @Autowired
    RolService rolService;
    @Autowired
    ProductoService productoService;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane anchorPaneMenu;
    @FXML
    private AnchorPane anchorPaneMenu2;
    @FXML
    private JFXHamburger menuHamburguesa;
    @FXML
    private JFXDrawer menuDrawer;
    @FXML
    private Label labelApeNom;
    @FXML
    private Button botonCargaMasiva;
    private Integer idEmpleado;
    private String nombreCompleto;
    @Autowired
    private ApplicationContext context;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
            labelApeNom.setText(usuarioModel.getNombreCompleto());
            idEmpleado = usuarioModel.getId();
            nombreCompleto = usuarioModel.getNombreCompleto();


            if (productoService.findAllProducto().size() > 2) {
                botonCargaMasiva.setVisible(false);
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/inicio.fxml"));
                fxmlLoader.setControllerFactory(context::getBean);
                Pane pane = fxmlLoader.load();
                anchorPaneMenu2.getChildren().setAll(pane);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            VBox vbox = FXMLLoader.load(getClass().getResource("/fxml/menu/drawercontent.fxml"));
            menuDrawer.setSidePane(vbox);
            for (Node node : vbox.getChildren()) {
                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        menuDrawer.close();
                        switch (node.getAccessibleText()) {
                            case "inicio": {
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/inicio.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar menu inicio",e1);
                                }

                                break;
                            }
                            case "clientes": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("clientesVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+ usuarioModel.getNombres()+ " ingresó a Clientes";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/clientes/clientes.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar clientes",e1);
                                }
                                break;
                            }
                            case "empleados": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("empleadosVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+usuarioModel.getNombres()+ "ingresó a Empleados";
                                    LOGGERAUDIT.info(logMensaje);

                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/empleados/empleados.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al ingresar a Empleados",e1);
                                }
                                break;
                            }
                            case "roles": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("rolesVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+usuarioModel.getNombres()+ "ingresó a Roles";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/roles/roles.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar roles",e1);
                                }
                                break;
                            }
                            case "ventas": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("proformasVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+usuarioModel.getNombres()+ " ingresó a Ventas";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/ventas/ventas.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar Ventas",e1);
                                }
                                break;
                            }
                            case "productos": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("productosVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+usuarioModel.getNombres()+ " ingresó a Productos";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/productos/productos.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar productos",e1);
                                }
                                break;
                            }
                            case "almacen": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("almacenVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+usuarioModel.getNombres()+ " ingresó a Almacen";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader;
                                    if (mapaService.findAllMapa().size() > 0) {
                                        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/almacenes/almacenesTabPadre.fxml"));
                                    } else {
                                        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/almacenes/cargarMapa.fxml"));
                                    }
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException q1) {
                                    LOGGER.error("Error al cargar almacén",q1);
                                }
                                break;
                            }
                            case "descuentos": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("descuentosVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        String logMensaje="El usuario "+usuarioModel.getNombres()+ "intento ingresar a Descuentos sin permisos";
                                        LOGGERAUDIT.info(logMensaje);
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+usuarioModel.getNombres()+ " ingresó a Descuentos";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuento.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);

                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar descuentos",e1);
                                }
                                break;
                            }
                            case "transportistas": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("transportistasVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+usuarioModel.getNombres()+ " ingresó a Transportistas";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar transportistas",e1);
                                }
                                break;
                            }
                            case "flete": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("fletesVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+ usuarioModel.getNombres() + " ingresó a Flete";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/fletes/tabpane.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar Flete",e1);
                                }
                                break;
                            }
                            case "solMovimiento": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("solmovVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+ usuarioModel.getNombres() + " ingresó a Kardex";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/kardex.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar kardex",e1);
                                }
                                break;
                            }
                            case "despacho": {
                                try {
                                    RolModel rolUsuario = usuarioModel.getIdRol();
                                    if (!Validador.tienePermiso("despachosVer", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                        break;
                                    }
                                    String logMensaje="El usuario "+ usuarioModel.getNombres() + " ingresó a Despacho";
                                    LOGGERAUDIT.info(logMensaje);
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/despacho.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar despacho",e1);
                                }
                                break;
                            }
                            case "ingreso": {
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/devoluciones/devoluciones.fxml"));
                                    fxmlLoader.setControllerFactory(context::getBean);
                                    Pane pane = fxmlLoader.load();
                                    anchorPaneMenu2.getChildren().setAll(pane);
                                } catch (IOException e1) {
                                    LOGGER.error("Error al cargar devoluciones",e1);
                            }
                                break;
                            }
                        }
                    });
                }
            }
            HamburgerNextArrowBasicTransition burguerTask = new HamburgerNextArrowBasicTransition(menuHamburguesa);
            burguerTask.setRate(-1);
            menuHamburguesa.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                burguerTask.setRate(burguerTask.getRate() * -1);
                burguerTask.play();
                if (menuDrawer.isShown()) {
                    menuDrawer.close();

                } else {
                    menuDrawer.open();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonSalir(MouseEvent event) throws IOException {

        System.exit(0);
    }

    @FXML
    void clickCargaMasiva(MouseEvent event) throws IOException, SQLException {
        ArrayList<String> listaQueries;
        String ruta;

        Connection conn = null;


        String jdbcUrl = "jdbc:postgresql://200.16.7.71:1046/postgres2";
        String username = "postgres";
        String password = "krp014";

/*
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "softwarebica";
*/
        conn = DriverManager.getConnection(jdbcUrl, username, password);

        Statement stmt = conn.createStatement();

        SQLReader sqlread = new SQLReader();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        ruta = file.getPath() + File.separator + "test.sql";
        listaQueries = sqlread.crearQueries(ruta);
        ArrayList<ResultSet> listaResultado = new ArrayList<ResultSet>();

        for (String query : listaQueries) {
            stmt.executeUpdate(query);      // INSERT, UPDATE y DELETE
        }

        stmt.close();
        conn.close();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha cargado correctamente");
        alert.showAndWait();
        botonCargaMasiva.setVisible(false);
    }

    @FXML
    public void buttonClave(MouseEvent mouseEvent) throws IOException {
        Stage dialog = new Stage();
        FXMLLoader fxmlLoad = new FXMLLoader(getClass().getResource("/fxml/menu/modal/cambioClave.fxml"));
        fxmlLoad.setControllerFactory(context::getBean);
        Parent root = fxmlLoad.load();
        dialog.setScene(new Scene(root));
        dialog.setTitle("Cambio de Contraseña");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(anchorPaneMenu.getScene().getWindow());
        dialog.showAndWait();
        dialog.close();
    }
}

