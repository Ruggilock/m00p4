package absortio.m00p4.negocio.ui.menu.empleados;

import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Empleado;
import absortio.m00p4.negocio.reportes.ReporteEmpleado;
import absortio.m00p4.negocio.service.PermisoService;
import absortio.m00p4.negocio.service.RolService;
import absortio.m00p4.negocio.service.UsuarioService;
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
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class EmpleadosController implements Initializable {

    @FXML
    Pane paneEmpleados;
    @FXML
    ComboBox<String> comboBoxRol;
    @FXML
    TextField textFieldNombre;
    @FXML
    TextField textFieldDni;
    @FXML
    private TableView<Empleado> tablaEmpleados;
    @FXML
    private TableColumn columnaN;
    @FXML
    private TableColumn columnaDni;
    @FXML
    private TableColumn columnaNombre;
    @FXML
    private TableColumn columnaCorreo;
    @FXML
    private TableColumn columnaRol;
    @FXML
    private TableColumn columnaAcciones;


    private ObservableList<Empleado> empleados;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PermisoService permisoService;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.inicializarTablaPersonas();
        ArrayList<UsuarioModel> usuarioModels = (ArrayList) usuarioService.findAllUsuario();
        setUsuarios(usuarioModels);

        ArrayList<String> nombres = new ArrayList<>();
        for (int i = 0; i < empleados.size(); i++) {
            nombres.add(empleados.get(i).getNombre());
        }
        ObservableList<String> olNombres = FXCollections.observableArrayList(nombres);
        TextFields.bindAutoCompletion(textFieldNombre, olNombres);

        ArrayList<String> documentoIdentidad = new ArrayList<>();
        for (int i = 0; i < empleados.size(); i++) {
            documentoIdentidad.add(empleados.get(i).getDni());
        }
        ObservableList<String> olDocumentoIdentidad = FXCollections.observableArrayList(documentoIdentidad);
        TextFields.bindAutoCompletion(textFieldDni, olDocumentoIdentidad);

        ArrayList<String> lroles = new ArrayList<>();

        ArrayList<RolModel> rolModels = (ArrayList<RolModel>) rolService.findAllRol();
        for (int i = 0; i < rolModels.size(); i++) {
            lroles.add(rolModels.get(i).getNombre());
        }
        ObservableList<String> olRol = FXCollections.observableArrayList(lroles);
        comboBoxRol.getItems().add("-Seleccione-");
        comboBoxRol.getItems().addAll(olRol);
        comboBoxRol.setEditable(true);
        comboBoxRol.setValue(comboBoxRol.getItems().get(0));
        TextFields.bindAutoCompletion(comboBoxRol.getEditor(), comboBoxRol.getItems());
    }

    private void setUsuarios(List<UsuarioModel> usuarioModels) {
        empleados.clear();
        for (int i = 0; i < usuarioModels.size(); i++) {
            Empleado empleado = new Empleado();
            empleado.setN(i + 1);
            empleado.setCorreo(usuarioModels.get(i).getCorreo());
            empleado.setDni(usuarioModels.get(i).getDocumentoIdentidad());
            empleado.setNombre(usuarioModels.get(i).getNombreCompleto());
            empleado.setRol(usuarioModels.get(i).getIdRol().getNombre());
            empleados.add(empleado);
        }

    }

    private void setUsuarios() {
        empleados.clear();
        ArrayList<UsuarioModel> usuarioModels = usuarioService.getByEstado("activo");
        for (int i = 0; i < usuarioModels.size(); i++) {
            Empleado empleado = new Empleado();
            empleado.setN(i + 1);
            empleado.setCorreo(usuarioModels.get(i).getCorreo());
            empleado.setDni(usuarioModels.get(i).getDocumentoIdentidad());
            empleado.setNombre(usuarioModels.get(i).getNombreCompleto());
            empleado.setRol(usuarioModels.get(i).getIdRol().getNombre());
            empleados.add(empleado);
        }

    }

    @FXML
    void clickBuscar(MouseEvent event) throws IOException {
        empleados.clear();
        String nombres = textFieldNombre.getText();
        System.out.println(nombres);
        String documentoIdentidad = textFieldDni.getText();
        System.out.println(documentoIdentidad);
        String comboRol = comboBoxRol.getValue();
        if (comboRol.compareTo("-Seleccione-") == 0) {
            comboRol = "";
        }
        setUsuarios(usuarioService.findByTodosFiltros(nombres, documentoIdentidad, comboRol));

        reiniciamosValoresIniciales();
    }

    public void reiniciamosValoresIniciales() {
        textFieldNombre.setText("");
        textFieldNombre.setText("");
        comboBoxRol.setValue("-Seleccione-");
    }

    @FXML
    void clickNuevoEmpleados(MouseEvent event) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("empleadosCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/empleados/nuevoempleado.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneEmpleados.getChildren().setAll(pane);
    }

    private void inicializarTablaPersonas() {
        columnaNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombre"));
        columnaN.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("n"));
        columnaDni.setCellValueFactory(new PropertyValueFactory<Empleado, String>("dni"));
        columnaRol.setCellValueFactory(new PropertyValueFactory<Empleado, String>("rol"));
        columnaCorreo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("correo"));
        addButtonToTable();
        empleados = FXCollections.observableArrayList();
        tablaEmpleados.setItems(empleados);
    }

    @FXML
    void exportarExcel(MouseEvent event) throws IOException {
        //if (SystemSingleton.getInstance().getRute() == null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = directoryChooser.showDialog(null);
            SystemSingleton.getInstance().setRute(file.getPath());
        //}
        if (SystemSingleton.getInstance().getRute() != null) {
            ReporteEmpleado reporteEmpleado = new ReporteEmpleado();
            ArrayList<UsuarioModel> empleadoModels = new ArrayList<UsuarioModel>();
            for (Empleado empleado : empleados) {
                empleadoModels.add(usuarioService.getByDocumentoIdentidad(empleado.getDni()));
            }
            reporteEmpleado.generarReporteEmpleado(empleadoModels);
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }

    private void addButtonToTable() {
        Callback<TableColumn<Empleado, Void>, TableCell<Empleado, Void>> cellFactory = new Callback<TableColumn<Empleado, Void>, TableCell<Empleado, Void>>() {
            @Override
            public TableCell<Empleado, Void> call(final TableColumn<Empleado, Void> param) {
                final TableCell<Empleado, Void> cell = new TableCell<Empleado, Void>() {
                    private final Button btnVer = new Button();

                    {
                        btnVer.setOnAction((event) -> {
                            Empleado data = getTableView().getItems().get(getIndex());
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/empleados/nuevoempleado.fxml"));
                            fxmlLoader.setControllerFactory(context::getBean);
                            try {
                                Pane pane = fxmlLoader.load();
                                fxmlLoader.<EmpleadosNuevoController>getController().cargardata(1, data);
                                paneEmpleados.getChildren().setAll(pane);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    private final Button btnEditar = new Button();

                    {
                        btnEditar.setOnMouseClicked((event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("empleadosEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Empleado data = getTableView().getItems().get(getIndex());
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/empleados/nuevoempleado.fxml"));
                            fxmlLoader.setControllerFactory(context::getBean);
                            try {
                                Pane pane = fxmlLoader.load();
                                fxmlLoader.<EmpleadosNuevoController>getController().cargardata(2, data);
                                paneEmpleados.getChildren().setAll(pane);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    private final Button btnEliminar = new Button();

                    {
                        btnEliminar.setOnAction((event) -> {
                            UsuarioModel usuarioModel2 = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel2.getIdRol();
                            if (!Validador.tienePermiso("empleadosEliminar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Empleado data = getTableView().getItems().get(getIndex());
                            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                            dialogoAlerta.setTitle("Ventana de Confirmacion");
                            dialogoAlerta.setHeaderText(null);
                            dialogoAlerta.initStyle(StageStyle.UTILITY);
                            dialogoAlerta.setContentText("¿Realmente quieres elimarlo");
                            Optional<ButtonType> result = dialogoAlerta.showAndWait();
                            ArrayList<UsuarioModel> usuarioModels = new ArrayList<>();
                            if (result.get() == ButtonType.OK) {
                                UsuarioModel usuarioModel = usuarioService.getByDocumentoIdentidad(data.getDni());
                                usuarioModel.setEstado("inactivo");
                                usuarioService.save(usuarioModel);
                            }
                            setUsuarios();
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

                            image = new Image(getClass().getResourceAsStream("/fxml/imagenes/deleteicon.png"));
                            iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnEliminar.setGraphic(iv1);

                            image = new Image(getClass().getResourceAsStream("/fxml/imagenes/viewicon.png"));
                            iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnVer.setGraphic(iv1);

                            HBox pane = new HBox(btnVer, btnEditar, btnEliminar);
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


}
