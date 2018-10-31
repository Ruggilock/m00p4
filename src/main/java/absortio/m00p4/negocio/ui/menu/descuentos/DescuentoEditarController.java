package absortio.m00p4.negocio.ui.menu.descuentos;

import absortio.m00p4.negocio.model.CondicionesComercialesModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.FormateadorDecimal;
import absortio.m00p4.negocio.model.auxiliares.Descuento;
import absortio.m00p4.negocio.service.CondicionesComercialesService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.sql.Date;
import java.text.Format;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class DescuentoEditarController implements Initializable {

    @Autowired
    private ApplicationContext context ;

    @FXML
    Pane paneEditarDescuento;

    @FXML
    ComboBox<String> comboBoxTipo;

    @FXML
    TextField textFieldProducto1Codigo;
    @FXML
    Label labelDescripcion;
    @FXML
    TextField textFieldCategoria;
    @FXML
    TextField textFieldProducto2Codigo;
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
    DatePicker dataPickerFechaFin;

    @Autowired
    CondicionesComercialesService condicionService;

    CondicionesComercialesModel condicionModel;

    String colorHabilitar="-fx-background-color: red;";
    String colorDeshabilitar="-fx-background-color: greenyellow;";
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<String> tipos = new ArrayList<>();
        tipos.add("cantidad*cantidad");
        tipos.add("cantidad*gratis");
        tipos.add("porcentaje*producto");
        tipos.add("porcentaje*categoria");

        dataPickerFechaIni.setStyle("-fx-background-color: greenyellow;");
        dataPickerFechaFin.setStyle("-fx-background-color: greenyellow;");
        textFieldNombre.setStyle("-fx-background-color: greenyellow;");

        comboBoxTipo.getItems().addAll(FXCollections.observableArrayList(tipos));
        comboBoxTipo.setEditable(false);
        TextFields.bindAutoCompletion(comboBoxTipo.getEditor(), comboBoxTipo.getItems());
    }

    @FXML
    public void seleccionTipo() {
        if(comboBoxTipo.getValue().compareTo("cantidad*cantidad")==0){
            labelDescripcion.setText("Descuentos tipo 2x1 , 3x2,etc");
            textFieldPorcentaje.setEditable(false); textFieldPorcentaje.setStyle(colorDeshabilitar);
            textFieldCategoria.setEditable(false);  textFieldCategoria.setStyle(colorDeshabilitar);
            textFieldProducto2Codigo.setEditable(false); textFieldProducto2Codigo.setStyle(colorDeshabilitar);

            textFieldProducto1Codigo.setEditable(true); textFieldProducto1Codigo.setStyle(colorHabilitar);
            textFieldCantidad1.setEditable(true);       textFieldCantidad1.setStyle(colorHabilitar);
            textFieldCantidad2.setEditable(true); textFieldCantidad2.setStyle(colorHabilitar);

        }else if(comboBoxTipo.getValue().compareTo("cantidad*gratis")==0){
            labelDescripcion.setText("Por una cantidad determinada del producto 1 se regala cierta cantidad del producto2");
            textFieldPorcentaje.setEditable(false); textFieldPorcentaje.setStyle(colorDeshabilitar);
            textFieldCategoria.setEditable(false);  textFieldCategoria.setStyle(colorDeshabilitar);

            textFieldProducto1Codigo.setEditable(true); textFieldProducto1Codigo.setStyle(colorHabilitar);
            textFieldProducto2Codigo.setEditable(true); textFieldProducto2Codigo.setStyle(colorHabilitar);
            textFieldCantidad1.setEditable(true);  textFieldCantidad1.setStyle(colorHabilitar);
            textFieldCantidad2.setEditable(true);  textFieldCantidad2.setStyle(colorHabilitar);

        }else if(comboBoxTipo.getValue().compareTo("porcentaje*producto")==0){
            labelDescripcion.setText("Descuento de porcentaje en productos determinados");
            textFieldCategoria.setEditable(false);  textFieldCategoria.setStyle(colorDeshabilitar);
            textFieldProducto2Codigo.setEditable(false); textFieldProducto2Codigo.setStyle(colorDeshabilitar);
            textFieldCantidad2.setEditable(false); textFieldCantidad2.setStyle(colorDeshabilitar);
            textFieldCantidad1.setEditable(false); textFieldCantidad1.setStyle(colorDeshabilitar);

            textFieldProducto1Codigo.setEditable(true); textFieldProducto1Codigo.setStyle(colorHabilitar);
            textFieldPorcentaje.setEditable(true); textFieldPorcentaje.setStyle(colorHabilitar);

        }else if(comboBoxTipo.getValue().compareTo("porcentaje*categoria")==0){
            labelDescripcion.setText("Descuentos de porcentajes en categorias de productos");
            textFieldProducto2Codigo.setEditable(false); textFieldProducto2Codigo.setStyle(colorDeshabilitar);
            textFieldCantidad2.setEditable(false); textFieldCantidad2.setStyle(colorDeshabilitar);
            textFieldProducto1Codigo.setEditable(false); textFieldProducto1Codigo.setStyle(colorDeshabilitar);
            textFieldCantidad1.setEditable(false); textFieldCantidad1.setStyle(colorDeshabilitar);

            textFieldPorcentaje.setEditable(true); textFieldPorcentaje.setStyle(colorHabilitar);
            textFieldCategoria.setEditable(true); textFieldCategoria.setStyle(colorHabilitar);

        }
    }

    @FXML
    void clickCancelar(MouseEvent event) throws IOException {
        Pane paneParent = (Pane) paneEditarDescuento.getParent();

        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuento.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    void clickAceptar(MouseEvent event) throws IOException {
        //CondicionesComercialesModel condicionModel = new CondicionesComercialesModel();
        if(comboBoxTipo.getValue().compareTo("cantidad*cantidad")==0){
            condicionModel.setDescripcion("Descuentos tipo 2x1 , 3x2,etc");
            condicionModel.setTipo("cantidad*cantidad");
            condicionModel.setNombre(textFieldNombre.getText());
            condicionModel.setCodigo1(Integer.parseInt(textFieldProducto1Codigo.getText()));
            condicionModel.setCantidad1(Integer.parseInt(textFieldCantidad1.getText()));
            condicionModel.setCantidad2(Integer.parseInt(textFieldCantidad2.getText()));

            condicionModel.setFechaIni(Date.valueOf(dataPickerFechaIni.getValue()));
            condicionModel.setFechaFin(Date.valueOf(dataPickerFechaFin.getValue()));

            condicionService.save(condicionModel);

        }else if(comboBoxTipo.getValue().compareTo("cantidad*gratis")==0){
            condicionModel.setDescripcion("Por una cantidad determinada del producto 1 se regala cierta cantidad del producto2");
            condicionModel.setTipo("cantidad*gratis");
            condicionModel.setNombre(textFieldNombre.getText());
            condicionModel.setCodigo1(Integer.parseInt(textFieldProducto1Codigo.getText()));
            condicionModel.setCodigo2(Integer.parseInt(textFieldProducto2Codigo.getText()));
            condicionModel.setCantidad1(Integer.parseInt(textFieldCantidad1.getText()));
            condicionModel.setCantidad2(Integer.parseInt(textFieldCantidad2.getText()));

            condicionModel.setFechaIni(Date.valueOf(dataPickerFechaIni.getValue()));
            condicionModel.setFechaFin(Date.valueOf(dataPickerFechaFin.getValue()));

            condicionService.save(condicionModel);

        }else if(comboBoxTipo.getValue().compareTo("porcentaje*producto")==0){
            condicionModel.setDescripcion("Descuento de porcentaje en productos determinados");
            condicionModel.setTipo("porcentaje*producto");
            condicionModel.setNombre(textFieldNombre.getText());
            condicionModel.setCodigo1(Integer.parseInt(textFieldProducto1Codigo.getText()));
            condicionModel.setPorcentaje(Double.parseDouble(textFieldPorcentaje.getText()));

            condicionModel.setFechaIni(Date.valueOf(dataPickerFechaIni.getValue()));
            condicionModel.setFechaFin(Date.valueOf(dataPickerFechaFin.getValue()));

            condicionService.save(condicionModel);
        }else if(comboBoxTipo.getValue().compareTo("porcentaje*categoria")==0){
            condicionModel.setDescripcion("Descuentos de porcentajes en categorias de productos");
            condicionModel.setTipo("porcentaje*categoria");
            condicionModel.setNombre(textFieldNombre.getText());
            condicionModel.setCategoria(textFieldCategoria.getText());
            condicionModel.setPorcentaje(Double.parseDouble(textFieldPorcentaje.getText()));

            condicionModel.setFechaIni(Date.valueOf(dataPickerFechaIni.getValue()));
            condicionModel.setFechaFin(Date.valueOf(dataPickerFechaFin.getValue()));

            condicionService.save(condicionModel);
        }
        LOGGERAUDIT.info("El usuario " + usuarioModel.getNombres() + " modifico el descuento "+condicionModel.getNombre());

            Pane paneParent = (Pane) paneEditarDescuento.getParent();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuento.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }



    public static final LocalDate LOCAL_DATE (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    @FXML
    public void cargarData(Descuento descuentoSeleccionado){
        condicionModel = condicionService.getById(descuentoSeleccionado.getId());

        comboBoxTipo.setValue(descuentoSeleccionado.getTipo());
        textFieldCantidad1.setText(String.valueOf(descuentoSeleccionado.getCantidad1()));
        textFieldCantidad2.setText(String.valueOf(descuentoSeleccionado.getCantidad2()));
        textFieldCategoria.setText(String.valueOf(descuentoSeleccionado.getCategoria()));
        textFieldPorcentaje.setText(FormateadorDecimal.formatear(descuentoSeleccionado.getPorcentaje()));
        textFieldProducto1Codigo.setText(String.valueOf(descuentoSeleccionado.getCodigo1()));
        textFieldProducto2Codigo.setText(String.valueOf(descuentoSeleccionado.getCodigo2()));
        textFieldNombre.setText(descuentoSeleccionado.getNombre());

        dataPickerFechaIni.setValue(LOCAL_DATE(descuentoSeleccionado.getFechaIni().toString()));
        dataPickerFechaFin.setValue(LOCAL_DATE(descuentoSeleccionado.getFechaFin().toString()));

        seleccionTipo();
    }
}
