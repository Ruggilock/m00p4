package absortio.m00p4.negocio.ui.menu.kardex;

import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.auxiliares.ProductoIngresoNuevo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class listaProductosAlmacen implements Initializable{

    @FXML
    private Pane paneProductos;

    @FXML
    private Pane paneProductoSeleccionar;

    @FXML
    private TableView<ProductoIngresoNuevo> tablaProductos;

    @FXML
    private TableColumn<ProductoIngresoNuevo,Integer> columnaN;

    @FXML
    private TableColumn<ProductoIngresoNuevo,Integer> columnaCodigo;

    @FXML
    private TableColumn<ProductoIngresoNuevo,String> columnaNombre;

    @FXML
    private TableColumn<ProductoIngresoNuevo,String> columnaMarca;

    @FXML
    private TableColumn<ProductoIngresoNuevo,String> columnaModelo;

    @FXML
    private TableColumn<ProductoIngresoNuevo,Button> columnaAcciones;

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

    @Autowired
    private ProductoService productoService;

    @FXML
    TextField textFieldOtraPantalla;
    @FXML
    private TextField textFieldOtraPantallaCodigoBarras;
    ObservableList<ProductoModel> olProductos;
    ObservableList<ProductoIngresoNuevo> productos;

    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        olProductos = FXCollections.observableArrayList();

        inicializarTablaProductos();
        setProductos();
    }

    @FXML
    void buscar(MouseEvent event) {


    }
    private void inicializarTablaProductos() {

        columnaN.setCellValueFactory(new PropertyValueFactory<ProductoIngresoNuevo, Integer>("n"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<ProductoIngresoNuevo, Integer>("codigo"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<ProductoIngresoNuevo, String>("nombre"));
        columnaMarca.setCellValueFactory(new PropertyValueFactory<ProductoIngresoNuevo, String>("marca"));
        columnaModelo.setCellValueFactory(new PropertyValueFactory<ProductoIngresoNuevo, String>("modelo"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<ProductoIngresoNuevo, Button>("seleccionar"));

        productos= FXCollections.observableArrayList();
        tablaProductos.setItems(productos);
    }
    private void setProductos() {
        ArrayList<ProductoModel> arrayProductos = (ArrayList<ProductoModel>) productoService.findAllProducto();
        for (int i = 0; i < arrayProductos.size(); i++) {
            ProductoIngresoNuevo productoIngresoNuevo = new ProductoIngresoNuevo();
            productoIngresoNuevo.setId(arrayProductos.get(i).getId());
            productoIngresoNuevo.setN(i + 1);
          //  productoIngresoNuevo.setCodigo(arrayProductos.get(i).getCodigoBarras());
            productoIngresoNuevo.setNombre(arrayProductos.get(i).getNombre());
            productoIngresoNuevo.setMarca(arrayProductos.get(i).getMarca());
            productoIngresoNuevo.setModelo(arrayProductos.get(i).getModelo());
            productoIngresoNuevo.setSeleccionar(new Button());
            productoIngresoNuevo.getSeleccionar().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                   ProductoModel productoModel= productoService.getById(productoIngresoNuevo.getId());
                    olProductos.add(productoModel);
                    textFieldOtraPantalla.setText(productoModel.getCodigoBarras());
                    textFieldOtraPantallaCodigoBarras.setText(productoModel.getNombre());
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                }
            });
            productos.add(productoIngresoNuevo);
        }

    }
    public void recibirDato(ObservableList<ProductoModel > olProductoIngresoNuevo,TextField textFieldnombre,TextField textFieldCodigoBarras){
        this.textFieldOtraPantalla = textFieldnombre;
        this.olProductos=olProductoIngresoNuevo;
        this.textFieldOtraPantallaCodigoBarras = textFieldCodigoBarras;
    }
}
