package absortio.m00p4.negocio.ui.menu.almacenes;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.KardexModel;
import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Coordenada;
import absortio.m00p4.negocio.service.BloqueService;
import absortio.m00p4.negocio.service.KardexService;
import absortio.m00p4.negocio.service.MapaService;
import absortio.m00p4.negocio.service.ProductoService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.ui.menu.almacenes.funciones.FuncionesMapa;
import absortio.m00p4.negocio.ui.menu.kardex.listaProductosAlmacen;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Component //Que estupidez
public class AlmacenVisualizarController {

    private ArrayList<ArrayList<Pane>> grillaCeldas;

    private ArrayList<Pane> celdaBloquesBusqueda;

    @Autowired
    MapaService mapaService;

    @Autowired
    FuncionesMapa funcionesMapa;

    @Autowired
    BloqueService bloqueService;

    private ArrayList<ArrayList<String>> matrizMapa;

    @FXML
    private GridPane gridPaneGrilla;

    ArrayList<BloqueModel> bloquesSeleccionados;

    private Pane panelSeleccionado;

    private HashMap<Pane, Coordenada> coordenadasPane;

    @FXML
    private ComboBox<String> comboBoxPiso;

    @FXML
    private Label labelCodigoLote;

    @FXML
    private Label labelCantidad;

    @FXML
    private TextArea textAreaNombreProducto;

    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");


    @FXML
    private Button botonBuscar;

    @FXML
    private TextField textFieldNombreProducto;

    @FXML
    private TextField invisible;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private KardexService kardexService;

    @Autowired
    private ApplicationContext context;

    private ObservableList<ProductoModel> productosAlmacen;

