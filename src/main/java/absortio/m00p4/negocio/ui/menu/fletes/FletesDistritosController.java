package absortio.m00p4.negocio.ui.menu.fletes;

import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Flete;
import absortio.m00p4.negocio.model.auxiliares.FleteDistrito;
import absortio.m00p4.negocio.reportes.ReporteFleteDistrito;
import absortio.m00p4.negocio.service.FleteDistritoService;
import absortio.m00p4.negocio.service.PermisoService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FletesDistritosController implements Initializable {

    @FXML
    private Pane paneFletesDistritos;

    @FXML
    private Button buttonBuscar;

    @FXML
    private TableView tablaFletesDistritos;

    @FXML
    private TableColumn columnaN;

    @FXML
    private TableColumn columnaDepartamento;

    @FXML
    private TableColumn columnaProvincia;

    @FXML
    private TableColumn columnaDistrito;

    @FXML
    private TableColumn columnaCosto;

    @FXML
    private TableColumn columnaAcciones;

    @FXML
    private ComboBox<String> comboBoxDepartamento;

    @FXML
    private ComboBox<String> comboBoxProvincia;

    @FXML
    private ComboBox<String> comboBoxDistrito;

    @FXML
    private Button buttonNuevo;

    @FXML
    private Spinner<Double> spinnerCostoMin;

    @FXML
    private Spinner<Double> spinnerCostoMax;

    @Autowired
    FleteDistritoService fleteDistritoService;

    @Autowired
    private ApplicationContext context ;

    @Autowired
    PermisoService permisoService;

    ObservableList<FleteDistrito> fletes;

    @FXML
    private Label labelTitulo;

    @FXML
    private Label labelFiltro;

    private Font fuenteLabelTitulo;

    private Font fuenteLabelFiltro;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fuenteLabelTitulo = new Font("Century Gothic", 49);
        labelTitulo.fontProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> ov, Font b4, Font aftr) {
                if (aftr.equals(fuenteLabelTitulo)) return;
                labelTitulo.setFont(fuenteLabelTitulo);
            }
        });
        fuenteLabelFiltro = new Font("Century Gothic", 20);
        labelFiltro.fontProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> ov, Font b4, Font aftr) {
                if (aftr.equals(fuenteLabelFiltro)) return;
                labelFiltro.setFont(fuenteLabelFiltro);
            }
        });
        this.inicializarTablaFletesDistritos();
        inicializarspinner();
        cargarGrilla();
        //cargar departamento combo solo sera Lima
        cargarCombos();
    }

    void inicializarspinner(){
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerCostoMax.setValueFactory ( valueFactory );
        SpinnerValueFactory<Double> valueFactory2 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerCostoMin.setValueFactory ( valueFactory2 );

    }


    private void inicializarTablaFletesDistritos() {
        columnaDepartamento.setCellValueFactory(new PropertyValueFactory<Flete, String>("departamento"));
        columnaProvincia.setCellValueFactory(new PropertyValueFactory<Flete, String>("provincia"));
        columnaDistrito.setCellValueFactory(new PropertyValueFactory<Flete, String>("distrito"));
        columnaCosto.setCellValueFactory(new PropertyValueFactory<Flete, Double>("costo"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<>("button"));
        columnaN.setCellValueFactory(new PropertyValueFactory<Flete, Integer>("n"));
        fletes = FXCollections.observableArrayList();
        //tablaRoles.getColumns().add(columnaAcciones);
        addButtonToTable();
        tablaFletesDistritos.setItems(fletes);
    }

    public void cargarGrilla(){
        //final ObservableList<Flete> tablaFleteRol = tablaFletes.getSelectionModel().getSelectedItems();
        //tablaPersonaSel.addListener(selectorTablaPersonas);
        ArrayList<FleteDistritoModel> fleteslist =(ArrayList)fleteDistritoService.findAllFleteDistrito();
        for (int j = 0; j < fleteslist.size(); j++) {
            FleteDistrito flete= new FleteDistrito();
            FleteDistritoModel distrito=fleteslist.get(j);
            flete.setDepartamento(distrito.getDepartamento());
            flete.setProvincia(distrito.getProvincia());
            flete.setDistrito(distrito.getDistrito());
            flete.setCosto(distrito.getCosto());
            flete.setN(j);
            fletes.add(flete);
        }
    }

    @FXML
    void clickBuscarFletesDistritos(MouseEvent event) throws IOException {
        //Hacer la superbusca con query
        this.inicializarTablaFletesDistritos();
        List<FleteDistritoModel>nuevalista=fleteDistritoService.buscar(comboBoxDepartamento.getValue(),  comboBoxProvincia.getValue(), comboBoxDistrito.getValue(), spinnerCostoMin.getValue(), spinnerCostoMax.getValue());
        for (int j = 0; j < nuevalista.size(); j++) {
            FleteDistrito flete= new FleteDistrito();
            FleteDistritoModel distrito=nuevalista.get(j);
            flete.setDepartamento(distrito.getDepartamento());
            flete.setProvincia(distrito.getProvincia());
            flete.setDistrito(distrito.getDistrito());
            flete.setCosto(distrito.getCosto());
            flete.setN(j);
            fletes.add(flete);
        }

        //(String) comboBoxDepartamento.getValue(), (String) comboBoxProvincia.getValue(), (String) comboBoxDistrito.getValue(), (Double)spinnerCostoMin.getValue(), (Double)spinnerCostoMax.getValue()

    }

    @FXML
    public void clickExportarFletesDistritos(MouseEvent mouseEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteFleteDistrito reporteEmpleado = new ReporteFleteDistrito();
        reporteEmpleado.generarReporte(fleteDistritoService.findAllFleteDistrito());

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }

    @FXML
    public void clickNuevoFleteDistrito(MouseEvent mouseEvent) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("fletesCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/fletes/nuevofletedistrito.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneFletesDistritos.getChildren().setAll(pane);
    }

    private void addButtonToTable() {
        Callback<TableColumn<FleteDistrito, Void>, TableCell<FleteDistrito, Void>> cellFactory = new Callback<TableColumn<FleteDistrito, Void>, TableCell<FleteDistrito, Void>>() {
            @Override
            public TableCell<FleteDistrito, Void> call(final TableColumn<FleteDistrito, Void> param) {
                final TableCell<FleteDistrito, Void> cell = new TableCell<FleteDistrito, Void>() {

                    private final Button btnEditar = new Button();
                    {
                        btnEditar.setOnMouseClicked( (event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("fletesEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            FleteDistrito data = getTableView().getItems().get(getIndex());
                            levantarPaneEditar(data);
                        });
                    }

//                    private final Button btnEliminar = new Button();
//                    {
//                        btnEliminar.setOnAction( (event) -> {
//                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
//                            RolModel rolUsuario = usuarioModel.getIdRol();
//                            if (!Validador.tienePermiso("fletesEliminar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
//                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
//                                return;
//                            }
//                            FleteDistrito data = getTableView().getItems().get(getIndex());
                            //System.out.println("selectedDataToEliminate: " + data.getNombre());
                            //Aca llamar a la ventana de confirmacion de Eliminar

//                            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
//                            dialogoAlerta.setTitle("Ventana de Confirmacion");
//                            dialogoAlerta.setHeaderText(null);
//                            dialogoAlerta.initStyle(StageStyle.UTILITY);
//                            dialogoAlerta.setContentText("¿Realmente quieres elimarlo?");
//                            Optional<ButtonType> result = dialogoAlerta.showAndWait();
//                            if (result.get() == ButtonType.OK) {
//                                fleteDistritoService.delete(data.getId());
//                                //vuelve a cargar la grilla
//                                //roles.clear();
//                                //cargarGrilla();
//                            }
                            //setUsuarios();


                        //});
                    //}

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {

                            Image image = new Image(getClass().getResourceAsStream("/fxml/imagenes/editicon.png"));
                            ImageView iv1=new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnEditar.setGraphic(iv1);

//                            image = new Image(getClass().getResourceAsStream("/fxml/imagenes/deleteicon.png"));
//                            iv1=new ImageView(image);
//                            iv1.setFitHeight(17);
//                            iv1.setFitWidth(20);
//                            btnEliminar.setGraphic(iv1);

                            HBox pane = new HBox(btnEditar);
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

    private void levantarPaneEditar(FleteDistrito data)  {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/fletes/editarfletedistrito.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane pane = fxmlLoader.load();
            fxmlLoader.<FletesDistritosEditarController>getController().cargarData(data);
            paneFletesDistritos.getChildren().setAll(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void cargarCombos(){
        HashMap<String, List<String>> provincias=SystemSingleton.getInstance().getProvinciasxdepartamento();
        List<String> provinciasLima = provincias.get("Lima");

        ObservableList combox1 = FXCollections.observableList(SystemSingleton.getInstance().getDepartamentos());
        comboBoxDepartamento.setItems(combox1);
        comboBoxDepartamento.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                comboBoxProvincia.getItems().clear();
                if (t1 != null) {
                    ObservableList combox2 = FXCollections.observableArrayList((List) SystemSingleton.getInstance().getProvinciasxdepartamento().get(t1));
                    comboBoxProvincia.setItems(combox2);
                    comboBoxProvincia.getItems().add(null);
                }
            }
        });

        comboBoxProvincia.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                comboBoxDistrito.getItems().clear();
                if (t1 != null ) {
                    ObservableList combox3 = FXCollections.observableArrayList((List) SystemSingleton.getInstance().getDistritosxprovincia().get(t1));
                    comboBoxDistrito.setItems(combox3);
                    comboBoxDistrito.getItems().add(null);
                }
            }
        });


    }
}
