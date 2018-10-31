package absortio.m00p4.negocio.ui.menu.despacho;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Despacho;
import absortio.m00p4.negocio.model.auxiliares.DetalleDespacho;
import absortio.m00p4.negocio.model.auxiliares.Venta;
import absortio.m00p4.negocio.model.auxiliares.DetalleDespachoAux;
import absortio.m00p4.negocio.reportes.ReporteDespacho;
import absortio.m00p4.negocio.reportes.ReporteVenta;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
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
public class DespachoController implements Initializable {


    @FXML
    private Pane paneDespacho;

    @FXML
    private Button buttonBuscar;

    @FXML
    private TableView<Despacho> tablaDespacho;

    @FXML
    private TableColumn columnN;

    @FXML
    private TableColumn columnSobreNombre;

    @FXML
    private TableColumn columnFecha;

    @Autowired
    DetalleEntregaService detalleEntregaService;

    @FXML
    private TableColumn columnPeso;
    @Autowired
    DocumentoService documentoService;

    @FXML
    private TableColumn columnArchivo;

    @FXML
    private TableColumn columnRuta;
    @Autowired
    DetalleDocumentoService detalleDocumentoService;
    @Autowired
    KardexService kardexService;
    @Autowired
    MotivoOperacionKardexService motivoOperacionKardexService;
    @Autowired
    BloqueService bloqueService;
    @Autowired
    LoteService loteService;
    @Autowired
    DetalleDespachoService detalleDespachoService;
    @Autowired
    FacturaParcialService facturaParcialService;

    @Autowired
    DespachoService despachoService;
    @FXML
    private TableColumn columnVolumen;
    @FXML
    private TableColumn columnTransportista;
    @FXML
    private TableColumn columnEstado;
    @FXML
    private TableColumn columnConfirmar;

    @FXML
    private ComboBox comboBoxEstadoDespacho;

    @FXML
    private TextField textFieldSobrenombre;

    @FXML
    private DatePicker datePickerFecha;

    ObservableList<Despacho> oldespachos;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PermisoService permisoService;

    @Autowired
    private DetalleFacturaParcialService detalleFacturaParcialService;

    @Autowired
    IgvService igvService;
    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");


