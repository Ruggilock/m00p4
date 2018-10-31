package absortio.m00p4.negocio.ui.menu.almacenes;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.auxiliares.Almacen;
import absortio.m00p4.negocio.model.auxiliares.ProductosAlmacen;
import absortio.m00p4.negocio.reportes.ReporteProductosAlmacen;
import absortio.m00p4.negocio.service.CategoriaProductoService;
import absortio.m00p4.negocio.service.KardexService;
import absortio.m00p4.negocio.service.RackService;
import absortio.m00p4.negocio.service.SectorAlmacenService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
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
public class ProductosAlmacenController implements Initializable {

    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    @Autowired
    KardexService proAlmacenService;
    @Autowired
    SectorAlmacenService sectorAlmacenService;
    @Autowired
    RackService rackService;
    @Autowired
    CategoriaProductoService categoriaProductoService;
    ObservableList<ProductosAlmacen> productosAlmacenes;
    Font fuenteLabelAlmacenes;
    @FXML
    private Label labelProdAlmacen;
    @FXML
    private AnchorPane paneAlmacenesProducto;
    @FXML
    private Button botonExportarExcel;
    @FXML
    private TableView<ProductosAlmacen> tablaProdAlmacen;
    @FXML
    private TableColumn columnaN;
    @FXML
    private TableColumn columnaLote;
    @FXML
    private TableColumn columnaBloque;
    @FXML
    private TableColumn columnaCodBarras;
    @FXML
    private TableColumn columnaProducto;
    @FXML
    private TableColumn columnaSeccionAlmacen;
    @FXML
    private TableColumn columnaRack;
    @FXML
    private TableColumn columnaMarca;
    @FXML
    private TableColumn columnaCategoria;
    @FXML
    private TableColumn columnaModelo;
    @FXML
    private TableColumn columnaStockFisico;
    @FXML
    private TextField textFieldLote;
    @FXML
    private Button botonBuscar;
    @FXML
    private TextField textFieldCodBarras;
    @FXML
    private TextField textFieldProducto;
    @FXML
    private ComboBox comboBoxMarca;
    @FXML
    private ComboBox comboBoxCategoria;
    @FXML
    private ComboBox comboBoxModelo;
    @FXML
    private ComboBox comboBoxSeccion;
    @FXML
    private ComboBox comboBoxRack;
    @Autowired
    private ApplicationContext context;

