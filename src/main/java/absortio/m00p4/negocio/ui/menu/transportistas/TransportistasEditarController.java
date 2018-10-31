package absortio.m00p4.negocio.ui.menu.transportistas;

import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.auxiliares.Transportista;
import absortio.m00p4.negocio.service.TransportistaService;
import absortio.m00p4.negocio.ui.menu.roles.RolesController;
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
public class TransportistasEditarController implements Initializable {
    private static final Logger LOGGER = LogManager.getLogger(TransportistasEditarController.class);
    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");

    @FXML
    Pane paneEditarTransportista;

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
    @FXML
    Label labelTitulo;

    @Autowired
    private ApplicationContext context;

    @Autowired
    TransportistaService transportistaService;

    private int flag_ok;
    private int flag_documento;
    private int flag_nombre;
    private int flag_apellidoPat;
    private int flag_apellidoMat;

    private TransportistaModel transportistaModelUpdateSee;

// ---------------------------------------------------
    // Métodos y procedimientos





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transportistaModelUpdateSee = new TransportistaModel();
    }

    public void cambiarOpciones(){
        if (tipoDocumento.compareTo("RUC") == 0) {

            try {
                labelNombre.setText("Nombre de empresa:");  // Cambiamos el nombre del label (los hilos se manejan internamente)
            } catch (Exception e) {
                System.out.println ("Error en el manejo de labels");
                e.printStackTrace();
            }

            textFieldApellidoPat.setDisable(true);     // Desabilitamos
            textFieldApellidoMat.setDisable(true);
            textFieldApellidoPat.setEditable(false);
            textFieldApellidoMat.setEditable(false);
            textFieldApellidoPat.setText("");           // Reiniciamos el valor
            textFieldApellidoMat.setText("");           // Reiniciamos el valor

        } else {
            try {
                labelNombre.setText("Nombres:");  // Cambiamos el nombre del label (los hilos se manejan internamente)
            } catch (Exception e) {
                System.out.println ("Error en el manejo de labels");
                e.printStackTrace();
            }

            textFieldApellidoMat.setDisable(false);     // Habilitamos
            textFieldApellidoPat.setDisable(false);     // Habilitamos
            textFieldApellidoPat.setEditable(true);
            textFieldApellidoMat.setEditable(true);
        }
    }


    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {
    // Volvemos a la pantalla anterior
        Pane paneParent = (Pane) paneEditarTransportista.getParent();    // Con esto obtengo la pantalla base "menuform.fxml"

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));      // Leo la pantalla "transportes.fxml"
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);      // Cargo la pantalla "transportes.fxml" sobre la pantalla base "menuform.fxml"
    }



    @FXML
    void buttonAceptar(MouseEvent event) throws IOException {       // Al hacer click en el boton Aceptar (se supone que ya todo fue seleccionado)

        // Definimos los estilos de los textField
        String eError = "    -fx-focus-color: #d35244;-fx-faint-focus-color: #d3524422;    -fx-highlight-fill: -fx-accent; -fx-highlight-text-fill: white;-fx-background-color:\n" + "        -fx-focus-color,\n" + "        -fx-control-inner-background,\n" + "        -fx-faint-focus-color,\n" + "        linear-gradient(from 0px 0px to 0px 5px, derive(-fx-control-inner-background, -9%), -fx-control-inner-background);\n" + "    -fx-background-insets: -0.2, 1, -1.4, 3;\n" + "    -fx-background-radius: 3, 2, 4, 0;\n" + "    -fx-prompt-text-fill: transparent;";
        String eInicial = textFieldNombres.getStyle().toString();

        // Realizamos las validaciones
        realizarValidaciones();

        if (flag_ok == 1) {
            guardarDato(); // Guardar y editar

            // Volvemos a la pantalla anterior
            Pane paneParent = (Pane) paneEditarTransportista.getParent();    // Con esto obtengo la pantalla base "menuform.fxml"

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));      // Leo la pantalla "transportes.fxml"
            fxmlLoader.setControllerFactory(context::getBean);
            Pane pane = fxmlLoader.load();
            paneParent.getChildren().setAll(pane);      // Cargo la pantalla "transportes.fxml" sobre la pantalla base "menuform.fxml"
        }
         else {
            if (flag_documento == 0) textFieldDocumento.setStyle(eError);
            if (flag_nombre == 0) textFieldNombres.setStyle(eError);
            if (flag_apellidoPat == 0) textFieldApellidoPat.setStyle(eError);
            if (flag_apellidoMat == 0) textFieldApellidoMat.setStyle(eError);
            if (flag_documento == 1) textFieldDocumento.setStyle(eInicial);
            if (flag_nombre == 1) textFieldNombres.setStyle(eInicial);
            if (flag_apellidoPat == 1) textFieldApellidoPat.setStyle(eInicial);
            if (flag_apellidoMat == 1) textFieldApellidoMat.setStyle(eInicial);
        }
    }

    void realizarValidaciones (){
        flag_ok = 0;
        flag_documento = 0;
        flag_nombre = 0;
        flag_apellidoPat = 0;
        flag_apellidoMat = 0;

        if (tipoDocumento.compareTo("DNI") == 0){
            if (textFieldDocumento.getText().length() == 8  && textFieldDocumento.getText().matches("[0-9]*"))   flag_documento = 1;
            if (!textFieldNombres.getText().isEmpty() && textFieldNombres.getText().matches("[[a-zA-Z][ ]]*")) flag_nombre = 1;
            if (!textFieldApellidoPat.getText().isEmpty() && textFieldApellidoPat.getText().matches("[[a-zA-Z][ ]]*")) flag_apellidoPat = 1;
            if (!textFieldApellidoMat.getText().isEmpty() && textFieldApellidoMat.getText().matches("[[a-zA-Z][ ]]*")) flag_apellidoMat = 1;
        }

        else if (tipoDocumento.compareTo("RUC") == 0) {
            flag_apellidoPat = 1;
            flag_apellidoMat = 1;
            if (textFieldDocumento.getText().length() == 11  && textFieldDocumento.getText().matches("[0-9]*"))   flag_documento = 1;
            if (!textFieldNombres.getText().isEmpty() && textFieldNombres.getText().matches("[[a-zA-Z][ ][0-9]]*")) flag_nombre = 1;        // Los nombres de empresa pueden contener números
        }

        if (flag_documento == 1 && flag_apellidoPat == 1 && flag_nombre == 1 && flag_apellidoMat == 1)   flag_ok = 1;
    }

    void guardarDato (){
        TransportistaModel transportistaModel = new TransportistaModel();

        transportistaModel.setId(transportistaModelUpdateSee.getId());      // Editar

        transportistaModel.setNombres(textFieldNombres.getText());
        transportistaModel.setTipoDocumento(tipoDocumento);
        transportistaModel.setDocumentoIdentidad(textFieldDocumento.getText());

        if (textFieldApellidoPat == null || textFieldApellidoPat.getText().compareTo("") == 0) {
            transportistaModel.setApellidoPaterno("");
        } else {
            transportistaModel.setApellidoPaterno(textFieldApellidoPat.getText());
        }

        if (textFieldApellidoMat == null || textFieldApellidoMat.getText().compareTo("") == 0) {
            transportistaModel.setApellidoMaterno("");
        } else {
            transportistaModel.setApellidoMaterno(textFieldApellidoMat.getText());
        }

        if (textFieldTelefono == null || textFieldTelefono.getText().compareTo("") == 0) {
            transportistaModel.setTelefono("");
        } else {
            transportistaModel.setTelefono(textFieldTelefono.getText());
        }

        transportistaModel.setNombreCompleto(transportistaModel.getNombres() + " " + transportistaModel.getApellidoPaterno() + " " + transportistaModel.getApellidoMaterno());//        usuarioModel.setId(7);
        transportistaModel.setEstado(transportistaModelUpdateSee.getEstado());
        transportistaService.save(transportistaModel);        // Actualizamos el nuevo valor
    }

    @FXML
    void leerDato(Transportista data){      // Esto va a servir para extraer todos los datos del transportista en los casos de editar y ver (colocamos los campos en pantalla)
        transportistaModelUpdateSee = transportistaService.getById(data.getId());       // Buscamos al transportista en la base

        textFieldTipoDocumento.setText(transportistaModelUpdateSee.getTipoDocumento());
        textFieldTipoDocumento.setDisable(true);        // Desabilitamos el tipoDocumento
        tipoDocumento = transportistaModelUpdateSee.getTipoDocumento();
        textFieldDocumento.setText(transportistaModelUpdateSee.getDocumentoIdentidad());
        textFieldApellidoPat.setText(transportistaModelUpdateSee.getApellidoPaterno());
        textFieldApellidoMat.setText(transportistaModelUpdateSee.getApellidoMaterno());
        textFieldNombres.setText(transportistaModelUpdateSee.getNombres());
        textFieldTelefono.setText(transportistaModelUpdateSee.getTelefono());

        cambiarOpciones();      // cambiamos las opciones dependiendo si es RUC o no
    }







}


