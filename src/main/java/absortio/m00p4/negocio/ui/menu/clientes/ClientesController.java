package absortio.m00p4.negocio.ui.menu.clientes;

import absortio.m00p4.negocio.model.ClienteModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Cliente;
import absortio.m00p4.negocio.reportes.ReporteCliente;
import absortio.m00p4.negocio.service.ClienteService;
import absortio.m00p4.negocio.service.PermisoService;
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
import javafx.scene.layout.AnchorPane;
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
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ClientesController implements Initializable {

    @FXML
    AnchorPane paneClientes;
    @FXML
    TextField textFieldNombres;
    @FXML
    TextField textFieldDNI;
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn columnaN;
    @FXML
    private TableColumn columnaDNI;
    @FXML
    private TableColumn columnaNombre;
    @FXML
    private TableColumn columnaCorreo;
    @FXML
    private TableColumn columnaTelefono;
    @FXML
    private TableColumn columnaAcciones;

    ObservableList<Cliente> clientes;

    @Autowired
    ClienteService clienteService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PermisoService permisoService;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.inicializarTablaPersonas();

        ArrayList<ClienteModel> clientesModels = (ArrayList) clienteService.findAll();
        setUsuarios(clientesModels);
        textFieldNombres.setText("");
        textFieldDNI.setText("");
        ArrayList<String>nombres = new ArrayList<>();
        for (int i = 0; i <clientes.size() ; i++) {
            nombres.add(clientes.get(i).getNombre());
        }
        ObservableList<String> olNombres=FXCollections.observableArrayList(nombres);
        TextFields.bindAutoCompletion(textFieldNombres,olNombres);

        ArrayList<String>documentoIdentidad = new ArrayList<>();
        for (int i = 0; i <clientes.size() ; i++) {
            documentoIdentidad.add(clientes.get(i).getDocumentoIdentidad());
        }
        ObservableList<String> olDocumentoIdentidad=FXCollections.observableArrayList(documentoIdentidad);
        TextFields.bindAutoCompletion(textFieldDNI,olDocumentoIdentidad);
    }


    private void setUsuarios(ArrayList<ClienteModel> clientesModels) {
        clientes.clear();

        if (!clientesModels.isEmpty()) {
            for (int i = 0; i < clientesModels.size(); i++) {
                Cliente cliente = new Cliente();
                cliente.setN(i+1);
                cliente.setDocumentoIdentidad(clientesModels.get(i).getDocumentoIdentidad());
                cliente.setCorreo(clientesModels.get(i).getCorreo());
                cliente.setNombre(clientesModels.get(i).getNombres());
                cliente.setTelefono(clientesModels.get(i).getTelefono());

                if (clientesModels.get(i).getEstado().compareTo("inactivo") == 0){
                }
                else if (clientesModels.get(i).getEstado().compareTo("activo") == 0){
                    clientes.add(cliente);
                }
            }
        }
    }


    @FXML
    void clickNuevoCliente(MouseEvent event) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("clientesCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "intento registrar cliente sin permiso";
            LOGGERAUDIT.info(logMensaje);
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/clientes/nuevocliente.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        AnchorPane pane = fxmlLoader.load();
        paneClientes.getChildren().setAll(pane);
    }

    private void inicializarTablaPersonas() {
        columnaNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
        columnaN.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("n"));
        columnaDNI.setCellValueFactory(new PropertyValueFactory<Cliente, String>("documentoIdentidad"));
        columnaCorreo.setCellValueFactory(new PropertyValueFactory<Cliente, String>("correo"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));
        clientes = FXCollections.observableArrayList();
          addButtonToTable();
        tablaClientes.setItems(clientes);
    }
    private void addButtonToTable() {
        Callback<TableColumn<Cliente, Void>, TableCell<Cliente, Void>> cellFactory = new Callback<TableColumn<Cliente, Void>, TableCell<Cliente, Void>>() {
            @Override
            public TableCell<Cliente, Void> call(final TableColumn<Cliente, Void> param) {
                final TableCell<Cliente, Void> cell = new TableCell<Cliente, Void>() {
                    private final Button btnVer = new Button();
                    {
                        btnVer.setOnAction( (event) -> {
                            Cliente data = getTableView().getItems().get(getIndex());
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/clientes/nuevocliente.fxml"));
                            fxmlLoader.setControllerFactory(context::getBean);
                            try {
                                Pane pane = fxmlLoader.load();
                                fxmlLoader.<ClientesNuevoController>getController().cargardata(1,data);
                                paneClientes.getChildren().setAll(pane);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    private final Button btnEditar = new Button();
                    {
                        btnEditar.setOnMouseClicked( (event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("clientesEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Cliente data = getTableView().getItems().get(getIndex());
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/clientes/nuevocliente.fxml"));
                            fxmlLoader.setControllerFactory(context::getBean);
                            try {
                                Pane pane = fxmlLoader.load();
                                fxmlLoader.<ClientesNuevoController>getController().cargardata(2,data);
                                paneClientes.getChildren().setAll(pane);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    private final Button btnEliminar = new Button();

                    {

                        btnEliminar.setOnAction( (event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("clientesEliminar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Cliente data = getTableView().getItems().get(getIndex());
                            //System.out.println("selectedDataToEliminate: " + data.getNombre());
                            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                            dialogoAlerta.setTitle("Ventana de Confirmacion");
                            dialogoAlerta.setHeaderText(null);
                            dialogoAlerta.initStyle(StageStyle.UTILITY);
                            dialogoAlerta.setContentText("¿Realmente quieres elimarlo?");
                            Optional<ButtonType> result = dialogoAlerta.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                clienteService.updateEstadoById(data.getDocumentoIdentidad(), "inactivo");
                            }
                            ArrayList<ClienteModel> clientesModels = (ArrayList) clienteService.findAll();
                           setUsuarios(clientesModels);

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
        //tablaEmpleadoes.getColumns().add(columnaAcciones);
    }

    @FXML
    void clickExportarClientes(MouseEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteCliente reporteCliente = new ReporteCliente();
        String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "generó reporte de clientes";
        LOGGERAUDIT.info(logMensaje);
        ArrayList<ClienteModel> clienteModels = new ArrayList<>();
        for (Cliente cliente : clientes){
            clienteModels.add(clienteService.getByDocumentoIdentidad(cliente.getDocumentoIdentidad()));
        }

        reporteCliente.generarReporteCliente(clienteModels);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }


    @FXML
    void clickBuscar(MouseEvent event) throws IOException {
        clientes.clear ();
        String documento, nombre;
        ArrayList<ClienteModel> clienteModels = new ArrayList<ClienteModel>();

        nombre = textFieldNombres.getText();
        documento = textFieldDNI.getText();
        clienteModels = (ArrayList<ClienteModel>) clienteService.findByTodosFiltros(nombre, documento);

        setUsuarios(clienteModels);
        String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "realizo consultas en cliente.";
        LOGGERAUDIT.info(logMensaje);
        reiniciamosValoresIniciales();
    }

    public void reiniciamosValoresIniciales(){
        textFieldNombres.setText("");
        textFieldDNI.setText("");
    }
}


