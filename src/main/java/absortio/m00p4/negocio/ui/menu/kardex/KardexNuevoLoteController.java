package absortio.m00p4.negocio.ui.menu.kardex;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class KardexNuevoLoteController implements Initializable {

    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    @FXML
    Pane paneNuevoLote;
    KardexModel kardexModel;
    ObservableList<ProductoModel> productosAlmacen;
    @FXML
    private TextField textFieldIdProducto;
    @FXML
    private TextField textFieldCodigoBarrasProducto;
    @FXML
    private TextField textFieldNombreProducto;
    @FXML
    private DatePicker datePickerFechaAdquisicion;
    @FXML
    private DatePicker datePickerFechaVentcimiento;
    @FXML
    private TextArea textAreaObservaciones;
    @FXML
    private Spinner<Integer> spinnerCantidad;
    @FXML
    private ComboBox comboBoxSectorAlmacen;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private BloqueService bloqueService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private LoteService loteService;
    @Autowired
    private SectorAlmacenService sectorAlmacenService;
    @Autowired
    private MotivoOperacionKardexService motivoOperacionKardexService;
    private ProductoModel productoasociado;
    private LoteModel lotenuevo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productosAlmacen = FXCollections.observableArrayList();
        kardexModel = new KardexModel();
        lotenuevo = new LoteModel();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 0, 1);
        spinnerCantidad.setValueFactory(valueFactory);
        List<SectorAlmacenModel> sectores = sectorAlmacenService.findAllSectorAlmacen();
        List<String> nombresectores = new ArrayList<>();
        for (int i = 0; i < sectores.size(); i++) {
            if (sectores.get(i).getNombre() != "rotos") nombresectores.add(sectores.get(i).getNombre());
        }
        comboBoxSectorAlmacen.getItems().setAll(nombresectores);


    }


    public void buttonCancelar(MouseEvent mouseEvent) throws IOException {
        Pane paneParent = (Pane) paneNuevoLote.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/kardex.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    public void buttonAnadir(MouseEvent mouseEvent) throws IOException {
        if (textFieldCodigoBarrasProducto.getText() != null && textFieldNombreProducto.getText() != null && spinnerCantidad.getValue() != null && datePickerFechaAdquisicion.getValue() != null && datePickerFechaVentcimiento.getValue() != null && productosAlmacen.get(0) != null && comboBoxSectorAlmacen.getValue() != null) {
            asignarbloquesnuevolote();
            loteService.save(lotenuevo);
            productosAlmacen.get(0).addLote(lotenuevo);
            productoService.save(productosAlmacen.get(0));
            String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "registro el nuevo lote "+lotenuevo.getProducto()+" "+lotenuevo.getStockAdquirido();
            LOGGERAUDIT.info(logMensaje);
            Pane paneParent = (Pane) paneNuevoLote.getParent();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/kardex.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Pane pane = fxmlLoader.load();
            paneParent.getChildren().setAll(pane);
        } else {
            //hacer las validaciones que deban hacerse
            //con text fields abajito de cada campo
            System.out.println("algun campo esta nulo");
            Validador.mostrarDialogError("¡Deben llenarse todos los campos!");
        }
    }

    public void buttonBuscar(MouseEvent mouseEvent) throws IOException {
        String regex = "\\d+";
/*Add button List*/


        productosAlmacen.clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/listaProductosAlmacen.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root1 = fxmlLoader.load();
        fxmlLoader.<listaProductosAlmacen>getController().recibirDato(productosAlmacen, textFieldNombreProducto, textFieldCodigoBarrasProducto);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar producto");
        stage.setScene(new Scene(root1));
        stage.show();

/*
        if(textFieldCodigoBarrasProducto.getText()!=null){
            productosAlmacen.get(0)=null;
            if ((productosAlmacen.get(0)=productoService.obtenerProducto(textFieldCodigoBarrasProducto.getText()))!=null){
                textFieldNombreProducto.setText(productosAlmacen.get(0).getNombre());
            } else{
                //validacion que no existe el producto con ese codigo
                System.out.println("Producto no existe");
                Validador.mostrarDialogError("Producto no existe");
            }
        }else {
            //hacer validacion que no es numero
            System.out.println("Ingrese codigo de barras");
            Validador.mostrarDialogError("Ingrese codigo de barras");
        }
        */
    }


    void asignarbloquesnuevolote() {
        SectorAlmacenModel sector = sectorAlmacenService.getByNombre(comboBoxSectorAlmacen.getValue().toString());
        //List<BloqueModel> bloqueslibres=bloqueService.findAllByEstado("activo");
        List<BloqueModel> bloqueslibres = bloqueService.findAllByEstadoAndAndIdRack_IdSectorAlmacen_Id("activo", sector.getId());
        Integer cantidadporingresar = spinnerCantidad.getValue().intValue();
        //Date fecha=Date.valueOf(datePickerFechaAdquisicion.getValue());
        //System.out.println(fecha.toString());
        lotenuevo.setFechaAdquisicion(Date.valueOf(datePickerFechaAdquisicion.getValue()));
        lotenuevo.setFechaVencimiento(Date.valueOf(datePickerFechaVentcimiento.getValue()));
        lotenuevo.setStockAdquirido(spinnerCantidad.getValue().intValue());
        lotenuevo.setStockComprometido(0);
        lotenuevo.setStockMerma(0);
        lotenuevo.setStockDisponible(spinnerCantidad.getValue().intValue());
        lotenuevo.setObservacion(textAreaObservaciones.getText());
        Integer cantidadIngresada;
        Integer i = 0;
        MotivoOperacionKardexModel motivo = motivoOperacionKardexService.getByNombre("por nuevo lote");
        while (cantidadporingresar > 0) {
            //Integer pesoingresando=productosAlmacen.get(0).getPeso()*cantidadintegresada;
            if (i >= bloqueslibres.size()) break;
            BloqueModel bloqueModel = bloqueslibres.get(i);
            Integer cantidadIngresadaPeso = (int) (bloqueModel.getPesoDisponible() / productosAlmacen.get(0).getPeso());
            Integer cantidadintegresadaVol = (int) (bloqueModel.getVolumenDisponible() / productosAlmacen.get(0).getVolumen());
            if (cantidadporingresar < cantidadIngresadaPeso && cantidadporingresar < cantidadintegresadaVol) {
                cantidadIngresada = cantidadporingresar;
            } else {
                if (cantidadIngresadaPeso > cantidadintegresadaVol) cantidadIngresada = cantidadintegresadaVol;
                else cantidadIngresada = cantidadIngresadaPeso;
            }
            bloqueService.updateEstadoaOcupado(bloqueModel.getId());
            BloqueModel bloqueutilizado = bloqueService.getById(bloqueModel.getId());

            lotenuevo.addBloquetoNuevoLote(bloqueutilizado, cantidadIngresada, motivo);
            cantidadporingresar = cantidadporingresar - cantidadIngresada;
            i++;
        }

        if (cantidadporingresar > 0) {
            Integer cantidadtotalingresada = spinnerCantidad.getValue().intValue() - cantidadporingresar;
            Validador.mostrarDialogError("Solo hubo espacio para ingresar " + cantidadtotalingresada.toString());
        }
    }


}
