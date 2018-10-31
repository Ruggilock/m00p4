package absortio.m00p4.negocio.ui.menu.ventas;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.auxiliares.Cliente;
import absortio.m00p4.negocio.model.auxiliares.Entrega;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import absortio.m00p4.negocio.model.auxiliares.Venta;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import absortio.m00p4.negocio.ui.menu.descuentos.ProductoSeleccionarController;
import absortio.m00p4.negocio.ui.menu.entregas.ProgramarEntregasController;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
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
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

@Component
public class VentaEditarController implements Initializable {
    @FXML
    TextField textFieldNoDocumento;
    @FXML
    TextField textFieldCliente;
    @FXML
    TextField textFieldTelefono;
    @Autowired
    TipoDocumentoService tipoDocumentoService;
    @Autowired
    DocumentoService documentoService;
    @Autowired
    DetalleDocumentoService detalleDocumentoService;
    @Autowired
    LoteService loteService;
    @Autowired
    MonedaService monedaService;
    @Autowired
    ProductoService productoService;
    @Autowired
    DetalleEntregaService detalleEntregaService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    IgvService igvService;
    @Autowired
    CondicionesComercialesService condicionService;
    ObservableList<Producto> productosOL;
    ObservableList<Cliente> clientes;
    ObservableList<Entrega> entregas;
    ClienteModel cliente;
    ObservableList<FleteModel> fleteModels;
    FleteModel flete;
    DocumentoModel documentoModel;
    boolean cambio;
    @FXML
    private Pane paneEditarVenta;
    @FXML
    private Label labelSubtotal;
    @FXML
    private Label labelImpuestos;
    @FXML
    private Label labelFlete;
    @FXML
    private Label labelDescuentos;
    @FXML
    private Label labelTotal;
    @FXML
    private TableColumn columnaDescuento;
    @FXML
    private TableColumn columnaUnidad;
    @FXML
    private TableColumn columnaImporte;
    @FXML
    private TableColumn columnaAcciones;
    @FXML
    private ComboBox<String> comboBoxTipo;
    @FXML
    private ComboBox<String> comboBoxMoneda;
    @FXML
    private CheckBox checkBoxConfirmar;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn columnaCodigo;
    @FXML
    private TableColumn columnaProductos;
    @FXML
    private TableColumn columnaPrecio;
    @FXML
    private TableColumn columnaCantidad;
    @Autowired
    private ApplicationContext context;
    private ArrayList<DetalleDocumentoModel> detallesGenerados = new ArrayList<DetalleDocumentoModel>();
    private ArrayList<DetalleEntregaModel> detallesEntregasListos = new ArrayList<DetalleEntregaModel>();
    private List<Label> listaLabels = new ArrayList<Label>();

    List<Producto> listaProductos;
    int asignoRegalo,cantP,codigoP;
    List<Integer> listaCantidades;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaCantidades = new ArrayList<Integer>();
        listaLabels.add(labelSubtotal);
        listaLabels.add(labelImpuestos);
        listaLabels.add(labelFlete);
        listaLabels.add(labelDescuentos);
        listaLabels.add(labelTotal);
        fleteModels = FXCollections.observableArrayList();
        flete = new FleteModel();
        entregas = FXCollections.observableArrayList();
        clientes = FXCollections.observableArrayList();
        inicializarTablaDescuentos();
        inicializarCombos();
        textFieldNoDocumento.setDisable(true);
        textFieldCliente.setDisable(true);

