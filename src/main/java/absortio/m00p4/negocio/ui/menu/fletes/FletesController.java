package absortio.m00p4.negocio.ui.menu.fletes;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.auxiliares.Flete;
import absortio.m00p4.negocio.model.auxiliares.Rol;
import absortio.m00p4.negocio.reportes.ReporteFlete;
import absortio.m00p4.negocio.service.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import absortio.m00p4.negocio.service.FleteCapacidadService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Component
public class FletesController implements Initializable {

    @FXML
    private Pane paneFletes;

    @FXML
    private Button buttonBuscar;

    @FXML
    private TableView tablaFletes;

    @FXML
    private TableColumn columnaN;

    @FXML
    private TableColumn columnaDepartamento;

    @FXML
    private TableColumn columnaProvincia;

    @FXML
    private TableColumn columnaDistrito;

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
    private Button buttonExportar;

    @FXML
    private ComboBox<String> comboBoxDepartamento;

    @FXML
    private ComboBox<String> comboBoxProvincia;

    @FXML
    private ComboBox<String> comboBoxDistrito;

    @FXML
    private ComboBox<String> comboBoxUMPeso;

    @FXML
    private ComboBox<String> comboBoxUMVol;

    @FXML
    private Spinner<Double> spinnerPesoMin;

    @FXML
    private Spinner<Double> spinnerPesoMax;

    @FXML
    private Spinner<Double> spinnerVolMin;

    @FXML
    private Spinner<Double> spinnerVolMax;

    @FXML
    private Spinner<Double> spinnerCostoMax;

    @FXML
    private Spinner<Double> spinnerCostoMin;

    @FXML
    private Label labelTitulo;

    @FXML
    private Label labelFiltro;

    private Font fuenteLabelTitulo;

    private Font fuenteLabelFiltro;

    ObservableList<Flete> fletes;

    @Autowired
    FleteDistritoService fleteDistritoService;

    @Autowired
    FleteCapacidadService fleteCapacidadService;


    @Autowired
    private ApplicationContext context ;

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
        this.inicializarTablaFletes();
        cargarGrilla();
        //cargar combos
        cargarCombos();
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
        SpinnerValueFactory<Double> valueFactory5 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerCostoMax.setValueFactory ( valueFactory5 );
        SpinnerValueFactory<Double> valueFactory6 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerCostoMin.setValueFactory ( valueFactory6 );


    }

    private void inicializarTablaFletes() {
        columnaDepartamento.setCellValueFactory(new PropertyValueFactory<Flete, String>("departamento"));
        columnaProvincia.setCellValueFactory(new PropertyValueFactory<Flete, String>("provincia"));
        columnaDistrito.setCellValueFactory(new PropertyValueFactory<Flete, String>("distrito"));
        columnaPesoMin.setCellValueFactory(new PropertyValueFactory<Flete, Double>("pesomin"));
        columnaPesoMax.setCellValueFactory(new PropertyValueFactory<Flete, Double>("pesomax"));
        columnaVolMin.setCellValueFactory(new PropertyValueFactory<Flete, Double>("volmin"));
        columnaVolMax.setCellValueFactory(new PropertyValueFactory<Flete, Double>("volmax"));
        columnaUnidPeso.setCellValueFactory(new PropertyValueFactory<Flete, String>("unidpeso"));
        columnaUnidVol.setCellValueFactory(new PropertyValueFactory<Flete, String>("unidvol"));
        columnaN.setCellValueFactory(new PropertyValueFactory<Flete, Integer>("n"));
        columnaCosto.setCellValueFactory(new PropertyValueFactory<Flete, Double>("costo"));

        fletes = FXCollections.observableArrayList();
        //tablaRoles.getColumns().add(columnaAcciones);
        //addButtonToTable();
        tablaFletes.setItems(fletes);
    }

    public void cargarGrilla(){
        //final ObservableList<Flete> tablaFleteRol = tablaFletes.getSelectionModel().getSelectedItems();
        //tablaPersonaSel.addListener(selectorTablaPersonas);
        ArrayList<FleteCapacidadModel> fleteslist =(ArrayList)fleteCapacidadService.findAllFleteCapacidad();
        Integer contador=0;
        for (int j = 0; j < fleteslist.size(); j++) {

            FleteCapacidadModel itemcapacidad=fleteslist.get(j);
            List<FleteModel> list =itemcapacidad.getFletes();
            for(int i=0;i<list.size();i++){
                Flete flete=new Flete();
                flete.setPesomin(itemcapacidad.getPesoMin());
                flete.setPesomax(itemcapacidad.getPesoMax());
                flete.setVolmax(itemcapacidad.getVolumenMax());
                flete.setVolmin(itemcapacidad.getVolumenMin());
                flete.setUnidpeso(itemcapacidad.getUnidadPeso());
                flete.setUnidvol(itemcapacidad.getUnidadvolumen());
                flete.setCosto(list.get(i).getCosto());
                FleteDistritoModel distrito=list.get(i).getDistrito();
                flete.setDepartamento(distrito.getDepartamento());
                flete.setProvincia(distrito.getProvincia());
                flete.setDistrito(distrito.getDistrito());
                flete.setN(contador);
                contador++;
                fletes.add(flete);
            }
        }
    }

    @FXML
    public void clickExportarFletes(MouseEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteFlete reporteEmpleado = new ReporteFlete();
        reporteEmpleado.generarReporteRol(fleteDistritoService.findAllFleteDistrito());

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }

    @FXML
    void clickBuscarFletes(MouseEvent mouseEvent) {

        //hacer query enorme
        inicializarTablaFletes();
        //List<FleteModel> nuevalista=

    }

    public void refresh(){
        fletes.clear();
        this.inicializarTablaFletes();
        cargarGrilla();
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
