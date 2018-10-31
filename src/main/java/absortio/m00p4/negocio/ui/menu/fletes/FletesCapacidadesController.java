package absortio.m00p4.negocio.ui.menu.fletes;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Flete;
import absortio.m00p4.negocio.model.auxiliares.FleteCapacidad;
import absortio.m00p4.negocio.model.auxiliares.FleteCapacidad;
import absortio.m00p4.negocio.model.auxiliares.FleteDistrito;
import absortio.m00p4.negocio.reportes.ReporteFleteCapacidad;
import absortio.m00p4.negocio.service.FleteCapacidadService;
import absortio.m00p4.negocio.service.PermisoService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class FletesCapacidadesController implements Initializable {

    @FXML
    private Pane paneFletesCapacidades;

    @FXML
    private Button buttonBuscar;

    @FXML
    private TableView tablaFletesCapacidades;

    @FXML
    private TableColumn columnaN;

    @FXML
    private TableColumn columnaPesoMin;

    @FXML
    private TableColumn columnaPesoMax;

    @FXML
    private TableColumn columnaUnidPeso;

    @FXML
    private TableColumn columnaVolMin;

    @FXML
    private TableColumn columnaVolMax;

    @FXML
    private TableColumn columnaUnidVol;

    @FXML
    private TableColumn columnaCosto;

    @FXML
    private TableColumn columnaAcciones;

    @FXML
    private Button buttonNuevo;

    @FXML
    private Spinner<Double> spinnerPesoMin;

    @FXML
    private Spinner<Double> spinnerPesoMax;

    @FXML
    private Spinner<Double> spinnerVolMin;

    @FXML
    private Spinner<Double> spinnerVolMax;

    @FXML
    private ComboBox comboUnidpeso;

    @FXML
    private ComboBox comboUnidvol;

    @FXML
    private Label labelTitulo;

    @FXML
    private Label labelFiltro;

    @Autowired
    private ApplicationContext context ;

    ObservableList<FleteCapacidad> fletes;

    @Autowired
    FleteCapacidadService fleteCapacidadService;

    @Autowired
    PermisoService permisoService;

    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    private Font fuenteLabelTitulo;

    private Font fuenteLabelFiltro;

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
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerPesoMax.setValueFactory ( valueFactory );
        SpinnerValueFactory<Double> valueFactory2 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerPesoMin.setValueFactory ( valueFactory2 );
        SpinnerValueFactory<Double> valueFactory3 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerVolMax.setValueFactory ( valueFactory3 );
        SpinnerValueFactory<Double> valueFactory4 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerVolMin.setValueFactory ( valueFactory4 );

        this.inicializarTablaFletesCapacidades();
        cargarGrilla();
        //cargar combos
        cargarCombos();
    }

    private void inicializarTablaFletesCapacidades() {
        columnaPesoMin.setCellValueFactory(new PropertyValueFactory<FleteCapacidad, Double>("pesomin"));
        columnaPesoMax.setCellValueFactory(new PropertyValueFactory<FleteCapacidad, Double>("pesomax"));
        columnaVolMin.setCellValueFactory(new PropertyValueFactory<FleteCapacidad, Double>("volmin"));
        columnaVolMax.setCellValueFactory(new PropertyValueFactory<FleteCapacidad, Double>("volmax"));
        columnaUnidVol.setCellValueFactory(new PropertyValueFactory<FleteCapacidad, String>("unidvol"));
        columnaUnidPeso.setCellValueFactory(new PropertyValueFactory<FleteCapacidad, String>("unidpeso"));
        columnaCosto.setCellValueFactory(new PropertyValueFactory<FleteCapacidad, Double>("costo"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<>("button"));
        columnaN.setCellValueFactory(new PropertyValueFactory<FleteCapacidad, Integer>("n"));
        fletes = FXCollections.observableArrayList();
        //tablaRoles.getColumns().add(columnaAcciones);
        addButtonToTable();
        tablaFletesCapacidades.setItems(fletes);
    }

    public void cargarGrilla(){
        //final ObservableList<Flete> tablaFleteRol = tablaFletes.getSelectionModel().getSelectedItems();
        //tablaPersonaSel.addListener(selectorTablaPersonas);
        ArrayList<FleteCapacidadModel> fleteslist =(ArrayList)fleteCapacidadService.findAllFleteCapacidad();
        for (int j = 0; j < fleteslist.size(); j++) {
            FleteCapacidad flete= new FleteCapacidad();
            FleteCapacidadModel capacidad=fleteslist.get(j);
            flete.setPesomax(capacidad.getPesoMax());
            flete.setPesomin(capacidad.getPesoMin());
            flete.setUnidpeso(capacidad.getUnidadPeso());
            flete.setUnidvol(capacidad.getUnidadvolumen());
            flete.setVolmax(capacidad.getVolumenMax());
            flete.setVolmin(capacidad.getVolumenMin());
            flete.setCosto(capacidad.getCosto());
            flete.setN(j);
            fletes.add(flete);
        }
    }

    private void addButtonToTable() {
        Callback<TableColumn<FleteCapacidad, Void>, TableCell<FleteCapacidad, Void>> cellFactory = new Callback<TableColumn<FleteCapacidad, Void>, TableCell<FleteCapacidad, Void>>() {
            @Override
            public TableCell<FleteCapacidad, Void> call(final TableColumn<FleteCapacidad, Void> param) {
                final TableCell<FleteCapacidad, Void> cell = new TableCell<FleteCapacidad, Void>() {

                    private final Button btnEditar = new Button();
                    {
                        btnEditar.setOnMouseClicked( (event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("fletesEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            FleteCapacidad data = getTableView().getItems().get(getIndex());
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
//                            FleteCapacidad data = getTableView().getItems().get(getIndex());
                            //System.out.println("selectedDataToEliminate: " + data.getNombre());
                            //Aca llamar a la ventana de confirmacion de Eliminar

//                            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
//                            dialogoAlerta.setTitle("Ventana de Confirmacion");
//                            dialogoAlerta.setHeaderText(null);
//                            dialogoAlerta.initStyle(StageStyle.UTILITY);
//                            dialogoAlerta.setContentText("¿Realmente quieres elimarlo?");
//                            Optional<ButtonType> result = dialogoAlerta.showAndWait();
//                            if (result.get() == ButtonType.OK) {
//                                FleteCapacidadService.delete(data.getId());
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

    void cargarCombos(){
        //comboUnidpeso.getItems().addAll(SystemSingleton.getInstance().getUnidadespeso());
        //comboUnidvol.getItems().addAll(SystemSingleton.getInstance().getUnidadesvol());
        comboUnidpeso.getSelectionModel().selectFirst();
        comboUnidvol.getSelectionModel().selectFirst();
        comboUnidvol.setDisable(true);
        comboUnidpeso.setDisable(true);
    }

    @FXML
    void clickBuscarFletesCapacidades(MouseEvent event) throws IOException {
        //Hacer la superbusca con query
        inicializarTablaFletesCapacidades();
        List<FleteCapacidadModel> nuevalista=fleteCapacidadService.buscar(spinnerPesoMin.getValue(),spinnerPesoMax.getValue(),spinnerVolMin.getValue(),spinnerVolMax.getValue());
        for (int j = 0; j < nuevalista.size(); j++) {
            FleteCapacidad flete= new FleteCapacidad();
            FleteCapacidadModel capacidad=nuevalista.get(j);
            flete.setPesomax(capacidad.getPesoMax());
            flete.setPesomin(capacidad.getPesoMin());
            flete.setUnidpeso(capacidad.getUnidadPeso());
            flete.setUnidvol(capacidad.getUnidadvolumen());
            flete.setVolmax(capacidad.getVolumenMax());
            flete.setVolmin(capacidad.getVolumenMin());
            flete.setCosto(capacidad.getCosto());
            flete.setN(j);
            fletes.add(flete);
        }
    }

    @FXML
    public void clickExportarFletesCapacidades(MouseEvent mouseEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteFleteCapacidad reporteEmpleado = new ReporteFleteCapacidad();
        reporteEmpleado.generarReporte(fleteCapacidadService.findAllFleteCapacidad());

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }

    @FXML
    public void clickNuevoFleteCapacidad(MouseEvent mouseEvent) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("fletesCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/fletes/nuevofletecapacidad.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneFletesCapacidades.getChildren().setAll(pane);
    }

    private void levantarPaneEditar(FleteCapacidad data)  {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/fletes/editarfletecapacidad.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane pane = fxmlLoader.load();
            fxmlLoader.<FletesCapacidadesEditarController>getController().cargarData(data);
            paneFletesCapacidades.getChildren().setAll(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