    @FXML
    void buscarBloquesProducto(MouseEvent event) throws IOException {

        for (Pane panel : celdaBloquesBusqueda) {
            panel.setBackground(new Background(new BackgroundFill(Color.web("#464646"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        celdaBloquesBusqueda.clear();
        productosAlmacen.clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/listaProductosAlmacen.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root1 = fxmlLoader.load();
        fxmlLoader.<listaProductosAlmacen>getController().recibirDato(productosAlmacen, invisible, textFieldNombreProducto);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar producto");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    private void setCampos(String texto) {
        textAreaNombreProducto.setText(texto);
        labelCodigoLote.setText(texto);
        labelCantidad.setText(texto);
    }

    @FXML
    void seleccionarPiso(ActionEvent event) {

        if (comboBoxPiso.getValue() == null) return;
        Integer piso = Integer.parseInt(comboBoxPiso.getValue().substring(5));
        for (BloqueModel bloque : bloquesSeleccionados) {
            if (bloque.getPiso().getId().equals(piso)) {
                if (bloque.getEstado().equals("ocupado")) {
                    try {
                        KardexModel kardex = kardexService.findLoteInBloque(bloque.getId());
                        textAreaNombreProducto.setText(kardex.getLote().getProducto().getNombre());
                        labelCodigoLote.setText(kardex.getLote().getId().toString());
                        labelCantidad.setText(kardex.getStockfisico().toString());
                    } catch (IndexOutOfBoundsException iobe) {
                        setCampos("Vacío.");
                    }
                } else {
                    setCampos("Vacío.");
                }
            }
        }
    }

    public void iniciar() {
        productosAlmacen = FXCollections.observableArrayList();
        grillaCeldas = new ArrayList<>();

        invisible.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String before, String after) {
                if (!before.equals(after)) {
                    ProductoModel producto = productosAlmacen.get(0);
                    ArrayList<BloqueModel> bloques = (ArrayList) bloqueService.findAllBloque();
                    for (BloqueModel bloque : bloques) {
                        try {
                            if (kardexService.findLoteInBloque(bloque.getId()).getLote().getProducto().getId().equals(producto.getId()) && bloque.getEstado().equals("ocupado")) {
                                celdaBloquesBusqueda.add(grillaCeldas.get(bloque.getPosY()).get(bloque.getPosX()));
                                grillaCeldas.get(bloque.getPosY()).get(bloque.getPosX()).setBackground(new Background(new BackgroundFill(Color.web("#0000ff"), CornerRadii.EMPTY, Insets.EMPTY)));
                            }
                        } catch (Exception npe) {

                        }
                    }
                }
            }
        });

        ArrayList<MapaModel> mapaModels = (ArrayList) mapaService.findAllMapa();
        //bloquesSeleccionados = new ArrayList<>();
        celdaBloquesBusqueda = new ArrayList<>();
        coordenadasPane = new HashMap<>();
        funcionesMapa.crearMatriz(mapaModels.get(0));
        funcionesMapa.llenarMapa(mapaModels.get(0));
        SystemSingleton.getInstance().setMapa(mapaModels.get(0));
        funcionesMapa.printMatriz(mapaModels.get(0));
        matrizMapa = funcionesMapa.getMapaDibujado();
        for (int i = 1; i < matrizMapa.get(0).size(); i++)
            gridPaneGrilla.addColumn(1);
        for (int j = 1; j < matrizMapa.size(); j++)
            gridPaneGrilla.addRow(1);
        for (int j = 0; j < matrizMapa.size(); j++) {
            grillaCeldas.add(new ArrayList<>());
            for (int i = 0; i < matrizMapa.get(j).size(); i++) {
                Pane panelActual = new Pane();
                panelActual.setPrefSize(14.0, 14.0);
                if (matrizMapa.get(j).get(i).equals("=") || matrizMapa.get(j).get(i).equals("r"))
                    panelActual.setBackground(new Background(new BackgroundFill(Color.web("#464646"), CornerRadii.EMPTY, Insets.EMPTY)));
                else
                    panelActual.setBackground(new Background(new BackgroundFill(Color.web("#c3c3c3"), CornerRadii.EMPTY, Insets.EMPTY)));
                int top = 0, left = 0;
                if (j == 0) top = 1;
                if (i == 0) left = 1;
                BorderWidths bw = new BorderWidths(top, 1, 1, left);
                panelActual.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, bw)));
                panelActual.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        if (panelSeleccionado == panelActual) return;
                        comboBoxPiso.getItems().clear();
                        setCampos("-Seleccione-");
                        Integer posX = coordenadasPane.get(panelActual).getX();
                        Integer posY = coordenadasPane.get(panelActual).getY();
                        if (panelSeleccionado != null) {
                            if (celdaBloquesBusqueda.contains(panelSeleccionado))
                                panelSeleccionado.setBackground(new Background(new BackgroundFill(Color.web("#0000ff"), CornerRadii.EMPTY, Insets.EMPTY)));
                            else
                                panelSeleccionado.setBackground(new Background(new BackgroundFill(Color.web("#464646"), CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                        panelSeleccionado = null;
                        if (!matrizMapa.get(posY).get(posX).equals("r")) return;
                        panelSeleccionado = panelActual;
                        if (celdaBloquesBusqueda.contains(panelActual))
                            panelActual.setBackground(new Background(new BackgroundFill(Color.web("#ff00ff"), CornerRadii.EMPTY, Insets.EMPTY)));
                        else
                            panelActual.setBackground(new Background(new BackgroundFill(Color.web("#ff0000"), CornerRadii.EMPTY, Insets.EMPTY)));
                        bloquesSeleccionados = (ArrayList) bloqueService.findAllByPosxAndPosy(posX, posY); //
                        for (BloqueModel bloque : bloquesSeleccionados) {
                            comboBoxPiso.getItems().add("Piso " + bloque.getPiso().getId());
                        }
                        java.util.Collections.sort(comboBoxPiso.getItems());
                    }
                });
                gridPaneGrilla.add(panelActual, i, j);
                grillaCeldas.get(j).add(panelActual);
                coordenadasPane.put(panelActual, new Coordenada(i, j, 0));
            }
        }
        grillaCeldas.get(mapaModels.get(0).getPuertay()).get(mapaModels.get(0).getPuertax()).setBackground(new Background(new BackgroundFill(Color.web("#c3c3c3"), CornerRadii.EMPTY, Insets.EMPTY)));

    }
}
