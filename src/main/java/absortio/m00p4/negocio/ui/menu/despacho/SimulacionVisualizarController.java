package absortio.m00p4.negocio.ui.menu.despacho;

import absortio.m00p4.negocio.model.BloqueModel;
import absortio.m00p4.negocio.model.MapaModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.algoritmo.algoritmosPrincipales.AlgoritmoGenetico;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Coordenada;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.ExcelSolucion;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Solucion;
import absortio.m00p4.negocio.service.BloqueService;
import absortio.m00p4.negocio.service.MapaService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.ui.menu.almacenes.funciones.FuncionesMapa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@Component //Que estupidez
public class SimulacionVisualizarController {

    @FXML
    private Pane paneSimulacionVis;

    @FXML
    private GridPane gridPaneGrilla;

    @FXML
    private Button buttonSiguientePaso;

    @Autowired
    FuncionesMapa funcionesMapa;

    @Autowired
    MapaService mapaService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    BloqueService bloqueService;

    private ArrayList<ArrayList<Pane>> grillaCeldas;

    private ArrayList<ArrayList<Coordenada>> coordenadas;

    private ArrayList<ProductoModel> productos;

    private Integer siguientePaso = 0;

    private Coordenada posicionActual;

    private Random randomRojo;

    private Random randomVerde;

    private Random randomAzul;

    private Integer puertaY;

    private Integer puertaX;

    private ArrayList<ArrayList<String>> matrizMapa;

    @FXML
    private Button buttonReiniciar;

    @FXML
    private Button buttonLimpiar;

    @FXML
    private Label labelAccion;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @FXML
    void clickExportarExcel(MouseEvent event) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = directoryChooser.showDialog(null);
            SystemSingleton.getInstance().setRute(file.getPath());
            ExcelSolucion.generarExcelSolucion(coordenadas, matrizMapa);

