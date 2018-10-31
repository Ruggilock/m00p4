package absortio.m00p4.negocio.ui.menu.ventas;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.FormateadorDecimal;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.DetalleDespachoAux;
import absortio.m00p4.negocio.model.auxiliares.DetalleEntrega;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.auxiliares.Venta;
import absortio.m00p4.negocio.reportes.ReporteVenta;
import absortio.m00p4.negocio.repository.DetalleDespachoRepository;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import absortio.m00p4.negocio.repository.LoteRepository;
import absortio.m00p4.negocio.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static absortio.m00p4.negocio.service.singleton.UsuarioSingleton.usuarioModel;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

@Component
public class VentasController implements Initializable {

    @FXML
    Pane paneVentas;
    @FXML
    TextField textFieldClientes;
    @FXML
    TextField textFieldNoDoc;
    @FXML
    TextField textFieldMontoIni;
    @FXML
    TextField textFieldMontoFin;
    @FXML
    DatePicker datePickFechaInicio;
    @FXML
    DatePicker datePickFechaFin;
    @FXML
    ComboBox comboBoxEstado;
    @FXML
    ComboBox comboBoxTipoDoc;
    @FXML
    private TableView<Venta> tablaVentas;
    @FXML
    private Button buttonBuscar;
    @FXML
    private TableColumn columnaN;
    @FXML
    private TableColumn columnaNdocumento;
    @FXML
    private TableColumn columnaCliente;
    @FXML
    private TableColumn columnaFecha;
    @FXML
    private TableColumn columnaMonto;
    @FXML
    private TableColumn columnaEstado;
    @FXML
    private TableColumn columnaAcciones;
    @Autowired
    private ApplicationContext context;

    ObservableList<Venta> ventas;
    @Autowired
    DocumentoService documentoService;
    @Autowired
    DetalleDocumentoService detalleDocumentoService;
    @Autowired
    DetalleEntregaService detalleEntregaService;
    @Autowired
    ProductoService productoService;
    @Autowired
    TipoDocumentoService tipoDocumentoService;
    @Autowired
    PermisoService permisoService;
    @Autowired
    LoteRepository loteService;
    @Autowired
    FacturaParcialService facturaParcialService;
    @Autowired
    DetalleFacturaParcialService detalleFacturaParcialService;

    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");

