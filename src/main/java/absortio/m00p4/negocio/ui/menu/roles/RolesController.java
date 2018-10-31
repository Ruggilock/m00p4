package absortio.m00p4.negocio.ui.menu.roles;

import absortio.m00p4.M00p4Application;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Rol;
import absortio.m00p4.negocio.reportes.ReporteRol;
import absortio.m00p4.negocio.service.PermisoService;
import absortio.m00p4.negocio.service.RolService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@Proxy(lazy = false)
public class RolesController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(RolesController.class);
    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    @FXML
    Pane paneRoles;
    ObservableList<Rol> roles;
    @Autowired
    RolService rolService;
    @Autowired
    PermisoService permisoService;
    @FXML
    private TextField textFieldBuscarNombre;
    @FXML
    private TableView<Rol> tablaRoles;
    @FXML
    private TableColumn columnaN;
    @FXML
    private TableColumn columnaNombre;
    @FXML
    private TableColumn columnaDescripcion;
    @FXML
    private TableColumn columnaAcciones;
    @FXML
    private TableColumn columnaId;
    private int posicionUsuarioModel;
    @Autowired
    private ApplicationContext context;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.inicializarTablaRoles();
        cargarGrilla();
    }

    @FXML
    void clickNuevoRoles(MouseEvent event) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("rolesCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/roles/nuevorol.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneRoles.getChildren().setAll(pane);
    }

    @FXML
    void clickBuscarRoles(MouseEvent event) throws IOException {

        this.inicializarTablaRoles();
        final ObservableList<Rol> tablaRolSel = tablaRoles.getSelectionModel().getSelectedItems();
        ArrayList<RolModel> rolModels;
        if (!textFieldBuscarNombre.equals("")) {
            rolModels = (ArrayList) rolService.findAllByNombreIsStartingWith(textFieldBuscarNombre.getText());
        } else rolModels = (ArrayList) rolService.findAllRol();
        for (int i = 0; i < rolModels.size(); i++) {
            Rol rol = new Rol();
            rol.setN(i);
            rol.setId(rolModels.get(i).getId());
            rol.setNombre(rolModels.get(i).getNombre());
            rol.setDescripcion(rolModels.get(i).getDescripcion());
            roles.add(rol);
        }

    }

    private void inicializarTablaRoles() {
        columnaNombre.setCellValueFactory(new PropertyValueFactory<Rol, String>("nombre"));
        columnaN.setCellValueFactory(new PropertyValueFactory<Rol, Integer>("n"));
        columnaId.setCellValueFactory(new PropertyValueFactory<Rol, Integer>("id"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Rol, String>("descripcion"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<>("button"));
        roles = FXCollections.observableArrayList();
        //tablaRoles.getColumns().add(columnaAcciones);
        addButtonToTable();
        tablaRoles.setItems(roles);
    }

    private void addButtonToTable() {
        Callback<TableColumn<Rol, Void>, TableCell<Rol, Void>> cellFactory = new Callback<TableColumn<Rol, Void>, TableCell<Rol, Void>>() {
            @Override
            public TableCell<Rol, Void> call(final TableColumn<Rol, Void> param) {
                final TableCell<Rol, Void> cell = new TableCell<Rol, Void>() {
                    private final Button btnVer = new Button();
                    private final Button btnEditar = new Button();

                    {
                        btnVer.setOnAction((event) -> {
                            Rol data = getTableView().getItems().get(getIndex());
                            levantarPaneVer(data);
                        });
                    }

                    {
                        btnEditar.setOnMouseClicked((event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("rolesEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Rol data = getTableView().getItems().get(getIndex());
                            levantarPaneEditar(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {

                            Image image = new Image(getClass().getResourceAsStream("/fxml/imagenes/editicon.png"));
                            ImageView iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnEditar.setGraphic(iv1);

                            image = new Image(getClass().getResourceAsStream("/fxml/imagenes/viewicon.png"));
                            iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnVer.setGraphic(iv1);

                            HBox pane = new HBox(btnVer, btnEditar);
                            pane.setSpacing(5);
                            setGraphic(pane);
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
                return cell;
            }
        };
        columnaAcciones.setCellFactory(cellFactory);
    }

    private void levantarPaneEditar(Rol data) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/roles/editarrol.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane pane = fxmlLoader.load();
            fxmlLoader.<RolesEditarController>getController().cargarData(data);
            paneRoles.getChildren().setAll(pane);

        } catch (IOException e) {
             LOGGER.error("Error al carga la data del rol",e);
        }
    }

    private void levantarPaneVer(Rol data) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/roles/verrol.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane pane = fxmlLoader.load();
            fxmlLoader.<RolesVerController>getController().cargarData(data);
            paneRoles.getChildren().setAll(pane);
        } catch (IOException e) {
            LOGGER.error("Error al carga la data del rol",e);
        }
    }

    @FXML
    void clickExportarRoles(MouseEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteRol reporteEmpleado = new ReporteRol();
        reporteEmpleado.generarReporteRol(rolService.findAllRol());
        String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "generó reporte de roles";
        LOGGERAUDIT.info(logMensaje);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }

    public void cargarGrilla() {
        ArrayList<RolModel> rolModels = (ArrayList) rolService.findAllRol();

        for (int i = 0; i < rolModels.size(); i++) {
            Rol rol = new Rol();
            rol.setId(rolModels.get(i).getId());
            rol.setN(i);
            rol.setNombre(rolModels.get(i).getNombre());
            rol.setDescripcion(rolModels.get(i).getDescripcion());
            if (rolModels.get(i).getEstado() != "inactivo") {
                roles.add(rol);
            }
        }
    }
}
