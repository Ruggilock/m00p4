package absortio.m00p4.negocio.ui.menu.devoluciones;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.DetalleFacturaParcial;
import absortio.m00p4.negocio.model.auxiliares.DetalleFacturaParcial;
import absortio.m00p4.negocio.model.auxiliares.Devolucion;
import absortio.m00p4.negocio.model.auxiliares.Producto;
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
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.*;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;

@Component
public class DevolucionesNuevoController implements Initializable {


    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    @FXML
    TextField textFieldNoDocumento;
    ObservableList<DetalleFacturaParcial> detallesOL;
    @Autowired
    MonedaService monedaService;
    @Autowired
    IgvService igvService;
    @Autowired
    LoteService loteService;
    @Autowired
    DocumentoService documentoService;
    @Autowired
    DevolucionService devolucionService;
    @Autowired
    SectorAlmacenService sectorAlmacenService;
    @Autowired
    BloqueService bloqueService;
    @Autowired
    TipoOperacionKardexService tipoOperacionKardexService;
    @Autowired
    MotivoOperacionKardexService motivoOperacionKardexService;
    @Autowired
    KardexService kardexService;

    @Autowired
    private DetalleFacturaParcialService detalleFacturaParcialService;
    @Autowired
    FacturaParcialService facturaParcialService;
    List<Integer> listaCantidades;
    @FXML
    private Pane paneNuevoVenta;
    @FXML
    private Label labelSubtotal;
    @FXML
    private Label labelImpuestos;
    @FXML
    private Label labelTotal;
    @FXML
    private TableColumn columnaDescuentoUnit;
    @FXML
    private TableColumn columnaLote;
    @FXML
    private TableColumn columnaImporte;
    @FXML
    private TableColumn columnaAcciones;
    @FXML
    private ComboBox<String> comboBoxMoneda;
    @FXML
    private TableView<DetalleFacturaParcial> tablaProductos;
    @FXML
    private TableColumn columnaCodigo;
    @FXML
    private TableColumn columnaNombre;
    @FXML
    private TableColumn columnaPrecioUnit;
    @FXML
    private TableColumn columnaCantidad;
    @FXML
    private Label labelDescuentos;
    @Autowired
    private ApplicationContext context;
    private List<Label> listaLabels;
    @Autowired
    private DetalleDespachoService detalleDespachoService;
    @Autowired
    private DetalleDevolucionService detalleDevolucionService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaLabels = new ArrayList<Label>();
        listaCantidades = new ArrayList<Integer>();
        listaLabels.clear();
        listaLabels.add(labelSubtotal);
        listaLabels.add(labelImpuestos);
        listaLabels.add(labelDescuentos);
        listaLabels.add(labelTotal);
        inicializarCombos();
        inicializarTabla();
        textFieldNoDocumento.setText("");

