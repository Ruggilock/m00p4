package absortio.m00p4.negocio.ui.menu.productos;

import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.model.PrecioModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import absortio.m00p4.negocio.service.CategoriaProductoService;
import absortio.m00p4.negocio.service.PrecioService;
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
public class ProductoEditarController implements Initializable {

    @FXML
    Pane paneEditarProducto;

    @FXML
    TextField textFieldCodigoBarras;

    @FXML
    TextField textFieldNombre;

    @FXML
    TextField textFieldUnidadPeso;

    @FXML
    TextField textFieldUnidadVolumen;

    @FXML
    TextField textFieldUnidadVenta;

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

    @FXML
    TextArea textAreaDescripcion;

    @FXML
    Spinner<Double> spinnerPrecio;

    ProductoModel productoModel;

    @Autowired
    private ApplicationContext context;

    @Autowired
    ProductoService productoService;

    @Autowired
    PrecioService precioService;

    @Autowired
    CategoriaProductoService categoriaProductoService;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @FXML
    public void cargarData(Producto productoseleccionado) {
        productoModel = productoService.obtenerProducto(productoseleccionado.getCodigo());
        textAreaDescripcion.setText(productoModel.getDescripcion());
        textFieldNombre.setText(productoModel.getNombre());
        textFieldCodigoBarras.setText(productoModel.getCodigoBarras());
        textFieldUnidadPeso.setText(productoModel.getUnidadPeso());
        textFieldUnidadVolumen.setText(productoModel.getUnidadVolumen());
        textFieldUnidadVenta.setText(productoModel.getUnidadVenta());
        comboBoxMarca.setValue(productoModel.getMarca());
        comboBoxModelo.setValue(productoModel.getModelo());
        comboBoxCategoria.setValue(productoModel.getCategoriaProducto().getNombre());
        spinnerPeso.getValueFactory().setValue(productoModel.getPeso().doubleValue());
        spinnerVolumen.getValueFactory().setValue(productoModel.getVolumen().doubleValue());
        spinnerPrecio.getValueFactory().setValue(productoService.getPrecio(productoModel.getCodigoBarras()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarComboBox();
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 0.5);
        SpinnerValueFactory<Double> valueFactoryVolumen =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 0.5);
        SpinnerValueFactory<Double> valueFactoryPrecio =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 0.5);
        spinnerPeso.setEditable(true);
        spinnerVolumen.setEditable(true);
        spinnerPrecio.setEditable(true);
        spinnerPeso.setValueFactory(valueFactory);
        spinnerVolumen.setValueFactory(valueFactoryVolumen);
        spinnerPrecio.setValueFactory(valueFactoryPrecio);
    }

    @FXML
    void cancelarBoton(MouseEvent event) throws IOException {
        obtenerPaneParent();
    }

    @FXML
    void aceptarBoton(MouseEvent event) throws IOException {
        if (!camposValidos()) return;
        productoModel.setCodigoBarras(textFieldCodigoBarras.getText());
        productoModel.setNombre(textFieldNombre.getText());
        productoModel.setMarca(comboBoxMarca.getValue());
        productoModel.setModelo(comboBoxModelo.getValue());
        CategoriaProductoModel categoria = categoriaProductoService.obtenerCategoriaPorNombre(comboBoxCategoria.getValue());
        productoModel.setCategoriaProducto(categoria);
        productoModel.setUnidadPeso(textFieldUnidadPeso.getText());
        productoModel.setUnidadVolumen(textFieldUnidadVolumen.getText());
        productoModel.setPeso(spinnerPeso.getValue());
        productoModel.setVolumen(spinnerVolumen.getValue());
        productoModel.setDescripcion(textAreaDescripcion.getText());
        productoModel.setUnidadVenta(textFieldUnidadVenta.getText());
        //DateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");

        PrecioModel precioActual = productoService.actualizarPrecio(productoModel.getCodigoBarras(), spinnerPrecio.getValue());
        productoModel.addPrecio(precioActual);
        productoService.save(productoModel);
        obtenerPaneParent();

    }

    //mirror en ProductoNuevoController.java
    private boolean camposValidos() {
        if (comboBoxMarca.getValue().equals("-Seleccione-") || comboBoxModelo.getValue().equals("-Seleccione-")
                || textAreaDescripcion.getText().equals("") || textFieldCodigoBarras.getText().equals("")
                || textFieldNombre.getText().equals("") || textFieldUnidadPeso.getText().equals("")
                || textFieldUnidadVenta.getText().equals("") || textFieldUnidadVolumen.getText().equals("")) {
            Validador.mostrarDialogError("¡Deben llenarse todos los campos!");
            return false;
        }
        CategoriaProductoModel categoria = categoriaProductoService.obtenerCategoriaPorNombre(comboBoxCategoria.getValue());
        if (categoria == null) {
            Validador.mostrarDialogError("¡La categoría ingresada no es válida!");
            return false;
        }

        return true;
    }

    public void obtenerPaneParent() throws IOException {
        Pane paneParent = (Pane) paneEditarProducto.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/productos/productos.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    public void cargarComboBox() {
        ArrayList<String> categorias = new ArrayList<>();
        ArrayList<CategoriaProductoModel> categoriasModels = (ArrayList<CategoriaProductoModel>) categoriaProductoService.findAllCategoriaProducto();
        for (int i = 0; i < categoriasModels.size(); i++) {
            categorias.add(categoriasModels.get(i).getNombre());
        }
        ObservableList<String> observableListCategorias = FXCollections.observableArrayList(categorias);
        comboBoxCategoria.getItems().addAll(observableListCategorias);
        comboBoxCategoria.setEditable(true);
        TextFields.bindAutoCompletion(comboBoxCategoria.getEditor(), comboBoxCategoria.getItems());


        ObservableList<String> marcas = FXCollections.observableArrayList(productoService.findAllMarca());
        comboBoxMarca.getItems().addAll(marcas);
        comboBoxMarca.setValue(marcas.get(0));
        comboBoxMarca.setEditable(true);
        TextFields.bindAutoCompletion(comboBoxMarca.getEditor(), comboBoxMarca.getItems());

        ObservableList<String> modelos = FXCollections.observableArrayList(productoService.findAllModelo());
        comboBoxModelo.getItems().addAll(modelos);
        comboBoxModelo.setValue(modelos.get(0));
        comboBoxModelo.setEditable(true);
        TextFields.bindAutoCompletion(comboBoxModelo.getEditor(), comboBoxModelo.getItems());
    }
}
