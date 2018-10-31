package absortio.m00p4.negocio.ui.menu.despacho;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.algoritmosPrincipales.AlgoritmoGenetico;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Coordenada;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.ExcelSolucion;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Solucion;
import absortio.m00p4.negocio.model.auxiliares.DetalleDespacho;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import absortio.m00p4.negocio.model.auxiliares.Simulacion;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.ui.menu.almacenes.funciones.FuncionesMapa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class SimulacionController implements Initializable {


    @FXML
    private Pane paneSimulacion;

    @FXML
    private Button buttonExportar;

    @FXML
    private Label labelSobrenombreDespacho;

    @FXML
    private Label labelFechaDespacho;

    @FXML
    private Label labelVolumenDespacho;

    @FXML
    private Label labelPesoDespacho;

    @FXML
    private Label labelTransportistaDespacho;

    @FXML
    private TableView<DetalleDespacho> tablaDetalleDespacho;

    @FXML
    private TableColumn t2columnaLote;

    @FXML
    private TableColumn t2columnaCantidad;

    @FXML
    private TableColumn t2columnaPeso;

    @FXML
    private TableColumn t2columnaVolumen;

    @FXML
    private TableView<Simulacion> tableSimulaciones;

    @FXML
    private TableColumn t1columnaSimulacion;

    @FXML
    private TableColumn t1ColumnaPuntuacion;

    @FXML
    private TableColumn t1ColumnaDescargar;

    @FXML
    private TableColumn t1ColumnaVisualizar;

    private int idDespacho;


    @Autowired
    DetalleDespachoService detalleDespachoService;

    @Autowired
    DespachoService despachoService;
    @Autowired
    private ApplicationContext context;

    @Autowired
    KardexService kardexService;
    @Autowired
    FuncionesMapa funcionesMapa;
    @Autowired
    MapaService mapaService;

    @Autowired
    DetalleEntregaService detalleEntregaService;

    @Autowired
    BloqueService bloqueService;

    @Autowired
    RutaService rutaService;

    @Autowired
    RutaPasoService rutaPasoService;

    @Autowired
    RutaCoordenadaService rutaCoordenadaService;

    private ArrayList<RutaModel> rutasModels;
    private Double pesoMax = 700.0;
    private Double volumenMax = 700.0;

    private ObservableList<DetalleDespacho> oldetalleDespachos;

    ObservableList<Simulacion> olSimulaciones;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @FXML
    void nuevaSimulacion(MouseEvent event) throws IOException {
        rutasModels = new ArrayList<>();
        ArrayList<BloqueModel> bloqueModels = new ArrayList<>();
        ArrayList<Double> pesos = new ArrayList<>();
        ArrayList<Double> volumen = new ArrayList<>();
        Double pesoacum = 0.0;
        Double volacum = 0.0;
        DespachoModel despachoModel = new DespachoModel();
        DetalleEntregaModel detalleEntregaModel = null;
        for (int i = 0; i < oldetalleDespachos.size(); i++) {
            detalleEntregaModel = detalleEntregaService.getById(oldetalleDespachos.get(i).getIddetalleentrega());
            LoteModel loteModel = detalleEntregaModel.getIdDetalleDocumento().getIdLote();
            List<Integer> bloquesdistinct = kardexService.findAllDistinctForLote(loteModel.getId());
            KardexModel ultimaoperacion;
            if (bloquesdistinct.isEmpty()) {
                System.out.println("No hay stock");
            }
            List<BloqueModel> bloquesdisponibles = new ArrayList<>();
            List<KardexModel> kardexconStockFinal = new ArrayList<>();
            for (int j = 0; j < bloquesdistinct.size(); j++) {

                List<KardexModel> operacionesxbloque = kardexService.findOperacion(bloquesdistinct.get(j));
                ultimaoperacion = operacionesxbloque.get(operacionesxbloque.size() - 1);

                if (ultimaoperacion.getStockfisico() > 0) {
                    bloquesdisponibles.add(ultimaoperacion.getBloque());
                    kardexconStockFinal.add(ultimaoperacion);
                }
            }
            Integer iterator = 0;
            Integer cantidad = 0;
            Integer cantidadporsacar = Integer.parseInt(oldetalleDespachos.get(i).getCantidad().getText());
            while (cantidadporsacar > 0) {
                Integer stockdeBloqueI = kardexconStockFinal.get(iterator).getStockfisico();
                if (cantidadporsacar - stockdeBloqueI <= 0) {
                    cantidad = cantidadporsacar;
                } else cantidad = stockdeBloqueI;
                cantidadporsacar -= stockdeBloqueI;
                bloqueModels.add(kardexconStockFinal.get(iterator).getBloque());
                pesos.add(cantidad * loteModel.getProducto().getPeso());
                volumen.add(cantidad * loteModel.getProducto().getVolumen());
                iterator++;
            }
            pesoacum += oldetalleDespachos.get(i).getPeso() * Integer.parseInt(oldetalleDespachos.get(i).getCantidad().getText());
            volacum += oldetalleDespachos.get(i).getVolumen() * Integer.parseInt(oldetalleDespachos.get(i).getCantidad().getText());
        }

//        DirectoryChooser directoryChooser = new DirectoryChooser();
//        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//        File file = directoryChooser.showDialog(null);
//        SystemSingleton.getInstance().setRute(file.getPath());

        ArrayList<MapaModel> mapaModels = (ArrayList) mapaService.findAllMapa();
        System.out.println(mapaModels.size());

        Alert dialogoAlerta = new Alert(Alert.AlertType.NONE);
        dialogoAlerta.setTitle("Generando ruta, espere por favor...");
        dialogoAlerta.setHeaderText(null);
        dialogoAlerta.initStyle(StageStyle.UTILITY);
        dialogoAlerta.show();

        funcionesMapa.crearMatriz(mapaModels.get(0));
        funcionesMapa.llenarMapa(mapaModels.get(0));
        SystemSingleton.getInstance().setMapa(mapaModels.get(0));
        funcionesMapa.printMatriz(mapaModels.get(0));
        ArrayList<ArrayList<String>> matrizMapa = funcionesMapa.getMapaDibujado();

        AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico(bloqueModels, pesos, volumen, pesoMax, volumenMax, matrizMapa, kardexService);
        Solucion solucion = algoritmoGenetico.correrAlgoritmo();
        //String nombreArchivo = solucion.exportarExcel();
        ArrayList<ProductoModel> productos = solucion.getListaProductos();

        RutaModel rutaModel = new RutaModel();
        rutaModel.setFuncionObjetivo(solucion.Puntuacion());
        rutaModel.setPesoTotal(pesoMax);
        rutaModel.setVolumenTotal(volumenMax);
        rutaModel.setRuta(solucion.serializar());
        rutaModel.setNombreArchivo("");
        rutaModel.setNumeroViajes(solucion.getPasos().size());
        rutaModel.setIdDespacho(despachoService.findById(idDespacho));

        rutaService.save(rutaModel);
        rutasModels.add(rutaModel);

        ArrayList<ArrayList<Coordenada>> rutasCoord = solucion.getRutasCoord();
        for (int i = 0; i < rutasCoord.size(); i++) {
            RutaPasoModel rutaPasoModel = new RutaPasoModel();
            rutaPasoModel.setIdProducto(productos.get(i));
            rutaPasoModel.setIdRuta(rutaModel);
            rutaPasoService.save(rutaPasoModel);
            for (int j = 0; j < rutasCoord.get(i).size(); j++) {
                RutaCoordenadaModel rutaCoordenadaModel = new RutaCoordenadaModel();
                rutaCoordenadaModel.setIdRutaPaso(rutaPasoModel);
                rutaCoordenadaModel.setCoordenadaX(rutasCoord.get(i).get(j).getX());
                rutaCoordenadaModel.setCoordenadaY(rutasCoord.get(i).get(j).getY());
                rutaCoordenadaService.save(rutaCoordenadaModel);
            }
        }

        Simulacion simulacion = new Simulacion();
        simulacion.setId(rutaModel.getId());
        simulacion.setCoordenadas(solucion.getRutasCoord());
        simulacion.setProductos(productos);
        simulacion.setName("Simulacion " + simulacion.getId());
        simulacion.setPuntuacion(rutaModel.getFuncionObjetivo());
        simulacion.setRutaSerializada(rutaModel.getRuta());
        simulacion.setButtonDescargar(new Button("Descargar"));
        simulacion.getButtonDescargar().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                    File file = directoryChooser.showDialog(null);
                    SystemSingleton.getInstance().setRute(file.getPath());
                    ExcelSolucion.deserializarSimulacion(simulacion.getRutaSerializada());
                    Alert dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
                    dialogoAlerta.setTitle("Listo!");
                    dialogoAlerta.setHeaderText(null);
                    dialogoAlerta.setContentText("El archivo se ha descargado satisfactoriamente.");
                    dialogoAlerta.initStyle(StageStyle.UTILITY);
                    dialogoAlerta.show();
                } catch (IOException fuimos) {
                    fuimos.printStackTrace();
                }
            }
        });
        simulacion.setButtonVisualizar(new Button("Visualizar"));
        simulacion.getButtonVisualizar().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/simulacionVisualizar.fxml"));
                fxmlLoader.setControllerFactory(context::getBean);
                Pane pane = null;
                try {
                    pane = fxmlLoader.load();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                fxmlLoader.<SimulacionVisualizarController>getController().recibirIdSimulacion(simulacion.getCoordenadas(), simulacion.getProductos());
                paneSimulacion.getChildren().setAll(pane);
            }
        });
        olSimulaciones.add(simulacion);
        LOGGERAUDIT.info("El usuario"+usuarioModel.getNombres()+ "generó una nueva simulacion");
        dialogoAlerta.setAlertType(Alert.AlertType.INFORMATION);
        dialogoAlerta.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {
        Pane paneParent = (Pane) paneSimulacion.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/despacho.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    public void recibirIdDespacho(Integer id) {
        idDespacho = id;
        DespachoModel despachoModel = despachoService.findById(idDespacho);
        labelSobrenombreDespacho.setText(despachoModel.getSobreNombre());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        labelFechaDespacho.setText(sdf.format(despachoModel.getFecha()));
        labelVolumenDespacho.setText(despachoModel.getVolumen().toString());
        labelPesoDespacho.setText(despachoModel.getPeso().toString());
        try {
            labelTransportistaDespacho.setText(despachoModel.getIdTransportista().getNombreCompleto());
        } catch (Exception e) {
            labelTransportistaDespacho.setText("");
        }

        t1columnaSimulacion.setCellValueFactory(new PropertyValueFactory<Simulacion, String>("name"));
        t1ColumnaPuntuacion.setCellValueFactory(new PropertyValueFactory<Simulacion, Double>("puntuacion"));
        t1ColumnaDescargar.setCellValueFactory(new PropertyValueFactory<Simulacion, Button>("buttonDescargar"));
        t1ColumnaVisualizar.setCellValueFactory(new PropertyValueFactory<Simulacion, Button>("buttonVisualizar"));
        olSimulaciones = FXCollections.observableArrayList();
        tableSimulaciones.setItems(olSimulaciones);

        ArrayList<RutaModel> rutas = (ArrayList) rutaService.findByIdDespacho(idDespacho);
        for (int i = 0; i < rutas.size(); i++) {
            Simulacion simulacion = new Simulacion();
            simulacion.setId(rutas.get(i).getId());
            simulacion.setName("Simulacion " + simulacion.getId());
            simulacion.setPuntuacion(rutas.get(i).getFuncionObjetivo());
            simulacion.setButtonDescargar(new Button("Descargar"));
            simulacion.setRutaSerializada(rutas.get(i).getRuta());
            simulacion.getButtonDescargar().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                        File file = directoryChooser.showDialog(null);
                        SystemSingleton.getInstance().setRute(file.getPath());
                        ExcelSolucion.deserializarSimulacion(simulacion.getRutaSerializada());
                        Alert dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
                        dialogoAlerta.setTitle("Listo!");
                        dialogoAlerta.setHeaderText(null);
                        dialogoAlerta.setContentText("El archivo se ha descargado satisfactoriamente.");
                        dialogoAlerta.initStyle(StageStyle.UTILITY);
                        dialogoAlerta.show();
                    } catch (IOException fuimos) {
                        fuimos.printStackTrace();
                    }
                }
            });
            simulacion.setButtonVisualizar(new Button("Visualizar"));
            simulacion.getButtonVisualizar().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/simulacionVisualizar.fxml"));
                    fxmlLoader.setControllerFactory(context::getBean);
                    Pane pane = null;
                    try {
                        pane = fxmlLoader.load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    ArrayList<ArrayList<Coordenada>> coords = new ArrayList<>();
                    ArrayList<ProductoModel> prods = new ArrayList<>();
                    ArrayList<RutaPasoModel> rutaPasoModels = (ArrayList) rutaPasoService.findAllByRutaId(simulacion.getId());
                    for (int h = 0; h < rutaPasoModels.size(); h++) {
                        ArrayList<RutaCoordenadaModel> rutacoord = (ArrayList) rutaCoordenadaService.findAllByIdRutaPaso(rutaPasoModels.get(h).getId());
                        ArrayList<Coordenada> coordsRutaActual = new ArrayList<>();
                        for (int m = 0; m < rutacoord.size(); m++) {
                            coordsRutaActual.add(new Coordenada(rutacoord.get(m).getCoordenadaX(), rutacoord.get(m).getCoordenadaY(),0));
                        }
                        prods.add(rutaPasoModels.get(h).getIdProducto());
                        coords.add(coordsRutaActual);
                    }
                    fxmlLoader.<SimulacionVisualizarController>getController().recibirIdSimulacion(coords, prods);
                    paneSimulacion.getChildren().setAll(pane);
                }
            });
            olSimulaciones.add(simulacion);
        }
        t2columnaLote.setCellValueFactory(new PropertyValueFactory<DetalleDespacho, String>("lote"));
        t2columnaCantidad.setCellValueFactory(new PropertyValueFactory<DetalleDespacho, TextField>("cantidad"));
        t2columnaPeso.setCellValueFactory(new PropertyValueFactory<DetalleDespacho, Double>("peso"));
        t2columnaVolumen.setCellValueFactory(new PropertyValueFactory<DetalleDespacho, Double>("volumen"));

        oldetalleDespachos = FXCollections.observableArrayList();
        tablaDetalleDespacho.setItems(oldetalleDespachos);

        ArrayList<DetalleDespachoModel> detallesDespacho = (ArrayList) detalleDespachoService.findAllDetalleDespachoByIdDespacho(idDespacho);
        for (int i = 0; i < detallesDespacho.size(); i++) {
            LoteModel lote;
            DetalleDespacho detalleDespacho = new DetalleDespacho();
            detalleDespacho.setIddetalleentrega(detallesDespacho.get(i).getIddetalleEntrega().getId());
            detalleDespacho.setLote((lote = detallesDespacho.get(i).getIddetalleEntrega().getIdDetalleDocumento().getIdLote()).getId());
            detalleDespacho.setCantidad(new TextField());
            detalleDespacho.getCantidad().setDisable(true);
            detalleDespacho.getCantidad().setText(detallesDespacho.get(i).getCantidadAtendida().toString());
            detalleDespacho.setPeso(lote.getProducto().getPeso().doubleValue());
            detalleDespacho.setVolumen(lote.getProducto().getVolumen().doubleValue());
            oldetalleDespachos.add(detalleDespacho);
        }
    }
}