            Alert dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
            dialogoAlerta.setTitle("Listo!");
            dialogoAlerta.setHeaderText(null);
            dialogoAlerta.setContentText("El archivo se ha descargado satisfactoriamente.");
            dialogoAlerta.initStyle(StageStyle.UTILITY);
            dialogoAlerta.show();
        } catch (IOException xd) {
            xd.printStackTrace();
        }
    }

    @FXML
    void limpiarMapa(MouseEvent event) {
        for (int j = 0; j < matrizMapa.size(); j++) {
            for (int i = 0; i < matrizMapa.get(j).size(); i++) {
                if (posicionActual.getX() == i && posicionActual.getY() == j) continue;
                Pane xd = grillaCeldas.get(j).get(i);
                if (matrizMapa.get(j).get(i).equals("=") || matrizMapa.get(j).get(i).equals("r"))
                    xd.setBackground(new Background(new BackgroundFill(Color.web("#464646"), CornerRadii.EMPTY, Insets.EMPTY)));
                else
                    xd.setBackground(new Background(new BackgroundFill(Color.web("#c3c3c3"), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
        if (posicionActual.getY() != puertaY && posicionActual.getX() != puertaX)
            grillaCeldas.get(puertaY).get(puertaX).setBackground(new Background(new BackgroundFill(Color.web("#c3c3c3"), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @FXML
    void reiniciarMapa(MouseEvent event) {
        for (int j = 0; j < matrizMapa.size(); j++) {
            for (int i = 0; i < matrizMapa.get(j).size(); i++) {
                Pane xd = grillaCeldas.get(j).get(i);
                if (matrizMapa.get(j).get(i).equals("=") || matrizMapa.get(j).get(i).equals("r"))
                    xd.setBackground(new Background(new BackgroundFill(Color.web("#464646"), CornerRadii.EMPTY, Insets.EMPTY)));
                else
                    xd.setBackground(new Background(new BackgroundFill(Color.web("#c3c3c3"), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
        siguientePaso = 0;
        randomRojo = new Random(348622472);
        randomVerde = new Random(561684240);
        randomAzul = new Random(272643300);
        grillaCeldas.get(puertaY).get(puertaX).setBackground(new Background(new BackgroundFill(Color.web("#c3c3c3"), CornerRadii.EMPTY, Insets.EMPTY)));
        buttonLimpiar.setDisable(true);
        buttonSiguientePaso.setDisable(false);
        labelAccion.setText("");
    }

    @FXML
    void clickSiguientePaso(MouseEvent event) {
        ArrayList<Coordenada> paso = coordenadas.get(siguientePaso);
        if (productos.get(siguientePaso) == null) {
            labelAccion.setText("Dejando los productos recogidos en la puerta");
        } else {
            labelAccion.setText("Recogiendo " + productos.get(siguientePaso).getNombre());
        }
        siguientePaso++;
        if (siguientePaso.equals(coordenadas.size())) buttonSiguientePaso.setDisable(true);
        Color colorPaso = Color.rgb(randomRojo.nextInt(256), randomVerde.nextInt(256), randomAzul.nextInt(256));
        for (Coordenada coord : paso) {
            grillaCeldas.get(coord.getY()).get(coord.getX()).setBackground(new Background(new BackgroundFill(colorPaso, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        posicionActual = paso.get(paso.size() - 1);
        buttonLimpiar.setDisable(false);
        buttonReiniciar.setDisable(false);
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {
        Pane paneParent = (Pane) paneSimulacionVis.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/despacho.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }


    @FXML
    public void recibirIdSimulacion(ArrayList<ArrayList<Coordenada>> pcoords, ArrayList<ProductoModel> prods) {
        productos = prods;
        coordenadas = pcoords;
        siguientePaso = 0;
        randomRojo = new Random(348622472);
        randomVerde = new Random(561684240);
        randomAzul = new Random(272643300);
        grillaCeldas = new ArrayList<>();
        ArrayList<MapaModel> mapaModels = (ArrayList) mapaService.findAllMapa();
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
                Pane xd = new Pane();
                xd.setPrefSize(14.0, 14.0);
                if (matrizMapa.get(j).get(i).equals("=") || matrizMapa.get(j).get(i).equals("r"))
                    xd.setBackground(new Background(new BackgroundFill(Color.web("#464646"), CornerRadii.EMPTY, Insets.EMPTY)));
                else
                    xd.setBackground(new Background(new BackgroundFill(Color.web("#c3c3c3"), CornerRadii.EMPTY, Insets.EMPTY)));
                int top = 0, left = 0;
                if (j == 0) top = 1;
                if (i == 0) left = 1;
                BorderWidths bw = new BorderWidths(top, 1, 1, left);
                xd.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, bw)));
                gridPaneGrilla.add(xd, i, j);
                grillaCeldas.get(j).add(xd);
            }
        }
        puertaX = mapaModels.get(0).getPuertax();
        puertaY = mapaModels.get(0).getPuertay();
        grillaCeldas.get(mapaModels.get(0).getPuertay()).get(mapaModels.get(0).getPuertax()).setBackground(new Background(new BackgroundFill(Color.web("#c3c3c3"), CornerRadii.EMPTY, Insets.EMPTY)));

        ///////////////////////////////////////////////    BORRAR    ///////////////////////////////////////////////


//        ArrayList<BloqueModel> bloqueModels = new ArrayList<>();
//        ArrayList<Double> pesos = new ArrayList<>();
//        ArrayList<Double> volumen = new ArrayList<>();
//        Double pesoMax = 700.0;
//        Double volumenMax = 700.0;
//
//        bloqueModels.clear();
//        pesos.clear();
//        volumen.clear();
//        bloqueModels.addAll(bloqueService.findAllBloque());
//        ArrayList<BloqueModel> bloquesSimulacionFalsa = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            BloqueModel bloque = bloqueService.getById((new Random()).nextInt(bloqueModels.size()));
//            bloquesSimulacionFalsa.add(bloque);
//            pesos.add(150.0);
//            volumen.add(150.0);
//        }
//
//        AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico(bloquesSimulacionFalsa, pesos, volumen, pesoMax, volumenMax, matrizMapa);
//        Solucion solucion = algoritmoGenetico.correrAlgoritmo();
//        coordenadas = solucion.getRutasCoord();
    }

}