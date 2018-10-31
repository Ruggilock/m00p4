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
public class UnidadesTransportesEditarController implements Initializable {

    @FXML
    Pane paneEditarUnidadTransporte;

    @FXML
    TextField textFieldPlaca;
    @FXML
    ComboBox comboBoxMarca;
    @FXML
    ComboBox comboBoxModelo;
    @FXML
    TextField textFieldVolumen;
    @FXML
    TextField textFieldPeso;
    @FXML
    ComboBox comboBoxUnidadVolumen;
    @FXML
    ComboBox comboBoxUnidadPeso;
    @FXML
    ComboBox comboBoxTransportista;

    //@FXML
    //Button aceptar;


    @Autowired
    private ApplicationContext context;

    @Autowired
    UnidadTransporteService unidadTransporteService;
    @Autowired
    TransportistaService transportistaService;
//    private int flag_ok;
//    private int flag_documento;
//    private int flag_nombre;
//    private int flag_apellidoPat;
//    private int flag_apellidoMat;

    private int flag_accion = 0;

    private TransportistaModel transportistaModel;
    private UnidadTransporteModel unidadTransporteModelUpdateSee;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;


// ---------------------------------------------------
    // Métodos y procedimientos


    void cargarComboBoxUnidadVolumen(){
        ArrayList<String> lUnidadVolumen = new ArrayList<>();
        lUnidadVolumen.add("m3.");
        lUnidadVolumen.add("lts.");

        ObservableList<String> olUnidadVolumen = FXCollections.observableArrayList(lUnidadVolumen);

        comboBoxUnidadVolumen.getItems().addAll(olUnidadVolumen);
        comboBoxUnidadVolumen.setValue(olUnidadVolumen.get(0));
        comboBoxUnidadVolumen.setEditable(true);

        TextFields.bindAutoCompletion(comboBoxUnidadVolumen.getEditor(), comboBoxUnidadVolumen.getItems());
    }

    void cargarComboBoxUnidadPeso(){
        ArrayList<String> lUnidadPeso = new ArrayList<>();
        lUnidadPeso.add("kg.");
        lUnidadPeso.add("gr.");
        lUnidadPeso.add("ton.");

        ObservableList<String> olUnidadPeso = FXCollections.observableArrayList(lUnidadPeso);

        comboBoxUnidadPeso.getItems().addAll(olUnidadPeso);
        comboBoxUnidadPeso.setValue(olUnidadPeso.get(0));
        comboBoxUnidadPeso.setEditable(true);

        TextFields.bindAutoCompletion(comboBoxUnidadPeso.getEditor(), comboBoxUnidadPeso.getItems());
    }


    void cargarComboBoxTransportista(){
        ArrayList<TransportistaModel> transportistaModels = (ArrayList) transportistaService.findAllTransportista();
        ArrayList<String> lTransportista = new ArrayList<>();

        for (int i = 0; i < transportistaModels.size(); i++) {
            lTransportista.add(transportistaModels.get(i).getNombreCompleto());     // Agregamos el nombreCompleto al comboBox
        }

        ObservableList<String> olTransportista = FXCollections.observableArrayList(lTransportista);

        comboBoxTransportista.getItems().addAll(olTransportista);
        comboBoxTransportista.setValue(olTransportista.get(0));     // seteamos el 1er valor
        comboBoxTransportista.setEditable(true);

        TextFields.bindAutoCompletion(comboBoxTransportista.getEditor(), comboBoxTransportista.getItems());
    }

    void cargarComboBoxMarca(){
        ArrayList<String> lMarca = new ArrayList<>();
        lMarca.add("Toyota");
        lMarca.add("Nissan");
        lMarca.add("Kia");

        ObservableList<String> olMarca = FXCollections.observableArrayList(lMarca);

        comboBoxMarca.getItems().addAll(olMarca);
        comboBoxMarca.setValue(olMarca.get(0));
        comboBoxMarca.setEditable(true);

        TextFields.bindAutoCompletion(comboBoxMarca.getEditor(), comboBoxMarca.getItems());
    }

