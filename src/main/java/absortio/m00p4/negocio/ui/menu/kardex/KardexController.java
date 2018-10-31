package absortio.m00p4.negocio.ui.menu.kardex;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Kardex;
import absortio.m00p4.negocio.reportes.ReporteKardex;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class KardexController implements Initializable {

    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    ObservableList<Kardex> operaciones;
    @Autowired
    KardexService kardexService;
    @Autowired
    BloqueService bloqueService;
    @Autowired
    LoteService loteService;
    @Autowired
    ProductoService productoService;
    @Autowired
    TipoOperacionKardexService tipoOperacionKardexService;
    @Autowired
    MotivoOperacionKardexService motivoOperacionKardexService;
    @Autowired
    SectorAlmacenService sectorAlmacenService;
    @Autowired
    RackService rackService;
    @Autowired
    PermisoService permisoService;
    private KardexModel kardex;
    private LoteModel lotenuevo;
    @FXML
    private Pane paneKardex;
    @FXML
    private Button buttonExportarExcel;
    @FXML
    private TextField textFieldCodLote;
    @FXML
    private Button buttonBuscar;
    @FXML
    private TableView<Kardex> tablaKardex;
    @FXML
    private TableColumn columnaN;
    @FXML
    private TableColumn columnaFecha;
    @FXML
    private TableColumn columnaCodLote;
    @FXML
    private TableColumn columnaCodBarras;
    @FXML
    private TableColumn columnaProducto;
    @FXML
    private TableColumn columnaCantidad;
    @FXML
    private TableColumn columnaSeccionAlmacen;
    @FXML
    private TableColumn columnaRack;
    @FXML
    private TableColumn columnaBloque;
    @FXML
    private TableColumn columnaTipoOperacion;
    @FXML
    private TableColumn columnaMotivo;
    @FXML
    private TextField textFieldCodBarras;
    @FXML
    private TextField textFieldProducto;
    @FXML
    private ComboBox comboBoxTipoOperacion;
    @FXML
    private ComboBox comboBoxSeccionAlmacen;
    @FXML
    private ComboBox comboBoxMotivo;
    @FXML
    private ComboBox comboBoxRack;
    @FXML
    private TextField textFieldMontoMin;
    @FXML
    private TextField textFieldMontoMax;
    @FXML
    private DatePicker datePickerFechaInicio;
    @FXML
    private DatePicker datePickerFechaFin;
    @FXML
    private Button buttonNuevoLote;
    @FXML
    private Button buttonNuevoMovimiento;
    @FXML
    private Spinner<Integer> spinnerCantidadMin;
    @FXML
    private Spinner<Integer> spinnerCantidadMax;
    @Autowired
    private ApplicationContext context;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.inicializarTablaOperacionesKardex();
        cargarGrilla();
        cargarCombos();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 10000, 5);
        spinnerCantidadMax.setValueFactory(valueFactory);
        SpinnerValueFactory<Integer> valueFactory2 =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0, 5);
        spinnerCantidadMin.setValueFactory(valueFactory2);
        comboBoxTipoOperacion.setValue(comboBoxTipoOperacion.getItems().get(0));

        comboBoxMotivo.setValue(comboBoxMotivo.getItems().get(0));

        comboBoxSeccionAlmacen.setValue(comboBoxSeccionAlmacen.getItems().get(0));

        comboBoxRack.setValue(comboBoxRack.getItems().get(0));


    }

    void inicializarTablaOperacionesKardex() {
        columnaBloque.setCellValueFactory(new PropertyValueFactory<Kardex, String>("bloque"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<Kardex, Integer>("cantidad"));
        columnaCodBarras.setCellValueFactory(new PropertyValueFactory<Kardex, String>("codbarras"));
        columnaCodLote.setCellValueFactory(new PropertyValueFactory<Kardex, Integer>("lote"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<Kardex, String>("fecha"));
        columnaMotivo.setCellValueFactory(new PropertyValueFactory<Kardex, String>("motivo"));
        columnaProducto.setCellValueFactory(new PropertyValueFactory<Kardex, String>("nombreproducto"));
        columnaRack.setCellValueFactory(new PropertyValueFactory<Kardex, String>("identificadorrack"));
        columnaSeccionAlmacen.setCellValueFactory(new PropertyValueFactory<Kardex, String>("seccionalmacen"));
        columnaTipoOperacion.setCellValueFactory(new PropertyValueFactory<Kardex, String>("tipooperacion"));
        columnaN.setCellValueFactory(new PropertyValueFactory<Kardex, Integer>("n"));
        operaciones = FXCollections.observableArrayList();
        tablaKardex.setItems(operaciones);
    }

    void cargarGrilla() {
        ArrayList<KardexModel> kardexModels = (ArrayList) kardexService.findAllKardex();
        for (int i = 0; i < kardexModels.size(); i++) {
            Kardex kardex = new Kardex();
            kardex.setBloque(kardexModels.get(i).getBloque().getIdentificador());
            kardex.setCantidad(kardexModels.get(i).getCantidad());
            kardex.setCodbarras(kardexModels.get(i).getLote().getProducto().getCodigoBarras());//codigo de barras del producto
            kardex.setFecha(kardexModels.get(i).getFechaoperacion().toString());
            kardex.setIdentificadorrack(kardexModels.get(i).getBloque().getIdRack().getIdentificador());
            kardex.setLote(kardexModels.get(i).getLote().getId());
            kardex.setMotivo(kardexModels.get(i).getMotivooperacion().getNombre());
            kardex.setN(i);
            kardex.setNombreproducto(kardexModels.get(i).getLote().getProducto().getNombre());
            kardex.setSeccionalmacen(kardexModels.get(i).getBloque().getIdRack().getIdSectorAlmacen().getNombre());
            kardex.setTipooperacion(kardexModels.get(i).getMotivooperacion().getTipooperacion().getNombre());
            operaciones.add(kardex);
        }
    }

    void cargarCombos() {
        List<TipoOperacionKardexModel> tipo = tipoOperacionKardexService.findAllTipoOperacionKardex();
        List<String> tipos = new ArrayList<>();
        tipos.add("-Seleccione-");
        for (int i = 0; i < tipo.size(); i++) {
            tipos.add(tipo.get(i).getNombre());
        }
        ObservableList combox1 = FXCollections.observableList(tipos);
        comboBoxTipoOperacion.setItems(combox1);
        comboBoxTipoOperacion.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                comboBoxMotivo.getItems().clear();
                if (t1 != null) {
                    List<MotivoOperacionKardexModel> motivo = motivoOperacionKardexService.findAllByTipooperacion_Nombre(t1.toString());
                    List<String> motivos = new ArrayList<>();
                    motivos.add("-Seleccione-");
                    for (int i = 1; i < motivo.size(); i++) {
                        motivos.add(motivo.get(i).getNombre());
                    }
                    ObservableList combox2 = FXCollections.observableArrayList(motivos);
                    comboBoxMotivo.setItems(combox2);
                    comboBoxMotivo.setValue(comboBoxMotivo.getItems().get(0));
                }
            }
        });

        List<SectorAlmacenModel> sector = sectorAlmacenService.findAllSectorAlmacen();
        List<String> sectores = new ArrayList<>();
        sectores.add("-Seleccione-");
        for (int i = 0; i < sector.size(); i++) {
            sectores.add(sector.get(i).getNombre());
        }
        ObservableList combox3 = FXCollections.observableList(sectores);
        comboBoxSeccionAlmacen.setItems(combox3);
        comboBoxSeccionAlmacen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                comboBoxRack.getItems().clear();
                if (t1 != null) {
                    List<RackModel> rack = rackService.getByIdSectorAlmacen_Nombre(t1.toString());
                    List<String> racks = new ArrayList<>();
                    racks.add("-Seleccione-");
                    for (int i = 1; i < rack.size(); i++) {
                        racks.add(rack.get(i).getIdentificador());
                    }
                    ObservableList combox4 = FXCollections.observableArrayList(racks);
                    comboBoxRack.setItems(combox4);
                    comboBoxRack.setValue(comboBoxRack.getItems().get(0));

                }
            }
        });


    }


    public void clickExportarExcel(MouseEvent mouseEvent) throws IOException {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteKardex reporteKardex = new ReporteKardex();
        reporteKardex.generarReporte(kardexService.findAllKardex());
        String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "generó reporte de operaciones";
        LOGGERAUDIT.info(logMensaje);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }

    public void clickBuscar(MouseEvent mouseEvent) {
        this.inicializarTablaOperacionesKardex();
        String codbarras, prod, tipo, motivo, sector, rack, fechaIni, fechaFin;
        fechaIni = "";
        fechaFin = "";
        Integer cantIni = 0, cantFin = 1000000;
        codbarras = textFieldCodBarras.getText();
        prod = textFieldProducto.getText();
        tipo = comboBoxTipoOperacion.getValue().toString();
        motivo = comboBoxMotivo.getValue().toString();
        sector = comboBoxSeccionAlmacen.getValue().toString();
        rack = comboBoxRack.getValue().toString();
        cantIni = spinnerCantidadMin.getValue();
        cantFin = spinnerCantidadMax.getValue();

        if (datePickerFechaInicio.getValue() == null) {
            fechaIni = "1900-01-01";
        } else fechaIni = datePickerFechaInicio.getValue().toString();
        if (datePickerFechaFin.getValue() == null) {
            fechaFin = "2100-12-25";
        } else fechaFin = datePickerFechaFin.getValue().toString();

        List<KardexModel> kardexModels = (ArrayList) kardexService.findByTodosFiltros(codbarras, prod, tipo, motivo, sector, rack, fechaIni, fechaFin, cantIni, cantFin);
        for (int i = 0; i < kardexModels.size(); i++) {
            Kardex kardex = new Kardex();
            kardex.setBloque(kardexModels.get(i).getBloque().getIdentificador());
            kardex.setCantidad(kardexModels.get(i).getCantidad());
            kardex.setCodbarras(kardexModels.get(i).getLote().getProducto().getCodigoBarras());//codigo de barras del producto
            kardex.setFecha(kardexModels.get(i).getFechaoperacion().toString());
            kardex.setIdentificadorrack(kardexModels.get(i).getBloque().getIdRack().getIdentificador());
            kardex.setLote(kardexModels.get(i).getLote().getId());
            kardex.setMotivo(kardexModels.get(i).getMotivooperacion().getNombre());
            kardex.setN(i);
            kardex.setNombreproducto(kardexModels.get(i).getLote().getProducto().getNombre());
            kardex.setSeccionalmacen(kardexModels.get(i).getBloque().getIdRack().getIdSectorAlmacen().getNombre());
            kardex.setTipooperacion(kardexModels.get(i).getMotivooperacion().getTipooperacion().getNombre());
            operaciones.add(kardex);
        }
        //List<KardexModel> lista=kardexService.findAllByMotivooperacion_Tipooperacion_NombreAndMotivooperacion_Nombre("","");
        //System.out.println(lista.size());

        //reiniciarValores();

    }

    public void reiniciarValores() {
        textFieldCodBarras.setText("");
        textFieldProducto.setText("");
        comboBoxTipoOperacion.setValue("-Seleccione-");
        comboBoxMotivo.setValue("-Seleccione-");
        comboBoxSeccionAlmacen.setValue("-Seleccione-");
        comboBoxRack.setValue("-Seleccione-");
        datePickerFechaInicio.setValue(null);
        datePickerFechaFin.setValue(null);

    }

    public void clickNuevoLote(MouseEvent mouseEvent) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("solmovCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        Pane paneParent = (Pane) paneKardex.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/nuevoloteaingresar.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);

    }

    public void clickNuevoMovimiento(MouseEvent mouseEvent) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("solmovCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        Pane paneParent = (Pane) paneKardex.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/nuevomovimiento.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    public void ingresandoTexto(ActionEvent event) {
        if (textFieldCodBarras.getText().compareTo("") == 0) {
            textFieldProducto.setDisable(true);
        } else {
            textFieldProducto.setDisable(false);
        }
    }


}