    ArrayList<FacturaParcialModel> facturaParcialModels;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.inicializarTablaDespachos();
        cargarComboBoxEstado();
        this.llenarTabaDespacho();

    }


    private void inicializarTablaDespachos() {
        columnN.setCellValueFactory(new PropertyValueFactory<Despacho, Integer>("n"));
        columnSobreNombre.setCellValueFactory(new PropertyValueFactory<Despacho, String>("sobreNombre"));
        columnFecha.setCellValueFactory(new PropertyValueFactory<Despacho, Date>("fecha"));
        columnVolumen.setCellValueFactory(new PropertyValueFactory<Despacho, Double>("volumen"));
        columnPeso.setCellValueFactory(new PropertyValueFactory<Despacho, Double>("peso"));
        columnTransportista.setCellValueFactory(new PropertyValueFactory<Despacho, String>("transportista"));
        columnArchivo.setCellValueFactory(new PropertyValueFactory<Despacho, String>("archivo"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<Despacho, String>("estado"));
        columnRuta.setCellValueFactory(new PropertyValueFactory<Despacho, Button>("buttonAgregarRuta"));
        columnConfirmar.setCellValueFactory(new PropertyValueFactory<Despacho, Button>("confirmar"));

        oldespachos = FXCollections.observableArrayList();
        tablaDespacho.setItems(oldespachos);

    }

    private void llenarTabaDespacho() {
        oldespachos.clear();
        ArrayList<DespachoModel> despachos = (ArrayList) despachoService.findAllDespacho();
        for (int i = 0; i < despachos.size(); i++) {
            Despacho despacho = new Despacho();
            despacho.setId(despachos.get(i).getId());
            despacho.setN(i);
            despacho.setSobreNombre(despachos.get(i).getSobreNombre());
            despacho.setFecha(despachos.get(i).getFecha());
            despacho.setVolumen(despachos.get(i).getVolumen());
            despacho.setPeso(despachos.get(i).getPeso());
            if (despachos.get(i).getIdTransportista() != null) {
                despacho.setTransportista(despachos.get(i).getIdTransportista().getDocumentoIdentidad());
            }
            despacho.setArchivo(despachos.get(i).getArchivo());
            despacho.setEstado(despachos.get(i).getEstadoDespacho());


            despacho.setButtonAgregarRuta(new Button("Simulaciones"));
            despacho.getButtonAgregarRuta().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/simulacion.fxml"));
                    fxmlLoader.setControllerFactory(context::getBean);
                    Pane pane = null;
                    try {
                        pane = fxmlLoader.load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    fxmlLoader.<SimulacionController>getController().recibirIdDespacho(despacho.getId());
                    paneDespacho.getChildren().setAll(pane);
                }
            });
            despacho.setConfirmar(new Button("Confirmar"));
            if (despachos.get(i).getEstadoDespacho().compareTo("confirmado") == 0)
                despacho.getConfirmar().setText("Ver Guía Rem.");
            despacho.getConfirmar().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                    RolModel rolUsuario = usuarioModel.getIdRol();
                    if (!Validador.tienePermiso("despachosEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                        return;
                    }
                    DespachoModel despachoModel = despachoService.findById(despacho.getId());
                    if (despachoModel.getEstadoDespacho().compareTo("confirmado") == 0) {
                        generarPDFdespacho(despachoModel);
                        llenarTabaDespacho();
                        return;

                    }

                    despachoModel.setEstadoDespacho("confirmado");
                    despachoService.save(despachoModel);

                    eliminarPorConfirmar();
                    actualizarResta(despachoModel);
                    verificarDocumento(despachoModel);
                    CrearFacturaParcial(despachoModel);

                    //reporteDespacho.pdfdevolucion(despachoModel );
                    generarPDFdespacho(despachoModel);
                    despacho.getConfirmar().setText("Ver Guía Rem.");
                    llenarTabaDespacho();


                }
            });
            oldespachos.add(despacho);
        }
    }


    public void cargarComboBoxEstado() {
        ArrayList<String> estados = new ArrayList<>();
        estados.add("por confirmar");
        estados.add("confirmado");

        ObservableList<String> observableListEstados = FXCollections.observableArrayList(estados);

        comboBoxEstadoDespacho.getItems().add("-Seleccione-");
        comboBoxEstadoDespacho.getItems().addAll(observableListEstados);
        comboBoxEstadoDespacho.setEditable(false);
        comboBoxEstadoDespacho.setValue(comboBoxEstadoDespacho.getItems().get(0));


        TextFields.bindAutoCompletion(comboBoxEstadoDespacho.getEditor(), comboBoxEstadoDespacho.getItems());
    }


    @FXML
    public void clickBuscar(MouseEvent mouseEvent) {
        //this.inicializarTablaOperacionesKardex();
        String sobrenombre, fecha, estado;
        fecha = "";

        estado = comboBoxEstadoDespacho.getValue().toString();
        sobrenombre = textFieldSobrenombre.getText();
        if (estado.compareTo("-Seleccione-") == 0) {
            estado = "";
        }
        if (datePickerFecha.getValue() == null) {
            fecha = "1900-01-01";
        } else fecha = datePickerFecha.getValue().toString();

        oldespachos.clear();
        List<DespachoModel> despachos = despachoService.findByTodosFiltros(sobrenombre, estado, fecha);

        for (int i = 0; i < despachos.size(); i++) {
            Despacho despacho = new Despacho();
            despacho.setId(despachos.get(i).getId());
            despacho.setN(i);
            despacho.setSobreNombre(despachos.get(i).getSobreNombre());
            despacho.setFecha(despachos.get(i).getFecha());
            despacho.setVolumen(despachos.get(i).getVolumen());
            despacho.setPeso(despachos.get(i).getPeso());
            if (despachos.get(i).getIdTransportista() != null) {
                despacho.setTransportista(despachos.get(i).getIdTransportista().getDocumentoIdentidad());
            }
            despacho.setArchivo(despachos.get(i).getArchivo());
            despacho.setEstado(despachos.get(i).getEstadoDespacho());


            despacho.setButtonAgregarRuta(new Button("Simulaciones"));
            despacho.getButtonAgregarRuta().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/simulacion.fxml"));
                    fxmlLoader.setControllerFactory(context::getBean);
                    Pane pane = null;
                    try {
                        pane = fxmlLoader.load();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    fxmlLoader.<SimulacionController>getController().recibirIdDespacho(despacho.getId());
                    paneDespacho.getChildren().setAll(pane);
                }
            });
            despacho.setConfirmar(new Button("Confirmar"));
            if (despachos.get(i).getEstadoDespacho().compareTo("confirmado") == 0)
                despacho.getConfirmar().setText("Ver Guía Rem.");
            despacho.getConfirmar().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                    RolModel rolUsuario = usuarioModel.getIdRol();
                    if (!Validador.tienePermiso("despachosEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                        Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                        return;
                    }
                    DespachoModel despachoModel = despachoService.findById(despacho.getId());
                    if (despachoModel.getEstadoDespacho().compareTo("confirmado") == 0) {
                        generarPDFdespacho(despachoModel);
                        llenarTabaDespacho();
                        return;
                    }
                    despachoModel.setEstadoDespacho("confirmado");
                    despachoService.save(despachoModel);

                    eliminarPorConfirmar();
                    actualizarResta(despachoModel);
                    verificarDocumento(despachoModel);
                    CrearFacturaParcial(despachoModel);

                    //reporteDespacho.pdfdevolucion(despachoModel );
                    generarPDFdespacho(despachoModel);
                    despacho.getConfirmar().setText("Ver Guía Rem.");
                    llenarTabaDespacho();


                }
            });
            oldespachos.add(despacho);
        }
    }


    private void CrearFacturaParcial(DespachoModel despachoModel) {
        facturaParcialModels = new ArrayList<>();
        ArrayList<DetalleDespachoModel> detalleDespachoModels = new ArrayList<DetalleDespachoModel>(despachoModel.getDetalleDespachos());
        for (int i = 0; i < detalleDespachoModels.size(); i++) {
            DocumentoModel documentoModel = detalleDespachoModels.get(i).getIddetalleEntrega().getIdDetalleDocumento().getIdDocumento();

            Integer cantidadAtendidad = detalleDespachoModels.get(i).getCantidadAtendida();

            DetalleDocumentoModel detalleDocumentoModel = detalleDespachoModels.get(i).getIddetalleEntrega().getIdDetalleDocumento();

            DetalleFacturaParcialModel detalleFacturaParcialModel = new DetalleFacturaParcialModel();
            detalleFacturaParcialModel.setIdDetalleDespacho(detalleDespachoModels.get(i).getId());
            detalleFacturaParcialModel.setEstado("activo");
            detalleFacturaParcialModel.setSubTotal(detalleDocumentoModel.getpU() * cantidadAtendidad);

            Double descuentoUnitario = detalleDocumentoModel.getdU();
            detalleFacturaParcialModel.setDescuento(descuentoUnitario * detalleDespachoModels.get(i).getCantidadAtendida());

            Double igv = igvService.findByEstado("activo").getValor();
            detalleFacturaParcialModel.setImpuestos(detalleDocumentoModel.getpU() * cantidadAtendidad * igv);
            ArrayList<DetalleDocumentoModel> detalleDocumentos = detalleDocumentoService.findAllDetalleDocumentobyDocumento_Id(documentoModel);

            Double val = calcularFlete(documentoModel.getFlete().getCosto(), cantidadAtendidad, detalleDocumentoModel.getIdLote().getProducto().getPeso(), detalleDocumentos);
            detalleFacturaParcialModel.setFlete(val);
            detalleFacturaParcialModel.setTotal(detalleDocumentoModel.getpU() * cantidadAtendidad * (1 + igv) - descuentoUnitario * detalleDespachoModels.get(i).getCantidadAtendida() + val);
            detalleFacturaParcialModel.setLote(detalleDocumentoModel.getIdLote().getId());

            boolean existeFacturaParcial = false;
            for (int j = 0; j < facturaParcialModels.size(); j++) {
                if (documentoModel.getId().compareTo(facturaParcialModels.get(j).getIdDocumento()) == 0) {
                    facturaParcialModels.get(j).addFacturaParcial(detalleFacturaParcialModel);
                    facturaParcialModels.get(j).setDescuento(facturaParcialModels.get(j).getDescuento() + detalleFacturaParcialModel.getDescuento());
                    facturaParcialModels.get(j).setFlete(facturaParcialModels.get(j).getFlete() + detalleFacturaParcialModel.getFlete());
                    facturaParcialModels.get(j).setImpuestos(facturaParcialModels.get(j).getImpuestos() + detalleFacturaParcialModel.getImpuestos());
                    facturaParcialModels.get(j).setSubTotal(facturaParcialModels.get(j).getSubTotal() + detalleFacturaParcialModel.getSubTotal());
                    facturaParcialModels.get(j).setTotal(facturaParcialModels.get(j).getTotal() + detalleFacturaParcialModel.getTotal());
                    existeFacturaParcial = true;
                }
            }
            if (!existeFacturaParcial) {
                FacturaParcialModel facturaParcialModel = new FacturaParcialModel();
                facturaParcialModel.setIdDocumento(documentoModel.getId());
                ArrayList<FacturaParcialModel> facturaParcialModelsCantidad = (ArrayList<FacturaParcialModel>) facturaParcialService.findAllByIdDocumentol(documentoModel.getId());
                facturaParcialModel.setNoDocumento(documentoModel.getNoDocumento() + "-" + facturaParcialModelsCantidad.size());
                facturaParcialModel.setDescuento(detalleFacturaParcialModel.getDescuento());
                facturaParcialModel.setEstado("activo");
                facturaParcialModel.setFlete(detalleFacturaParcialModel.getFlete());
                facturaParcialModel.setImpuestos(detalleFacturaParcialModel.getImpuestos());
                facturaParcialModel.setSubTotal(detalleFacturaParcialModel.getSubTotal());
                facturaParcialModel.setTotal(detalleFacturaParcialModel.getTotal());
                facturaParcialModel.setFecha(new Date());
                facturaParcialModel.addFacturaParcial(detalleFacturaParcialModel);

                facturaParcialModels.add(facturaParcialModel);
            }
        }

        for (int i = 0; i < facturaParcialModels.size(); i++) {
            facturaParcialService.save(facturaParcialModels.get(i));
            LOGGERAUDIT.info("El usuario "+usuarioModel.getNombres()+" generó la factura "+facturaParcialModels.get(i).getNoDocumento());
        }
    }

    private Double calcularFlete(Double flete, Integer cantidadAtendidad, Double peso, ArrayList<DetalleDocumentoModel> detalleDocumentoModels) {
        Double totalPeso = 0.0;
        for (int i = 0; i < detalleDocumentoModels.size(); i++) {
            totalPeso += detalleDocumentoModels.get(i).getIdLote().getProducto().getPeso() * detalleDocumentoModels.get(i).getCantidad();
        }
        return flete * peso * cantidadAtendidad / totalPeso;
    }

    @FXML
    void clickNuevo(MouseEvent event) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("despachosCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/despacho/Agregardespacho.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneDespacho.getChildren().setAll(pane);

    }

    @FXML
    void exportarExcel(MouseEvent event) {

    }

    private void eliminarPorConfirmar() {
        ArrayList<DespachoModel> despachoModels = despachoService.findByEstadoDespacho("por confirmar");
        for (int i = 0; i < despachoModels.size(); i++) {
            despachoService.deleteDespachoById(despachoModels.get(i).getId());
        }
    }

    private void actualizarResta(DespachoModel despachoModel) {
        System.out.println(despachoModel.getId());
        ArrayList<DetalleDespachoModel> detalleDespachos = (ArrayList) detalleDespachoService.findAllByIdDespacho(despachoModel);
        for (int i = 0; i < detalleDespachos.size(); i++) {
            DetalleEntregaModel detalleEntregaModel = detalleDespachos.get(i).getIddetalleEntrega();
            detalleEntregaModel.setCantidadEntregar(detalleEntregaModel.getCantidadEntregar() - detalleDespachos.get(i).getCantidadAtendida());

            movimientoKardex(detalleEntregaModel, detalleDespachos.get(i).getCantidadAtendida());

            if (detalleEntregaModel.getCantidadEntregar() == 0) {
                detalleEntregaModel.setEstadoEntrega("concluido");
            }
            detalleEntregaService.save(detalleEntregaModel);
        }
    }

    private void verificarDocumento(DespachoModel despachoModel) {
        ArrayList<DocumentoModel> documentoModels = new ArrayList<>();
        Boolean flagDocumento;
        for (int i = 0; i < despachoModel.getDetalleDespachos().size(); i++) {
            flagDocumento = false;
            DocumentoModel idDocumento = despachoModel.getDetalleDespachos().get(i).getIddetalleEntrega().getIdDetalleDocumento().getIdDocumento();
            for (int j = 0; j < documentoModels.size(); j++) {
                if (idDocumento.getId().compareTo(documentoModels.get(j).getId()) == 0) {
                    flagDocumento = true;
                    break;
                }
            }
            if (!flagDocumento) {
                documentoModels.add(idDocumento);
            }
        }

        DocumentoModel documentoModel;

        for (int z = 0; z < documentoModels.size(); z++) {
            documentoModel = documentoModels.get(z);
            ArrayList<DetalleDocumentoModel> detalleDocumentos = detalleDocumentoService.findAllDetalleDocumentobyDocumento_Id(documentoModel);
            Boolean flag = true;
            Integer i = 0, j = 0;
            while (flag && i < detalleDocumentos.size()) {
                ArrayList<DetalleEntregaModel> detalleEntregas = (ArrayList<DetalleEntregaModel>) detalleEntregaService.findAllByIdDetalleDocumento2(detalleDocumentos.get(i));
                while (flag && j < detalleEntregas.size()) {
                    if (detalleEntregas.get(j).getEstadoEntrega().compareTo("concluido") != 0) {
                        flag = false;
                    }
                    j = j + 1;
                }
                i = i + 1;
            }
            if (flag) {
                documentoModel.setEstado("concluido");
                documentoService.save(documentoModel);
                // generarPDFVenta(documentoModel);
                String logMensaje = "El usuario " + UsuarioSingleton.getInstance().getUsuarioModel().getNombres() + "confirmó despacho " + documentoModel.getNoDocumento();
                LOGGERAUDIT.info(logMensaje);
            }
        }
    }

    private void generarPDFVenta(DocumentoModel documentoModel) {
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

    private void generarPDFdespacho(DespachoModel despachoModel) {
        //List<DetalleDocumentoModel> detalles = detalleDocumentoService.findAllDetalleDocumentobyDocumento_Id(documentoModel);
        //ReporteVenta reporteVenta = new ReporteVenta();
        //reporteVenta.pdfventa(documentoModel, detalles);
        List<DetalleDespachoModel> detalles = detalleDespachoService.findAllByIdDespacho(despachoModel);
        ReporteDespacho reporteDespacho = new ReporteDespacho();
        reporteDespacho.pdfdevolucion(despachoModel, detalles);
    }


    private void movimientoKardex(DetalleEntregaModel detalleEntrega, Integer cantidadAtendida) {

        List<Integer> bloquesdistinct = kardexService.findAllDistinctForLote(detalleEntrega.getIdDetalleDocumento().getIdLote().getId());
        //System.out.println(bloquesdistinct.size());
        KardexModel ultimaoperacion = new KardexModel();
        Integer cantidadporsalir = cantidadAtendida;
        Integer cantidadensalida;
        if (bloquesdistinct.isEmpty()) {
            Validador.mostrarDialogError("No hay stock de ese lote");
        }
        for (int i = 0; i < bloquesdistinct.size(); i++) {
            if (cantidadporsalir == 0) {
                System.out.println("Entra al break qe ua es cero");
                break;
            }
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
                MotivoOperacionKardexModel motivo = motivoOperacionKardexService.getByNombreAndTipo_id("por despacho", 2);

                nuevaoperacion.setMotivooperacion(motivo);
                nuevaoperacion.setLote(detalleEntrega.getIdDetalleDocumento().getIdLote());
                nuevaoperacion.setBloque(bloqueService.getById(bloquesdistinct.get(i)));
                cantidadporsalir = cantidadporsalir - cantidadensalida;
                nuevaoperacion.setStockfisico(stockfisico);
                nuevaoperacion.setDescripcion("por  detalle" + detalleEntrega.getId());
                kardexService.save(nuevaoperacion);
                if (nuevaoperacion.getStockfisico() == 0) {
                    //update al bloque que pase a estado activo
                    bloqueService.updateEstadoaActivo(nuevaoperacion.getBloque().getId());
                }

                //loteService.reduceStockDisponible(detalleEntrega.getIdDetalleDocumento().getIdLote().getId(), detalleEntrega.getIdDetalleDocumento().getIdLote().getStockDisponible() - nuevaoperacion.getCantidad());

            }

        }
        if (cantidadporsalir > 0) {
            Integer cantidadsacadafinal = cantidadAtendida - cantidadporsalir;
            //Validador.mostrarDialogError("No hubo stock suficiente, solo se retiro " + cantidadsacadafinal.toString());
            //System.out.println("No hubo stock suficiente, solo se retiro "+ cantidadsacadafinal.toString());
        }
    }
}