    public void cargardata(Integer i, Almacen a) {
    }

    ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fuenteLabelAlmacenes = new Font("Century Gothic", 49);
        labelProdAlmacen.fontProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> ov, Font b4, Font aftr) {
                if (aftr.equals(fuenteLabelAlmacenes)) return;
                labelProdAlmacen.setFont(fuenteLabelAlmacenes);
            }
        });

        this.inicializarTablaProductosAlmacenes();

        cargarComboBoxSeccionAlmacenYRack();
        cargarComboBoxCategoriaMarcaModelo();

        ArrayList<KardexModel> proAlmacenModel = (ArrayList) proAlmacenService.findAllOrdenados();

        this.cargarListaEnGrilla(proAlmacenModel);      // Leemos todos los transportistas para la grilla

    }

    void inicializarTablaProductosAlmacenes() {

        columnaN.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, Integer>("n"));
        columnaLote.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, String>("lote"));
        columnaBloque.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, String>("bloque"));
        columnaCodBarras.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, Integer>("codBarras"));
        columnaProducto.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, String>("producto"));
        columnaSeccionAlmacen.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, Integer>("secAlmacen"));
        columnaRack.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, String>("rack"));
        columnaCategoria.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, String>("categoria"));
        columnaMarca.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, String>("marca"));
        columnaModelo.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, String>("modelo"));
        columnaStockFisico.setCellValueFactory(new PropertyValueFactory<ProductosAlmacen, Integer>("stockFisico"));

        productosAlmacenes = FXCollections.observableArrayList();
        tablaProdAlmacen.setItems(productosAlmacenes);
    }


    public void cargarComboBoxSeccionAlmacenYRack() {
        List<SectorAlmacenModel> sector = sectorAlmacenService.findAllSectorAlmacen();
        List<String> sectores = new ArrayList<>();
        sectores.add("-Seleccione-");
        for (int i = 0; i < sector.size(); i++) {
            sectores.add(sector.get(i).getNombre());
        }
        ObservableList combox3 = FXCollections.observableList(sectores);
        comboBoxSeccion.setItems(combox3);
        comboBoxSeccion.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
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

        comboBoxSeccion.setValue("-Seleccione-");
        comboBoxRack.setValue("-Seleccione-");
    }


    public void cargarComboBoxCategoriaMarcaModelo() {
        ArrayList<String> categorias = new ArrayList<>();
        ArrayList<CategoriaProductoModel> categoriasModels = (ArrayList<CategoriaProductoModel>) categoriaProductoService.findAllCategoriaProducto();
        for (int i = 0; i < categoriasModels.size(); i++) {
            categorias.add(categoriasModels.get(i).getNombre());
        }
        ObservableList<String> observableListCategorias = FXCollections.observableArrayList(categorias);
        comboBoxCategoria.getItems().add("-Seleccione-");
        comboBoxCategoria.getItems().addAll(observableListCategorias);
        comboBoxCategoria.setEditable(true);
        comboBoxCategoria.setValue(comboBoxCategoria.getItems().get(0));
        TextFields.bindAutoCompletion(comboBoxCategoria.getEditor(), comboBoxCategoria.getItems());


        ObservableList<String> marcas = FXCollections.observableArrayList("-Seleccione-", "Marca1", "Marca2", "Marca3");
        comboBoxMarca.getItems().addAll(marcas);
        comboBoxMarca.setEditable(true);
        comboBoxMarca.setValue(marcas.get(0));
        TextFields.bindAutoCompletion(comboBoxMarca.getEditor(), comboBoxMarca.getItems());

        ObservableList<String> modelos = FXCollections.observableArrayList("-Seleccione-", "Modelo1", "Modelo2");
        comboBoxModelo.getItems().addAll(modelos);
        comboBoxModelo.setEditable(true);
        comboBoxModelo.setValue(modelos.get(0));
        TextFields.bindAutoCompletion(comboBoxModelo.getEditor(), comboBoxModelo.getItems());
    }


    public void cargarListaEnGrilla(ArrayList<KardexModel> proAlmacenModels) {
        productosAlmacenes.clear();
        List<String> mylista = new ArrayList<>();
        String myid = "";

        if (!proAlmacenModels.isEmpty()) {
            for (int i = 0; i < proAlmacenModels.size(); i++) {
                // Insertamos el nuevo registro en la tabla con la mayor cantidad de informacion posible, pues luego la vamos a buscar por ella
                BloqueModel bloqueAux = new BloqueModel();
                bloqueAux = proAlmacenModels.get(i).getBloque();
                RackModel rackAux = new RackModel();
                rackAux = bloqueAux.getIdRack();
                SectorAlmacenModel sectorAux = new SectorAlmacenModel();
                sectorAux = rackAux.getIdSectorAlmacen();
                LoteModel loteAux = new LoteModel();
                loteAux = proAlmacenModels.get(i).getLote();
                ProductoModel productoAux = new ProductoModel();
                productoAux = loteAux.getProducto();

                myid = loteAux.getId().toString() + bloqueAux.getId().toString();
                if (!mylista.contains(myid)) {
                    ProductosAlmacen proAlmacen = new ProductosAlmacen();
                    proAlmacen.setN(mylista.size() + 1);
                    proAlmacen.setLote(proAlmacenModels.get(i).getLote().getObservacion());
                    proAlmacen.setBloque(bloqueAux.getIdentificador());
                    proAlmacen.setCodBarras(productoAux.getCodigoBarras());
                    proAlmacen.setProducto(productoAux.getNombre());
                    proAlmacen.setRack(rackAux.getIdentificador());
                    proAlmacen.setSecAlmacen(sectorAux.getNombre());
                    proAlmacen.setCategoria(productoAux.getCategoriaProducto().getNombre());
                    proAlmacen.setMarca(productoAux.getMarca());
                    proAlmacen.setModelo(productoAux.getModelo());

                    proAlmacen.setStockFisico(proAlmacenService.hallarStockByLoteYBloque(loteAux.getId(), bloqueAux.getId()));
                    productosAlmacenes.add(proAlmacen);
                    mylista.add(myid);
                }
            }
        }
    }


    public void clickExportarExcel(MouseEvent mouseEvent) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteProductosAlmacen reporteProAlmacen = new ReporteProductosAlmacen();
        reporteProAlmacen.generarReporte(proAlmacenService.findAllOrdenados());
        String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "generó reporte de  productos por almacén";
        LOGGERAUDIT.info(logMensaje);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }


    public void clickBuscar(MouseEvent mouseEvent) {

        String lote, codbarras, producto, categoria, marca, modelo, seccion, rack;

        lote = textFieldLote.getText();
        codbarras = textFieldCodBarras.getText();
        producto = textFieldProducto.getText();
        categoria = comboBoxCategoria.getValue().toString();
        marca = comboBoxMarca.getValue().toString();
        modelo = comboBoxModelo.getValue().toString();
        seccion = comboBoxSeccion.getValue().toString();
        rack = comboBoxRack.getValue().toString();

        ArrayList<KardexModel> proAlmacenModels = (ArrayList) proAlmacenService.findByTodosFiltros(lote, codbarras, producto, categoria, marca, modelo, seccion, rack);
        //ArrayList<KardexModel> proAlmacenModels = new ArrayList<>();
        cargarListaEnGrilla(proAlmacenModels);

        reiniciarValores();
    }

    public void reiniciarValores() {
        textFieldLote.setText("");
        textFieldCodBarras.setText("");
        textFieldProducto.setText("");
        comboBoxCategoria.setValue("-Seleccione-");
        comboBoxMarca.setValue("-Seleccione-");
        comboBoxModelo.setValue("-Seleccione-");
        comboBoxSeccion.setValue("-Seleccione-");
        comboBoxRack.setValue("-Seleccione-");
    }

}

