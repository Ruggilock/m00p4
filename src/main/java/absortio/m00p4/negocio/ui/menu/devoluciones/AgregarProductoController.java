package absortio.m00p4.negocio.ui.menu.devoluciones;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.auxiliares.DetalleFacturaParcial;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import absortio.m00p4.negocio.service.*;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AgregarProductoController implements Initializable {

    @FXML
    Pane paneProductoSeleccionar;

    ObservableList<DetalleFacturaParcial> detalles;

    @Autowired
    ProductoService productoService;
    @Autowired
    CategoriaProductoService categoriaProductoService;
    @FXML
    TextField textFieldLote;
    @FXML
    TextField textFieldNombre;

    @FXML
    private TableView<DetalleFacturaParcial> tablaDetalles;
    @FXML
    private TableColumn columnaN;
    @FXML
    private TableColumn columnaCodigo;
    @FXML
    private TableColumn columnaNombre;
    @FXML
    private TableColumn columnaLote;
    @FXML
    private TableColumn columnaPrecioUnit;
    @FXML
    private TableColumn columnaDescuentoUnit;
    @FXML
    private TableColumn columnaCantidad;
    @FXML
    private TableColumn columnaAcciones;
    @FXML
    private Label labelDesc;

    @Autowired
    private ApplicationContext context;


    @Autowired
    private CondicionesComercialesService condicionService;

    @Autowired
    private PrecioService precioService;

    @Autowired
    private DetalleFacturaParcialService detalleFacturaParcialService;

    @Autowired
    private  DocumentoService documentoService;

    @Autowired
    private  DevolucionService devolucionService;

    @Autowired
    private  LoteService loteService;

    @Autowired
    private  FacturaParcialService facturaParcialService;

    @Autowired
    private  DetalleDespachoService detalleDespachoService;

    private List<Label> listaLabels;

    DetalleFacturaParcial detalleFacturaParcial;

    TextField text;

    String nodocumento;

    List<Integer> listaCantidades;

    ObservableList<DetalleFacturaParcial> detallesOL;

    TableView<Producto> tableViewProductos;
    TableView<DetalleFacturaParcial> tablaNuevaVenta;
    int codigoP=0,cantP=0,asignoRegalo=0,ventana;
    @Autowired
    private DetalleDevolucionService detalleDevolucionService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicializarTablaProductos ();
        //detalleFacturaParcial=new DetalleFacturaParcial();

    }


    private void inicializarTablaProductos() {
        columnaN.setCellValueFactory ( new PropertyValueFactory<Producto, Integer>( "n" ) );
        columnaCodigo.setCellValueFactory ( new PropertyValueFactory<Producto, String> ( "codigobarras" ) );
        columnaNombre.setCellValueFactory ( new PropertyValueFactory<Producto, String> ( "nombreproducto" ) );
        columnaLote.setCellValueFactory ( new PropertyValueFactory<Producto, Integer> ( "lote" ) );
        columnaPrecioUnit.setCellValueFactory ( new PropertyValueFactory<Producto, Double> ( "preciounitario" ) );
        columnaDescuentoUnit.setCellValueFactory ( new PropertyValueFactory<Producto, Double> ( "descuentounitario" ) );
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("cantidad"));
        columnaAcciones.setCellValueFactory ( new PropertyValueFactory<> ( "button" ) );
        agregarBotones ();
        detalles = FXCollections.observableArrayList ();
        tablaDetalles.setItems ( detalles );
    }
    private void cargarGrilla() {
        //DocumentoModel doc=documentoService.getByNoDocumento(nodocumento);
        FacturaParcialModel doc=facturaParcialService.getByNoDocumento(nodocumento);
        //ArrayList<DetalleFacturaModel> detalles = (ArrayList) DetalleFacturaService.findAllDetalleFacturabyDocumento_Id(doc);
        ArrayList<DetalleFacturaParcialModel> detalles=(ArrayList)  detalleFacturaParcialService.findAllByFacturaParcialModel_NoDocumento(nodocumento);
        for(int i=0; i<detalles.size();i++){
            DetalleFacturaParcial detalleDocumento=new DetalleFacturaParcial();
            detalleDocumento.setN(i);
            LoteModel loteModel=loteService.getById(detalles.get(i).getLote());
            detalleDocumento.setCodigobarras(loteModel.getProducto().getCodigoBarras());
            detalleDocumento.setLote(loteModel.getId());
            Integer cantidadentregada=hallarCantidadEntregadaxDetalleFacturaParcial(detalles.get(i),doc);
            DetalleDespachoModel detalleDespacho=detalleDespachoService.getById(detalles.get(i).getIdDetalleDespacho());
            detalleDocumento.setCantidad(detalleDespacho.getCantidadAtendida());
            detalleDocumento.setNombreproducto(loteModel.getProducto().getNombre());
            detalleDocumento.setDescuentounitario(detalles.get(i).getDescuento()/detalleDocumento.getCantidad());
            detalleDocumento.setPreciounitario(detalleDespacho.getIddetalleEntrega().getIdDetalleDocumento().getpU());
            if(!contieneLote(detallesOL, detalleDocumento.getLote()) && detalleDocumento.getCantidad()>0)
                if (!contieneLote(this.detalles, detalleDocumento.getLote()))
                    this.detalles.add(detalleDocumento);
                else
                    actualizarCantidadLote(this.detalles, detalleDocumento.getLote(), detalleDocumento.getCantidad());
        }

        ArrayList<DevolucionModel> devoluciones = (ArrayList) devolucionService.findAllDevolucionByDocumento(doc.getId());
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

        for (Integer idLote : lotesACantidadDevuelta.keySet()){
            actualizarCantidadLote(this.detalles,idLote,-lotesACantidadDevuelta.get(idLote));
        }

    }

    private void actualizarCantidadLote (ObservableList<DetalleFacturaParcial> detallesOL, Integer idLote, Integer cantidadAnadir) {
        for (DetalleFacturaParcial detalle : detallesOL) {
            if (detalle.getLote().equals(idLote)) {
                detalle.setCantidad(detalle.getCantidad() + cantidadAnadir);
                if (detalle.getCantidad() <= 0)
                    detallesOL.remove(detalle);
                return;
            }
        }
    }

    private boolean contieneLote(ObservableList<DetalleFacturaParcial> detallesOL, Integer idLote) {
        for (DetalleFacturaParcial detalle : detallesOL) {
            if (detalle.getLote().equals(idLote))
                return true;
        }
        return false;
    }


    private Integer hallarCantidadEntregadaxDetalleFacturaParcial(DetalleFacturaParcialModel detalle,FacturaParcialModel doc){
        List<DevolucionModel> devoluciones=devolucionService.findAllDevolucionByDocumento(doc.getId());
        Integer cantidadyadevuelta=0;
        for(int i=0;i<devoluciones.size();i++){
            List<DetalleDevolucionModel> detallesdevolucion=devoluciones.get(i).getDetallesdev();
            for(int j=0;j<detallesdevolucion.size();j++){
                if(detalle.getLote().equals(detallesdevolucion.get(j).getIdLote().getId())){
                    cantidadyadevuelta=cantidadyadevuelta+detallesdevolucion.get(j).getCantidad();
                }
            }
        }
        return cantidadyadevuelta;
    }

    private void agregarBotones() {
        Callback<TableColumn<DetalleFacturaParcial, Void>, TableCell<DetalleFacturaParcial, Void>> cellFactory = new Callback<TableColumn<DetalleFacturaParcial, Void>, TableCell<DetalleFacturaParcial, Void>> () {
            @Override
            public TableCell<DetalleFacturaParcial, Void> call(final TableColumn<DetalleFacturaParcial, Void> param) {
                final TableCell<DetalleFacturaParcial, Void> cell = new TableCell<DetalleFacturaParcial, Void> () {
                    private final Button btnSelect = new Button ();
                    {
                        btnSelect.setOnAction ( (event) -> {
                            // se captura el seleccionado , se almacena y se cierra
                            DetalleFacturaParcial data = getTableView ().getItems ().get ( getIndex () );
                            System.out.println(data.getNombreproducto());
                            System.out.println(data.getNombreproducto());
                            detalleFacturaParcial.asignarObjeto(data);
                            //detalleFacturaParcial.getComboBox().setValue("0");


                            detalleFacturaParcial.getTextfield().setOnAction((event2 -> {
                                int inde = getIndex();
                                inde = hallarIndiceListaCantidades();
                                listaCantidades.add(inde,Integer.valueOf(detallesOL.get(inde).getTextfield().getText()));

                                System.out.println(detallesOL.get(inde).getNombreproducto());
                                System.out.println(detallesOL.get(inde).getTextfield().getText());
                                Double importe=Integer.parseInt(detallesOL.get(inde).getTextfield().getText())*(detallesOL.get(inde).getPreciounitario());
                                detallesOL.get(inde).setImporte(importe);
                                detallesOL.get(inde).setCantidad(Integer.parseInt(detallesOL.get(inde).getTextfield().getText()));
                                tablaNuevaVenta.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                                actualizarListaLabels();
                            }));


                            detalleFacturaParcial.getButtonEliminar().setOnAction((event3) -> {
                                DetalleFacturaParcial pro = getTableView().getItems().get(getIndex());

                                //Aca llamar a la ventana de confirmacion de Eliminar

                                Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                                dialogoAlerta.setTitle("Ventana de Confirmacion");
                                dialogoAlerta.setHeaderText(null);
                                dialogoAlerta.initStyle(StageStyle.UTILITY);
                                dialogoAlerta.setContentText("Realmente Quieres Elimarlo");
                                Optional<ButtonType> result = dialogoAlerta.showAndWait();
                                if (result.get() == ButtonType.OK) {

                                    System.out.println(hallarIndice(pro));
                                    detallesOL.remove(hallarIndice(pro));


                                    System.out.println("tamaño "+detallesOL.size());
                                    tablaNuevaVenta.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                                    actualizarListaLabels();
                                }
                                //setUsuarios();
                            });
                            listaCantidades.add(0);
                            detallesOL.add(detalleFacturaParcial);
                            actualizarListaLabels();

                            ((Node)(event.getSource())).getScene().getWindow().hide(); //para cerrar la ventana
                        } );
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem ( item, empty );
                        if (empty) {
                            setGraphic ( null );
                        } else {

                            Image image = new Image ( getClass ().getResourceAsStream ( "/fxml/imagenes/select.jpg" ) );
                            ImageView iv1 = new ImageView ( image );
                            iv1.setFitHeight ( 17 );
                            iv1.setFitWidth ( 20 );
                            btnSelect.setGraphic ( iv1 );

                            HBox pane = new HBox (btnSelect);
                            pane.setSpacing ( 5 );
                            setGraphic ( pane );
                            setAlignment ( Pos.CENTER );

                        }
                    }
                };
                return cell;
            }
        };
        columnaAcciones.setCellFactory ( cellFactory );
    }


    /***************************metodos para ventas nuevas********************************************************/



    private int hallarIndice(DetalleFacturaParcial pro){  // calcula el indice de un producto en el observable list
        int ind = 0;
        for (int i=0;i<detallesOL.size();i++){
            if(detallesOL.get(i).getId()==pro.getId()){
                ind = i;
                break;
            }
        }
        return ind;
    }

    private void actualizarListaLabels(){
        Double acum=0.0;
        Double descuentos =0.0;
        for (int i = 0; i<detallesOL.size();i++){
            acum += detallesOL.get(i).getImporte();
            descuentos += detallesOL.get(i).getDescuentounitario()*detallesOL.get(i).getCantidad();
        }

        listaLabels.get(0).setText(String.valueOf(Math.rint(acum*100)/100));
        listaLabels.get(1).setText(String.valueOf( Math.rint(acum*0.18*100)/100));
        listaLabels.get(2).setText(String.valueOf(Math.rint(descuentos*100)/100));
        listaLabels.get(3).setText(String.valueOf(Math.rint((acum*1.18-descuentos)*100)/100));
    }
    @FXML
    void buscar(MouseEvent event) throws IOException {
        this.inicializarTablaProductos ();

    }


    @FXML
    public void asignar(Producto data,TextField dataT,Label dataL, String nodocumento){

        //producto = data;
        text = dataT;
        labelDesc=dataL;
        ventana = 1;
        cargarGrilla ();
        this.nodocumento =nodocumento;
    }

    @FXML
    public void asignarAventas(DetalleFacturaParcial dataP,ObservableList<DetalleFacturaParcial> dataPr,TableView<DetalleFacturaParcial> t,List<Label> listaLabels,String nodocumento,List<Integer> listaCantidades){

        detalleFacturaParcial = dataP;
        ventana = 3;
        detallesOL = dataPr;
        tablaNuevaVenta = t;
        this.listaLabels = listaLabels;
        this.nodocumento =nodocumento;
        this.listaCantidades = listaCantidades;
        cargarGrilla ();

    }
    private Double calcularDescuento(String codigo,int cantidad,Double precio){
        ArrayList<CondicionesComercialesModel> condicionModels = (ArrayList) condicionService.findAllCondicion();
        Double desc = 0.0;
        int indicador1=0,indicador2=0,indicador3=0,indicador4=0,id,cant1,cant2;
        String categoria;
        for (int i = 0; i<condicionModels.size();i++){
            if(condicionModels.get(i).getTipo().compareTo("cantidad*cantidad")==0){//indicador1,2x1
                id = productoService.hallarIdByCodigoBarras(codigo);
                if(id==condicionModels.get(i).getCodigo1() && indicador1 ==0){
                    indicador1 = 1;
                    cant1 = condicionModels.get(i).getCantidad1();
                    cant2 = condicionModels.get(i).getCantidad2();
                    desc += (cant1-cant2)*(cantidad/cant1)*precio;
                }
            }else if (condicionModels.get(i).getTipo().compareTo("cantidad*gratis")==0){//indicador2,100x1
                id = productoService.hallarIdByCodigoBarras(codigo);
                if (id==condicionModels.get(i).getCodigo1()&& indicador2 ==0){
                    cant1 = cantidad/condicionModels.get(i).getCantidad1();
                    if(cant1>=0){
                        indicador2=1;
                        codigoP = condicionModels.get(i).getCodigo2();
                        cantP = cant1*condicionModels.get(i).getCantidad2();
                    }
                }
            }else if(condicionModels.get(i).getTipo().compareTo("porcentaje*producto")==0 && indicador3==0){//indicador3
                id = productoService.hallarIdByCodigoBarras(codigo);
                if(id==condicionModels.get(i).getCodigo1()&& indicador3 == 0){
                    indicador3 = 1;
                    desc += cantidad*precio*condicionModels.get(i).getPorcentaje()/100;
                }
            }else if(condicionModels.get(i).getTipo().compareTo("porcentaje*categoria")==0 && indicador4==0){//indicador4
                categoria = productoService.hallarNombreCategoria(codigo);
                if (categoria.compareTo(condicionModels.get(i).getCategoria())==0){
                    indicador4 = 1;
                    desc += cantidad*precio*condicionModels.get(i).getPorcentaje();
                }
            }
            if(indicador3 == 1 && indicador4 ==1) break;
            if(indicador1==1) break;
            if(indicador2==1) break;

        }
        //descuento = desc;

        return desc;
    }

    private Double hallarPrecio(List<PrecioModel> listaPrecios){ // calcula el precio más actual
        int indice = 0;
        Date date=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            date = sdf.parse("1900-01-01");
        }catch (ParseException e){
            e.printStackTrace();
        }

        for (int i = 0; i<listaPrecios.size();i++){
            if(date.compareTo(listaPrecios.get(i).getFechaInicio())<0){
                indice = i;
                date = listaPrecios.get(i).getFechaFin();
            }
        }
        return listaPrecios.get(indice).getPrecio();
    }


    private int hallarIndiceListaCantidades(){
        int index=0;
        int num ;
        for (int i = 0;i< listaCantidades.size();i++){
            num = Integer.valueOf((detallesOL.get(i).getTextfield().getText().toString()));
            if(num!=listaCantidades.get(i) && listaCantidades.get(i) != -1){
                index = i;
                break;
            }
        }
        return index;
    }
}