    @Autowired
    private DetalleDespachoService detalleDespachoService;
    //public static final Logger LOG  = LogManager.getLogger(VentasController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarTablaVentas();
        cargarGrilla();
        cargarComboBox();

    }

    @FXML
    void clickExportarExcel(MouseEvent event) throws IOException {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        if (SystemSingleton.getInstance().getRute() != null) {
            ReporteVenta reporteVenta = new ReporteVenta();
            ArrayList<DocumentoModel> documentoModels = new ArrayList<DocumentoModel>();
            for (Venta venta : ventas) {
                documentoModels.add(documentoService.getByNoDocumento(venta.getNoDocumento()));
            }
            reporteVenta.generarReporteVentas(documentoModels);
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }

    @FXML
    void clickNuevoVentas(MouseEvent event) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("proformasCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/ventas/nuevoventa.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneVentas.getChildren().setAll(pane);
    }

    private void inicializarTablaVentas() {
        //LOG.info("Se inicia el panel de ventas");

        columnaN.setCellValueFactory(new PropertyValueFactory<Venta, Integer>("n"));
        columnaNdocumento.setCellValueFactory(new PropertyValueFactory<Venta, String>("noDocumento"));
        columnaCliente.setCellValueFactory(new PropertyValueFactory<Venta, String>("cliente"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<Venta, String>("fecha"));
        columnaMonto.setCellValueFactory(new PropertyValueFactory<Venta, String>("monto"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<Venta, String>("estado"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<>("button"));
        agregarBotones();
        ventas = FXCollections.observableArrayList();
        tablaVentas.setItems(ventas);

    }

    public void cargarGrilla() {
        ventas.clear();
        ArrayList<DocumentoModel> ventaModels = (ArrayList) documentoService.obtenerVentas();
        for (int i = 0; i < ventaModels.size(); i++) {
            Venta venta = new Venta();
            venta.setCliente(ventaModels.get(i).getCliente().getNombres());
            venta.setN(i + 1);
            venta.setEstado(ventaModels.get(i).getEstado());
            venta.setFecha(ventaModels.get(i).getFechaEmision());
            venta.setNoDocumento(ventaModels.get(i).getNoDocumento());
            venta.setMonto(FormateadorDecimal.formatear(ventaModels.get(i).getMontoTotal()));
            ventas.add(venta);
        }
    }

    private void agregarBotones() {
        Callback<TableColumn<Venta, Void>, TableCell<Venta, Void>> cellFactory = new Callback<TableColumn<Venta, Void>, TableCell<Venta, Void>>() {
            @Override
            public TableCell<Venta, Void> call(final TableColumn<Venta, Void> param) {
                final TableCell<Venta, Void> cell = new TableCell<Venta, Void>() {
                    private final Button btnVer = new Button();
                    private final Button btnEliminar = new Button();
                    private final Button btnEditar = new Button();

                    {
                        btnVer.setOnAction((event) -> {
                            Venta venta = getTableView().getItems().get(getIndex());
                            paneVer(venta);
                        });
                    }

                    {
                        btnEditar.setOnMouseClicked((event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("proformasEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Venta venta = getTableView().getItems().get(getIndex());
                            if (venta.getEstado().equals("proforma")) {
                                confirmar(venta);
                            } else {
                                Alert alerta = new Alert(Alert.AlertType.WARNING);
                                alerta.setTitle("Edicion no valida");
                                alerta.setHeaderText(null);
                                alerta.setContentText("Solo se puede editar los documentos proforma");
                                alerta.showAndWait();
                            }
                        });
                    }

                    {
                        btnEliminar.setOnAction((event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("proformasEliminar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Venta ventaSeleccionada = getTableView().getItems().get(getIndex());
                            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                            dialogoAlerta.setTitle("Ventana de Confirmacion");
                            dialogoAlerta.setHeaderText(null);
                            dialogoAlerta.initStyle(StageStyle.UTILITY);
                            dialogoAlerta.setContentText("¿Realmente Quieres Anularlo?");
                            Optional<ButtonType> result = dialogoAlerta.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                anular(ventaSeleccionada.getNoDocumento());
                                cargarGrilla();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Image image = new Image(getClass().getResourceAsStream("/fxml/imagenes/confirmicon.png"));
                            ImageView iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnEditar.setGraphic(iv1);

                            image = new Image(getClass().getResourceAsStream("/fxml/imagenes/deleteicon.png"));
                            iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnEliminar.setGraphic(iv1);

                            image = new Image(getClass().getResourceAsStream("/fxml/imagenes/viewicon.png"));
                            iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnVer.setGraphic(iv1);

                            HBox pane = new HBox(btnVer, btnEditar, btnEliminar);
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

    /*
        private void agregarConfirmar() {
            Callback<TableColumn<Venta, Void>, TableCell<Venta, Void>> cellFactory = new Callback<TableColumn<Venta, Void>, TableCell<Venta, Void>>() {
                @Override
                public TableCell<Venta, Void> call(final TableColumn<Venta, Void> param) {
                    final TableCell<Venta, Void> cell = new TableCell<Venta, Void>() {
                        private final Button btnConfirmar = new Button();

                        {
                            btnConfirmar.setOnMouseClicked((event) -> {
                                System.out.println(getTableView().getItems().get(getIndex()).getClass());
                                Venta venta = getTableView().getItems().get(getIndex());
                                confirmar(venta);
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                Image image = new Image(getClass().getResourceAsStream("/fxml/imagenes/confirmicon.png"));
                                ImageView iv1 = new ImageView(image);
                                iv1.setFitHeight(17);
                                iv1.setFitWidth(20);
                                btnConfirmar.setGraphic(iv1);

                                HBox pane = new HBox(btnConfirmar);
                                pane.setSpacing(5);
                                setGraphic(pane);
                                setAlignment(Pos.CENTER);

                            }
                        }
                    };
                    return cell;
                }
            };
            columnaConfirmar.setCellFactory(cellFactory);
        }
    */
    private void confirmar(Venta venta) {
        DocumentoModel documento = documentoService.getByNoDocumento(venta.getNoDocumento());
        if (!comprometerStock(documento)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No se pudo realizar la venta");
            alert.setHeaderText(null);
            alert.setContentText("No hay stock disponible");
            alert.showAndWait();
        } else {
            documento.setEstado("por atender");
            documento.setNoDocumento(documento.getTipodocumento().getNombre().substring(0, 3).toUpperCase() + "-" + documento.getId().toString());

            //documentoService.setNoDoc(documento.getId(), documento.getTipodocumento().getNombre());
            documentoService.save(documento);
            String logMensaje = "El usuario " + usuarioModel.getNombres() + " confirmo proforma emitiendo nuevo pedido";
            LOGGERAUDIT.info(logMensaje);
            LOGGERAUDIT.info("Pedido registrado con codigo: " + documento.getNoDocumento());
        }
        cargarGrilla();
    }

    private boolean comprometerStock(DocumentoModel documento) {
        for (DetalleDocumentoModel detalle : documento.getDetalleDocumentos()
                ) {
            LoteModel lote = loteService.findById(detalle.getIdLote().getId());
            if (lote.getStockDisponible() >= detalle.getCantidad()) {
                lote.setStockComprometido(lote.getStockComprometido() + detalle.getCantidad());
                lote.setStockDisponible(lote.getStockDisponible() - detalle.getCantidad());
                loteService.save(lote);
            } else return false;
            for (DetalleEntregaModel detalleEnt : detalle.getDetalleEntregas()
                    ) {
                detalleEnt.setEstadoEntrega("por atender");
            }
        }
        return true;
    }

    private void paneEditar(Venta venta) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/ventas/editarVenta.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane pane = fxmlLoader.load();
            fxmlLoader.<VentaEditarController>getController().cargarData(venta);
            paneVentas.getChildren().setAll(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void paneVer(Venta venta) {
        generarPDF(venta);
    }


    private void generarPDF(Venta venta) {
        DocumentoModel documentoModel = documentoService.getByNoDocumento(venta.getNoDocumento());
        List<DetalleDocumentoModel> detalles = detalleDocumentoService.findAllDetalleDocumentobyDocumento_Id(documentoModel);
        ReporteVenta reporteVenta = new ReporteVenta();
        List<FacturaParcialModel> facturaParcialModels = facturaParcialService.findAllByIdDocumentol(documentoModel.getId());
        List<List<DetalleFacturaParcialModel>> detalleFacturaParcialModels = new ArrayList<>();
        List<List<DetalleDespachoAux>> detallesAux = new ArrayList<>();
        for (FacturaParcialModel facturaParcialModel : facturaParcialModels) {
            List<DetalleFacturaParcialModel> det = detalleFacturaParcialService.findAllDetalleFacturaParcialByIdFacturaParcial(facturaParcialModel.getId());
            detalleFacturaParcialModels.add(det);
            List<DetalleDespachoAux> listaAux = new ArrayList<>();
            for (DetalleFacturaParcialModel detalleFacturaParcialModel : det) {
                DetalleDespachoAux detalleAux = new DetalleDespachoAux();
                detalleAux.lote = detalleDespachoService.getById(detalleFacturaParcialModel.getIdDetalleDespacho()).getIddetalleEntrega().getIdDetalleDocumento().getIdLote();
                detalleAux.precioUnitario = detalleDespachoService.getById(detalleFacturaParcialModel.getIdDetalleDespacho()).getIddetalleEntrega().getIdDetalleDocumento().getpU();
                detalleAux.cantidad = detalleDespachoService.getById(detalleFacturaParcialModel.getIdDetalleDespacho()).getCantidadAtendida();
                detalleAux.descuentoUnitario = detalleFacturaParcialModel.getDescuento();
                listaAux.add(detalleAux);
            }
            detallesAux.add(listaAux);
        }
        reporteVenta.pdfventa(documentoModel, detalles, facturaParcialModels, detalleFacturaParcialModels, detallesAux);
    }

    private void anular(String noDocumento) {
        DocumentoModel documentoModel = documentoService.getByNoDocumento(noDocumento);
        documentoModel.setEstado("anulado");
        for (DetalleDocumentoModel detalle : documentoModel.getDetalleDocumentos()
                ) {
            for (DetalleEntregaModel detalleEnt : detalle.getDetalleEntregas()
                    ) {
                detalleEnt.setEstadoEntrega("anulado");
            }
        }
        documentoService.save(documentoModel);
        String logMensaje = "El usuario " + usuarioModel.getNombres() + " anuló el documento: " + documentoModel.getNoDocumento();
        LOGGERAUDIT.info(logMensaje);

    }

    @FXML
    public void clickBuscar(MouseEvent mouseEvent) throws IOException {
        ventas.clear();
        String cliente, documento, estado, tipoDoc, fechaIni, fechaFin;
        Double cantIni = 0.0, cantFin = 1000000.0;
        ArrayList<DocumentoModel> ventaModels = new ArrayList<DocumentoModel>();

        cliente = textFieldClientes.getText();
        documento = textFieldNoDoc.getText();
        estado = comboBoxEstado.getValue().toString();
        tipoDoc = comboBoxTipoDoc.getValue().toString();

        if (estado.compareTo("-Seleccione-") == 0) estado = "";
        if (tipoDoc.compareTo("-Seleccione-") == 0) tipoDoc = "";
        if (datePickFechaInicio.getValue() == null) {
            fechaIni = "1900-01-01";
        } else fechaIni = datePickFechaInicio.getValue().toString();
        if (datePickFechaFin.getValue() == null) {
            fechaFin = "2100-12-25";
        } else fechaFin = datePickFechaFin.getValue().toString();

        if (textFieldMontoIni.getText().compareTo("") == 0) cantIni = 0.0;
        else cantIni = Double.parseDouble(textFieldMontoIni.getText());
        if (textFieldMontoFin.getText().compareTo("") == 0) cantFin = 1000000.0;
        else cantFin = Double.parseDouble(textFieldMontoFin.getText());

        // Buscamos con el query
        ventaModels = (ArrayList<DocumentoModel>) documentoService.findByTodosFiltros(cliente, documento, fechaIni, fechaFin, estado, tipoDoc, cantIni, cantFin);

        for (int i = 0; i < ventaModels.size(); i++) {
            Venta venta = new Venta();
            venta.setCliente(ventaModels.get(i).getCliente().getNombres());
            venta.setN(i + 1);
            venta.setEstado(ventaModels.get(i).getEstado());
            venta.setFecha(ventaModels.get(i).getFechaEmision());
            venta.setNoDocumento(ventaModels.get(i).getNoDocumento());
            venta.setMonto(FormateadorDecimal.formatear(ventaModels.get(i).getMontoTotal()));
            ventas.add(venta);
        }
    }

    public void cargarComboBox() {
        ArrayList<String> estados = new ArrayList<>();
        estados.add("proforma");
        estados.add("por atender");
        estados.add("concluido");

        ObservableList<String> observableListEstados = FXCollections.observableArrayList(estados);

        comboBoxEstado.getItems().add("-Seleccione-");
        comboBoxEstado.getItems().addAll(observableListEstados);
        comboBoxEstado.setEditable(false);
        comboBoxEstado.setValue(comboBoxEstado.getItems().get(0));

        TextFields.bindAutoCompletion(comboBoxEstado.getEditor(), comboBoxEstado.getItems());

        ArrayList<String> tipos = new ArrayList<>();
        ArrayList<TipoDocumentoModel> tipoModels = (ArrayList<TipoDocumentoModel>) tipoDocumentoService.findAllByEstado("activo");
        for (int i = 0; i < tipoModels.size(); i++) {
            tipos.add(tipoModels.get(i).getNombre());
        }

        ObservableList<String> observableListTipos = FXCollections.observableArrayList(tipos);

        comboBoxTipoDoc.getItems().add("-Seleccione-");
        comboBoxTipoDoc.getItems().addAll(observableListTipos);
        comboBoxTipoDoc.setEditable(false);
        comboBoxTipoDoc.setValue(comboBoxTipoDoc.getItems().get(0));   // Seleccione
        TextFields.bindAutoCompletion(comboBoxTipoDoc.getEditor(), comboBoxTipoDoc.getItems());

    }

}
