package absortio.m00p4.negocio.ui.menu.unidadestransportes;

import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.UnidadTransporteModel;
import absortio.m00p4.negocio.model.auxiliares.Transportista;
import absortio.m00p4.negocio.model.auxiliares.UnidadTransporte;
import absortio.m00p4.negocio.reportes.ReporteTransportista;
import absortio.m00p4.negocio.reportes.ReporteUnidadTransporte;
import absortio.m00p4.negocio.service.TransportistaService;
import absortio.m00p4.negocio.service.UnidadTransporteService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.ui.menu.transportistas.TransportistasEditarController;
import absortio.m00p4.negocio.ui.menu.transportistas.TransportistasVerController;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class UnidadesTransportesController implements Initializable {

    @FXML
    Pane paneUnidadTransporte;
    @FXML
    ComboBox comboBoxCapacidadU;
    @FXML
    TextField textFieldPlacaU;
    @FXML
    TextField textFieldNombreTransportistaU;
    @FXML
    TableView <UnidadTransporte> tablaUnidadesTransporte;
    @FXML
    private TableColumn columnaNU;
    @FXML
    private TableColumn columnaPlacaU;
    @FXML
    private TableColumn columnaMarcaU;
    @FXML
    private TableColumn columnaModeloU;
    @FXML
    private TableColumn columnaVolumenU;
    @FXML
    private TableColumn columnaPesoU;
    @FXML
    private TableColumn columnaTransportistaU;
    @FXML
    private Button buttonBuscarU;
    @FXML
    private Button buttonExportarExcelU;
    @FXML
    private Button buttonNuevoU;
    @FXML
    private TableColumn columnaAccionesU;

    @FXML
    private Label labelTitulo;

    @FXML
    private Label labelFiltro;

    private Font fuenteLabelTitulo;

    private Font fuenteLabelFiltro;
    @Autowired
    TransportistaService transportistaService;

    ObservableList<UnidadTransporte> transportes;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Autowired
    UnidadTransporteService unidadTransporteService;          // Creamos un objeto de la clase, para que podamos usar todos sus métodos

    @Autowired
    private ApplicationContext context ;

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

        this.inicializarTablaUnidadTransporte();

        this.cargarGrillaUnidadesTransporte();       // Leemos todas las unidades de transporte
    }


    private void inicializarTablaUnidadTransporte() {
        columnaPlacaU.setCellValueFactory(new PropertyValueFactory<UnidadTransporte, String>("placa"));      // Los valores de la grilla son tomados de la clase auxiliar Transportista
        columnaNU.setCellValueFactory(new PropertyValueFactory<UnidadTransporte, Integer>("n"));
        columnaMarcaU.setCellValueFactory(new PropertyValueFactory<UnidadTransporte, String>("marca"));
        columnaModeloU.setCellValueFactory(new PropertyValueFactory<UnidadTransporte, String>("modelo"));
        columnaPesoU.setCellValueFactory(new PropertyValueFactory<UnidadTransporte, Float>("pesoSoportado"));
        columnaVolumenU.setCellValueFactory(new PropertyValueFactory<UnidadTransporte, Float>("volumenSoportado"));
        columnaTransportistaU.setCellValueFactory(new PropertyValueFactory<UnidadTransporte, String>("nombreTransportista"));

        transportes = FXCollections.observableArrayList();      // Inicializo la lista de unidades de transporte
        addButtonToTableUnidadTransporte();         // Agregamos los botones
        tablaUnidadesTransporte.setItems(transportes);          // Asocio la tabla con la lista
    }





    public void cargarGrillaUnidadesTransporte() {
        String estadoAux = "";

        transportes.clear();
        ArrayList<UnidadTransporteModel> unidadTransporteModels = (ArrayList) unidadTransporteService.findAllUnidadTransporte();  // Hacemos el query
        for (int i = 0; i < unidadTransporteModels.size(); i++) {
            // Insertamos el nuevo registro en la tabla con la mayor cantidad de informacion posible, pues luego la vamos a buscar por ella
            UnidadTransporte unidadTransporte = new UnidadTransporte();
            unidadTransporte.setN(i+1);
            unidadTransporte.setId(unidadTransporteModels.get(i).getId());
            unidadTransporte.setPlaca(unidadTransporteModels.get(i).getPlaca());
            unidadTransporte.setMarca(unidadTransporteModels.get(i).getMarca());
            unidadTransporte.setModelo(unidadTransporteModels.get(i).getModelo());
            unidadTransporte.setUnidadPeso(unidadTransporteModels.get(i).getUnidadPeso());
            unidadTransporte.setPesoSoportado(unidadTransporteModels.get(i).getPesoSoportado());
            unidadTransporte.setUnidadVolumen(unidadTransporteModels.get(i).getUnidadVolumen());
            unidadTransporte.setVolumenSoportado(unidadTransporteModels.get(i).getVolumenSoportado());
            unidadTransporte.setIdTransportista(unidadTransporteModels.get(i).getTransportista().getId());
            unidadTransporte.setNombreTransportista(unidadTransporteModels.get(i).getTransportista().getNombreCompleto());

            estadoAux = unidadTransporteModels.get(i).getEstado();
            if (estadoAux.compareTo("inactivo") == 0){
                // No se hace nada
            }
            else if (estadoAux.compareTo("activo") == 0){
                transportes.add(unidadTransporte);
            }
        }
    }

    @FXML
    void clickNuevoUnidadTransporte(MouseEvent event) throws IOException {
        Pane paneTransporte = (Pane) paneUnidadTransporte.getParent().getParent().getParent();         // ActualPane --> Tab --> TabPane --> Pane : Llegamos al PaneTransporte

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/nuevounidadestransporte.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneTransporte.getChildren().setAll(pane);
    }


    private void addButtonToTableUnidadTransporte() {
        Callback<TableColumn<UnidadTransporte, Void>, TableCell<UnidadTransporte, Void>> cellFactory = new Callback<TableColumn<UnidadTransporte, Void>, TableCell<UnidadTransporte, Void>>() {
            @Override
            public TableCell<UnidadTransporte, Void> call(final TableColumn<UnidadTransporte, Void> param) {
                final TableCell<UnidadTransporte, Void> cell = new TableCell<UnidadTransporte, Void>() {
                    private final Button btnVer = new Button();
                    {
                        btnVer.setOnAction( (event) -> {
                            UnidadTransporte data = getTableView().getItems().get(getIndex());  // obtenemos la información básica (n, doc, nombreCompleto, telefono) de un elemento en la grilla
                            levantarPaneVer(data);
                        });

                    }
                    private final Button btnEditar = new Button();
                    {
                        btnEditar.setOnMouseClicked( (event) -> {
                            UnidadTransporte data = getTableView().getItems().get(getIndex());
                            levantarPaneEditar(data);
                        });
                    }
                    private final Button btnEliminar = new Button();

                    {

                        btnEliminar.setOnAction( (event) -> {
                            UnidadTransporte data = getTableView().getItems().get(getIndex());
                            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                            dialogoAlerta.setTitle("Ventana de Confirmacion");
                            dialogoAlerta.setHeaderText(null);
                            dialogoAlerta.initStyle(StageStyle.UTILITY);
                            dialogoAlerta.setContentText("Realmente Quieres Elimarlo");
                            Optional<ButtonType> result = dialogoAlerta.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                unidadTransporteService.updateEstadoById(data.getId(), "inactivo");
                                //unidadTransporteService.deleteByPlaca(data.getPlaca());
                            }

                            cargarGrillaUnidadesTransporte();
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
        columnaAccionesU.setCellFactory(cellFactory);
    }


    private void levantarPaneEditar(UnidadTransporte data)  {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/editarunidadestransporte.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane paneTransporte = (Pane) paneUnidadTransporte.getParent().getParent().getParent();         // ActualPane --> Tab --> TabPane --> Pane : Llegamos al PaneTransporte
            Pane pane = fxmlLoader.load();
            fxmlLoader.<UnidadesTransportesEditarController>getController().leerDato(data);
            paneTransporte.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void levantarPaneVer(UnidadTransporte data)  {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/verunidadestransporte.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane paneTransporte = (Pane) paneUnidadTransporte.getParent().getParent().getParent();         // ActualPane --> Tab --> TabPane --> Pane : Llegamos al PaneTransporte
            Pane pane = fxmlLoader.load();
            fxmlLoader.<UnidadesTransportesVerController>getController().leerDato(data);
            paneTransporte.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void clickExportarUnidadesTransporte(MouseEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteUnidadTransporte reporteUnidadTransporte = new ReporteUnidadTransporte();
        reporteUnidadTransporte.generarReporteUnidadTransporte(unidadTransporteService.findAllUnidadTransporte());

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }


}


