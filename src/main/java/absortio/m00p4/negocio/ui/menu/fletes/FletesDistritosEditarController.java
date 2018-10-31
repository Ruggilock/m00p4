package absortio.m00p4.negocio.ui.menu.fletes;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
import absortio.m00p4.negocio.model.auxiliares.Flete;
import absortio.m00p4.negocio.model.auxiliares.FleteDistrito;
import absortio.m00p4.negocio.service.FleteCapacidadService;
import absortio.m00p4.negocio.service.FleteDistritoService;
import absortio.m00p4.negocio.service.FleteModelService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class FletesDistritosEditarController implements Initializable{

    @FXML
    private AnchorPane paneEditarFleteDistrito;
    @FXML
    private ComboBox comboDepartamento;
    @FXML
    private ComboBox comboProvincia;
    @FXML
    private ComboBox comboDistrito;

    @FXML
    private Spinner<Double> spinnerCosto;

    @Autowired
    private ApplicationContext context ;

    @Autowired
    FleteDistritoService fleteDistritoService;

    @Autowired
    FleteCapacidadService fleteCapacidadService;

    @Autowired
    FleteModelService fleteModelService;

    private FleteDistritoModel fleteDistritoModel;

    @FXML
    private Label labelTitulo;

    @FXML
    private Label labelFiltro;

    private Font fuenteLabelTitulo;

    private Font fuenteLabelFiltro;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fuenteLabelTitulo = new Font("Century Gothic", 49);
        labelTitulo.fontProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> ov, Font b4, Font aftr) {
                if (aftr.equals(fuenteLabelTitulo)) return;
                labelTitulo.setFont(fuenteLabelTitulo);
            }
        });
        fuenteLabelFiltro = new Font("Century Gothic", 25);
        labelFiltro.fontProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> ov, Font b4, Font aftr) {
                if (aftr.equals(fuenteLabelFiltro)) return;
                labelFiltro.setFont(fuenteLabelFiltro);
            }
        });
        fleteDistritoModel=new FleteDistritoModel();
        //cargar departamento combo solo sera Lima
        cargarCombos();

    }


    public void buttonCancelar(MouseEvent mouseEvent) throws IOException{
        Pane paneParent = (Pane) paneEditarFleteDistrito.getParent();
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/fletes/fletesdistritos.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    public void buttonAceptar(MouseEvent mouseEvent) throws IOException {
        //Primero valido que no se inserten repetidos
        List<FleteDistritoModel> fleteDistritoModels=fleteDistritoService.findAllByDepartamentoAndProvinciaAndDistrito(comboDepartamento.getValue().toString(), comboProvincia.getValue().toString(),comboDistrito.getValue().toString());

        if(fleteDistritoModels.size()==0 || fleteDistritoModel.getId().equals(fleteDistritoModels.get(0).getId()) ){
            //Aca se graba el nuevo distrito y todos los insert a la tabla cruce
            fleteDistritoService.updateDepartamentoProvinciaDistritoCosto(fleteDistritoModel.getId(), comboDepartamento.getValue().toString(), comboProvincia.getValue().toString(), comboDistrito.getValue().toString(), spinnerCosto.getValue().doubleValue());

            List<FleteModel> lista=fleteDistritoModel.getFletes();
            for(int i=0;i<lista.size();i++){
                lista.get(i).setCosto(spinnerCosto.getValue() * 0.5 + lista.get(i).getCapacidad().getCosto()*0.5);
            }
            List<FleteModel> listafletes=fleteModelService.findAllFlete();
            List<FleteModel> deesedistrito=fleteModelService.findByDistrito_Id(fleteDistritoModel.getId());
            for(int i=0;i<deesedistrito.size();i++){
                Double costonuevo=deesedistrito.get(i).getCapacidad().getCosto()*0.5+deesedistrito.get(i).getDistrito().getCosto()*0.5;
                fleteModelService.updateCostoFletes(costonuevo, deesedistrito.get(i).getCapacidad(), deesedistrito.get(i).getDistrito());
            }

            Pane paneParent = (Pane) paneEditarFleteDistrito.getParent();
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/fletes/fletesdistritos.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Pane pane =fxmlLoader.load();
            paneParent.getChildren().setAll(pane);
        }
        else {
            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
            dialogoAlerta.setTitle("Ventana de Validacion");
            dialogoAlerta.setHeaderText(null);
            dialogoAlerta.initStyle(StageStyle.UTILITY);
            dialogoAlerta.setContentText("El distrito seleccionado ya se encuentra registrado");
            Optional<ButtonType> result = dialogoAlerta.showAndWait();
        }
    }

    @FXML
    public void cargarData(FleteDistrito seleccion){
        fleteDistritoModel=fleteDistritoService.findAllByDepartamentoAndProvinciaAndDistrito(seleccion.getDepartamento(), seleccion.getProvincia(), seleccion.getDistrito()).get(0);
        comboDepartamento.setValue(seleccion.getDepartamento());
        comboProvincia.setValue(seleccion.getProvincia());
        comboDistrito.setValue(seleccion.getDistrito());
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, seleccion.getCosto(), 0.5 );
        spinnerCosto.setValueFactory ( valueFactory );
    }

    void cargarCombos(){
        HashMap<String, List<String>> provincias= SystemSingleton.getInstance().getProvinciasxdepartamento();
        List<String> provinciasLima = provincias.get("Lima");

        ObservableList combox1 = FXCollections.observableList(SystemSingleton.getInstance().getDepartamentos());
        comboDepartamento.setItems(combox1);
        comboDepartamento.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                ObservableList combox2 = FXCollections.observableArrayList((List) SystemSingleton.getInstance().getProvinciasxdepartamento().get(t1));
                comboProvincia.setItems(combox2);
            }
        });

        comboProvincia.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 != null) {
                    ObservableList combox3 = FXCollections.observableArrayList((List) SystemSingleton.getInstance().getDistritosxprovincia().get(t1));
                    comboDistrito.setItems(combox3);
                }
            }
        });


    }




}
