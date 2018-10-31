package absortio.m00p4.negocio.ui.menu.clientes;

import absortio.m00p4.negocio.model.ClienteModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Cliente;
import absortio.m00p4.negocio.service.ClienteService;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ClientesNuevoController implements Initializable {


    @FXML
    AnchorPane paneNuevoCliente;

    @FXML
    TextField textFieldNombres;
    @FXML
    TextField textFieldTelefono;
    @FXML
    TextField textFieldDNI;
    @FXML
    TextField textFieldContacto;
    @FXML
    TextField textFieldCorreo;
    @FXML
    TextField textFieldDireccion;
    @FXML
    Label labelNuevo;
    @FXML
    ComboBox comboBoxTipoDoc;
    @FXML
    Button botonAceptar;


    @Autowired
    private ApplicationContext context;

    @Autowired
    ClienteService clienteService;

    Integer flag;

    ClienteModel clienteModelUpdateSee;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        flag = 0;

        ObservableList<String> olGenero = FXCollections.observableArrayList("DNI", "RUC", "Carnet Extranjería", "Pasaporte");
        comboBoxTipoDoc.getItems().addAll(olGenero);
        comboBoxTipoDoc.setValue(olGenero.get(0));
        comboBoxTipoDoc.setEditable(false);
        TextFields.bindAutoCompletion(comboBoxTipoDoc.getEditor(), comboBoxTipoDoc.getItems());

        textFieldContacto.setText("");
        textFieldCorreo.setText("");
        textFieldDireccion.setText("");
        textFieldDNI.setText("");
        textFieldNombres.setText("");
        textFieldTelefono.setText("");
    }

    void setDatos(Cliente data) {
        clienteModelUpdateSee = clienteService.getByDocumentoIdentidad(data.getDocumentoIdentidad());
        comboBoxTipoDoc.setValue(clienteModelUpdateSee.getTipoDocumento());
        textFieldDNI.setText(clienteModelUpdateSee.getDocumentoIdentidad());
        textFieldNombres.setText(clienteModelUpdateSee.getNombres());
        textFieldTelefono.setText(clienteModelUpdateSee.getTelefono());
        textFieldCorreo.setText(clienteModelUpdateSee.getCorreo());
        textFieldContacto.setText(clienteModelUpdateSee.getContacto());
        textFieldDireccion.setText(clienteModelUpdateSee.getDireccion());
    }

    void bloquearDatos() {
        if (flag == 1) {
            comboBoxTipoDoc.setDisable(true);
            textFieldCorreo.setDisable(true);
            textFieldTelefono.setDisable(true);
            textFieldNombres.setDisable(true);
            textFieldContacto.setDisable(true);
            textFieldDireccion.setDisable(true);
            textFieldDNI.setDisable(true);
        }
    }

    @FXML
    public void cargardata(int flag, Cliente data) {

        if (flag == 1) {
            this.flag = 1;
            setDatos(data);
            bloquearDatos();
            labelNuevo.setText("Ver");
            botonAceptar.setDisable(true);
        } else if (flag == 2) {
            this.flag = 2;
            comboBoxTipoDoc.setDisable(true);
            labelNuevo.setText("Editar");
            setDatos(data);
            botonAceptar.setDisable(false);
        }
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {
        AnchorPane paneParent = (AnchorPane) paneNuevoCliente.getParent();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/clientes/clientes.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    void buttonAceptar(MouseEvent event) throws IOException {
        if (!camposValidos()) return;

        if (flag == 0 || flag == 2) {
            ClienteModel clienteModel = new ClienteModel();
            if (flag == 2) {
                clienteModel.setId(clienteModelUpdateSee.getId());
                clienteModel.setTipoDocumento(clienteModelUpdateSee.getTipoDocumento());
            } else {
                clienteModel.setTipoDocumento(comboBoxTipoDoc.getValue().toString());
            }
            clienteModel.setDocumentoIdentidad(textFieldDNI.getText());
            clienteModel.setNombres(textFieldNombres.getText());
            clienteModel.setCorreo(textFieldCorreo.getText());
            clienteModel.setTelefono(textFieldTelefono.getText());
            clienteModel.setContacto(textFieldContacto.getText());
            clienteModel.setDireccion(textFieldDireccion.getText());
            clienteModel.setEstado("activo");
            clienteService.save(clienteModel);
            String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres();
            if(flag==0){
                logMensaje= logMensaje+ " registro nuevo cliente "+ clienteModel.getNombres();
            }else {
                logMensaje= logMensaje+" modifico cliente "+clienteModel.getNombres();
            }
            LOGGERAUDIT.info(logMensaje);
        }
        Pane paneParent = (Pane) paneNuevoCliente.getParent();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/clientes/clientes.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    private boolean camposValidos() {
        if (comboBoxTipoDoc.getValue() == null || textFieldContacto.getText().equals("")
                || (textFieldCorreo.getText().equals("")) || textFieldDireccion.getText().equals("")
                || (textFieldDNI.getText().equals("")) || textFieldNombres.getText().equals("")
                || textFieldTelefono.getText().equals("")) {
            Validador.mostrarDialogError("¡Deben llenarse todos los campos!");
            return false;
        }
        if (!Validador.formatoCorreo(textFieldCorreo.getText())) { //El resto de caracteres no son todos letras o numeros
            Validador.mostrarDialogError("¡El correo ingresado no es válido!");
            return false;
        }
        if(!Validador.soloLetras(textFieldNombres.getText())){
            Validador.mostrarDialogError("¡El nombre ingresado sólo debe poseer letras!");
            return false;
        }
        if(!Validador.formatoTelefono(textFieldTelefono.getText())){
            Validador.mostrarDialogError("¡El teléfono ingresado sólo debe poseer números!");
            return false;
        }

        if (flag == 0 && clienteService.getByDocumentoIdentidad(textFieldDNI.getText()) != null) {
            Validador.mostrarDialogError("Dicho documento de identidad ya pertenece a un cliente.");
            return false;
        }
        return true;
    }
}
