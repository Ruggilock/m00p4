package absortio.m00p4.negocio.ui.menu.empleados;

import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Empleado;
import absortio.m00p4.negocio.service.RolService;
import absortio.m00p4.negocio.service.UsuarioService;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
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
import org.hibernate.ejb.criteria.expression.function.AggregationFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import static absortio.m00p4.M00p4Application.passwordMail;
import static absortio.m00p4.M00p4Application.usernameMail;

@Component
public class EmpleadosNuevoController implements Initializable {

    @FXML
    Pane paneNuevoEmpleado;
    @FXML
    Label labelNuevo;
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
    TextField textFieldCorreo;

    @FXML
    DatePicker dataPickerFechaNac;
    @FXML
    Button botonAceptar;

    @FXML
    ComboBox<String> comboBoxGenero;
    @FXML
    ComboBox<String> comboBoxRol;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolService rolService;
    Integer flag;
    UsuarioModel usuarioModelUpdateSee;
    @Autowired
    private ApplicationContext context;
    private static final int MAX_CARACTERES=8;

    public static String NUMEROS = "0123456789";

    public static String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final Logger LOGGER= LogManager.getLogger(EmpleadosNuevoController.class);
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flag = 0;       // inicializamos

        ObservableList<String> olGenero = FXCollections.observableArrayList("Masculino", "Femenino", "Otro");
        comboBoxGenero.getItems().addAll(olGenero);
        comboBoxGenero.setValue(olGenero.get(0));
        comboBoxGenero.setEditable(false);
        TextFields.bindAutoCompletion(comboBoxGenero.getEditor(), comboBoxGenero.getItems());