    void cargarComboBoxModelo(){
        ArrayList<String> lModelo = new ArrayList<>();
        lModelo.add("XYZ");
        lModelo.add("ZYX");
        lModelo.add("ZZZ");

        ObservableList<String> olModelo = FXCollections.observableArrayList(lModelo);

        comboBoxModelo.getItems().addAll(olModelo);
        comboBoxModelo.setValue(olModelo.get(0));
        comboBoxModelo.setEditable(true);

        TextFields.bindAutoCompletion(comboBoxModelo.getEditor(), comboBoxModelo.getItems());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cargarComboBoxUnidadVolumen();
        cargarComboBoxUnidadPeso();
        cargarComboBoxTransportista();
        cargarComboBoxMarca();
        cargarComboBoxModelo();
    }


    @FXML
    void clickButtonCancelar(MouseEvent event) throws IOException {
        // Volvemos a la pantalla anterior
        Pane paneParent = (Pane) paneEditarUnidadTransporte.getParent();    // Con esto obtengo la pantalla base "menuform.fxml"

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));      // Leo la pantalla "transportes.fxml"
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);      // Cargo la pantalla "transportes.fxml" sobre la pantalla base "menuform.fxml"
    }



    @FXML
    void clickButtonAceptar(MouseEvent event) throws IOException {       // Al hacer click en el boton Aceptar (se supone que ya todo fue seleccionado)

        guardarDato(); // Guardar

        // Volvemos a la pantalla anterior
        Pane paneParent = (Pane) paneEditarUnidadTransporte.getParent();    // Con esto obtengo la pantalla base "menuform.fxml"
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));      // Leo la pantalla "transportes.fxml"
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);      // Cargo la pantalla "transportes.fxml" sobre la pantalla base "menuform.fxml"

    }

    void leerDato(UnidadTransporte data){      // Esto va a servir para extraer todos los datos del transportista en los casos de editar y ver (colocamos los campos en pantalla)
        unidadTransporteModelUpdateSee = unidadTransporteService.getById(data.getId()); // Buscamos al transportista en la base

        textFieldPlaca.setText(unidadTransporteModelUpdateSee.getPlaca());
        comboBoxMarca.setValue (unidadTransporteModelUpdateSee.getMarca());
        comboBoxModelo.setValue (unidadTransporteModelUpdateSee.getModelo());
        comboBoxUnidadVolumen.setValue (unidadTransporteModelUpdateSee.getUnidadVolumen());
        textFieldVolumen.setText(unidadTransporteModelUpdateSee.getVolumenSoportado() + "");
        comboBoxUnidadPeso.setValue (unidadTransporteModelUpdateSee.getUnidadPeso());
        textFieldPeso.setText(unidadTransporteModelUpdateSee.getPesoSoportado() + "");
        comboBoxTransportista.setValue (unidadTransporteModelUpdateSee.getTransportista().getNombreCompleto());


        textFieldPlaca.setDisable(true);
        comboBoxMarca.setDisable(true);
        comboBoxModelo.setDisable(true);
    }

    void guardarDato (){
        UnidadTransporteModel unidadTransporteModel = new UnidadTransporteModel();

        unidadTransporteModel.setId(unidadTransporteModelUpdateSee.getId());      // Editar

        unidadTransporteModel.setPlaca(textFieldPlaca.getText());
        unidadTransporteModel.setMarca(comboBoxMarca.getValue().toString());
        unidadTransporteModel.setModelo(comboBoxModelo.getValue().toString());
        unidadTransporteModel.setUnidadPeso(comboBoxUnidadPeso.getValue().toString());
        unidadTransporteModel.setPesoSoportado(Float.parseFloat(textFieldPeso.getText()));
        unidadTransporteModel.setUnidadVolumen(comboBoxUnidadVolumen.getValue().toString());
        unidadTransporteModel.setVolumenSoportado(Float.parseFloat(textFieldVolumen.getText()));

        // Buscamos en la BD al transportista
        transportistaModel = new TransportistaModel();
        transportistaModel = transportistaService.getByNombreCompleto(comboBoxTransportista.getValue().toString());     // Buscamos en la BD el id

        unidadTransporteModel.setTransportista(transportistaModel);
        unidadTransporteModel.setEstado(unidadTransporteModelUpdateSee.getEstado());
        unidadTransporteService.save(unidadTransporteModel);        // Guardamos el nuevo valor
    }


}


