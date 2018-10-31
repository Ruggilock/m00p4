package absortio.m00p4.negocio.ui.menu.despacho;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.algoritmosPrincipales.AlgoritmoGenetico;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Solucion;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.DetalleDespacho;
import absortio.m00p4.negocio.model.auxiliares.DetalleEntrega;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.ui.menu.almacenes.funciones.FuncionesMapa;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class AgregarDespachoController implements Initializable {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane paneAgregarDespacho;

    @FXML
    private TableView<DetalleEntrega> tablaDetalleEntrega;
    @FXML
    private TableColumn t1columnaLote;
    @FXML
    private TableColumn t1columnaFechaLimite;
    @FXML
    private TableColumn t1columnaCantidad;
    @FXML
    private TableColumn t1columnaResta;
    @FXML
    private TableColumn t1columnaEstado;
    @FXML
    private TableView<DetalleDespacho> tablaDetalleDespacho;
    @FXML
    private TableColumn t2columnaLote;
    @FXML
    private TableColumn t2columnaCantidad;
    @FXML
    private TableColumn t2columnaPeso;
    @FXML
    private TextField textFieldSobreNombre;
    @FXML
    private ComboBox<String> comboBoxTransportista;
    @FXML
    private TableColumn t2columnaVolumen;


    @Autowired
    DetalleEntregaService detalleEntregaService;

    @Autowired
    DetalleDespachoService detalleDespachoService;

    @Autowired
    LoteService loteService;

    @Autowired
    DespachoService despachoService;

    @Autowired
    KardexService kardexService;
    @Autowired
    FuncionesMapa funcionesMapa;
    @Autowired
    MapaService mapaService;
    @Autowired
    TransportistaService transportistaService;

    private ArrayList<BloqueModel> bloqueModels;
    private ArrayList<Double> pesos;
    private ArrayList<Double> volumen;

    ObservableList<String> oltransportista;

    private Double pesoMax = 60.0;
    private Double volumenMax = 60.0;

    @Autowired
    private ApplicationContext context;

    private ObservableList<DetalleDespacho> oldetalleDespachos;
    private ObservableList<DetalleEntrega> oldetalleEntregas;

    private ArrayList<RutaModel> rutasModels;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldSobreNombre.setText("");
        bloqueModels = new ArrayList<>();
        pesos = new ArrayList<>();
        volumen = new ArrayList<>();
        rutasModels = new ArrayList<>();

        inicializarTabladetalleDespacho();
        inicializarTabladetalleEntrega();
        llenarTabaDetalleEntrega();

        ArrayList<TransportistaModel> transportista = (ArrayList) transportistaService.findAllTransportista();
        ArrayList<String> transportistas = new ArrayList<>();
        for (int i = 0; i < transportista.size(); i++) {
            transportistas.add(transportista.get(i).getNombreCompleto());
        }
        oltransportista = FXCollections.observableArrayList(transportistas);
        comboBoxTransportista.getItems().add("-Seleccione-");
        comboBoxTransportista.getItems().addAll(oltransportista);
        comboBoxTransportista.setEditable(false);
        comboBoxTransportista.setValue(comboBoxTransportista.getItems().get(0));
    }

    private void inicializarTabladetalleEntrega() {
        t1columnaLote.setCellValueFactory(new PropertyValueFactory<DetalleEntrega, String>("lote"));
        t1columnaFechaLimite.setCellValueFactory(new PropertyValueFactory<DetalleEntrega, Date>("fechaLimite"));
        t1columnaCantidad.setCellValueFactory(new PropertyValueFactory<DetalleEntrega, Integer>("cantidad"));
        t1columnaResta.setCellValueFactory(new PropertyValueFactory<DetalleEntrega, Integer>("resta"));
        t1columnaEstado.setCellValueFactory(new PropertyValueFactory<DetalleEntrega, String>("estado"));
        oldetalleEntregas = FXCollections.observableArrayList();
        tablaDetalleEntrega.setItems(oldetalleEntregas);
    }

    private void llenarTabaDetalleEntrega() {
        ArrayList<DetalleEntregaModel> detalleEntregas = (ArrayList) detalleEntregaService.findAllByEstadoEntrega("por atender");
        for (int i = 0; i < detalleEntregas.size(); i++) {
            DetalleEntrega detalleEntrega = new DetalleEntrega();

            detalleEntrega.setCantidad(detalleEntregas.get(i).getCantidadtotal());
            detalleEntrega.setFechaLimite(detalleEntregas.get(i).getFechaEntrega());
            detalleEntrega.setEstado(detalleEntregas.get(i).getEstadoEntrega());
            detalleEntrega.setResta(detalleEntregas.get(i).getCantidadEntregar());
            detalleEntrega.setLote(detalleEntregas.get(i).getIdDetalleDocumento().getIdLote().getId());
            detalleEntrega.setId(detalleEntregas.get(i).getId());
            oldetalleEntregas.add(detalleEntrega);
        }
    }


    private void inicializarTabladetalleDespacho() {
        t2columnaLote.setCellValueFactory(new PropertyValueFactory<DetalleDespacho, String>("lote"));
        t2columnaCantidad.setCellValueFactory(new PropertyValueFactory<DetalleDespacho, TextField>("cantidad"));
        t2columnaPeso.setCellValueFactory(new PropertyValueFactory<DetalleDespacho, Double>("peso"));
        t2columnaVolumen.setCellValueFactory(new PropertyValueFactory<DetalleDespacho, Double>("volumen"));

        oldetalleDespachos = FXCollections.observableArrayList();
        tablaDetalleDespacho.setItems(oldetalleDespachos);
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {
        Pane paneParent = (Pane) paneAgregarDespacho.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/despacho.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }


    @FXML
    void buttonAgregar(MouseEvent event) {

        DetalleEntrega detalleEntrega = tablaDetalleEntrega.getSelectionModel().getSelectedItem();
        DetalleDespacho detalleDespacho = new DetalleDespacho();

        detalleDespacho.setDetalleEntrega(detalleEntrega);
        detalleDespacho.setLote(detalleEntrega.getLote());
        LoteModel lote = loteService.findById(detalleEntrega.getLote());
        detalleDespacho.setPeso(lote.getProducto().getPeso().doubleValue());
        detalleDespacho.setVolumen(lote.getProducto().getVolumen().doubleValue());
        detalleDespacho.setCantidad(new TextField());
        detalleDespacho.getCantidad().setText(detalleEntrega.getResta().toString());
        detalleDespacho.getCantidad().setOnAction((eventText)->{
            if(Integer.parseInt(detalleDespacho.getCantidad().getText())>detalleEntrega.getCantidad()){
                detalleDespacho.getCantidad().setText(detalleEntrega.getCantidad()+"");
            }
        });
        detalleDespacho.getCantidad().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (t1.equals("")){
                    detalleDespacho.getCantidad().setText("1");
                    return;
                }
                try {
                    if (Integer.parseInt(detalleDespacho.getCantidad().getText()) > detalleEntrega.getResta()) {
                        detalleDespacho.getCantidad().setText(detalleEntrega.getResta() + "");
                    }
                } catch (NumberFormatException nfe) {
                    detalleDespacho.getCantidad().setText(t);
                }
            }
        });
        detalleDespacho.setIddetalleentrega(detalleEntrega.getId());
        oldetalleDespachos.add(detalleDespacho);
        oldetalleEntregas.remove(detalleEntrega);
    }

    @FXML
    void buttonDespachar(MouseEvent event) throws IOException {

        if (oldetalleDespachos.size() == 0) {
            Validador.mostrarDialogError("Debe seleccionar los productos para su despacho.");
            return;
        }

        if (comboBoxTransportista.getValue().equals("-Seleccione-")) {
            Validador.mostrarDialogError("¡Seleccione un Transportista!");
            return;
        }

        for (int i = 0; i < oldetalleDespachos.size(); i++) {
            System.out.println(oldetalleDespachos.get(i).getCantidad().getText());
        }
        Double pesoacum = 0.0;
        Double volacum = 0.0;
        DespachoModel despachoModel = new DespachoModel();
        DetalleEntregaModel detalleEntregaModel;

        for (int i = 0; i < oldetalleDespachos.size(); i++) {

            DetalleDespachoModel detalleDespachoModel = new DetalleDespachoModel();

            detalleDespachoModel.setCantidadAtendida(Integer.parseInt(oldetalleDespachos.get(i).getCantidad().getText()));
            detalleEntregaModel = detalleEntregaService.getById(oldetalleDespachos.get(i).getIddetalleentrega());
            detalleDespachoModel.setIddetalleEntrega(detalleEntregaModel);
            detalleDespachoModel.setEstadoDespacho("por atender");
            detalleDespachoModel.setEstado("activo");
            despachoModel.addDetalleDespacho(detalleDespachoModel);

            //detalleEntregaModel.addDetalleDespacho(detalleDespachoModel);

            pesoacum += oldetalleDespachos.get(i).getPeso() * Integer.parseInt(oldetalleDespachos.get(i).getCantidad().getText());
            volacum += oldetalleDespachos.get(i).getVolumen() * Integer.parseInt(oldetalleDespachos.get(i).getCantidad().getText());

        }

        despachoModel.setSobreNombre(textFieldSobreNombre.getText());
        despachoModel.setFecha(new Date());
        despachoModel.setPeso(pesoacum);
        despachoModel.setVolumen(volacum);
        despachoModel.setEstado("activo");
        despachoModel.setEstadoDespacho("por confirmar");
        TransportistaModel transportistaModel = transportistaService.getByNombreCompleto(comboBoxTransportista.getValue());
        despachoModel.setIdTransportista(transportistaModel);

        despachoService.save(despachoModel);
        LOGGERAUDIT.info("El usuario "+usuarioModel.getNombres()+" generó un despacho "+despachoModel.getSobreNombre());
        //detalleEntregaService.save(detalleEntregaModel);
        Pane paneParent = (Pane) paneAgregarDespacho.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/despacho.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    void buttonQuitar(MouseEvent event) {
        DetalleDespacho detalleDespacho = tablaDetalleDespacho.getSelectionModel().getSelectedItem();
        DetalleEntregaModel detalleEntregaModel = detalleEntregaService.getById(detalleDespacho.getIddetalleentrega());
        DetalleEntrega detalleEntrega = new DetalleEntrega();
        detalleEntrega.setCantidad(detalleEntregaModel.getCantidadEntregar());
        detalleEntrega.setFechaLimite(detalleEntregaModel.getFechaEntrega());
        detalleEntrega.setEstado(detalleEntregaModel.getEstadoEntrega());
        detalleEntrega.setResta(detalleEntregaModel.getCantidadEntregar());
        detalleEntrega.setLote(detalleEntregaModel.getIdDetalleDocumento().getIdLote().getId());
        detalleEntrega.setId(detalleEntregaModel.getId());
        oldetalleEntregas.add(detalleEntrega);
        oldetalleDespachos.remove(detalleDespacho);
    }
}
