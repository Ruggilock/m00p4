package absortio.m00p4.negocio.ui.menu.almacenes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import absortio.m00p4.negocio.model.SectorAlmacenModel;
import absortio.m00p4.negocio.model.auxiliares.Almacen;
import absortio.m00p4.negocio.service.SectorAlmacenService;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AlmacenesController implements Initializable {

    @FXML
    private Label labelAlmacenesTitulo;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane paneAlmacenesDistribucion;
    @FXML
    private Button botonExportarExcel1;
    @FXML
    private Button buttonNuevo1;
    @FXML
    private TableView<Almacen> tablaAlmacenes;
    @FXML
    private TableColumn columnaId;
    @FXML
    private TableColumn columnaNombreSeccion;
    @FXML
    private TableColumn columnaTipo;
    @FXML
    private TableColumn columnaDescripcion;
    @FXML
    private TableColumn columnaAcciones;
    @FXML
    private TextField textFieldNombres;
    @FXML
    private ComboBox<String> comboBoxTipo;
    @FXML
    private Button botonBuscar1;


    Font fuenteLabelAlmacenes;

    ObservableList<Almacen> almacenes;

    @Autowired
    SectorAlmacenService almacenesService;

    @Autowired
    private ApplicationContext context;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fuenteLabelAlmacenes = new Font("Century Gothic", 49);
        labelAlmacenesTitulo.fontProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> ov, Font b4, Font aftr) {
                if (aftr.equals(fuenteLabelAlmacenes)) return;
                labelAlmacenesTitulo.setFont(fuenteLabelAlmacenes);
            }
        });
        inicializarTablaAlmacenes();
        setAlmacenes();
        textFieldNombres.setText("");
        ArrayList<String> nombres = new ArrayList<>();
        for (int i = 0; i < almacenes.size(); i++) {
            nombres.add(almacenes.get(i).getNombreSeccion());
        }
        ObservableList<String> olNombres = FXCollections.observableArrayList(nombres);
        TextFields.bindAutoCompletion(textFieldNombres, olNombres);
        ArrayList<String> lroles = new ArrayList<>();
        lroles.add("");
        lroles.add("Tipo 1");
        lroles.add("Tipo 2");
        ObservableList<String> olRol = FXCollections.observableArrayList(lroles);
        comboBoxTipo.getItems().addAll(olRol);
        comboBoxTipo.setValue(olRol.get(0));

    }

    private void inicializarTablaAlmacenes() {
        columnaId.setCellValueFactory(new PropertyValueFactory<Almacen, Integer>("id"));
        columnaNombreSeccion.setCellValueFactory(new PropertyValueFactory<Almacen, String>("nombreSeccion"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<Almacen, String>("tipo"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Almacen, String>("descripcion"));
        almacenes = FXCollections.observableArrayList();
        //addButtonToTable();
        tablaAlmacenes.setItems(almacenes);
    }

    public void setAlmacenes() {
        almacenes.clear();
        ArrayList<SectorAlmacenModel> almacenesModels = (ArrayList) almacenesService.findAllSectorAlmacen();
        for (int i = 0; i < almacenesModels.size(); i++) {
            Almacen almacen = new Almacen();
            almacen.setId(i);
            almacen.setNombreSeccion(almacenesModels.get(i).getNombre());
            almacen.setTipo(almacenesModels.get(i).getTipo());
            almacen.setDescripcion(almacenesModels.get(i).getDescripcion());
            almacenes.add(almacen);
        }
    }


    @FXML
    void clickBuscar(MouseEvent event) throws IOException {
        String nombreAlmacen = textFieldNombres.getText();
        String tipo = comboBoxTipo.getValue();
        if (!textFieldNombres.equals("")) {
            ArrayList<SectorAlmacenModel> almacenesFiltrados = (ArrayList) almacenesService.filtrarAlmacenesTodos(nombreAlmacen, tipo);
            for (int i = 0; i < almacenesFiltrados.size(); i++) {
                Almacen almacen = new Almacen();
                almacen.setNombreSeccion(almacenesFiltrados.get(i).getNombre());
                almacen.setTipo(almacenesFiltrados.get(i).getTipo());
                almacen.setDescripcion(almacenesFiltrados.get(i).getDescripcion());
                almacenes.add(almacen);
            }
        }
    }

    @FXML
    void clickVisualizarAlmacen(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/almacenes/almacenVisualizar.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Parent root1 = fxmlLoader.load();
            fxmlLoader.<AlmacenVisualizarController>getController().iniciar();
            Stage stage = new Stage();
            stage.setTitle("Distribución del Almacén");
            stage.setScene(new Scene(root1));
            String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "visualizo la distribucion del almacen";
            LOGGERAUDIT.info(logMensaje);
            stage.show();
        } catch (Exception xd) {
            xd.printStackTrace();
        }
    }


//    @FXML
//    void clickNuevoCliente(MouseEvent event) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/clientes/nuevocliente.fxml"));
//        fxmlLoader.setControllerFactory(context::getBean);
//        AnchorPane pane = fxmlLoader.load();
//        paneClientes.getChildren().setAll(pane);
//    }

    private void addButtonToTable() {
        Callback<TableColumn<Almacen, Void>, TableCell<Almacen, Void>> cellFactory = new Callback<TableColumn<Almacen, Void>, TableCell<Almacen, Void>>() {
            @Override
            public TableCell<Almacen, Void> call(final TableColumn<Almacen, Void> param) {
                final TableCell<Almacen, Void> cell = new TableCell<Almacen, Void>() {
                    private final Button btnVer = new Button();

                    {
                        btnVer.setOnAction((event) -> {
                            Almacen data = getTableView().getItems().get(getIndex());
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/almaccenes/almacenesProducto.fxml"));
                            fxmlLoader.setControllerFactory(context::getBean);
                            try {
                                Pane pane = fxmlLoader.load();
                                fxmlLoader.<ProductosAlmacenController>getController().cargardata(1, data);
                                paneAlmacenesDistribucion.getChildren().setAll(pane);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Image image = new Image(getClass().getResourceAsStream("/fxml/imagenes/viewicon.png"));
                            ImageView iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnVer.setGraphic(iv1);

                            HBox pane = new HBox(btnVer);
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
        //tablaEmpleadoes.getColumns().add(columnaAcciones);
    }
}

