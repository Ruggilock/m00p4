package absortio.m00p4.negocio.ui.menu.unidadestransportes;

import absortio.m00p4.negocio.model.TransportistaModel;
import absortio.m00p4.negocio.model.UnidadTransporteModel;
import absortio.m00p4.negocio.model.auxiliares.Transportista;
import absortio.m00p4.negocio.service.TransportistaService;
import absortio.m00p4.negocio.service.UnidadTransporteService;
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
public class UnidadesTransportesNuevoController implements Initializable {

    @FXML
    Pane paneNuevoUnidadTransporte;

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

    private TransportistaModel transportistaModel;
    private UnidadTransporteModel unidadTransporteModelUpdateSee;


// ---------------------------------------------------
    // Métodos y procedimientos


    void cargarComboBoxUnidadVolumen() {
        ArrayList<String> lUnidadVolumen = new ArrayList<>();
        lUnidadVolumen.add("m3.");
        lUnidadVolumen.add("lts.");

        ObservableList<String> olUnidadVolumen = FXCollections.observableArrayList(lUnidadVolumen);

        comboBoxUnidadVolumen.getItems().addAll(olUnidadVolumen);
        comboBoxUnidadVolumen.setValue(olUnidadVolumen.get(0));
        comboBoxUnidadVolumen.setEditable(true);

        TextFields.bindAutoCompletion(comboBoxUnidadVolumen.getEditor(), comboBoxUnidadVolumen.getItems());
    }

    void cargarComboBoxUnidadPeso() {
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


    void cargarComboBoxTransportista() {
        ArrayList<TransportistaModel> transportistaModels = (ArrayList) transportistaService.findAllTransportista();
        ArrayList<String> lTransportista = new ArrayList<>();
        String estadoAux = "";

        for (int i = 0; i < transportistaModels.size(); i++) {
            estadoAux = transportistaModels.get(i).getEstado();
            if (estadoAux.compareTo("inactivo") == 0) {
                // No se hace nada
            } else if (estadoAux.compareTo("activo") == 0) {
                lTransportista.add(transportistaModels.get(i).getNombreCompleto());     // Agregamos el nombreCompleto al comboBox
            }
        }

        ObservableList<String> olTransportista = FXCollections.observableArrayList(lTransportista);

        comboBoxTransportista.getItems().addAll(olTransportista);
        comboBoxTransportista.setValue(olTransportista.get(0));     // seteamos el 1er valor
        comboBoxTransportista.setEditable(true);

        TextFields.bindAutoCompletion(comboBoxTransportista.getEditor(), comboBoxTransportista.getItems());
    }

    void cargarComboBoxMarca() {
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

    void cargarComboBoxModelo() {
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
        Pane paneParent = (Pane) paneNuevoUnidadTransporte.getParent();    // Con esto obtengo la pantalla base "menuform.fxml"

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));      // Leo la pantalla "transportes.fxml"
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);      // Cargo la pantalla "transportes.fxml" sobre la pantalla base "menuform.fxml"
    }


    @FXML
    void clickButtonAceptar(MouseEvent event) throws IOException {       // Al hacer click en el boton Aceptar (se supone que ya todo fue seleccionado)

        guardarDato(); // Guardar

        // Volvemos a la pantalla anterior
        Pane paneParent = (Pane) paneNuevoUnidadTransporte.getParent();    // Con esto obtengo la pantalla base "menuform.fxml"
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/transportes/transportes.fxml"));      // Leo la pantalla "transportes.fxml"
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);      // Cargo la pantalla "transportes.fxml" sobre la pantalla base "menuform.fxml"

    }


    void guardarDato() {
        UnidadTransporteModel unidadTransporteModel = new UnidadTransporteModel();

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
        unidadTransporteModel.setEstado("activo");
        unidadTransporteService.save(unidadTransporteModel);        // Guardamos el nuevo valor
    }


}


