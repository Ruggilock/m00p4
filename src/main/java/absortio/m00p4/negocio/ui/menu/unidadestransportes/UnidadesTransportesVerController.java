package absortio.m00p4.negocio.ui.menu.unidadestransportes;

import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.UnidadTransporteModel;
import absortio.m00p4.negocio.model.auxiliares.UnidadTransporte;
import absortio.m00p4.negocio.service.TransportistaService;
import absortio.m00p4.negocio.service.UnidadTransporteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class UnidadesTransportesVerController implements Initializable {

    @FXML
    Pane paneVerUnidadTransporte;

    @FXML
    TextField textFieldPlaca;
    @FXML
    TextField textFieldMarca;
    @FXML
    TextField textFieldModelo;
    @FXML
    TextField textFieldVolumen;
    @FXML
    TextField textFieldPeso;
    @FXML
    TextField textFieldUnidadVolumen;
    @FXML
    TextField textFieldUnidadPeso;
    @FXML
    TextField textFieldTransportista;

    //@FXML
    //Button aceptar;


    @Autowired
    private ApplicationContext context;

    @Autowired
    UnidadTransporteService unidadTransporteService;
    @Autowired
    TransportistaService transportistaService;

    private TransportistaModel transportistaModel;
    private UnidadTransporteModel unidadTransporteModelUpdateSee;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;
// ---------------------------------------------------
    // Métodos y procedimientos




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    void clickButtonAceptar(MouseEvent event) throws IOException {       // Al hacer click en el boton Aceptar (se supone que ya todo fue seleccionado)

        // Volvemos a la pantalla anterior
        Pane paneParent = (Pane) paneVerUnidadTransporte.getParent();    // Con esto obtengo la pantalla base "menuform.fxml"
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));      // Leo la pantalla "transportes.fxml"
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);      // Cargo la pantalla "transportes.fxml" sobre la pantalla base "menuform.fxml"

    }

    void leerDato(UnidadTransporte data){      // Esto va a servir para extraer todos los datos del transportista en los casos de editar y ver (colocamos los campos en pantalla)
        unidadTransporteModelUpdateSee = unidadTransporteService.getById(data.getId()); // Buscamos al transportista en la base

        textFieldPlaca.setText(unidadTransporteModelUpdateSee.getPlaca());
        textFieldMarca.setText(unidadTransporteModelUpdateSee.getMarca());
        textFieldModelo.setText(unidadTransporteModelUpdateSee.getModelo());
        textFieldUnidadVolumen.setText(unidadTransporteModelUpdateSee.getUnidadVolumen());
        textFieldVolumen.setText(unidadTransporteModelUpdateSee.getVolumenSoportado() + "");
        textFieldUnidadPeso.setText(unidadTransporteModelUpdateSee.getUnidadPeso());
        textFieldPeso.setText(unidadTransporteModelUpdateSee.getPesoSoportado() + "");
        textFieldTransportista.setText(unidadTransporteModelUpdateSee.getTransportista().getNombreCompleto());

        bloquearDato();
    }

    void bloquearDato(){                   // Esto va a desabilitar todos los campos en el caso de la opcion VER
        textFieldPlaca.setDisable(true);
        textFieldMarca.setDisable(true);
        textFieldModelo.setDisable(true);
        textFieldUnidadVolumen.setDisable(true);
        textFieldVolumen.setDisable(true);
        textFieldUnidadPeso.setDisable(true);
        textFieldPeso.setDisable(true);
        textFieldTransportista.setDisable(true);
    }





}


