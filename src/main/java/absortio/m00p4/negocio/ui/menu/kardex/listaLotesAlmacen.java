package absortio.m00p4.negocio.ui.menu.kardex;

import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.model.LoteModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.auxiliares.LotesMovimiento;
import absortio.m00p4.negocio.model.auxiliares.ProductoIngresoNuevo;
import absortio.m00p4.negocio.service.KardexService;
import absortio.m00p4.negocio.service.CategoriaProductoService;
import absortio.m00p4.negocio.service.LoteService;
import absortio.m00p4.negocio.service.ProductoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class listaLotesAlmacen implements Initializable{

    @FXML
    private Pane paneProductos;

    @FXML
    private Pane paneProductoSeleccionar;

    @FXML
    private TableView<LotesMovimiento> tablaProductos;

    @FXML
    private TableColumn<LotesMovimiento,Integer> columnaN;

    @FXML
    private TableColumn<LotesMovimiento,Integer> columnaCodigo;

    @FXML
    private TableColumn<LotesMovimiento,String> columnaNombre;

    @FXML
    private TableColumn<LotesMovimiento,String> columnaFechaAdquisicion;

    @FXML
    private TableColumn<LotesMovimiento,String> columnaFechaVencimiento;

    @FXML
    private TableColumn<LotesMovimiento,Button> columnaAcciones;

    @FXML
    private TextField textFieldCodigoBarras;

    @FXML
    private TextField textFieldNombre;

    @FXML
    private ComboBox<?> comboBoxMarca;

    @FXML
    private ComboBox<?> comboBoxCategoria;

    @FXML
    private ComboBox<?> comboBoxModelo;

    @FXML
    private ComboBox<?> comboBoxSectorAlmacen;

    @FXML
    private ComboBox comboBoxTipoMovimiento;

    private LoteModel loteAsociado;


    @Autowired
    private LoteService loteService;

    @Autowired
    ProductoService productoService;

    @Autowired
    KardexService kardexService;

    @Autowired
    CategoriaProductoService categoriaProductoService;

    @FXML
    TextField textFieldOtraPantalla;
    @FXML
    TextField textFieldOtraPantallaCodigoBarras;
    @FXML
    TextField textFieldCodigoLote;
    ObservableList<LoteModel> olLote;
    ObservableList<LotesMovimiento> lote;
    ObservableList<ComboBox> olComboAlmacen;

    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        olLote = FXCollections.observableArrayList();
        olComboAlmacen = FXCollections.observableArrayList();
        olComboAlmacen.add(comboBoxSectorAlmacen);

        inicializarTablaProductos();
        setProductos();
    }



    @FXML
    void buscar(MouseEvent event) {


    }
    private void inicializarTablaProductos() {

        columnaN.setCellValueFactory(new PropertyValueFactory<LotesMovimiento, Integer>("n"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<LotesMovimiento, Integer>("codigo"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<LotesMovimiento, String>("nombre"));
        columnaFechaAdquisicion.setCellValueFactory(new PropertyValueFactory<LotesMovimiento, String>("fechaAdquisicion"));
        columnaFechaVencimiento.setCellValueFactory(new PropertyValueFactory<LotesMovimiento, String>("fechaVencimiento"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<LotesMovimiento, Button>("acciones"));

        lote= FXCollections.observableArrayList();
        tablaProductos.setItems(lote);
    }
    private void setProductos() {
        ArrayList<LoteModel> arrayLote = (ArrayList<LoteModel>) loteService.findAllLote();

        for (int i = 0; i < arrayLote.size(); i++) {
            LotesMovimiento lotesMovimientoNuevo= new LotesMovimiento();
            lotesMovimientoNuevo.setCodigo(arrayLote.get(i).getId());
            lotesMovimientoNuevo.setN(i + 1);
            lotesMovimientoNuevo.setCodigo(arrayLote.get(i).getId());
            productoService.getById(arrayLote.get(i).getId());
            //ProductoModel productoModel = productoService.getById(arrayLote.get(i).getProducto().getId());
            ProductoModel productoModel = arrayLote.get(i).getProducto();
            lotesMovimientoNuevo.setNombre(productoModel.getNombre());
            lotesMovimientoNuevo.setFechaAdquisicion(arrayLote.get(i).getFechaAdquisicion());
            lotesMovimientoNuevo.setFechaVencimiento(arrayLote.get(i).getFechaVencimiento());
            lotesMovimientoNuevo.setAcciones(new Button());
            lotesMovimientoNuevo.getAcciones().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                   LoteModel loteModel= loteService.getById(lotesMovimientoNuevo.getCodigo());
                    olLote.add(loteModel);
                    textFieldOtraPantalla.setText(productoModel.getNombre());
                    textFieldOtraPantallaCodigoBarras.setText(productoModel.getCodigoBarras());
                    textFieldCodigoLote.setText(loteModel.getId()+"");
                    cargarcomboalmacen(loteModel.getId());
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                }
            });
            lote.add(lotesMovimientoNuevo);
        }

    }
    public void recibirDato(ObservableList<LoteModel > olProductoIngresoNuevo,TextField textFieldnombre,TextField textFieldCodigoBarras,TextField textFieldCodigoLote, ComboBox comboBoxSectorAlmacen, ComboBox comboBoxTipoMovimiento){
        this.textFieldOtraPantalla = textFieldnombre;
        this.olLote=olProductoIngresoNuevo;
        this.textFieldOtraPantallaCodigoBarras = textFieldCodigoBarras;
        this.textFieldCodigoLote =textFieldCodigoLote    ;
        this.comboBoxSectorAlmacen=comboBoxSectorAlmacen;
        //this.olComboAlmacen=olComboAlmacen;
        this.comboBoxTipoMovimiento=comboBoxTipoMovimiento;
        //this.loteAsociado=loteasociado;
    }

    void cargarcomboalmacen(Integer loteid) {

        //seteo combobox almacenes si se trata de una salida
        if (comboBoxTipoMovimiento.getValue().toString().equals("salida")) {
            //tengo todos los bloques en todos los almacenes
            //loteAsociado=loteService.findAllByProducto();
            List<String> bloquesdistinct = kardexService.findAllAlmacenesWithLote(loteid);
            ObservableList combox1 = FXCollections.observableList(bloquesdistinct);
            comboBoxSectorAlmacen.setItems(combox1);
        }

        //System.out.println(bloquesdistinct.size());
        //System.out.println(bloquesdistinct.get(0));

    }

}
