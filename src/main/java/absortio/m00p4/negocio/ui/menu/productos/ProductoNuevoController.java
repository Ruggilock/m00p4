package absortio.m00p4.negocio.ui.menu.productos;


import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.model.PrecioModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.service.CategoriaProductoService;
import absortio.m00p4.negocio.service.PrecioService;
import absortio.m00p4.negocio.service.ProductoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ProductoNuevoController implements Initializable {

    @FXML
    Pane paneNuevoProducto;

    @FXML
    TextField textFieldCodigoBarras;

    @FXML
    TextField textFieldNombre;

    @FXML
    ComboBox<String> comboBoxUnidadPeso;

    @FXML
    ComboBox<String> comboBoxUnidadVolumen;

    @FXML
    ComboBox<String> comboBoxUnidadVenta;
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
    Spinner<Double> spinnerPrecio;

    @FXML
    TextArea textAreaDescripcion;
    @Autowired
    ProductoService productoService;
    @Autowired
    CategoriaProductoService categoriaProductoService;
    @Autowired
    PrecioService precioService;

    ProductoModel productoModel;
    @Autowired
    private ApplicationContext context;

    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productoModel = new ProductoModel();
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

        ProductoModel productoModel = new ProductoModel();
        productoModel.setCodigoBarras(textFieldCodigoBarras.getText());
        productoModel.setNombre(textFieldNombre.getText());
        productoModel.setMarca(comboBoxMarca.getValue());
        productoModel.setModelo(comboBoxModelo.getValue());
        CategoriaProductoModel categoria = categoriaProductoService.obtenerCategoriaPorNombre(comboBoxCategoria.getValue());
        productoModel.setCategoriaProducto(categoria);
        productoModel.setUnidadPeso(comboBoxUnidadPeso.getValue());
        productoModel.setUnidadVolumen(comboBoxUnidadVolumen.getValue());
        productoModel.setPeso(spinnerPeso.getValue());
        productoModel.setVolumen(spinnerVolumen.getValue());
        productoModel.setDescripcion(textAreaDescripcion.getText());
        productoModel.setUnidadVenta(comboBoxUnidadVenta.getValue());
        //DateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        productoService.save(productoModel);
        productoModel = productoService.obtenerProducto(productoModel.getCodigoBarras());

        PrecioModel precio = new PrecioModel(spinnerPrecio.getValue(), date, "activo", productoModel);
        precioService.save(precio);
        productoModel.addPrecio(precio);
        obtenerPaneParent();
    }

    private boolean camposValidos() {
        if (comboBoxMarca.getValue().equals("-Seleccione-") || comboBoxModelo.getValue().equals("-Seleccione-")
                || textAreaDescripcion.getText().equals("") || textFieldCodigoBarras.getText().equals("")
                || textFieldNombre.getText().equals("") || comboBoxUnidadPeso.getValue().equals("-Seleccione-")
                || comboBoxUnidadVenta.getValue().equals("-Seleccione-") || comboBoxUnidadVolumen.getValue().equals("-Seleccione-")) {
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
        Pane paneParent = (Pane) paneNuevoProducto.getParent();
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
        comboBoxCategoria.setEditable(false);
        comboBoxCategoria.setValue("-Seleccione-");
        TextFields.bindAutoCompletion(comboBoxCategoria.getEditor(), comboBoxCategoria.getItems());

        ArrayList<String> modelosAux = new ArrayList<>();
        modelosAux.add("-Seleccione-");
        modelosAux.addAll(productoService.findAllMarca());
        ObservableList<String> marcas = FXCollections.observableArrayList(modelosAux);
        comboBoxMarca.getItems().addAll(marcas);
        comboBoxMarca.setEditable(true);
        comboBoxMarca.setValue(marcas.get(0));
        TextFields.bindAutoCompletion(comboBoxMarca.getEditor(), comboBoxMarca.getItems());

        modelosAux.clear();
        modelosAux.add("-Seleccione-");
        modelosAux.addAll(productoService.findAllModelo());
        ObservableList<String> modelos = FXCollections.observableArrayList(modelosAux);
        comboBoxModelo.getItems().addAll(modelos);
        comboBoxModelo.setEditable(true);
        comboBoxModelo.setValue(modelos.get(0));
        TextFields.bindAutoCompletion(comboBoxModelo.getEditor(), comboBoxModelo.getItems());

        ObservableList<String> unidadesPeso = FXCollections.observableArrayList("-Seleccione-", "kg");
        comboBoxUnidadPeso.getItems().addAll(unidadesPeso);
        comboBoxUnidadPeso.setEditable(false);
        comboBoxUnidadPeso.setValue(unidadesPeso.get(0));
        TextFields.bindAutoCompletion(comboBoxUnidadPeso.getEditor(), comboBoxUnidadPeso.getItems());

        ObservableList<String> unidadesVolumen = FXCollections.observableArrayList("-Seleccione-", "m3");
        comboBoxUnidadVolumen.getItems().addAll(unidadesVolumen);
        comboBoxUnidadVolumen.setEditable(false);
        comboBoxUnidadVolumen.setValue(unidadesVolumen.get(0));
        TextFields.bindAutoCompletion(comboBoxUnidadVolumen.getEditor(), comboBoxUnidadVolumen.getItems());

        ObservableList<String> unidadesVenta = FXCollections.observableArrayList("-Seleccione-", "unidad");
        comboBoxUnidadVenta.getItems().addAll(unidadesVenta);
        comboBoxUnidadVenta.setEditable(false);
        comboBoxUnidadVenta.setValue(unidadesVenta.get(0));
        TextFields.bindAutoCompletion(comboBoxUnidadVenta.getEditor(), comboBoxUnidadVenta.getItems());
    }
}
