package absortio.m00p4.negocio.ui.menu.ventas;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.auxiliares.*;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
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


import javafx.scene.control.TableView;

@Component
public class VentaNuevaController implements Initializable {

    @FXML
    private Pane paneNuevoVenta;
    @FXML
    TextField textFieldNoDocumento;
    @FXML
    TextField textFieldCliente;
    @FXML
    TextField textFieldTelefono;
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
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn columnaCodigo;
    @FXML
    private TableColumn columnaProductos;
    @FXML
    private TableColumn columnaPrecio;
    @FXML
    private TableColumn columnaCantidad;
    @FXML
    private Button botonAceptar;
    @Autowired
    private ApplicationContext context;
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


    ObservableList<Producto> productosOL;
    ObservableList<Cliente> clientes;
    ObservableList<Entrega> entregas;

    ObservableList<ClienteModel> ventaClientes;

    ClienteModel cliente;
    ObservableList<FleteModel> fleteModels;
    FleteModel flete;
    private ArrayList<DetalleDocumentoModel> detallesGenerados ;
    private List<Label> listaLabels ;

    List<Producto> listaProductosSel;
    List<Integer> listaCantidades;

    boolean pendejada = true;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaCantidades = new ArrayList<Integer>();
        detallesGenerados = new ArrayList<DetalleDocumentoModel>();
        listaLabels = new ArrayList<Label>();
        ventaClientes = FXCollections.observableArrayList();
        labelSubtotal.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (!t.equals(t1))
                    botonAceptar.setDisable(pendejada);
            }
        });
        botonAceptar.setDisable(true);
        listaLabels.clear();
        listaLabels.add(labelSubtotal);
        listaLabels.add(labelImpuestos);
        listaLabels.add(labelFlete);
        listaLabels.add(labelDescuentos);
        listaLabels.add(labelTotal);
        fleteModels = FXCollections.observableArrayList();
        flete = new FleteModel();
        fleteModels.add(flete);
        entregas = FXCollections.observableArrayList();
        clientes = FXCollections.observableArrayList();
        inicializarTablaDescuentos();
        inicializarCombos();
        textFieldNoDocumento.setText("");
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

        listaProductosSel = new ArrayList<Producto>();
        cargarListaProductosSel();


        comboBoxMoneda.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                System.out.println(t1.toString());
                Double factor = monedaService.findByNombre("dolares").getValor();
                if (t1.toString().equals("soles") && t.toString().equals("dolares")) {
                    for (int i = 0; i < productosOL.size(); i++) {
                        //vuelvo a soles
                        if (!productosOL.get(i).getComboBox().isDisable()) {
                            productosOL.get(i).setPrecio(productoService.getPrecio(productosOL.get(i).getCodigo()));

                            productosOL.get(i).setDescuento(Math.rint(productosOL.get(i).getDescuento() * factor * 100) / 100);
                            System.out.println("entra y es " + productosOL.get(i).getCodigo());
                            productosOL.get(i).setImporte(Math.rint(productosOL.get(i).getPrecio() * Integer.valueOf(productosOL.get(i).getComboBox().getValue()) * 100) / 100);
                            Double flete=Math.rint(Double.valueOf(labelFlete.getText().toString())*factor*100)/100;
                            labelFlete.setText(flete.toString());
                        }

                    }
                } else if (t1.toString().equals("dolares") && t.toString().equals("soles")) {
                    for (int i = 0; i < productosOL.size(); i++) {
                        if (!productosOL.get(i).getComboBox().isDisable()) {
                            productosOL.get(i).setPrecio(Math.rint(productosOL.get(i).getPrecio() * 100 / factor) / 100);

                            productosOL.get(i).setDescuento(Math.rint(productosOL.get(i).getDescuento() * 100 / factor) / 100);
                            System.out.println("entra y es " + productosOL.get(i).getCodigo());

                            productosOL.get(i).setImporte(Math.rint(productosOL.get(i).getImporte() * 100 / factor) / 100);
                            Double flete=Math.rint(Double.valueOf(labelFlete.getText().toString())*100/factor)/100;
                            labelFlete.setText(flete.toString());
                        }

                    }
                }
                pendejada = false;
                actualizarListaLabels();

                pendejada = true;
                tablaProductos.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE); // para refrescar la tabla*//*

            }
        });
    }

    @FXML
    void clickBuscarCliente(MouseEvent event) throws IOException {
        ventaClientes.clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/ventas/listaClientesVenta.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root1 = fxmlLoader.load();
        fxmlLoader.<listaClientesVentaController>getController().recibirDato(ventaClientes, textFieldNoDocumento, textFieldCliente, textFieldTelefono);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar producto");
        stage.setScene(new Scene(root1));
        stage.show();


    }

    private void cargarListaProductosSel() {
        ArrayList<ProductoModel> productoModels = (ArrayList) productoService.findAllProducto();
        for (int i = 0; i < productoModels.size(); i++) {
            Producto producto = new Producto();
            producto.setCodigo(productoModels.get(i).getCodigoBarras());
            producto.setN(i + 1);
            producto.setNombre(productoModels.get(i).getNombre());
            producto.setMarca(productoModels.get(i).getMarca());
            producto.setUnidadVenta(productoModels.get(i).getUnidadVenta());
            producto.setCantidadMax(productoService.hallarStockDisponibleById(productoService.hallarIdByCodigoBarras(producto.getCodigo())));
            // producto.setCategoria ( productoModels.get ( i ).getCategoriaProducto ().getNombre () );
            producto.setModelo(productoModels.get(i).getModelo());
            producto.setDescripcion(productoModels.get(i).getDescripcion());
            producto.setCategoria(productoModels.get(i).getCategoriaProducto().getNombre());
            if (producto.getCantidadMax() > 0) {
                listaProductosSel.add(producto);
            }
        }
    }

    private void inicializarCombos() {
        ArrayList<String> tipoModels = new ArrayList<>();
        tipoModels.add("boleta");
        tipoModels.add("factura");
//                (ArrayList) tipoDocumentoService.findAllTipoDocumento();

        for (int i = 0; i < tipoModels.size(); i++) {

            comboBoxTipo.getItems().add(tipoModels.get(i));

        }
        ArrayList<MonedaModel> monedaModels = (ArrayList) monedaService.findAllMoneda();
        for (int i = 0; i < monedaModels.size(); i++) {
            comboBoxMoneda.getItems().add(monedaModels.get(i).getNombre());
        }
        comboBoxMoneda.setValue(comboBoxMoneda.getItems().get(0));

    }

    @FXML
    void clickNuevoProducto(MouseEvent event) throws IOException {

        Producto producto = new Producto();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/productosSeleccionar.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root1 = fxmlLoader.load();
        fxmlLoader.<ProductoSeleccionarController>getController().asignarAventas(producto, productosOL, tablaProductos, listaLabels, listaProductosSel, listaCantidades, comboBoxMoneda);
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
        //agregarBoton();
        tablaProductos.setItems(productosOL);
    }

    private void llenarComboBox(ComboBox comboBox) {
        comboBox.setEditable(true);
        comboBox.setValue("0");

        for (int i = 1; i < 30; i++) {
            comboBox.getItems().add(String.valueOf(i));
        }
    }


    private void agregarBoton() {
        Callback<TableColumn<Producto, Void>, TableCell<Producto, Void>> cellFactory = new Callback<TableColumn<Producto, Void>, TableCell<Producto, Void>>() {
            @Override
            public TableCell<Producto, Void> call(final TableColumn<Producto, Void> param) {
                final TableCell<Producto, Void> cell = new TableCell<Producto, Void>() {
                    private final Button btnEliminar = new Button();
                    {
                        btnEliminar.setOnAction((event) -> {
                            productosOL.remove(getIndex());
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {

                            Image image = new Image(getClass().getResourceAsStream("/fxml/imagenes/deleteicon.png"));
                            ImageView iv1 = new ImageView(image);
                            iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnEliminar.setGraphic(iv1);


                            HBox pane = new HBox(btnEliminar);
                            pane.setSpacing(5);
                            setGraphic(pane);
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
                return cell;
            }
        };
        columnaAcciones.setCellFactory(cellFactory);
    }

    void calcularlotes() {
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
                    detalle.setdU(productosOL.get(i).getDescuento()/detalle.getCantidad());
                    detalle.setImporte(detalle.getpU() * detalle.getCantidad());

                    detalle.setIdLote(listaLotes.get(j));
                    if(repiteLote(detalle)==0)
                    detallesGenerados.add(detalle);
                } else {
                    DetalleDocumentoModel detalle = new DetalleDocumentoModel();
                    detalle.setCantidad(cantPed);
                    cantPed = 0;// se debería actualizar base de datos
                    detalle.setpU(productosOL.get(i).getPrecio());
                    //detalle.setdU(productosOL.get(i).getDescuento());
                    detalle.setdU(productosOL.get(i).getDescuento()/detalle.getCantidad());
                    detalle.setImporte(detalle.getpU() * detalle.getCantidad());

                    detalle.setIdLote(listaLotes.get(j));
                    if(repiteLote(detalle)==0)
                    detallesGenerados.add(detalle);
                }
                j++;
            }

        }
        imprimirListas();
    }



    private  int repiteLote(DetalleDocumentoModel detalle){

        int res =0;
        for (int i = 0;i<detallesGenerados.size();i++){
            if(detallesGenerados.get(i).getIdLote().getId().equals(detalle.getIdLote().getId())){
                res = 1;
                break;
            }
        }
        return res;
    }
    public void imprimirListas() {

        for (int i = 0; i < detallesGenerados.size(); i++) {
            System.out.print(detallesGenerados.get(i).getCantidad() + " ");
            System.out.print(detallesGenerados.get(i).getpU() + " ");
            System.out.print(detallesGenerados.get(i).getImporte() + " ");
            System.out.print(detallesGenerados.get(i).getIdLote().getId() + "\n");
        }
    }

    @FXML
    private void clickProgramarEntregas() throws IOException, ParseException {
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/entregas/programarEntregas.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Parent pane = fxmlLoader.load();
        fxmlLoader.<ProgramarEntregasController>getController().asignarProductos(entregas, fleteModels, labelFlete, labelTotal, comboBoxMoneda.getValue());
        Stage stage = new Stage();
        stage.setTitle("Programar entregas");
        stage.setScene(new Scene(pane));
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(e -> e.consume());
        stage.show();
        botonAceptar.setDisable(false);
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {

        Pane paneParent = (Pane) paneNuevoVenta.getParent();
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
        if (cliente != null && monedaModel!=null &&tipoDocumentoModel!=null && igvModel!=null && (detallesGenerados.size()>0)) {
            DocumentoModel documento = new DocumentoModel();
            documento.setCliente(cliente);
            documento.setDescuentoTotal(Double.valueOf(labelDescuentos.getText()));
            documento.setSubtotal(Double.valueOf(labelSubtotal.getText()));
            documento.setMontoTotal(Double.valueOf(labelTotal.getText()));
            documento.setFleteTotal(Double.valueOf(labelFlete.getText()));
            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
            documento.setUsuaio(usuarioModel);
            documento.setEstado("proforma");
            documento.setDepartamento(entregas.get(0).getDepartamento());
            documento.setProvincia(entregas.get(0).getProvincia());
            documento.setDistrito(entregas.get(0).getDistrito());
            documento.setDireccion(entregas.get(0).getDireccion());
            documento.setFechaEmision(new Date());
            documento.setTipodocumento(tipoDocumentoModel);
            documento.setMoneda(monedaModel);
            documento.setIgv(igvModel);
            documento.setFlete(fleteModels.get(1));
            guardarEntregas(detallesGenerados, entregas);

            for (int i = 0; i < detallesGenerados.size(); i++) {
                documento.addDetalleDocumento(detallesGenerados.get(i));
            }
            DocumentoModel documentoActualizado = documentoService.save(documento);
            documentoActualizado.setNoDocumento("pr-"+ documentoActualizado.getId());


            //ArrayList<DetalleDocumentoModel> detallesDocumentoActualizados = detalleDocumentoService.findAllDetalleDocumentobyDocumento_Id(documentoActualizado);
            //guardarEntregas(detallesDocumentoActualizados, entregas);
            documentoService.save(documentoActualizado);
            String logMensaje="El usuario "+ usuarioModel.getNombres()+ "registro nueva proforma "+documentoActualizado.getNoDocumento();
            LOGGERAUDIT.info(logMensaje);
            Pane paneParent = (Pane) paneNuevoVenta.getParent();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/ventas/ventas.fxml"));
            fxmlLoader.setControllerFactory(context::getBean);
            Pane pane = fxmlLoader.load();
            paneParent.getChildren().setAll(pane);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information no ingresada");
            alert.setHeaderText(null);
            alert.setContentText("Por favor ingrese la informacion requerida");
            alert.showAndWait();

        }
    }

    private void guardarEntregas(ArrayList<DetalleDocumentoModel> detallesDocumentoActualizados, ObservableList<Entrega> entregas) {
        for (DetalleDocumentoModel detalle : detallesDocumentoActualizados) {
            String nombreProducto = detalle.getIdLote().getProducto().getNombre();
            Integer cantidadPedida = detalle.getCantidad();
            for (Entrega e : entregas) {
                if (e.getCantidadEntrega() != 0 && Objects.equals(e.getNombre(), nombreProducto)) {
                    if ( e.getCantidadEntrega() <= cantidadPedida) {
                        DetalleEntregaModel entregaModel = new DetalleEntregaModel();
                        entregaModel.setIdDetalleDocumento(detalle);
                        entregaModel.setCantidadtotal(e.getCantidadEntrega());
                        entregaModel.setCantidadEntregar(e.getCantidadEntrega());
                        cantidadPedida = cantidadPedida - e.getCantidadEntrega();
                        e.setCantidadEntrega(0);
                        entregaModel.setEstado("activo");
                        entregaModel.setFechaEntrega(e.getFechaEntrega());
                        entregaModel.setEstadoEntrega("proforma");
                        //detalleEntregaService.save(entregaModel);
                        detalle.addDetalleEntrega(entregaModel);

                        System.out.println(entregaModel.getIdDetalleDocumento().getIdLote().getId() + entregaModel.getCantidadEntregar());
                    } else {
                        DetalleEntregaModel entregaModel = new DetalleEntregaModel();
                        entregaModel.setIdDetalleDocumento(detalle);
                        entregaModel.setCantidadtotal(cantidadPedida);
                        entregaModel.setCantidadEntregar(cantidadPedida);
                        e.setCantidadEntrega(e.getCantidadEntrega()- cantidadPedida);
                        entregaModel.setEstado("activo");
                        entregaModel.setFechaEntrega(e.getFechaEntrega());
                        entregaModel.setEstadoEntrega("proforma");
                        //detalleEntregaService.save(entregaModel);
                        detalle.addDetalleEntrega(entregaModel);
                        break;
                    }
                }
            }
        }
    }

    private void actualizarListaLabels() {
        Double acum = 0.0;
        Double descuentos = 0.0;
        for (int i = 0; i < productosOL.size(); i++) {
            acum += productosOL.get(i).getImporte();
            descuentos += productosOL.get(i).getDescuento();
        }
        Double factor = monedaService.findByNombre("dolares").getValor();
        listaLabels.get(0).setText(String.valueOf(Math.rint(acum * 100) / 100));
        listaLabels.get(1).setText(String.valueOf(Math.rint(acum * 0.18 * 100) / 100));
        listaLabels.get(3).setText(String.valueOf(Math.rint(descuentos * 100) / 100));
        listaLabels.get(4).setText(String.valueOf(Math.rint(((acum * 1.18 - descuentos + Double.valueOf(listaLabels.get(2).getText())) * 100)) / 100));

    }


}