        ArrayList<String> lroles = new ArrayList<>();
        ArrayList<RolModel> rolModels = (ArrayList<RolModel>) rolService.findAllRol();
        for (int i = 0; i < rolModels.size(); i++) {
            lroles.add(rolModels.get(i).getNombre());
        }
        ObservableList<String> olRol = FXCollections.observableArrayList(lroles);
        comboBoxRol.getItems().addAll(olRol);
        comboBoxRol.setValue(olRol.get(0));
        comboBoxRol.setEditable(false);
        TextFields.bindAutoCompletion(comboBoxRol.getEditor(), comboBoxRol.getItems());
    }

    void setDatos(Empleado data) {
        usuarioModelUpdateSee = usuarioService.getByDocumentoIdentidad(data.getDni());
        textFieldDocumento.setText(usuarioModelUpdateSee.getDocumentoIdentidad());
        textFieldApellidoPat.setText(usuarioModelUpdateSee.getApellidoPaterno());
        textFieldApellidoMat.setText(usuarioModelUpdateSee.getApellidoMaterno());
        textFieldNombres.setText(usuarioModelUpdateSee.getNombres());
        textFieldTelefono.setText(usuarioModelUpdateSee.getTelefono());
        textFieldCorreo.setText(usuarioModelUpdateSee.getCorreo());
        comboBoxRol.setValue(usuarioModelUpdateSee.getIdRol().getNombre());
        comboBoxGenero.setValue(usuarioModelUpdateSee.getGenero());
        dataPickerFechaNac.setValue(usuarioModelUpdateSee.getFechaNacimiento().toLocalDate());

    }

    void bloquearDatos() {
        if (flag == 1) {
            textFieldCorreo.setDisable(true);
            textFieldTelefono.setDisable(true);
            textFieldNombres.setDisable(true);
            textFieldApellidoMat.setDisable(true);
            textFieldDocumento.setDisable(true);
            textFieldApellidoPat.setDisable(true);
            comboBoxGenero.setDisable(true);
            comboBoxRol.setDisable(true);
            dataPickerFechaNac.setDisable(true);
        }
    }

    @FXML
    public void cargardata(int flag, Empleado data) {
        if (flag == 1) {
            this.flag = 1;
            setDatos(data);
            bloquearDatos();
            labelNuevo.setText("Ver");
            botonAceptar.setDisable(true);

        } else if (flag == 2) {
            this.flag = 2;
            labelNuevo.setText("Editar");
            setDatos(data);
            botonAceptar.setDisable(false);
        }

    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {

        Pane paneParent = (Pane) paneNuevoEmpleado.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/empleados/empleados.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    void buttonAceptar(MouseEvent event) throws IOException {

        if (!camposValidos()) return;


        if (flag == 0 || flag == 2) {
            UsuarioModel usuarioModel = new UsuarioModel();
            if (flag == 2) {
                usuarioModel.setId(usuarioModelUpdateSee.getId());
                usuarioModel.setContrasena(usuarioModelUpdateSee.getContrasena());
            }
            usuarioModel.setDocumentoIdentidad(textFieldDocumento.getText());
            usuarioModel.setNombres(textFieldNombres.getText());
            usuarioModel.setApellidoPaterno(textFieldApellidoPat.getText());
            usuarioModel.setApellidoMaterno(textFieldApellidoMat.getText());
            usuarioModel.setCorreo(textFieldCorreo.getText());
            usuarioModel.setTelefono(textFieldTelefono.getText());
            Date dpCurrentDate1 = Date.valueOf(dataPickerFechaNac.getValue());
            usuarioModel.setFechaNacimiento(dpCurrentDate1);
            usuarioModel.setEstado("activo");
            RolModel rolModel = rolService.getByNombre(comboBoxRol.getValue());
            usuarioModel.setIdRol(rolModel);
            if (flag == 0) {
                //usuarioModel.setFechaUltimaConexion(dpCurrentDate1);
                usuarioModel.setEstado("activo");
                String clave = "";
                String keys=MINUSCULAS+NUMEROS+MAYUSCULAS;
                for (int i = 0 ; i < MAX_CARACTERES ; i++) {
                    clave += keys.charAt((new java.util.Random()).nextInt(keys.length()));
                }

                usuarioModel.setContrasena(clave);
                String nombre= textFieldNombres.getText();
                sendMail(usuarioModel.getCorreo(),clave,nombre);

            }
            usuarioModel.setGenero(comboBoxGenero.getValue());
            usuarioModel.setNombreCompleto(textFieldNombres.getText() + " " + textFieldApellidoPat.getText() + " " + textFieldApellidoMat.getText());
            usuarioService.save(usuarioModel);
            if(flag==0){
                LOGGERAUDIT.info("El usuario "+ UsuarioSingleton.usuarioModel.getNombres()+" registró el nuevo empleado "+ usuarioModel.getNombres());
            }

        }
        Pane paneParent = (Pane) paneNuevoEmpleado.getParent();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/empleados/empleados.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);

    }

    private boolean camposValidos() {
        if (comboBoxGenero.getValue() == null || comboBoxRol.getValue() == null
                || textFieldApellidoPat.getText().equals("") || textFieldApellidoMat.getText().equals("")
                || (textFieldCorreo.getText().equals(""))
                || (textFieldDocumento.getText().equals("")) || textFieldNombres.getText().equals("")
                || textFieldTelefono.getText().equals("") || dataPickerFechaNac.getValue() == null) {
            Validador.mostrarDialogError("¡Deben llenarse todos los campos!");
            return false;
        }
        RolModel rolIngresado = rolService.getByNombre(comboBoxRol.getValue());
        if (rolIngresado == null) {
            Validador.mostrarDialogError("¡El rol ingresado no es válido!");
            return false;
        }

        if (Date.valueOf(dataPickerFechaNac.getValue()).compareTo(Date.valueOf(LocalDate.now())) > 0) {
            Validador.mostrarDialogError("¡La fecha de nacimiento no puede ser en el futuro!");
            return false;
        }

        if (!Validador.formatoCorreo(textFieldCorreo.getText())) { //El resto de caracteres no son todos letras o numeros
            Validador.mostrarDialogError("¡El correo ingresado no es válido!");
            return false;
        }
        if (!Validador.soloLetras(textFieldNombres.getText())) {
            Validador.mostrarDialogError("¡El nombre ingresado sólo debe poseer letras!");
            return false;
        }
        if (!Validador.soloLetras(textFieldApellidoMat.getText()) || !Validador.soloLetras(textFieldApellidoPat.getText())) {
            Validador.mostrarDialogError("¡Los apellidos ingresados sólo deben poseer letras!");
            return false;
        }
        if (!Validador.formatoTelefono(textFieldTelefono.getText())) {
            Validador.mostrarDialogError("¡El teléfono ingresado sólo debe poseer números!");
            return false;
        }

        if (flag == 0 && usuarioService.getByDocumentoIdentidad(textFieldDocumento.getText()) != null) {
            Validador.mostrarDialogError("Dicho documento de identidad ya pertenece a un empleado.");
            return false;
        }

        return true;
    }


    public static void sendMail(String clientMail,String clave, String nombre){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usernameMail, passwordMail);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usernameMail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(clientMail));
            message.setSubject("Registro en MOPPA");
            message.setText("Bienvenido a MOPPA, "+nombre+".\nSu contraseña para ingresar al sistema es:\n"+clave);

            Transport.send(message);
            LOGGER.info("Se envio un mensaje de correo...");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ventana de Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Se ha enviado un correo con la contraseña");
            alert.showAndWait();

        } catch (MessagingException e) {
            LOGGER.error("Error al enviar correo");
            Validador.mostrarDialogError("Error al enviar correo");
            throw new RuntimeException(e);
        }
    }


}