        ArrayList<String> documentos = new ArrayList<>();
        for (int i = 0; i < clientes.size(); i++) {
            documentos.add(clientes.get(i).getDocumentoIdentidad());
        }
        ObservableList<String> olDocumentos = FXCollections.observableArrayList(documentos);
        TextFields.bindAutoCompletion(textFieldNoDocumento, olDocumentos);
        textFieldNoDocumento.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                cliente = clienteService.getByDocumentoIdentidad(textFieldNoDocumento.getText());
                if (cliente != null) {
                    textFieldCliente.setText(cliente.getNombres());
                    textFieldTelefono.setText(cliente.getTelefono());
                } else {
                    textFieldCliente.setText("");
                    textFieldTelefono.setText("");
                }
            }
        });
    }

    private void inicializarCombos() {
        ArrayList<TipoDocumentoModel> tipoModels = (ArrayList) tipoDocumentoService.findAllTipoDocumento();

        for (int i = 0; i < tipoModels.size(); i++) {
            comboBoxTipo.getItems().add(tipoModels.get(i).getNombre());
        }
        ArrayList<MonedaModel> monedaModels = (ArrayList) monedaService.findAllMoneda();
        for (int i = 0; i < monedaModels.size(); i++) {
            comboBoxMoneda.getItems().add(monedaModels.get(i).getNombre());
        }

    }

    @FXML
    void clickNuevoProducto(MouseEvent event) throws IOException {
        cambio = true;
        Producto producto = new Producto();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/productosSeleccionar.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root1 = fxmlLoader.load();
        fxmlLoader.<ProductoSeleccionarController>getController().asignarAeditar(producto, productosOL, tablaProductos);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar producto");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private void inicializarTablaDescuentos() {
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigo"));
        columnaProductos.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombre"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precio"));
        columnaUnidad.setCellValueFactory(new PropertyValueFactory<Producto, String>("unidadVenta"));
        columnaDescuento.setCellValueFactory(new PropertyValueFactory<Producto, Double>("descuento"));
        columnaImporte.setCellValueFactory(new PropertyValueFactory<Producto, Double>("importe"));
        columnaImporte.setEditable(true);
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<Producto, ComboBox>("comboBox"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<Producto, Button>("buttonEliminar"));

        productosOL = FXCollections.observableArrayList();
        tablaProductos.setItems(productosOL);
    }

    void calcularlotes() {
        // TODO este debe recalcular los lotes y editar los detalles documento que ya estan en detalles generados
        // y tb agregar las nuevas lienas de detalle
        int cantPed, j;
        for (int i = 0; i < productosOL.size(); i++) {

            List<LoteModel> listaLotes = loteService.hallarLotesById(
                    productoService.hallarIdByCodigoBarras(productosOL.get(i).getCodigo()));

            cantPed = Integer.valueOf(productosOL.get(i).getComboBox().getValue());

            j = 0;
            while (cantPed > 0) {
                if (cantPed >= listaLotes.get(j).getStockDisponible()) {
                    cantPed = cantPed - listaLotes.get(j).getStockDisponible();

                    DetalleDocumentoModel detalle = new DetalleDocumentoModel();
                    detalle.setCantidad(listaLotes.get(j).getStockDisponible());
                    detalle.setpU(productosOL.get(i).getPrecio());
                    //detalle.setdU(productosOL.get(i).getDescuento());
                    detalle.setImporte(detalle.getpU() * detalle.getCantidad());

                    detalle.setIdLote(listaLotes.get(j));
                    detallesGenerados.add(detalle);
                } else {
                    DetalleDocumentoModel detalle = new DetalleDocumentoModel();
                    detalle.setCantidad(cantPed);
                    cantPed = 0;// se debería actualizar base de datos
                    detalle.setpU(productosOL.get(i).getPrecio());
                    //detalle.setdU(productosOL.get(i).getDescuento());
                    detalle.setImporte(detalle.getpU() * detalle.getCantidad());

                    detalle.setIdLote(listaLotes.get(j));
                    detallesGenerados.add(detalle);
                }
                j++;
            }
        }
    }

    @FXML
    void cargarData(Venta venta) {
        documentoModel = documentoService.getByNoDocumento(venta.getNoDocumento());
        textFieldNoDocumento.setText(documentoModel.getNoDocumento());
        cliente = documentoModel.getCliente();
        textFieldCliente.setText(cliente.getNombres());
        textFieldTelefono.setText(cliente.getTelefono());
        comboBoxMoneda.setValue(documentoModel.getMoneda().getNombre());
        comboBoxTipo.setValue(documentoModel.getTipodocumento().getNombre());
        labelDescuentos.setText(String.valueOf(documentoModel.getDescuentoTotal()));
        Double impuesto = documentoModel.getIgv().getValor() * documentoModel.getMontoTotal();
        labelImpuestos.setText(String.valueOf(impuesto));
        flete = documentoModel.getFlete();
        fleteModels.add(flete);
        labelFlete.setText(String.valueOf(flete.getCosto()));
        labelSubtotal.setText(String.valueOf(documentoModel.getSubtotal()));

        labelTotal.setText(String.valueOf(documentoModel.getMontoTotal()));
        //detallesGenerados= documentoModel.getDetalleDocumentos();

        Producto productoa = new Producto();
        productoa.setN(1);
        productoa.setNombre(documentoModel.getDetalleDocumentos().get(0).getIdLote().getProducto().getNombre());
        productoa.setCategoria(documentoModel.getDetalleDocumentos().get(0).getIdLote().getProducto().getCategoriaProducto().getNombre());
        productoa.setUnidadVenta(documentoModel.getDetalleDocumentos().get(0).getIdLote().getProducto().getUnidadVenta());
        productoa.setCodigo(documentoModel.getDetalleDocumentos().get(0).getIdLote().getProducto().getCodigoBarras());
        productoa.setCantidadMax(productoService.hallarStockDisponibleById(productoService.hallarIdByCodigoBarras(productoa.getCodigo())));
        productoa.setImporte(documentoModel.getDetalleDocumentos().get(0).getImporte());
        productoa.setPrecio(documentoModel.getDetalleDocumentos().get(0).getpU());
        //producto.getComboBox().setValue(detalleD.getCantidad().toString());
        productosOL.add(productoa);
        for (int i =1; i < documentoModel.getDetalleDocumentos().size(); i++) {
            DetalleDocumentoModel detalleD = documentoModel.getDetalleDocumentos().get(i);
            ProductoModel productoModel = productoService.obtenerProducto(detalleD.getIdLote().getProducto().getCodigoBarras());
            Producto producto = new Producto();
            producto.setN(i + 1);
            producto.setNombre(productoModel.getNombre());
            producto.setCategoria(productoModel.getCategoriaProducto().getNombre());
            producto.setUnidadVenta(productoModel.getUnidadVenta());
            producto.setCodigo(productoModel.getCodigoBarras());
            producto.setCantidadMax(productoService.hallarStockDisponibleById(productoService.hallarIdByCodigoBarras(producto.getCodigo())));


            producto.getComboBox().setValue(detalleD.getCantidad().toString());
            productosOL.add(producto);
            //List<DetalleEntregaModel> entregaModels = detalleEntregaService.findAllByIdDetalleDocumento(detalleD);
            for (int j = 0; j < detalleD.getDetalleEntregas().size(); j++) {
                Entrega entrega = new Entrega();
                entrega.setNombre(producto.getNombre());
                entrega.setCodigo(producto.getCodigo());
                entrega.setFechaEntrega(detalleD.getDetalleEntregas().get(i).getFechaEntrega());
                entrega.setN(producto.getN());
                entrega.setCantidadEntrega(detalleD.getDetalleEntregas().get(i).getCantidadEntregar());
                entrega.setCantPedida(producto.getCantP());
                entrega.setDepartamento(documentoModel.getDepartamento());
                entrega.setProvincia(documentoModel.getProvincia());
                entrega.setDistrito(documentoModel.getDistrito());
                entrega.setDireccion(documentoModel.getDireccion());
                entregas.add(entrega);
            }
        }
        tablaProductos.setItems(productosOL);
    }


    private void actualizarListaLabels() {
        Double acum = 0.0;
        Double descuentos = 0.0;
        for (int i = 0; i < productosOL.size(); i++) {
            acum += productosOL.get(i).getImporte();
            descuentos += productosOL.get(i).getDescuento();
        }

        listaLabels.get(0).setText(String.valueOf(Math.rint(acum * 100) / 100));
        listaLabels.get(1).setText(String.valueOf(Math.rint(acum * 0.18 * 100) / 100));
        listaLabels.get(3).setText(String.valueOf(Math.rint(descuentos * 100) / 100));
        listaLabels.get(4).setText(String.valueOf(Math.rint((acum * 1.18 - descuentos) * 100) / 100));
    }

    private Double calcularDescuento(String codigo, int cantidad, Double precio) {
        ArrayList<CondicionesComercialesModel> condicionModels = (ArrayList) condicionService.findAllCondicion();
        Double desc = 0.0;
        int indicador1 = 0, indicador2 = 0, indicador3 = 0, indicador4 = 0, id, cant1, cant2;
        String categoria;
        for (int i = 0; i < condicionModels.size(); i++) {
            if (condicionModels.get(i).getTipo().compareTo("cantidad*cantidad") == 0) {//indicador1,2x1
                id = productoService.hallarIdByCodigoBarras(codigo);
                if (id == condicionModels.get(i).getCodigo1() && indicador1 == 0) {
                    indicador1 = 1;
                    cant1 = condicionModels.get(i).getCantidad1();
                    cant2 = condicionModels.get(i).getCantidad2();
                    desc += (cant1 - cant2) * (cantidad / cant1) * precio;
                }
            } else if (condicionModels.get(i).getTipo().compareTo("cantidad*gratis") == 0) {//indicador2,100x1
                id = productoService.hallarIdByCodigoBarras(codigo);
                if (id == condicionModels.get(i).getCodigo1() && indicador2 == 0) {
                    cant1 = cantidad / condicionModels.get(i).getCantidad1();
                    if (cant1 >= 0) {
                        indicador2 = 1;
                        codigoP = condicionModels.get(i).getCodigo2();
                        cantP = cant1 * condicionModels.get(i).getCantidad2();
                    }
                }
            } else if (condicionModels.get(i).getTipo().compareTo("porcentaje*producto") == 0 && indicador3 == 0) {//indicador3
                id = productoService.hallarIdByCodigoBarras(codigo);
                if (id == condicionModels.get(i).getCodigo1() && indicador3 == 0) {
                    indicador3 = 1;
                    desc += cantidad * precio * condicionModels.get(i).getPorcentaje() / 100;
                }
            } else if (condicionModels.get(i).getTipo().compareTo("porcentaje*categoria") == 0 && indicador4 == 0) {//indicador4
                categoria = productoService.hallarNombreCategoria(codigo);
                if (categoria.compareTo(condicionModels.get(i).getCategoria()) == 0) {
                    indicador4 = 1;
                    desc += cantidad * precio * condicionModels.get(i).getPorcentaje();
                }
            }
            if (indicador3 == 1 && indicador4 == 1) break;
            if (indicador1 == 1) break;
            if (indicador2 == 1) break;

        }
        //descuento = desc;

        return desc;
    }

    @FXML
    private void clickProgramarEntregas() throws IOException, ParseException {
        if (cambio) {
            entregas.clear();
            for (int i = 0; i < productosOL.size(); i++) {
                productosOL.get(i).setCantP(Integer.parseInt(productosOL.get(i).getComboBox().getValue()));
                Entrega entrega = new Entrega();
                entrega.setN(productosOL.get(i).getN());
                entrega.setCodigo(productosOL.get(i).getCodigo());
                entrega.setNombre(productosOL.get(i).getNombre());
                entrega.setCantPedida(productosOL.get(i).getCantP());
                entregas.add(entrega);
            }
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/entregas/programarEntregas.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent pane = fxmlLoader.load();
        fxmlLoader.<ProgramarEntregasController>getController().asignarProductos(entregas, fleteModels, labelFlete, labelTotal,comboBoxMoneda.getValue());
        Stage stage = new Stage();
        stage.setTitle("Programar entregas");
        stage.setScene(new Scene(pane));
        stage.show();
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {

        Pane paneParent = (Pane) paneEditarVenta.getParent();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/ventas/ventas.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneParent.getChildren().setAll(pane);
    }


    @FXML
    private void buttonAceptar(MouseEvent event) throws IOException {
        calcularlotes();
        TipoDocumentoModel tipoDocumentoModel = tipoDocumentoService.getTipodocumento(comboBoxTipo.getValue());
        MonedaModel monedaModel = monedaService.findByNombre(comboBoxMoneda.getValue());
        IgvModel igvModel = igvService.findByEstado("activo");
        if (cliente != null && monedaModel != null && tipoDocumentoModel != null && igvModel != null && (detallesGenerados.size() > 0)) {

            documentoModel.setDescuentoTotal(Double.valueOf(labelDescuentos.getText()));
            documentoModel.setSubtotal(Double.valueOf(labelSubtotal.getText()));
            documentoModel.setMontoTotal(Double.valueOf(labelTotal.getText()));
            documentoModel.setFleteTotal(Double.valueOf(labelFlete.getText()));
            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
            documentoModel.setUsuaio(usuarioModel);
            documentoModel.setDepartamento(entregas.get(0).getDepartamento());
            documentoModel.setProvincia(entregas.get(0).getProvincia());
            documentoModel.setDistrito(entregas.get(0).getDistrito());
            documentoModel.setDireccion(entregas.get(0).getDireccion());
            documentoModel.setFechaEmision(new Date());
            documentoModel.setTipodocumento(tipoDocumentoModel);
            documentoModel.setMoneda(monedaModel);
            documentoModel.setIgv(igvModel);
            documentoModel.setFlete(fleteModels.get(fleteModels.size() - 1));
            guardarEntregas(detallesGenerados, entregas);

            if (checkBoxConfirmar.isSelected()) {
                documentoService.setNoDoc(documentoModel.getId(), tipoDocumentoModel.getNombre());
                documentoModel.setEstado("por atender");
                if(!comprometerStock()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No se pudo realizar la venta");
                    alert.setHeaderText(null);
                    alert.setContentText("No hay stock disponible");
                    alert.showAndWait();
                }
            }
            documentoService.save(documentoModel);
            String logMensaje="El usuario "+ UsuarioSingleton.getInstance().getUsuarioModel().getNombres()+ "modifico el documento";
            LOGGERAUDIT.info(logMensaje);

            Pane paneParent = (Pane) paneEditarVenta.getParent();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/ventas/ventas.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Pane pane = fxmlLoader.load();
            paneParent.getChildren().setAll(pane);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information no ingresada");
            alert.setHeaderText(null);
            alert.setContentText("Por favor ingrese la informacion requerida");
            alert.showAndWait();

        }
    }

    private boolean comprometerStock() {
        for (DetalleDocumentoModel detalle : detallesGenerados
                ) {
            LoteModel lote = loteService.findById(detalle.getIdLote().getId());
            if (lote.getStockDisponible()>detalle.getCantidad()) {
                lote.setStockComprometido(lote.getStockComprometido() + detalle.getCantidad());
                lote.setStockDisponible(lote.getStockAdquirido() - lote.getStockComprometido() - lote.getStockMerma());
            }   else return false;
        }
        for (DetalleEntregaModel detalleEnt : detallesEntregasListos
                ) {
            detalleEnt.setEstadoEntrega("por atender");
        }
        return true;
    }

    private void guardarEntregas(ArrayList<DetalleDocumentoModel> detallesDocumentoActualizados, ObservableList<Entrega> entregas) {
        for (DetalleDocumentoModel detalle : detallesDocumentoActualizados) {
            String nombreProducto = detalle.getIdLote().getProducto().getNombre();
            Integer cantidadPedida = detalle.getCantidad();
            for (Entrega e : entregas) {
                if (Objects.equals(e.getNombre(), nombreProducto)) {
                    if (e.getCantidadEntrega() <= cantidadPedida) {
                        DetalleEntregaModel entregaModel = new DetalleEntregaModel();
                        entregaModel.setIdDetalleDocumento(detalle);
                        entregaModel.setCantidadtotal(detalle.getCantidad());
                        entregaModel.setCantidadEntregar(e.getCantidadEntrega());
                        entregaModel.setEstado("activo");
                        entregaModel.setFechaEntrega(e.getFechaEntrega());
                        entregaModel.setEstadoEntrega("proforma");
                        //detalleEntregaService.save(entregaModel);
                        detalle.addDetalleEntrega(entregaModel);
                        cantidadPedida = cantidadPedida - e.getCantidadEntrega();
                        System.out.println(entregaModel.getIdDetalleDocumento().getIdLote().getId() + entregaModel.getCantidadEntregar());
                    } else {
                        DetalleEntregaModel entregaModel = new DetalleEntregaModel();
                        entregaModel.setIdDetalleDocumento(detalle);
                        entregaModel.setCantidadtotal(detalle.getCantidad());
                        entregaModel.setCantidadEntregar(detalle.getCantidad());
                        entregaModel.setEstado("activo");
                        entregaModel.setFechaEntrega(e.getFechaEntrega());
                        entregaModel.setEstadoEntrega("proforma");
                        //detalleEntregaService.save(entregaModel);
                        detalle.addDetalleEntrega(entregaModel);
                        e.setCantidadEntrega(e.getCantidadEntrega() - detalle.getCantidad());
                    }
                }
            }
        }
    }

    @FXML
    public void clickActualizar(MouseEvent mouseEvent) {
        for (int i = 0; i<productosOL.size();i++){

            productosOL.get(i).setPrecio(productoService.obtenerPrecio(productosOL.get(i).getCodigo()));
            productosOL.get(i).setDescuento(calcularDescuento(productosOL.get(i).getCodigo(),Integer.parseInt(productosOL.get(i).getComboBox().getValue().toString()),productosOL.get(i).getPrecio()));
            productosOL.get(i).setImporte(productosOL.get(i).getPrecio()*Integer.valueOf(productosOL.get(i).getComboBox().getValue()));

            listaCantidades.add(0);
            Producto proReg = new Producto();
            productosOL.get(i).getComboBox().setOnAction((event2 -> {
                int inde = hallarIndiceListaCantidades();
                listaCantidades.add(inde,Integer.valueOf(productosOL.get(inde).getComboBox().getValue()));


                if(Integer.parseInt(productosOL.get(inde).getComboBox().getValue().toString())>productosOL.get(inde).getCantidadMax()){
                    // se sobrepaso la cantidad maxima
                    productosOL.get(inde).getComboBox().setValue(String.valueOf(productosOL.get(inde).getCantidadMax())); //se asigna la cantidad Maxima
                }
                productosOL.get(inde).setDescuento(calcularDescuento(productosOL.get(inde).getCodigo(),Integer.parseInt(productosOL.get(inde).getComboBox().getValue().toString()),productosOL.get(inde).getPrecio()));

                if(codigoP != 0 && asignoRegalo ==0 && cantP>0){// asignar producto de regalo

                    asignoRegalo =1;
                    ProductoModel productoM= productoService.hallarProductoXid(codigoP);
                    proReg.getComboBox().setValue(String.valueOf(cantP)); //proReg.getComboBox().setEditable(false);
                    proReg.getComboBox().setDisable(true);
                    proReg.setImporte(0.0);
                    proReg.setPrecio(0.0);
                    proReg.setDescuento(0.0);
                    proReg.setNombre(productoM.getNombre());
                    proReg.setCodigo(productoM.getCodigoBarras());
                    productosOL.get(inde).setIdRegalo(codigoP);
                    productosOL.add(proReg);
                }else if (codigoP != 0 && asignoRegalo == 0 && cantP==0){//no se llego a la cantidad
                    asignoRegalo =0;
                }
                else if(codigoP !=0 && asignoRegalo ==1 && cantP ==0){// si asigno regalo pero se disminuye la cantidad
                    productosOL.remove(proReg);
                    asignoRegalo=0;

                }else if (codigoP !=0 && asignoRegalo ==1 && cantP>0){
                    proReg.getComboBox().setValue(String.valueOf((cantP)));
                }

                productosOL.get(inde).setImporte(Integer.parseInt(productosOL.get(inde).getComboBox().getValue().toString())*productosOL.get(inde).getPrecio());
                tablaProductos.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE); // para refrescar la tabla*//*
                actualizarListaLabels();
            }));
            /*producto.getButtonEliminar().setOnAction((event3) -> {
                Producto pro = getTableView().getItems().get(getIndex());

                //Aca llamar a la ventana de confirmacion de Eliminar

                Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                dialogoAlerta.setTitle("Ventana de Confirmacion");
                dialogoAlerta.setHeaderText(null);
                dialogoAlerta.initStyle(StageStyle.UTILITY);
                dialogoAlerta.setContentText("Realmente Quieres Elimarlo");
                Optional<ButtonType> result = dialogoAlerta.showAndWait();
                if (result.get() == ButtonType.OK) {

                    System.out.println(hallarIndice(pro));
                    productosOL.remove(hallarIndice(pro));


                    if(producto.getIdRegalo()!=0){
                        productosOL.remove(hallarIndiceXid(producto.getIdRegalo()));
                    }
                    System.out.println("tamaño "+productosOL.size());
                    tablaNuevaVenta.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                }
                //setUsuarios();
                actualizarListaLabels();
            });*/
        }
        tablaProductos.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE); // para refrescar la tabla*//*

    }

    private int hallarIndiceListaCantidades(){
        int index=0;
        for (int i = 0;i< listaCantidades.size();i++){
            if(Integer.valueOf((productosOL.get(i).getComboBox().getValue()))!=listaCantidades.get(i)){
                index = i;
                break;
            }
        }
        return index;

        //recalcular
    }
}
