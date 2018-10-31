package absortio.m00p4.negocio.ui.menu.transportistas;

import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Transportista;
import absortio.m00p4.negocio.reportes.ReporteRol;
import absortio.m00p4.negocio.reportes.ReporteTransportista;
import absortio.m00p4.negocio.service.PermisoService;
import absortio.m00p4.negocio.service.TransportistaService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.StageStyle;
import javafx.util.Callback;
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
public class TransportistasController implements Initializable {

    @FXML
    Pane paneTransportista;
    @FXML
    ComboBox comboBoxTipoDocumento;
    @FXML
    TextField textFieldNombreTransportista;
    @FXML
    TextField textFieldDocumentoTransportista;
    @FXML
    TextField textFieldTelefonoTransportista;
    @FXML
    TableView <Transportista> tablaTransportistas;
    @FXML
    private TableColumn columnaNT;
    @FXML
    private TableColumn columnaNombreT;
    @FXML
    private TableColumn columnaTelefonoT;
    @FXML
    private TableColumn columnaDocumentoT;
    @FXML
    private TableColumn columnaAcciones;
    @FXML
    private Button buttonAgregarNuevo;
    @FXML
    private Button buttonExportarExcel;

    @FXML
    private Label labelTitulo;

    @FXML
    private Label labelFiltro;

    private Font fuenteLabelTitulo;

    private Font fuenteLabelFiltro;

    ObservableList<Transportista> transportistas;

    @Autowired
    TransportistaService transportistaService;          // Creamos un objeto de la clase, para que podamos usar todos sus métodos

    @Autowired
    private ApplicationContext context ;

    @Autowired
    PermisoService permisoService;