        textFieldNoDocumento.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                tablaProductos.getItems().clear();
                comboBoxMoneda.setValue(null);
                labelSubtotal.setText("0.0");
                labelImpuestos.setText("0.0");
                labelDescuentos.setText("0.0");
                labelTotal.setText("0.0");
            }
        });

    }

    private boolean cantidadesValidas() {
        FacturaParcialModel factura = facturaParcialService.getByNoDocumento(textFieldNoDocumento.getText());
        ArrayList<DetalleFacturaParcialModel> detallesFactura = (ArrayList) detalleFacturaParcialService.findAllDetalleFacturaParcialByIdFacturaParcial(factura.getId());
        HashMap<Integer,Integer> lotesACantidadEntregada = new HashMap<>();
        for (DetalleFacturaParcialModel detalle : detallesFactura){
            if (!lotesACantidadEntregada.containsKey(detalle.getLote()))
                lotesACantidadEntregada.put(detalle.getLote(), detalleDespachoService.getById(detalle.getIdDetalleDespacho()).getCantidadAtendida());
            else
                lotesACantidadEntregada.put(detalle.getLote(), lotesACantidadEntregada.get(detalle.getLote()) +  detalleDespachoService.getById(detalle.getIdDetalleDespacho()).getCantidadAtendida());
        }

        ArrayList<DevolucionModel> devoluciones = (ArrayList) devolucionService.findAllDevolucionByDocumento(factura.getId());
        HashMap<Integer,Integer> lotesACantidadDevuelta = new HashMap<>();
        for (DevolucionModel devolucion : devoluciones) {
            ArrayList<DetalleDevolucionModel> detallesDevolucion = (ArrayList) detalleDevolucionService.findAllDetalleDevolucionByIdDevolucion(devolucion.getId());
            for (DetalleDevolucionModel detalleDevolucionModel : detallesDevolucion){
                if (!lotesACantidadDevuelta.containsKey(detalleDevolucionModel.getIdLote().getId()))
                    lotesACantidadDevuelta.put(detalleDevolucionModel.getIdLote().getId(), detalleDevolucionModel.getCantidad());
                else
                    lotesACantidadDevuelta.put(detalleDevolucionModel.getIdLote().getId(), lotesACantidadDevuelta.get(detalleDevolucionModel.getIdLote().getId()) + detalleDevolucionModel.getCantidad());
            }
        }

        for (int i = 0; i < detallesOL.size(); i++) {
            Integer idLote = detallesOL.get(i).getLote();
            Integer cantidadEntregada = lotesACantidadEntregada.get(idLote);
            Integer cantidadYaDevuelta = lotesACantidadDevuelta.get(idLote);
            if (cantidadYaDevuelta == null) cantidadYaDevuelta = 0;
            Integer cantidadQuieroDevolver = Integer.parseInt(detallesOL.get(i).getTextfield().getText());
            if (cantidadQuieroDevolver <= 0 || cantidadQuieroDevolver > cantidadEntregada - cantidadYaDevuelta) {
                Validador.mostrarDialogError("Una de las cantidades que ingresó no es válida. ");
                return false;
            }
        }

        return true;
    }

    public void buttonAceptar(MouseEvent mouseEvent) throws IOException {
        Double subtotal = 0.0;
        Double descuentos = 0.0;

        if (textFieldNoDocumento.getText() != null) {
            //if (documentoService.getByNoDocumentoAndEstado(textFieldNoDocumento.getText().toString(), "concluido") != null) {
            if (facturaParcialService.getByNoDocumento(textFieldNoDocumento.getText().toString()) != null) {
                if (!cantidadesValidas()) return;
                MonedaModel monedaModel = monedaService.findByNombre(comboBoxMoneda.getValue());
                IgvModel igvModel = igvService.findByEstado("activo");
                DevolucionModel devolucion = new DevolucionModel();
                for (int i = 0; i < detallesOL.size(); i++) {
                    DetalleDevolucionModel detalleDevolucionModel = new DetalleDevolucionModel();
                    detalleDevolucionModel.setCantidad(Integer.parseInt(detallesOL.get(i).getTextfield().getText().toString()));
                    detalleDevolucionModel.setdU(detallesOL.get(i).getDescuentounitario());
                    LoteModel lote = loteService.findById(detallesOL.get(i).getLote());
                    detalleDevolucionModel.setIdLote(lote);
                    detalleDevolucionModel.setImporte(detallesOL.get(i).getImporte());
                    detalleDevolucionModel.setpU(detallesOL.get(i).getPreciounitario());
                    devolucion.addDetalleDevolucion(detalleDevolucionModel);
                    subtotal = subtotal + detalleDevolucionModel.getCantidad() * detalleDevolucionModel.getpU();
                    descuentos = descuentos + detalleDevolucionModel.getCantidad() * detalleDevolucionModel.getdU();
                }
                Double impuestos = subtotal * 0.18;
                Double total = subtotal + impuestos - descuentos;
                devolucion.setSubtotal(subtotal);
                devolucion.setImpuestos(impuestos);
                devolucion.setDescuentos(descuentos);
                devolucion.setTotal(total);
                devolucion.setDescripcion("Se realizo una devolucion ");
                //DocumentoModel doc = documentoService.getByNoDocumento(textFieldNoDocumento.getText().toString());
                FacturaParcialModel doc=facturaParcialService.getByNoDocumento(textFieldNoDocumento.getText().toString());
                devolucion.setDocumento(doc);
                devolucion.setFechaEmision(new Date());
                devolucionService.save(devolucion);
                devolucion.setNodevolucion("dev-" + devolucion.getId());
                devolucionService.save(devolucion);
                LOGGERAUDIT.info("El usuario "+usuarioModel.getNombres()+" registró una nueva devolucion con codigo: "+devolucion.getNodevolucion());
                muevokardex(devolucion);

                Pane paneParent = (Pane) paneNuevoVenta.getParent();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/devoluciones/devoluciones.fxml"));
                fxmlLoader.setControllerFactory(context::getBean);
                Pane pane = fxmlLoader.load();
                paneParent.getChildren().setAll(pane);

            } else {
                Validador.mostrarDialogError("El documento ingresado no es una venta");
            }
        } else {
            Validador.mostrarDialogError("Ingrese documento");
        }
    }

    @FXML
    public void buttonCancelar(MouseEvent event) throws IOException {

        Pane paneParent = (Pane) paneNuevoVenta.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/devoluciones/devoluciones.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }

    @FXML
    public void clickNuevoProducto(MouseEvent mouseEvent) throws IOException {
        //MonedaModel moneda=documentoService.getByNoDocumento(textFieldNoDocumento.getText().toString()).getMoneda();
        //comboBoxMoneda.setValue(comboBoxMoneda.getItems().get(0));
        //comboBoxMoneda.setDisable(true);
        MonedaModel moneda;
        try {
            //moneda = documentoService.getByNoDocumento(textFieldNoDocumento.getText().toString()).getMoneda();
            Integer iddocVenta=facturaParcialService.getByNoDocumento(textFieldNoDocumento.getText().toString()).getIdDocumento();
            moneda=documentoService.getById(iddocVenta).getMoneda();
        } catch (NullPointerException npe) {
            Validador.mostrarDialogError("¡No existe dicho documento!");
            return;
        }
        comboBoxMoneda.setValue(moneda.getNombre());
        comboBoxMoneda.setDisable(true);

        //DetalleFacturaParcial producto = new DetalleFacturaParcial();
        DetalleFacturaParcial producto =new DetalleFacturaParcial();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/devoluciones/agregarproducto_devolucion.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root1 = fxmlLoader.load();
        fxmlLoader.<AgregarProductoController>getController().asignarAventas(producto, detallesOL, tablaProductos, listaLabels, textFieldNoDocumento.getText().toString(), listaCantidades);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar producto");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private void inicializarCombos() {

        ArrayList<MonedaModel> monedaModels = (ArrayList) monedaService.findAllMoneda();

        for (int i = 0; i < monedaModels.size(); i++) {

            comboBoxMoneda.getItems().add(monedaModels.get(i).getNombre());

        }
        comboBoxMoneda.setDisable(true);


    }

    private void inicializarTabla() {
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigobarras"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreproducto"));
        columnaLote.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("lote"));
        columnaPrecioUnit.setCellValueFactory(new PropertyValueFactory<Producto, Double>("preciounitario"));
        columnaDescuentoUnit.setCellValueFactory(new PropertyValueFactory<Producto, Double>("descuentounitario"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<Producto, TextField>("textfield"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<Producto, Button>("buttonEliminar"));
        columnaImporte.setCellValueFactory(new PropertyValueFactory<Producto, Double>("importe"));

        detallesOL = FXCollections.observableArrayList();
        //agregarBoton();
        tablaProductos.setItems(detallesOL);
    }

    void muevokardex(DevolucionModel devolucionModel) {
        //SectorAlmacenModel sector = sectorAlmacenService.getByNombre(comboBoxSectorAlmacen.getValue().toString());
        //List<BloqueModel> bloqueslibres=bloqueService.findAllByEstado("activo");
        List<DetalleDevolucionModel> detallesdevolucion = devolucionModel.getDetallesdev();
        for (int j = 0; j < detallesdevolucion.size(); j++) {
            TipoOperacionKardexModel tipo = tipoOperacionKardexService.getByNombre("ingreso");
            MotivoOperacionKardexModel motivo = motivoOperacionKardexService.getByNombreAndTipo_id("por devolucion", tipo.getId());

            List<BloqueModel> bloqueslibres = bloqueService.findAllByEstado("activo");
            Integer cantidadporingresar = detallesdevolucion.get(j).getCantidad();
            Integer cantidadIngresada;
            Integer i = 0;
            //LoteModel loteAsociado=new LoteModel();
            LoteModel loteAsociado = detallesdevolucion.get(j).getIdLote();
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
                //loteAsociado.addBloquetoLote(bloqueutilizado, cantidadIngresada, motivo, "ingreso por devolucion");
                //loteService.reduceStockDisponible(loteAsociado.getId(), loteAsociado.getStockDisponible() + cantidadIngresada);
                loteAsociado.setStockDisponible(loteAsociado.getStockDisponible() + cantidadIngresada);
                //loteAsociado.addBloquetoLote(bloqueutilizado, cantidadIngresada, motivo, textAreaDescripcion.getText());
                KardexModel postTag = new KardexModel();
                postTag.setBloque(bloqueutilizado);
                postTag.setLote(loteAsociado);
                postTag.setCantidad(cantidadIngresada);
                postTag.setFechaoperacion(new Date());
                postTag.setMotivooperacion(motivo);
                postTag.setStockfisico(cantidadIngresada);
                postTag.setDescripcion("por devolucion del documento de venta " + devolucionModel.getDocumento().getNoDocumento());
                kardexService.save(postTag);
                cantidadporingresar = cantidadporingresar - cantidadIngresada;
                i++;
            }
            if (cantidadporingresar > 0) {
                Integer cantidadtotalingresada = detallesdevolucion.get(j).getCantidad() - cantidadporingresar;
                Validador.mostrarDialogError("Solo hubo espacio para ingresar " + cantidadtotalingresada.toString());
            }
            loteService.save(loteAsociado);
            String logMensaje = "El usuario " + usuarioModel.getNombres() + " registró una devolucion de" + loteAsociado.getProducto() + " de" + detallesdevolucion.get(j).getCantidad();
            LOGGERAUDIT.info(logMensaje);
        }
    }
}
