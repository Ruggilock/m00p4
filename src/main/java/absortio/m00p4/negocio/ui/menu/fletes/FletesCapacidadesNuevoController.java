package absortio.m00p4.negocio.ui.menu.fletes;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.service.FleteCapacidadService;
import absortio.m00p4.negocio.service.FleteDistritoService;
import absortio.m00p4.negocio.service.FleteModelService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class FletesCapacidadesNuevoController implements Initializable {

    @FXML
    private AnchorPane paneNuevoFleteCapacidad;

    @FXML
    private ComboBox comboUnipeso;

    @FXML
    private ComboBox comboUnidvol;

    @FXML
    private Spinner<Double> spinnerCosto;

    @FXML
    private Spinner<Double>  spinnerPesomin;

    @FXML
    private Spinner<Double>  spinnerPesomax;

    @FXML
    private Spinner<Double>  spinnerVolmin;

    @FXML
    private Spinner<Double>  spinnerVolmax;

    @Autowired
    private ApplicationContext context ;

    @Autowired
    FleteDistritoService fleteDistritoService;

    @Autowired
    FleteCapacidadService fleteCapacidadService;

    FleteCapacidadModel fleteCapacidadModel;

    @Autowired
    FleteModelService fleteModelService;

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
        fleteCapacidadModel=new FleteCapacidadModel();
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, 0.0, 0.5 );
        SpinnerValueFactory<Double> valueFactory1 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, 0.0, 0.5 );
        SpinnerValueFactory<Double> valueFactory2 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, 0.0, 0.5 );
        SpinnerValueFactory<Double> valueFactory3 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, 0.0, 0.5 );
        SpinnerValueFactory<Double> valueFactory4 =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, 0.0, 0.5 );

        spinnerCosto.setValueFactory ( valueFactory );
        spinnerPesomax.setValueFactory ( valueFactory1 );
        spinnerPesomin.setValueFactory ( valueFactory2 );
        spinnerVolmax.setValueFactory ( valueFactory3 );
        spinnerVolmin.setValueFactory ( valueFactory4 );
        //cargar UM pesos y volumenes
        cargarCombos();

    }

    @FXML
    public void buttonAceptar(MouseEvent mouseEvent) throws IOException {

        if(validacion()){
            //save
            List<FleteDistritoModel> distritos=fleteDistritoService.findAllFleteDistrito();

            for(int i=0;i<distritos.size();i++){
                fleteCapacidadModel.addDistrito(distritos.get(i));
            }
            fleteCapacidadModel.setEstado("activo");
            fleteCapacidadService.save(fleteCapacidadModel);

            for(int i=0; i<fleteCapacidadModel.getFletes().size();i++){
                fleteCapacidadModel.getFletes().get(i).setCapacidad(fleteCapacidadModel);
                fleteCapacidadModel.getFletes().get(i).setIdcapacidad(fleteCapacidadModel.getId());
                fleteModelService.save(fleteCapacidadModel.getFletes().get(i));
            }
            LOGGERAUDIT.info("El usuario "+ usuarioModel.getNombres()+ " registró un nuevo flete por capacidad "+ fleteCapacidadModel.getPesoMin() + "a" +fleteCapacidadModel.getPesoMax());
            Pane paneParent = (Pane) paneNuevoFleteCapacidad.getParent();
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/fletes/fletescapacidades.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Pane pane =fxmlLoader.load();
            paneParent.getChildren().setAll(pane);
        }

    }

    @FXML
    public void buttonCancelar(MouseEvent mouseEvent) throws IOException {
        Pane paneParent = (Pane) paneNuevoFleteCapacidad.getParent();
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/fletes/fletescapacidades.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    void cargarCombos(){
        comboUnipeso.getItems().addAll(SystemSingleton.getInstance().getUnidadespeso());
        comboUnidvol.getItems().addAll(SystemSingleton.getInstance().getUnidadesvol());
    }

    Boolean validacion(){
        if(spinnerVolmin.getValue() == null || spinnerVolmax.getValue()==null||spinnerPesomin.getValue()==null||spinnerPesomax.getValue()==null||comboUnidvol.getValue()==null||comboUnipeso.getValue()==null){
            //Generar alerta que todos los datos deben estar llenos
            System.out.println("Campos vacios");
            return false;
        }
        else{
            Double pesomin=spinnerPesomin.getValue().doubleValue();
            Double pesomax=spinnerPesomax.getValue().doubleValue();
            Double volmin=spinnerVolmin.getValue().doubleValue();
            Double volmax=spinnerVolmax.getValue().doubleValue();
            String umpeso=comboUnipeso.getValue().toString();
            String umvol=comboUnidvol.getValue().toString();
            Boolean flagpesos=false;
            Boolean flagvolumenes=false;

            if(pesomin>=pesomax || volmin>=volmax ){
                //generar alerta que los rangos no son validos
                Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                dialogoAlerta.setTitle("Ventana de Validacion");
                dialogoAlerta.setHeaderText(null);
                dialogoAlerta.initStyle(StageStyle.UTILITY);
                dialogoAlerta.setContentText("Rangos incongruentes");
                Optional<ButtonType> result = dialogoAlerta.showAndWait();
                return false;
            }
            //en bd se guarda  en kg y en m3
            switch (umpeso){
                case "gr":{
                    //convertir a kg
                    pesomin=pesomin/1000;
                    pesomax=pesomax/1000;
                }

            }
            List<FleteCapacidadModel>registrosquelocontienePeso=fleteCapacidadService.findByPesoMaxIsGreaterThanAndPesoMinIsLessThanEqual(pesomax,pesomin);
            //List<FleteCapacidadModel> resgitrostraslapados=fleteCapacidadService
            if(registrosquelocontienePeso.size()==0){
                flagpesos=true;
            }
            else{
                //alerta de ya esiste un rango que lo contiene para peso
                Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                dialogoAlerta.setTitle("Ventana de Validacion");
                dialogoAlerta.setHeaderText(null);
                dialogoAlerta.initStyle(StageStyle.UTILITY);
                dialogoAlerta.setContentText("Registro esta contenido en un registro existente para peso");
                Optional<ButtonType> result = dialogoAlerta.showAndWait();
                return false;
            }
            switch (umvol){
                case "litro":{
                    //conevrtir
                    volmax=volmax/1000;
                    volmin=volmin/1000;
                }

            }
            List<FleteCapacidadModel>registrosquelocontieneVol=fleteCapacidadService.findByPesoMaxIsGreaterThanAndPesoMinIsLessThanEqual(pesomax,pesomin);
            if(registrosquelocontieneVol.size()==0){
                flagvolumenes=true;
            }else{
                //alerta de ya esiste un rango que lo contiene para vol
                Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                dialogoAlerta.setTitle("Ventana de Validacion");
                dialogoAlerta.setHeaderText(null);
                dialogoAlerta.initStyle(StageStyle.UTILITY);
                dialogoAlerta.setContentText("Registro esta contenido en un registro existente para volumen");
                Optional<ButtonType> result = dialogoAlerta.showAndWait();
                return  false;
            }
            if(flagpesos && flagvolumenes) {
                fleteCapacidadModel.setUnidadvolumen("m3");
                fleteCapacidadModel.setUnidadPeso("kg");
                fleteCapacidadModel.setVolumenMax(volmax);
                fleteCapacidadModel.setVolumenMin(volmin);
                fleteCapacidadModel.setPesoMin(pesomin);
                fleteCapacidadModel.setPesoMax(pesomax);
                fleteCapacidadModel.setCosto(spinnerCosto.getValue().doubleValue());

                return true;
            }

        }
        return false;
    }

}
