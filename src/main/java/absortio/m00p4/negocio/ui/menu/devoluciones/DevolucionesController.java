package absortio.m00p4.negocio.ui.menu.devoluciones;

import absortio.m00p4.negocio.model.DetalleDevolucionModel;
import absortio.m00p4.negocio.model.DevolucionModel;
import absortio.m00p4.negocio.model.auxiliares.Devolucion;
import absortio.m00p4.negocio.reportes.ReporteDevolucion;
import absortio.m00p4.negocio.service.DevolucionService;
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
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class DevolucionesController implements Initializable {

    @FXML
    private Pane paneDevoluciones;

    @FXML
    private Button buttonBuscar;

    @FXML
    private TableColumn columnN;

    @FXML
    private TableColumn columnNoDocDevolucion;

    @FXML
    private TableColumn columnNoDocVenta;

    @FXML
    private TableColumn columnFecha;

    @FXML
    private TableColumn columnDescripcion;

    @FXML
    private TableColumn columnAcciones;

    @FXML
    private Button buttonNuevo;

    @FXML
    private Button buttonExportar;

    @FXML
    private TextField textFieldNoDocDev;

    @FXML
    private DatePicker datePickerFecha;

    @FXML
    private TextField textFieldNoDocVenta;

    ObservableList<Devolucion> devoluciones;

    @FXML
    private TableView<Devolucion> tablaDevoluciones;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private DevolucionService devolucionService;


    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarTabla();
        cargarGrilla();
    }

    private void cargarGrilla() {
        ArrayList<DevolucionModel> rolModels =(ArrayList)devolucionService.findAllDevolucion();

        for (int i = 0; i < rolModels.size(); i++) {
            Devolucion rol = new Devolucion();

            rol.setDescripcion(rolModels.get(i).getDescripcion());
            rol.setFecha(rolModels.get(i).getFechaEmision());
            rol.setNoDocDevolucion(rolModels.get(i).getNodevolucion());
            rol.setNoDocVenta(rolModels.get(i).getDocumento().getNoDocumento());
            rol.setN(i);
            devoluciones.add(rol);
        }
    }

    private void inicializarTabla() {
        columnN.setCellValueFactory(new PropertyValueFactory<Devolucion, Integer>("n"));
        columnNoDocDevolucion.setCellValueFactory(new PropertyValueFactory<Devolucion,String>("noDocDevolucion"));
        columnNoDocVenta.setCellValueFactory(new PropertyValueFactory<Devolucion,String>("noDocVenta"));
        columnFecha.setCellValueFactory(new PropertyValueFactory<Devolucion,String>("fecha"));
        columnDescripcion.setCellValueFactory(new PropertyValueFactory<Devolucion,String>("descripcion"));
        columnAcciones.setCellValueFactory(new PropertyValueFactory<>("button"));
        devoluciones = FXCollections.observableArrayList();
        //tablaRoles.getColumns().add(columnaAcciones);
        addButtonToTable();
        tablaDevoluciones.setItems(devoluciones);
    }


    private void addButtonToTable() {
        Callback<TableColumn<Devolucion, Void>, TableCell<Devolucion, Void>> cellFactory = new Callback<TableColumn<Devolucion, Void>, TableCell<Devolucion, Void>>() {
            @Override
            public TableCell<Devolucion, Void> call(final TableColumn<Devolucion, Void> param) {
                final TableCell<Devolucion, Void> cell = new TableCell<Devolucion, Void>() {
                    private final Button btnVer = new Button();
                    {
                        btnVer.setOnAction( (event) -> {
                            Devolucion data = getTableView().getItems().get(getIndex());
                            generarNotaCredito(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {

                            Image image = new Image(getClass().getResourceAsStream("/fxml/imagenes/viewicon.png"));
                            ImageView iv1=new ImageView(image);
                            //iv1=new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnVer.setGraphic(iv1);
                            HBox pane = new HBox ( btnVer);
                            pane.setSpacing ( 5 );
                            setGraphic ( pane );
                            setAlignment ( Pos.CENTER );
                        }
                    }
                };
                return cell;
            }
        };
        columnAcciones.setCellFactory(cellFactory);
    }



    public void clickBuscar(MouseEvent mouseEvent) {
    }

    public void clickNuevo(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/devoluciones/nuevodevoluciones.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneDevoluciones.getChildren().setAll(pane);
    }

    public void exportarExcel(MouseEvent mouseEvent) {
    }

    void generarNotaCredito(Devolucion data){
        DevolucionModel devolucion=devolucionService.getByNodevolucion(data.getNoDocDevolucion());
        List<DetalleDevolucionModel> detalles=devolucion.getDetallesdev();
        //hacer el pdf
//        DevolucionModel documentoModel = devolucionService.getByNodevolucion(devolucion.getNodevolucion());
//        List<DetalleDocumentoModel> detalles = detalleDocumentoService.findAllDetalleDocumentobyDocumento_Id(documentoModel);
        ReporteDevolucion reporteDevolucion = new ReporteDevolucion();
        reporteDevolucion.pdfdevolucion(devolucion, detalles);

    }




}
