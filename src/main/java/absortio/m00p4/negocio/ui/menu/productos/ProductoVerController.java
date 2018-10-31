package absortio.m00p4.negocio.ui.menu.productos;

import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import absortio.m00p4.negocio.service.CategoriaProductoService;
import absortio.m00p4.negocio.service.ProductoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class ProductoVerController implements Initializable {
    @FXML
    Pane paneVerProducto;

    @FXML
    TextField textFieldCodigoBarras;

    @FXML
    TextField textFieldNombre;

    @FXML
    TextField textFieldUnidadPeso;

    @FXML
    TextField textFieldUnidadVolumen;

    @FXML
    ComboBox<String> comboBoxMarca;

    @FXML
    ComboBox<String> comboBoxCategoria;

    @FXML
    ComboBox<String> comboBoxModelo;

    @FXML
    Spinner<Double> spinnerPeso;

    @FXML
    Spinner<Double> spinnerVolumen;

    ProductoModel productoModel;

    @FXML
    TextArea textAreaDescripcion;
    @Autowired
    ProductoService productoService;
    @Autowired
    CategoriaProductoService categoriaProductoService;
    @Autowired
    private ApplicationContext context;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;


    @FXML
    public void cargarData(Producto productoseleccionado) {
        productoModel = productoService.obtenerProducto ( productoseleccionado.getCodigo () );
        textAreaDescripcion.setText ( productoModel.getDescripcion () );
        textFieldNombre.setText ( productoModel.getNombre () );
        textFieldCodigoBarras.setText ( productoModel.getCodigoBarras () );
        textFieldUnidadPeso.setText ( productoModel.getUnidadPeso () );
        textFieldUnidadVolumen.setText ( productoModel.getUnidadVolumen () );
        comboBoxMarca.setValue ( productoModel.getMarca () );
        comboBoxModelo.setValue ( productoModel.getModelo () );
        comboBoxCategoria.setValue ( productoModel.getCategoriaProducto ().getNombre () );
        spinnerPeso.getValueFactory().setValue (productoModel.getPeso () );
        spinnerVolumen.getValueFactory().setValue ( productoModel.getVolumen ());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarComboBox ();
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        SpinnerValueFactory<Double> valueFactoryVolumen =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerPeso.setValueFactory ( valueFactory );
        spinnerVolumen.setValueFactory ( valueFactoryVolumen );
    }

    @FXML
    void aceptarBoton(MouseEvent event) throws IOException {
        obtenerPaneParent ();
    }

    public void obtenerPaneParent() throws IOException {
        Pane paneParent = (Pane) paneVerProducto.getParent ();
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "/fxml/menu/productos/productos.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        Pane pane = fxmlLoader.load ();
        paneParent.getChildren ().setAll ( pane );
    }

    public void cargarComboBox() {
        ArrayList<String> categorias = new ArrayList<> ();
        ArrayList<CategoriaProductoModel> categoriasModels = (ArrayList<CategoriaProductoModel>) categoriaProductoService.findAllCategoriaProducto ();
        for (int i = 0; i < categoriasModels.size (); i++) {
            categorias.add ( categoriasModels.get ( i ).getNombre () );
        }
        ObservableList<String> observableListCategorias = FXCollections.observableArrayList ( categorias );
        comboBoxCategoria.getItems ().addAll ( observableListCategorias );
        comboBoxCategoria.setValue ( observableListCategorias.get ( 0 ) );
        comboBoxCategoria.setEditable ( true );
        TextFields.bindAutoCompletion ( comboBoxCategoria.getEditor (), comboBoxCategoria.getItems () );


        ObservableList<String> marcas = FXCollections.observableArrayList ( "-Seleccione-", "Marca2", "Marca3" );
        comboBoxMarca.getItems ().addAll ( marcas );
        comboBoxMarca.setValue ( marcas.get ( 0 ) );
        comboBoxMarca.setEditable ( true );
        TextFields.bindAutoCompletion ( comboBoxMarca.getEditor (), comboBoxMarca.getItems () );

        ObservableList<String> modelos = FXCollections.observableArrayList ( "-Seleccione-", "Modelo1", "Modelo2" );
        comboBoxModelo.getItems ().addAll ( modelos );
        comboBoxModelo.setValue ( modelos.get ( 0 ) );
        comboBoxModelo.setEditable ( true );
        TextFields.bindAutoCompletion ( comboBoxModelo.getEditor (), comboBoxModelo.getItems () );
    }


}
