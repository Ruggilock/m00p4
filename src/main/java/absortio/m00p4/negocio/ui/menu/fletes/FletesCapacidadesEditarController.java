package absortio.m00p4.negocio.ui.menu.fletes;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.FleteCapacidad;
import absortio.m00p4.negocio.model.auxiliares.FleteDistrito;
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
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class FletesCapacidadesEditarController implements Initializable{

    @FXML
    private AnchorPane paneEditarFleteCapacidad;

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
        fleteCapacidadModel=new FleteCapacidadModel();
        //cargar combos
        comboUnidvol.getItems().addAll(SystemSingleton.getInstance().getUnidadesvol());
        comboUnipeso.getItems().addAll(SystemSingleton.getInstance().getUnidadespeso());
        //cargarData();

    }

    @FXML
    public void cargarData(FleteCapacidad seleccion) {
        //fleteCapacidadModel=fleteCapacidadService.100000000000000000000000000
        fleteCapacidadModel=fleteCapacidadService.findAllByPesoMaxAndPesoMinAndVolumenMaxAndVolumenMin(seleccion.getPesomax(), seleccion.getPesomin(), seleccion.getVolmax(), seleccion.getVolmin()).get(0);
        comboUnipeso.setValue(seleccion.getUnidpeso());
        comboUnidvol.setValue(seleccion.getUnidvol());
        SpinnerValueFactory<Double> valueFactoryCosto =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, seleccion.getCosto(), 0.5 );
        spinnerCosto.setValueFactory ( valueFactoryCosto );
        SpinnerValueFactory<Double> valueFactoryPesomin =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, seleccion.getPesomin(), 0.5 );
        spinnerPesomin.setValueFactory ( valueFactoryPesomin );
        SpinnerValueFactory<Double> valueFactoryPesomax =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, seleccion.getPesomax(), 0.5 );
        spinnerPesomax.setValueFactory ( valueFactoryPesomax );
        SpinnerValueFactory<Double> valueFactoryVolmin =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, seleccion.getVolmin(), 0.5 );
        spinnerVolmin.setValueFactory ( valueFactoryVolmin );
        SpinnerValueFactory<Double> valueFactoryVolmax =
                new SpinnerValueFactory.DoubleSpinnerValueFactory ( 0.0, 10000000000000000000000000000.0, seleccion.getVolmax(), 0.5 );
        spinnerVolmax.setValueFactory ( valueFactoryVolmax );


    }

    public void buttonAceptar(MouseEvent mouseEvent) throws IOException {
        //Primero valido que no se inserten repetidos

        if(validacion()){

            //Aca se graba el nuevo distrito y todos los insert a la tabla cruce
            fleteCapacidadService.updateTodo(fleteCapacidadModel.getId(),comboUnipeso.getValue().toString(), spinnerPesomax.getValue().doubleValue(), spinnerPesomin.getValue().doubleValue(),comboUnidvol.getValue().toString(),spinnerVolmax.getValue().doubleValue(),spinnerVolmin.getValue().doubleValue(), spinnerCosto.getValue().doubleValue());
            List<FleteModel> lista=fleteCapacidadModel.getFletes();
            for(int i=0;i<lista.size();i++){
                lista.get(i).setCosto(spinnerCosto.getValue() * 0.5 + lista.get(i).getDistrito().getCosto()*0.5);
            }
            List<FleteModel> listafletes=fleteModelService.findAllFlete();
            List<FleteModel> deesacapacidad=fleteModelService.findByCapacidad_Id(fleteCapacidadModel.getId());
            for(int i=0;i<deesacapacidad.size();i++){
                Double costonuevo=deesacapacidad.get(i).getDistrito().getCosto()*0.5+deesacapacidad.get(i).getCapacidad().getCosto()*0.5;
                fleteModelService.updateCostoFletes(costonuevo, deesacapacidad.get(i).getCapacidad(), deesacapacidad.get(i).getDistrito());
            }

            Pane paneParent = (Pane) paneEditarFleteCapacidad.getParent();
            FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/fletes/fletescapacidades.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Pane pane =fxmlLoader.load();
            paneParent.getChildren().setAll(pane);
        }
        else {
//            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
//            dialogoAlerta.setTitle("Ventana de Validacion");
//            dialogoAlerta.setHeaderText(null);
//            dialogoAlerta.initStyle(StageStyle.UTILITY);
//            dialogoAlerta.setContentText("El distrito seleccionado ya se encuentra registrado");
//            Optional<ButtonType> result = dialogoAlerta.showAndWait();
        }
    }

    @FXML
    public void buttonCancelar(MouseEvent mouseEvent) throws IOException {
        Pane paneParent = (Pane) paneEditarFleteCapacidad.getParent();
        FXMLLoader fxmlLoader =new FXMLLoader(getClass().getResource("/fxml/menu/fletes/fletescapacidades.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    Boolean validacion(){
        if(spinnerVolmin.getValue() == null || spinnerVolmax.getValue()==null||spinnerPesomin.getValue()==null||spinnerPesomax.getValue()==null||comboUnidvol.getValue()==null||comboUnipeso.getValue()==null){
            //Generar alerta que todos los datos deben estar llenos
            Validador.mostrarDialogError("Deben llenarse todos los campos!");
            return false;
        }
        else{
            DecimalFormat formateador = new DecimalFormat("#.##");
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
                Validador.mostrarDialogError("Rangos incongruentes en peso");
                return false;
            }
            //en bd se guarda en kg y m3
            switch (umpeso){
                case "gr":{
                    //convertir a kg
                    pesomin=pesomin/1000;
                    pesomax=pesomax/1000;
                }

            }
            List<FleteCapacidadModel>registrosquelocontienePeso=fleteCapacidadService.findByPesoMaxIsGreaterThanAndPesoMinIsLessThanEqual(pesomax,pesomin);
            registrosquelocontienePeso.remove(fleteCapacidadModel);
            List<FleteCapacidadModel> fleteCapacidadModels=fleteCapacidadService.findAllByPesoMaxAndPesoMinAndVolumenMaxAndVolumenMin(spinnerPesomax.getValue().doubleValue(), spinnerPesomin.getValue().doubleValue(), spinnerVolmax.getValue().doubleValue(), spinnerVolmin.getValue().doubleValue());
            fleteCapacidadModels.remove(fleteCapacidadModel);

            if(registrosquelocontienePeso.size()==0 ){
                flagpesos=true;
            }
            else{
                //alerta de ya esiste un rango que lo contiene para peso
                Validador.mostrarDialogError("Rango de peso ya existe o se superpone a un rango existente");
            }
            switch (umvol){
                case "litro":{
                    //conevrtir
                    volmax=volmax/1000;
                    volmin=volmin/1000;
                }

            }
            List<FleteCapacidadModel>registrosquelocontieneVol=fleteCapacidadService.findByPesoMaxIsGreaterThanAndPesoMinIsLessThanEqual(pesomax,pesomin);
            registrosquelocontieneVol.remove(fleteCapacidadModel);
            List<FleteCapacidadModel> fleteCapacidadModels2=fleteCapacidadService.findAllByPesoMaxAndPesoMinAndVolumenMaxAndVolumenMin(spinnerPesomax.getValue().doubleValue(), spinnerPesomin.getValue().doubleValue(), spinnerVolmax.getValue().doubleValue(), spinnerVolmin.getValue().doubleValue());
            if(registrosquelocontieneVol.size()==0 ){
                flagvolumenes=true;
            }else{
                //alerta de ya esiste un rango que lo contiene para vol
                Validador.mostrarDialogError("Rango de volumen ya existe o se superpone a un rango existente");
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
