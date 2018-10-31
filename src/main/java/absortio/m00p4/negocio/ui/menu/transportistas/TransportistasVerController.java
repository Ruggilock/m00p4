package absortio.m00p4.negocio.ui.menu.transportistas;

import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.auxiliares.Transportista;
import absortio.m00p4.negocio.service.TransportistaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class TransportistasVerController implements Initializable {

    @FXML
    Pane paneVerTransportista;

    @FXML
    TextField textFieldNombres;
    @FXML
    TextField textFieldApellidoPat;
    @FXML
    TextField textFieldApellidoMat;
    @FXML
    TextField textFieldDocumento;
    @FXML
    TextField textFieldTelefono;
    @FXML
    TextField textFieldTipoDocumento;

    private String tipoDocumento;
    //@FXML
    //Button aceptar;

    @FXML
    Label labelNombre;          // si vamos a hacer cambios en el nombre, hay q asegurarnoos q quepa toda la información en el espacio
    // ---------------------------------------------------

    @Autowired
    private ApplicationContext context;

    @Autowired
    TransportistaService transportistaService;


    private TransportistaModel transportistaModelUpdateSee;

// ---------------------------------------------------
    // Métodos y procedimientos


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transportistaModelUpdateSee = new TransportistaModel();
    }


    @FXML
    void buttonAceptar(MouseEvent event) throws IOException {       // Al hacer click en el boton Aceptar (se supone que ya todo fue seleccionado)

            // Volvemos a la pantalla anterior
            Pane paneParent = (Pane) paneVerTransportista.getParent();    // Con esto obtengo la pantalla base "menuform.fxml"

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));      // Leo la pantalla "transportes.fxml"
            fxmlLoader.setControllerFactory(context::getBean);
            Pane pane = fxmlLoader.load();
            paneParent.getChildren().setAll(pane);      // Cargo la pantalla "transportes.fxml" sobre la pantalla base "menuform.fxml"
    }




    void leerDato(Transportista data){      // Esto va a servir para extraer todos los datos del transportista en los casos de editar y ver (colocamos los campos en pantalla)
        transportistaModelUpdateSee = transportistaService.getById(data.getId());
        //transportistaModelUpdateSee = transportistaService.getByDocumentoIdentidad(data.getDocumentoIdentidad());       // Buscamos al transportista en la base

        textFieldTipoDocumento.setText(transportistaModelUpdateSee.getTipoDocumento());
        textFieldDocumento.setText(transportistaModelUpdateSee.getDocumentoIdentidad());
        textFieldApellidoPat.setText(transportistaModelUpdateSee.getApellidoPaterno());
        textFieldApellidoMat.setText(transportistaModelUpdateSee.getApellidoMaterno());
        textFieldNombres.setText(transportistaModelUpdateSee.getNombres());
        textFieldTelefono.setText(transportistaModelUpdateSee.getTelefono());

        bloquearDato();
    }

    void bloquearDato(){                   // Esto va a desabilitar todos los campos en el caso de la opcion VER
            textFieldTipoDocumento.setDisable(true);
            textFieldTelefono.setDisable(true);
            textFieldNombres.setDisable(true);
            textFieldApellidoMat.setDisable(true);
            textFieldDocumento.setDisable(true);
            textFieldApellidoPat.setDisable(true);
    }






}


