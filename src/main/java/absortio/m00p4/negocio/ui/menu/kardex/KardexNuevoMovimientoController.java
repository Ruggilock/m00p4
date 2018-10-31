package absortio.m00p4.negocio.ui.menu.kardex;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class KardexNuevoMovimientoController implements Initializable {

    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    ObservableList<LoteModel> OllotesIngresar;
    @FXML
    private AnchorPane paneNuevoMovimiento;
    @FXML
    private TextField textFieldCodigoLote;
    @FXML
    private TextField textFieldNombreProducto;
    @FXML
    private TextField textFieldCodigoBarras;
    @FXML
    private Spinner<Integer> spinnerCantidad;
    @FXML
    private ComboBox comboBoxTipoMovimiento;
    @FXML
    private ComboBox comboBoxMotivoMovimiento;
    @FXML
    private ComboBox comboBoxSectorAlmacen;
    @FXML
    private Label labelSectorAlmacen;
    @FXML
    private TextArea textAreaDescripcion;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private TipoOperacionKardexService tipoOperacionKardexService;
    @Autowired
    private MotivoOperacionKardexService motivoOperacionKardexService;
    @Autowired
    private LoteService loteService;
    @Autowired
    private BloqueService bloqueService;
    @Autowired
    private SectorAlmacenService sectorAlmacenService;
    @Autowired
    private KardexService kardexService;
    private KardexModel kardexModel;
    private LoteModel loteAsociado;
    private String nombreAlmacenRotos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OllotesIngresar = FXCollections.observableArrayList();

        kardexModel = new KardexModel();
        loteAsociado = new LoteModel();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0, 1);
        spinnerCantidad.setValueFactory(valueFactory);
        List<SectorAlmacenModel> sectores = sectorAlmacenService.findAllSectorAlmacen();
        List<String> nombresectores = new ArrayList<>();
        for (int i = 0; i < sectores.size(); i++) {
            if (sectores.get(i).getNombre().compareTo("rotos") != 0) nombresectores.add(sectores.get(i).getNombre());
            else nombreAlmacenRotos = sectores.get(i).getNombre();
        }
        comboBoxSectorAlmacen.getItems().setAll(nombresectores);
        cargarcombos();
    }

    void cargarcombos() {
        List<TipoOperacionKardexModel> tipo = tipoOperacionKardexService.findAllTipoOperacionKardex();
        List<String> tipos = new ArrayList<>();
        for (int i = 0; i < tipo.size(); i++) {
            tipos.add(tipo.get(i).getNombre());
        }
        ObservableList combox1 = FXCollections.observableList(tipos);
        comboBoxTipoMovimiento.setItems(combox1);
        comboBoxTipoMovimiento.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                List<MotivoOperacionKardexModel> motivo = motivoOperacionKardexService.findAllByTipooperacion_Nombre(t1.toString());
                List<String> motivos = new ArrayList<>();
                for (int i = 1; i < motivo.size(); i++) {
                    motivos.add(motivo.get(i).getNombre());
                }
                ObservableList combox2 = FXCollections.observableArrayList(motivos);
                comboBoxMotivoMovimiento.setItems(combox2);
                if (t1.toString().equals("salida")) {
                    //comboBoxSectorAlmacen.setValue("salida");
                    if (loteAsociado != null) {
                        //List<String>bloquesdistinct= kardexService.findAllAlmacenesWithLote(loteAsociado.getId());
                        //ObservableList combox1 = FXCollections.observableList(bloquesdistinct);
                        //comboBoxSectorAlmacen.setItems(combox1);
                    }

                } else {
                    comboBoxSectorAlmacen.setValue(null);
                }
            }
        });

    }

    public void buttonAceptar(MouseEvent mouseEvent) throws IOException {
        System.out.println(nombreAlmacenRotos);
        if (comboBoxTipoMovimiento.getValue() != null && comboBoxMotivoMovimiento.getValue() != null && textFieldCodigoBarras.getText() != null && textFieldCodigoLote.getText() != null && spinnerCantidad.getValue() != null) {
            KardexModel operacion = new KardexModel();
            TipoOperacionKardexModel tipo = tipoOperacionKardexService.getByNombre(comboBoxTipoMovimiento.getValue().toString());
            MotivoOperacionKardexModel motivo = motivoOperacionKardexService.getByNombreAndTipo_id(comboBoxMotivoMovimiento.getValue().toString(), tipo.getId());
            loteAsociado=loteService.findById(Integer.parseInt(textFieldCodigoLote.getText()));
            if (tipo.getNombre().equals("ingreso")) {
                if (comboBoxSectorAlmacen.getValue() != null) {
                    asignarbloques();
                    loteService.save(loteAsociado);
                    LOGGERAUDIT.info("El usuario "+usuarioModel.getNombres()+" registró un nuevo movimiento de tipo "+ tipo);
                    LOGGERAUDIT.info("del lote "+loteAsociado.getId()+" del producto "+loteAsociado.getProducto());
                    LOGGERAUDIT.info("La operacion registrada fue con motivo: "+motivo);
                    Pane paneParent = (Pane) paneNuevoMovimiento.getParent();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/kardex.fxml"));
                    fxmlLoader.setControllerFactory(context::getBean);
                    Pane pane = fxmlLoader.load();
                    paneParent.getChildren().setAll(pane);
                } else {
                    //valida que falta sector
                    System.out.println("falta sector donde se ingresara");
                    Validador.mostrarDialogError("Falta sector seleccionar el sector donde se ingresara");
                }

            } else if (tipo.getNombre().equals(("salida"))) {
                salidadebloque(motivo);
                Pane paneParent = (Pane) paneNuevoMovimiento.getParent();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/kardex.fxml"));
                fxmlLoader.setControllerFactory(context::getBean);
                Pane pane = fxmlLoader.load();
                paneParent.getChildren().setAll(pane);
                LOGGERAUDIT.info("El usuario "+usuarioModel.getNombres()+" registró un nuevo movimiento de tipo "+ tipo);
                LOGGERAUDIT.info("del lote "+loteAsociado.getId()+" del producto "+loteAsociado.getProducto());
                LOGGERAUDIT.info("La operacion registrada fue con motivo: "+motivo);

            }
        } else {
            //hacer las validaciones que deban hacerse
            //con text fields abajito de cada campo
            System.out.println("algun campo esta nulo");
            Validador.mostrarDialogError("Deben llenarse todos los campos!");
        }


    }

    void salidadebloque(MotivoOperacionKardexModel motivo) {

        //List<Integer> bloquesdistinct = kardexService.findAllDistinctforLoteByAlmacen(loteAsociado.getId(), comboBoxSectorAlmacen.getValue().toString());
        List<Integer> bloquesdistinct = kardexService.findAllDistinctforLoteByAlmacen(loteAsociado.getId(), comboBoxSectorAlmacen.getValue().toString());
        //System.out.println(bloquesdistinct.size());
        KardexModel ultimaoperacion = new KardexModel();
        Integer cantidadporsalir = spinnerCantidad.getValue().intValue();
        Integer cantidadensalida;
        if (bloquesdistinct.isEmpty()) {
            //Mensaje de salida que no hay stock en ese lote
            Validador.mostrarDialogError("No hay stock de ese lote");
        }
        for (int i = 0; i < bloquesdistinct.size(); i++) {
            if (cantidadporsalir == 0) break;
            List<KardexModel> operacionesxbloque = kardexService.findOperacion(bloquesdistinct.get(i));
            ultimaoperacion = operacionesxbloque.get(operacionesxbloque.size() - 1);
            if (ultimaoperacion.getStockfisico() == 0) {
                //Ese bloque no tiene stock
                i++;
            } else {
                if (cantidadporsalir > ultimaoperacion.getStockfisico()) {
                    cantidadensalida = ultimaoperacion.getStockfisico();
                } else {
                    cantidadensalida = cantidadporsalir;
                }
                //tengo cantidad, motivo, lote, bloque
                Integer stockfisico = ultimaoperacion.getStockfisico() - cantidadensalida;
                //tengo cantidad, motivo, lote, bloque, stockfisico
                KardexModel nuevaoperacion = new KardexModel();
                nuevaoperacion.setCantidad(cantidadensalida);
                nuevaoperacion.setFechaoperacion(new Date());
                nuevaoperacion.setMotivooperacion(motivo);
                nuevaoperacion.setLote(loteAsociado);
                nuevaoperacion.setBloque(bloqueService.getById(bloquesdistinct.get(i)));
                cantidadporsalir = cantidadporsalir - cantidadensalida;
                nuevaoperacion.setStockfisico(stockfisico);
                nuevaoperacion.setDescripcion(textAreaDescripcion.getText());
                kardexService.save(nuevaoperacion);
                if (nuevaoperacion.getStockfisico() == 0) {
                    //update al bloque que pase a estado activo
                    bloqueService.updateEstadoaActivo(nuevaoperacion.getBloque().getId());
                }
                if (nuevaoperacion.getMotivooperacion().getNombre().compareTo("por ruptura") == 0) {
                    loteService.reduceStockDisponible(loteAsociado.getId(), loteAsociado.getStockDisponible() - nuevaoperacion.getCantidad());
                    loteService.aumentaStockMerma(loteAsociado.getId(), loteAsociado.getStockMerma() + nuevaoperacion.getCantidad());
                    ingresoARotos(cantidadensalida, stockfisico);
                } else {
                    loteService.reduceStockDisponible(loteAsociado.getId(), loteAsociado.getStockDisponible() - nuevaoperacion.getCantidad());
                }
                String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "registro el nuevo movimiento de"+loteAsociado.getProducto()+" "+nuevaoperacion.getCantidad();
                LOGGERAUDIT.info(logMensaje);
            }
        }
        if (cantidadporsalir > 0) {
            Integer cantidadsacadafinal = spinnerCantidad.getValue().intValue() - cantidadporsalir;
            Validador.mostrarDialogError("No hubo stock suficiente, solo se retiro " + cantidadsacadafinal.toString());
            //System.out.println("No hubo stock suficiente, solo se retiro "+ cantidadsacadafinal.toString());
        }
    }

    void asignarbloques() {
        SectorAlmacenModel sector = sectorAlmacenService.getByNombre(comboBoxSectorAlmacen.getValue().toString());
        //List<BloqueModel> bloqueslibres=bloqueService.findAllByEstado("activo");
        List<BloqueModel> bloqueslibres = bloqueService.findAllByEstadoAndAndIdRack_IdSectorAlmacen_Id("activo", sector.getId());
        Integer cantidadporingresar = spinnerCantidad.getValue().intValue();
        Integer cantidadIngresada;
        Integer i = 0;
        TipoOperacionKardexModel tipo = tipoOperacionKardexService.getByNombre(comboBoxTipoMovimiento.getValue().toString());
        MotivoOperacionKardexModel motivo = motivoOperacionKardexService.getByNombreAndTipo_id(comboBoxMotivoMovimiento.getValue().toString(), tipo.getId());

        while (cantidadporingresar > 0) {
            //Integer pesoingresando=productoAsociado.getPeso()*cantidadintegresada;
            BloqueModel bloqueModel = bloqueslibres.get(i);
            Integer cantidadIngresadaPeso = (int) (bloqueModel.getPesoDisponible() / loteAsociado.getProducto().getPeso());
            Integer cantidadintegresadaVol = (int) (bloqueModel.getVolumenDisponible() / loteAsociado.getProducto().getVolumen());
            if (cantidadporingresar < cantidadIngresadaPeso && cantidadporingresar < cantidadintegresadaVol) {
                cantidadIngresada = cantidadporingresar;
            } else {
                if (cantidadIngresadaPeso > cantidadintegresadaVol) cantidadIngresada = cantidadintegresadaVol;
                else cantidadIngresada = cantidadIngresadaPeso;
            }
            bloqueService.updateEstadoaOcupado(bloqueModel.getId());
            BloqueModel bloqueutilizado = bloqueService.getById(bloqueModel.getId());
            loteAsociado.setStockDisponible( loteAsociado.getStockDisponible() + cantidadIngresada);
            //loteAsociado.addBloquetoLote(bloqueutilizado, cantidadIngresada, motivo, textAreaDescripcion.getText());
            KardexModel postTag = new KardexModel();
            postTag.setBloque(bloqueutilizado);
            postTag.setLote(loteAsociado);
            postTag.setCantidad(cantidadIngresada);
            postTag.setFechaoperacion(new Date());
            postTag.setMotivooperacion(motivo);
            postTag.setStockfisico(cantidadIngresada);
            postTag.setDescripcion(textAreaDescripcion.getText().toString());
            kardexService.save(postTag);
            //operaciones.add(postTag);
            //tag.getOperaciones().add(postTag);
            //loteService.reduceStockDisponible(loteAsociado.getId(), loteAsociado.getStockDisponible() + cantidadIngresada);
            cantidadporingresar = cantidadporingresar - cantidadIngresada;
            i++;
            String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "registro ingreso de "+loteAsociado.getProducto()+" "+cantidadIngresada;
            LOGGERAUDIT.info(logMensaje);
        }
        if (cantidadporingresar > 0) {
            Integer cantidadtotalingresada = spinnerCantidad.getValue().intValue() - cantidadporingresar;
            Validador.mostrarDialogError("Solo hubo espacio para ingresar " + cantidadtotalingresada.toString());
        }
    }

    public void buttonCancelar(MouseEvent mouseEvent) throws IOException {
        Pane paneParent = (Pane) paneNuevoMovimiento.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/kardex.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    public void buttonBuscar(MouseEvent mouseEvent) throws IOException {

        OllotesIngresar.clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/kardex/listaLotesAlmacen.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root1 = fxmlLoader.load();
        fxmlLoader.<listaLotesAlmacen>getController().recibirDato(OllotesIngresar,textFieldNombreProducto,textFieldCodigoBarras,textFieldCodigoLote, comboBoxSectorAlmacen, comboBoxTipoMovimiento);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar producto");
        stage.setScene(new Scene(root1));
        stage.show();


    }


    void ingresoARotos(Integer cantidadensalida, Integer stockfisico) {
        TipoOperacionKardexModel tipo = tipoOperacionKardexService.getByNombre("ingreso");
        MotivoOperacionKardexModel motivo = motivoOperacionKardexService.getByNombreAndTipo_id("por ruptura", tipo.getId());
        KardexModel nuevaoperacion = new KardexModel();
        nuevaoperacion.setCantidad(cantidadensalida);
        nuevaoperacion.setFechaoperacion(new Date());
        nuevaoperacion.setMotivooperacion(motivo);
        nuevaoperacion.setLote(loteAsociado);
        nuevaoperacion.setBloque(bloqueService.findAllByIdRack_IdSectorAlmacen_Nombre(nombreAlmacenRotos).get(0));
        nuevaoperacion.setStockfisico(stockfisico);
        nuevaoperacion.setDescripcion("");

        kardexService.save(nuevaoperacion);
    }
}
