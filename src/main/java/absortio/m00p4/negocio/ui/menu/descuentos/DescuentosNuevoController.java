package absortio.m00p4.negocio.ui.menu.descuentos;

import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.service.CategoriaProductoService;
import absortio.m00p4.negocio.service.ProductoService;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

import absortio.m00p4.negocio.model.CondicionesComercialesModel;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import absortio.m00p4.negocio.service.CondicionesComercialesService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class DescuentosNuevoController implements Initializable {

    @Autowired
    private ApplicationContext context;


    @FXML
    Pane paneNuevoDescuento;

    @FXML
    ComboBox<String> comboBoxTipo;

    @FXML
    TextField textFieldProducto1Nombre;


    @FXML
    TextField textFieldProducto2Nombre;
    @FXML
    TextField textFieldCantidad1;
    @FXML
    TextField textFieldCantidad2;
    @FXML
    TextField textFieldPorcentaje;
    @FXML
    TextField textFieldNombre;
    @FXML
    DatePicker dataPickerFechaIni;
    @FXML
    ComboBox comboBoxCategoria;

    @FXML
    DatePicker dataPickerFechaFin;

    @FXML
    private Label labelDescProducto1;

    @FXML
    private Label labelDescProducto2;

    @FXML
    private Button buttonProducto1;

    @FXML
    private Button buttonProducto2;

    @FXML
    private TextArea textAreaDesc;

    @FXML
    private Label labelPorcentaje;

    @FXML
    private Label labelFechaIni;

    @FXML
    private Label labelCantidad1;

    @FXML
    private Label labelCantidad2;

    @FXML
    private Label labelFechaFin;

    @FXML
    private Label labelCategoria;

    @FXML
    private Label labelNombreProd1;

    @FXML
    private Label labelNombreProd2;

    @Autowired
    CondicionesComercialesService condicionService;

    @Autowired
    ProductoService productoService;

    @Autowired
    CategoriaProductoService categoriaProductoService;

    Producto producto1, producto2;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        producto1 = new Producto();
        producto2 = new Producto();

        textFieldNombre.setEditable(false);
        textFieldProducto1Nombre.setEditable(false);
        textFieldProducto2Nombre.setEditable(false);
        textFieldCantidad1.setEditable(false);
        textFieldCantidad2.setEditable(false);
        comboBoxCategoria.setDisable(true);
        textFieldPorcentaje.setEditable(false);
        dataPickerFechaFin.setDisable(true);
        dataPickerFechaIni.setDisable(true);
        buttonProducto1.setDisable(true);
        buttonProducto2.setDisable(true);

        iniciarComboBoxTipo();
        iniciarComboBoxCategoria();
        TextFields.bindAutoCompletion(comboBoxTipo.getEditor(), comboBoxTipo.getItems());
    }

    private void iniciarComboBoxTipo() {
        ArrayList<String> tipos = new ArrayList<>();
        tipos.add("cantidad*cantidad");
        tipos.add("cantidad*gratis");
        tipos.add("porcentaje*producto");
        tipos.add("porcentaje*categoria");
        comboBoxTipo.getItems().addAll(FXCollections.observableArrayList(tipos));
        comboBoxTipo.setEditable(false);
    }

    private void iniciarComboBoxCategoria() {
        ArrayList<String> categorias = new ArrayList<>();
        ArrayList<CategoriaProductoModel> categoriasModels = (ArrayList<CategoriaProductoModel>) categoriaProductoService.findAllCategoriaProducto();
        for (int i = 0; i < categoriasModels.size(); i++) {
            categorias.add(categoriasModels.get(i).getNombre());
        }
        ObservableList<String> observableListCategorias = FXCollections.observableArrayList(categorias);
        comboBoxCategoria.getItems().addAll(observableListCategorias);
        comboBoxCategoria.setValue("-Seleccione-");
        comboBoxCategoria.setEditable(false);
        TextFields.bindAutoCompletion(comboBoxCategoria.getEditor(), comboBoxCategoria.getItems());
    }

    @FXML
    public void seleccionTipo() {
        if (comboBoxTipo.getValue().compareTo("cantidad*cantidad") == 0) {
            textAreaDesc.setText("Descuentos tipo 2x1, 3x2, etc.");

            buttonProducto1.setDisable(false);
            buttonProducto1.setVisible(true);
            textFieldProducto1Nombre.setVisible(true);
            labelNombreProd1.setVisible(true);

            textFieldCantidad1.setEditable(true);
            textFieldCantidad1.setVisible(true);
            labelCantidad1.setVisible(true);

            buttonProducto2.setDisable(true);
            buttonProducto2.setVisible(false);
            textFieldProducto2Nombre.setVisible(false);
            labelNombreProd2.setVisible(false);

            textFieldCantidad2.setEditable(true);
            textFieldCantidad2.setVisible(true);
            labelCantidad2.setVisible(true);

            comboBoxCategoria.setDisable(true);
            comboBoxCategoria.setVisible(false);
            labelCategoria.setVisible(false);

            textFieldPorcentaje.setEditable(false);
            textFieldPorcentaje.setVisible(false);
            labelPorcentaje.setVisible(false);

            //textFieldCantidad1.setStyle("-fx-border-color: red;");
            textFieldNombre.setEditable(true);
            textFieldNombre.setVisible(true);
            //textFieldNombre.setStyle("-fx-border-color: red;");
            dataPickerFechaIni.setDisable(false);
            dataPickerFechaIni.setVisible(true);
            //dataPickerFechaFin.setStyle("-fx-border-color: red;");
            dataPickerFechaFin.setDisable(false);
            dataPickerFechaFin.setVisible(true);
            //dataPickerFechaIni.setStyle("-fx-border-color: red;");

            limpiarCampos();
        } else if (comboBoxTipo.getValue().compareTo("cantidad*gratis") == 0) {
            textAreaDesc.setText("Por una cantidad determinada del producto 1 se regala cierta cantidad del producto 2.");

            buttonProducto1.setDisable(false);
            buttonProducto1.setVisible(true);
            textFieldProducto1Nombre.setVisible(true);
            labelNombreProd1.setVisible(true);

            textFieldCantidad1.setEditable(true);
            textFieldCantidad1.setVisible(true);
            labelCantidad1.setVisible(true);

            buttonProducto2.setDisable(false);
            buttonProducto2.setVisible(true);
            textFieldProducto2Nombre.setVisible(true);
            labelNombreProd2.setVisible(true);

            textFieldCantidad2.setEditable(true);
            textFieldCantidad2.setVisible(true);
            labelCantidad2.setVisible(true);

            comboBoxCategoria.setDisable(true);
            comboBoxCategoria.setVisible(false);
            labelCategoria.setVisible(false);

            textFieldPorcentaje.setEditable(false);
            textFieldPorcentaje.setVisible(false);
            labelPorcentaje.setVisible(false);

            textFieldNombre.setEditable(true);
            textFieldNombre.setVisible(true);
            dataPickerFechaIni.setDisable(false);
            dataPickerFechaIni.setVisible(true);
            dataPickerFechaFin.setDisable(false);
            dataPickerFechaFin.setVisible(true);

            limpiarCampos();
        } else if (comboBoxTipo.getValue().compareTo("porcentaje*producto") == 0) {
            textAreaDesc.setText("Descuento de porcentaje en productos determinados.");

            buttonProducto1.setDisable(false);
            buttonProducto1.setVisible(true);
            textFieldProducto1Nombre.setVisible(true);
            labelNombreProd1.setVisible(true);

            textFieldCantidad1.setEditable(false);
            textFieldCantidad1.setVisible(false);
            labelCantidad1.setVisible(false);

            buttonProducto2.setDisable(true);
            buttonProducto2.setVisible(false);
            textFieldProducto2Nombre.setVisible(false);
            labelNombreProd2.setVisible(false);

            textFieldCantidad2.setEditable(false);
            textFieldCantidad2.setVisible(false);
            labelCantidad2.setVisible(false);

            comboBoxCategoria.setDisable(true);
            comboBoxCategoria.setVisible(false);
            labelCategoria.setVisible(false);

            textFieldPorcentaje.setEditable(true);
            textFieldPorcentaje.setVisible(true);
            labelPorcentaje.setVisible(true);

            textFieldNombre.setEditable(true);
            textFieldNombre.setVisible(true);
            dataPickerFechaIni.setDisable(false);
            dataPickerFechaIni.setVisible(true);
            dataPickerFechaFin.setDisable(false);
            dataPickerFechaFin.setVisible(true);

            limpiarCampos();
        } else if (comboBoxTipo.getValue().compareTo("porcentaje*categoria") == 0) {
            textAreaDesc.setText("Descuentos de porcentajes en categorias de productos.");

            buttonProducto1.setDisable(true);
            buttonProducto1.setVisible(false);
            textFieldProducto1Nombre.setVisible(false);
            labelNombreProd1.setVisible(false);

            textFieldCantidad1.setEditable(false);
            textFieldCantidad1.setVisible(false);
            labelCantidad1.setVisible(false);

            buttonProducto2.setDisable(true);
            buttonProducto2.setVisible(false);
            textFieldProducto2Nombre.setVisible(false);
            labelNombreProd2.setVisible(false);

            textFieldCantidad2.setEditable(false);
            textFieldCantidad2.setVisible(false);
            labelCantidad2.setVisible(false);

            comboBoxCategoria.setDisable(false);
            comboBoxCategoria.setVisible(true);
            labelCategoria.setVisible(true);

            textFieldPorcentaje.setEditable(true);
            textFieldPorcentaje.setVisible(true);
            labelPorcentaje.setVisible(true);

            textFieldNombre.setEditable(true);
            textFieldNombre.setVisible(true);
            dataPickerFechaIni.setDisable(false);
            dataPickerFechaIni.setVisible(true);
            dataPickerFechaFin.setDisable(false);
            dataPickerFechaFin.setVisible(true);

            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        //Limpiar Campos
        textFieldNombre.setText("");
        textFieldProducto1Nombre.setText("");
        textFieldProducto2Nombre.setText("");
        textFieldCantidad1.setText("");
        textFieldCantidad2.setText("");
        textFieldPorcentaje.setText("");
        dataPickerFechaIni.getEditor().clear();
        dataPickerFechaFin.getEditor().clear();
        comboBoxCategoria.getSelectionModel().selectFirst();
    }

    @FXML
    void clickAceptar(MouseEvent event) throws IOException {
        CondicionesComercialesModel condicionModel = new CondicionesComercialesModel();

        if (!camposValidos()) return;

        if (comboBoxTipo.getValue().compareTo("cantidad*cantidad") == 0) {
            condicionModel.setDescripcion("Descuentos tipo 2x1 , 3x2,etc");
            condicionModel.setTipo("cantidad*cantidad");
            condicionModel.setEstado("activo");
            condicionModel.setNombre(textFieldNombre.getText());
            //condicionModel.setCodigo1(Integer.parseInt(textFieldProducto1Codigo.getText()));
            condicionModel.setCodigo1(productoService.hallarIdByCodigoBarras(producto1.getCodigo()));
            condicionModel.setCantidad1(Integer.parseInt(textFieldCantidad1.getText()));
            condicionModel.setCantidad2(Integer.parseInt(textFieldCantidad2.getText()));

            condicionModel.setFechaIni(Date.valueOf(dataPickerFechaIni.getValue()));
            condicionModel.setFechaFin(Date.valueOf(dataPickerFechaFin.getValue()));

            condicionService.save(condicionModel);

        } else if (comboBoxTipo.getValue().compareTo("cantidad*gratis") == 0) {
            condicionModel.setDescripcion("Por una cantidad determinada del producto 1 se regala cierta cantidad del producto2");
            condicionModel.setTipo("cantidad*gratis");
            condicionModel.setEstado("activo");
            condicionModel.setNombre(textFieldNombre.getText());
            condicionModel.setCodigo1(productoService.hallarIdByCodigoBarras(producto1.getCodigo()));
            condicionModel.setCodigo2(productoService.hallarIdByCodigoBarras(producto2.getCodigo()));
            condicionModel.setCantidad1(Integer.parseInt(textFieldCantidad1.getText()));
            condicionModel.setCantidad2(Integer.parseInt(textFieldCantidad2.getText()));

            condicionModel.setFechaIni(Date.valueOf(dataPickerFechaIni.getValue()));
            condicionModel.setFechaFin(Date.valueOf(dataPickerFechaFin.getValue()));

            condicionService.save(condicionModel);

        } else if (comboBoxTipo.getValue().compareTo("porcentaje*producto") == 0) {
            condicionModel.setDescripcion("Descuento de porcentaje en productos determinados");
            condicionModel.setTipo("porcentaje*producto");
            condicionModel.setEstado("activo");
            condicionModel.setNombre(textFieldNombre.getText());
            condicionModel.setCodigo1(productoService.hallarIdByCodigoBarras(producto1.getCodigo()));
            condicionModel.setPorcentaje(Double.parseDouble(textFieldPorcentaje.getText()));

            condicionModel.setFechaIni(Date.valueOf(dataPickerFechaIni.getValue()));
            condicionModel.setFechaFin(Date.valueOf(dataPickerFechaFin.getValue()));

            condicionService.save(condicionModel);
        } else if (comboBoxTipo.getValue().compareTo("porcentaje*categoria") == 0) {
            condicionModel.setDescripcion("Descuentos de porcentajes en categorias de productos");
            condicionModel.setTipo("porcentaje*categoria");
            condicionModel.setEstado("activo");
            condicionModel.setNombre(textFieldNombre.getText());
            condicionModel.setCategoria(comboBoxCategoria.getValue().toString());
            condicionModel.setPorcentaje(Double.parseDouble(textFieldPorcentaje.getText()));

            condicionModel.setFechaIni(Date.valueOf(dataPickerFechaIni.getValue()));
            condicionModel.setFechaFin(Date.valueOf(dataPickerFechaFin.getValue()));

            condicionService.save(condicionModel);
        }
        LOGGERAUDIT.info("El usuario " + usuarioModel.getNombres() + " creo el descuento "+condicionModel.getNombre());
        Pane paneParent = (Pane) paneNuevoDescuento.getParent();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuento.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    boolean camposValidos() {
        try {
            comboBoxTipo.getValue().compareTo("cantidad*cantidad");
        } catch (NullPointerException npe) {
            Validador.mostrarDialogError("¡Debe seleccionar el tipo de descuento!");
            return false;
        }

        if (textFieldNombre.getText().equals("") || dataPickerFechaIni.getValue() == null || dataPickerFechaFin.getValue() == null) {
            Validador.mostrarDialogError("¡Debe llenar todos los campos!");
            return false;
        }
        if (comboBoxTipo.getValue().compareTo("cantidad*cantidad") == 0) {
            if (textFieldProducto1Nombre.getText().equals("") || textFieldCantidad1.getText().equals("")
                    || textFieldCantidad2.getText().equals("")) {
                Validador.mostrarDialogError("¡Debe llenar todos los campos!");
                return false;
            }
        } else if (comboBoxTipo.getValue().compareTo("cantidad*gratis") == 0) {
            if (textFieldProducto1Nombre.getText().equals("") || textFieldCantidad1.getText().equals("")
                    || textFieldProducto2Nombre.getText().equals("") || textFieldCantidad2.getText().equals("")) {
                Validador.mostrarDialogError("¡Debe llenar todos los campos!");
                return false;
            }
        } else if (comboBoxTipo.getValue().compareTo("porcentaje*producto") == 0) {
            if (textFieldProducto1Nombre.getText().equals("") || textFieldPorcentaje.getText().equals("")) {
                Validador.mostrarDialogError("¡Debe llenar todos los campos!");
                return false;
            }
        } else if (comboBoxTipo.getValue().compareTo("porcentaje*categoria") == 0) {
            if (textFieldPorcentaje.getText().equals("")) {
                Validador.mostrarDialogError("¡Debe llenar todos los campos!");
                return false;
            }
        }


        return true;
    }

    @FXML
    void clickCancelar(MouseEvent event) throws IOException {
        Pane paneParent = (Pane) paneNuevoDescuento.getParent();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuento.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    void clickProducto1(MouseEvent event) throws IOException {

        /*FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "/fxml/menu/descuentos/productosSeleccionar.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        Pane pane = fxmlLoader.load ();
        paneNuevoDescuento.getChildren ().setAll ( pane );*/

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/productosSeleccionar.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root1 = fxmlLoader.load();
        fxmlLoader.<ProductoSeleccionarController>getController().asignar(producto1, textFieldProducto1Nombre, labelDescProducto1);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar producto");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void clickProducto2(MouseEvent event) throws IOException {
        //textFieldProducto1Codigo.setText(producto.getNombre());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/productosSeleccionar.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root1 = fxmlLoader.load();
        fxmlLoader.<ProductoSeleccionarController>getController().asignar(producto2, textFieldProducto2Nombre, labelDescProducto2);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar producto");
        stage.setScene(new Scene(root1));
        stage.show();
    }

}
