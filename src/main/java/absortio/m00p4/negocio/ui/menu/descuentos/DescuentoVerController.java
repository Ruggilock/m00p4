package absortio.m00p4.negocio.ui.menu.descuentos;

import absortio.m00p4.negocio.model.CondicionesComercialesModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.FormateadorDecimal;
import absortio.m00p4.negocio.model.auxiliares.Descuento;
import absortio.m00p4.negocio.service.CondicionesComercialesService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class DescuentoVerController implements Initializable {
    @FXML
    Pane paneVerDescuento;

    @FXML
    TextField textFieldTipo;

    @FXML
    TextField textFieldCategoria;

    @FXML
    TextField textFieldDescripcion;

    @FXML
    TextField textFieldCodigo1;

    @FXML
    TextField textFieldCodigo2;

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
    private ApplicationContext context ;

    @Autowired
    CondicionesComercialesService condicionService;

    CondicionesComercialesModel condicionModel;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        condicionModel = new CondicionesComercialesModel();
    }

    @FXML
    void clickAceptar(MouseEvent event) throws IOException {
        Pane paneParent = (Pane) paneVerDescuento.getParent();

        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuento.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    void clickCancelar(MouseEvent event) throws IOException {
        Pane paneParent = (Pane) paneVerDescuento.getParent();

        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuento.fxml"));
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

        textFieldTipo.setText(condicionModel.getTipo());
        textFieldCantidad1.setText(String.valueOf(condicionModel.getCantidad1()));
        textFieldCantidad2.setText(String.valueOf(condicionModel.getCantidad2()));
        textFieldCodigo1.setText(String.valueOf(condicionModel.getCodigo1()));
        textFieldCodigo2.setText(String.valueOf(condicionModel.getCodigo2()));
        textFieldDescripcion.setText(condicionModel.getDescripcion());
        Double porcentaje = condicionModel.getPorcentaje();
        if (porcentaje != null)
            textFieldPorcentaje.setText(FormateadorDecimal.formatear(porcentaje));
        else
            textFieldPorcentaje.setText("");
        textFieldCategoria.setText(String.valueOf(condicionModel.getCategoria()));
        textFieldNombre.setText(condicionModel.getNombre());
        System.out.println(condicionModel.getNombre());

        dataPickerFechaIni.setValue(LOCAL_DATE(condicionModel.getFechaIni().toString()));
        dataPickerFechaFin.setValue(LOCAL_DATE(condicionModel.getFechaFin().toString()));
        LOGGERAUDIT.info("El usuario " + usuarioModel.getNombres() + " visualizo el descuento "+condicionModel.getNombre());

    }
}
