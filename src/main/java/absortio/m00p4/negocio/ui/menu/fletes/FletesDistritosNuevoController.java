package absortio.m00p4.negocio.ui.menu.fletes;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class FletesDistritosNuevoController implements Initializable {

    @FXML
    private AnchorPane paneNuevoFleteDistrito;

    @FXML
    private ComboBox comboDepartamento;
    @FXML
    private ComboBox comboProvincia;
    @FXML
    private ComboBox comboDistrito;

    @FXML
    private Spinner<Double> spinnerCosto;

    FleteDistritoModel fleteDistritoModel;

    @Autowired
    private ApplicationContext context ;

    @Autowired
    FleteDistritoService fleteDistritoService;

    @Autowired
    FleteCapacidadService fleteCapacidadService;

    @Autowired
    FleteModelService fleteModelService;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @FXML
    private Label labelTitulo;

    @FXML
    private Label labelFiltro;

    private Font fuenteLabelTitulo;

    private Font fuenteLabelFiltro;

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
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 1000.0, 0.0, 0.5 );
        spinnerCosto.setValueFactory ( valueFactory );
        //cargar departamento combo solo sera Lima
        cargarCombos();

    }


    @FXML
    public void buttonAceptar(MouseEvent mouseEvent) throws IOException{
        //Primero valido que no se inserten repetidos
        if(fleteDistritoService.findAllByDepartamentoAndProvinciaAndDistrito(comboDepartamento.getValue().toString(), comboProvincia.getValue().toString(),comboDistrito.getValue().toString()).size()== 0){
            //Aca se graba el nuevo distrito y todos los insert a la tabla cruce
            fleteDistritoModel.setDepartamento(comboDepartamento.getValue().toString());
            fleteDistritoModel.setProvincia(comboProvincia.getValue().toString());
            fleteDistritoModel.setDistrito(comboDistrito.getValue().toString());
            fleteDistritoModel.setCosto(spinnerCosto.getValue().doubleValue());
            List<FleteCapacidadModel> capacidades=fleteCapacidadService.findAllFleteCapacidad();
            for(int i=0;i<capacidades.size();i++){
                fleteDistritoModel.addCapacidad(capacidades.get(i));
            }
            fleteDistritoModel.setEstado("activo");
            fleteDistritoService.save(fleteDistritoModel);
            //List<FleteDistritoModel>fleteDistritoModelf=fleteDistritoService.findAllByDepartamentoAndProvinciaAndDistrito(fleteDistritoModel.getDepartamento(), fleteDistritoModel.getProvincia(),fleteDistritoModel.getDistrito());
            for(int i=0; i<fleteDistritoModel.getFletes().size();i++){
                fleteDistritoModel.getFletes().get(i).setDistrito(fleteDistritoModel);
                fleteDistritoModel.getFletes().get(i).setIddistrito(fleteDistritoModel.getId());
                fleteModelService.save(fleteDistritoModel.getFletes().get(i));
            }
            LOGGERAUDIT.info("El usuario "+ usuarioModel.getNombres()+ " registró un nuevo flete por capacidad "+ fleteDistritoModel.getDistrito());

            Pane paneParent = (Pane) paneNuevoFleteDistrito.getParent();
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
    public void buttonCancelar(MouseEvent mouseEvent) throws IOException {
        Pane paneParent = (Pane) paneNuevoFleteDistrito.getParent();
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/fletes/fletesdistritos.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    void cargarCombos(){
        HashMap<String, List<String>> provincias=SystemSingleton.getInstance().getProvinciasxdepartamento();
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