    // Colocamos lo que se debe inicializar en la pantalla principal
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fuenteLabelTitulo = new Font("Century Gothic", 49);
        labelTitulo.fontProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> ov, Font b4, Font aftr) {
                if (aftr.equals(fuenteLabelTitulo)) return;
                labelTitulo.setFont(fuenteLabelTitulo);
            }
        });
        fuenteLabelFiltro = new Font("Century Gothic", 25);
        labelFiltro.fontProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> ov, Font b4, Font aftr) {
                if (aftr.equals(fuenteLabelFiltro)) return;
                labelFiltro.setFont(fuenteLabelFiltro);
            }
        });

        this.inicializarTablaTransportista();

        ArrayList<TransportistaModel> transportistaModels = (ArrayList) transportistaService.findAllTransportista();  // Hacemos el query para obtener todos los transportistas

        this.cargarListaEnGrilla(transportistaModels);      // Leemos todos los transportistas para la grilla
    }


    private void inicializarTablaTransportista() {
        columnaNombreT.setCellValueFactory(new PropertyValueFactory<Transportista, String>("nombreCompleto"));      // Los valores de la grilla son tomados de la clase auxiliar Transportista
        columnaNT.setCellValueFactory(new PropertyValueFactory<Transportista, Integer>("n"));
        columnaDocumentoT.setCellValueFactory(new PropertyValueFactory<Transportista, String>("documentoIdentidad"));
        columnaTelefonoT.setCellValueFactory(new PropertyValueFactory<Transportista, String>("telefono"));

        transportistas = FXCollections.observableArrayList();       // Inicializo la lista de transportistas
        addButtonToTable();         // Agregamos los botones
        tablaTransportistas.setItems(transportistas);
    }

    @FXML
    void clickNuevoTransportistas(MouseEvent event) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("transportistasCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }

        Pane paneTransporte = (Pane) paneTransportista.getParent().getParent().getParent();         // ActualPane --> Tab --> TabPane --> Pane : Llegamos al PaneTransporte

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/nuevotransportistas.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneTransporte.getChildren().setAll(pane);
    }


    private void addButtonToTable() {
        Callback<TableColumn<Transportista, Void>, TableCell<Transportista, Void>> cellFactory = new Callback<TableColumn<Transportista, Void>, TableCell<Transportista, Void>>() {
            @Override
            public TableCell<Transportista, Void> call(final TableColumn<Transportista, Void> param) {
                final TableCell<Transportista, Void> cell = new TableCell<Transportista, Void>() {
                    private final Button btnVer = new Button();
                    {
                        btnVer.setOnAction( (event) -> {
                            Transportista data = getTableView().getItems().get(getIndex());  // obtenemos la información completa de un elemento en la grilla
                            levantarPaneVer(data);
                        });

                    }
                    private final Button btnEditar = new Button();
                    {
                        btnEditar.setOnMouseClicked( (event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("transportistasEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Transportista data = getTableView().getItems().get(getIndex());
                            levantarPaneEditar(data);
                        });
                    }
                    private final Button btnEliminar = new Button();

                    {
                        btnEliminar.setOnAction( (event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("transportistasEliminar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            System.out.println(getTableView().getItems().get(getIndex()).getClass());
                            Transportista data = getTableView().getItems().get(getIndex());
                            //System.out.println("selectedDataToEliminate: " + data.getNombre());
                            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                            dialogoAlerta.setTitle("Ventana de Confirmacion");
                            dialogoAlerta.setHeaderText(null);
                            dialogoAlerta.initStyle(StageStyle.UTILITY);
                            dialogoAlerta.setContentText("¿Realmente quieres elimarlo?");
                            Optional<ButtonType> result = dialogoAlerta.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                transportistaService.updateEstadoById(data.getId(), "inactivo");
                            }
                            ArrayList<TransportistaModel> transportistaModels = (ArrayList) transportistaService.findAllTransportista();
                            cargarListaEnGrilla(transportistaModels);

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

    private void levantarPaneEditar(Transportista data)  {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/editartransportistas.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane paneTransporte = (Pane) paneTransportista.getParent().getParent().getParent();         // ActualPane --> Tab --> TabPane --> Pane : Llegamos al PaneTransporte
            Pane pane = fxmlLoader.load();
            fxmlLoader.<TransportistasEditarController>getController().leerDato(data);
            paneTransporte.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void levantarPaneVer(Transportista data)  {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/vertransportistas.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane paneTransporte = (Pane) paneTransportista.getParent().getParent().getParent();         // ActualPane --> Tab --> TabPane --> Pane : Llegamos al PaneTransporte
            Pane pane = fxmlLoader.load();
            fxmlLoader.<TransportistasVerController>getController().leerDato(data);
            paneTransporte.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void clickExportarTransportistas(MouseEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteTransportista reporteTransportista = new ReporteTransportista();
        reporteTransportista.generarReporteTransportista(transportistaService.findAllTransportista());

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }

    @FXML
    void clickBuscar(MouseEvent event) throws IOException {
        transportistas.clear ();       // borramos, asi en caso de errores no se mostrará nada

        String documento, nombre, telefono;
        ArrayList<TransportistaModel> transportistaModels = new ArrayList<TransportistaModel>();

        nombre = textFieldNombreTransportista.getText();
        documento = textFieldDocumentoTransportista.getText();
        telefono = textFieldTelefonoTransportista.getText();

        // Buscamos con el query
        transportistaModels = (ArrayList<TransportistaModel>) transportistaService.findByTodosFiltros(nombre, documento, telefono);

        cargarListaEnGrilla(transportistaModels);
        //reiniciamosValoresIniciales();
    }

    public void reiniciamosValoresIniciales(){
        textFieldNombreTransportista.setText("");
        textFieldDocumentoTransportista.setText("");
        textFieldTelefonoTransportista.setText("");
    }

    public void cargarListaEnGrilla(ArrayList<TransportistaModel> transportistaModels) {
        transportistas.clear();
        String estadoAux = "";
        if (!transportistaModels.isEmpty()) {
            for (int i = 0; i < transportistaModels.size(); i++) {
                Transportista transportista = new Transportista();
                transportista.setN(i+1);
                transportista.setId(transportistaModels.get(i).getId());
                transportista.setDocumentoIdentidad(transportistaModels.get(i).getDocumentoIdentidad());
                transportista.setApellidoPaterno(transportistaModels.get(i).getApellidoPaterno());
                transportista.setApellidoMaterno(transportistaModels.get(i).getApellidoMaterno());
                transportista.setNombres(transportistaModels.get(i).getNombres());
                transportista.setNombreCompleto(transportistaModels.get(i).getNombreCompleto());
                transportista.setTelefono(transportistaModels.get(i).getTelefono());

                estadoAux = transportistaModels.get(i).getEstado();
                if (estadoAux.compareTo("inactivo") == 0){
                    // No se hace nada
                }
                else if (estadoAux.compareTo("activo") == 0){
                    transportistas.add(transportista);
                }
            }
        }
    }


}


