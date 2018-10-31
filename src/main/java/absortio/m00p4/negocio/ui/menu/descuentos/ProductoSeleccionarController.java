package absortio.m00p4.negocio.ui.menu.descuentos;


import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.model.CondicionesComercialesModel;
import absortio.m00p4.negocio.model.PrecioModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.FormateadorDecimal;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import absortio.m00p4.negocio.service.*;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Component
public class ProductoSeleccionarController implements Initializable {
    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    @FXML
    Pane paneProductoSeleccionar;
    ObservableList<Producto> productos;
    @Autowired
    ProductoService productoService;
    @Autowired
    CategoriaProductoService categoriaProductoService;
    @FXML
    TextField textFieldCodigoBarras;
    @FXML
    TextField textFieldNombre;
    @FXML
    ComboBox<String> comboBoxMarca;
    @FXML
    ComboBox<String> comboBoxCategoria;
    @FXML
    ComboBox<String> comboBoxModelo;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn columnaN;
    @FXML
    private TableColumn columnaCodigo;
    @FXML
    private TableColumn columnaNombre;
    @FXML
    private TableColumn columnaMarca;
    @FXML
    private TableColumn columnaCategoria;
    @FXML
    private TableColumn columnaModelo;
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
    private List<Label> listaLabels;

    List<Integer> listaCantidades;

    Producto producto;
    TextField text;

    ObservableList<Producto> productosOL;

    TableView<Producto> tableViewProductos;
    TableView<Producto> tablaNuevaVenta;
    TableView<Producto> tablaEditarVenta;
    int codigoP = 0, cantP = 0, asignoRegalo = 0, ventana;
    List<Producto> listaProductosSel;

    ComboBox comboBoxMoneda;
    @Autowired
    MonedaService monedaService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicializarTablaProductos();
        cargarComboBox();

    }


    private void inicializarTablaProductos() {

        columnaN.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("n"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigo"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombre"));
        columnaMarca.setCellValueFactory(new PropertyValueFactory<Producto, String>("marca"));
        columnaCategoria.setCellValueFactory(new PropertyValueFactory<Producto, String>("categoria"));
        columnaModelo.setCellValueFactory(new PropertyValueFactory<Producto, String>("modelo"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadMax"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<>("button"));

        agregarBotones();
        productos = FXCollections.observableArrayList();
        tablaProductos.setItems(productos);
    }

    private Integer seEncuentraEnOL(Producto producto) {
        Integer ind = 0;
        for (int i = 0; i < productosOL.size(); i++) {
            if (producto.getCodigo().compareTo(productosOL.get(i).getCodigo()) == 0) {
                ind = 1;
                break;
            }
        }
        return ind;
    }

    private Integer seEncuentraEnOL2(String codigo) {
        Integer ind = 0;
        for (int i = 0; i < productosOL.size(); i++) {
            if (codigo.compareTo(productosOL.get(i).getCodigo()) == 0) {
                ind = 1;
                break;
            }
        }
        return ind;
    }

    private void cargarGrilla2() {

        for (int i = 0; i < listaProductosSel.size(); i++) {
            productos.add(listaProductosSel.get(i));
        }
    }

    private void cargarGrilla() {
        int res = 0;
        String codigo;
        ArrayList<ProductoModel> productoModels = (ArrayList) productoService.findAllProducto();
        for (int i = 0; i < productoModels.size(); i++) {
            Producto producto = new Producto();
            if (ventana == 2) res = seEncuentraEnOL2(productoModels.get(i).getCodigoBarras());
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

            if (ventana == 1) {
                productos.add(producto);
            } else if (res == 0) {

                if (producto.getCantidadMax() > 0 /*&& res==0*/) {
                    //res = seEncuentraEnOL((producto));
                    //if(res ==0)
                    productos.add(producto);
                }
            }
        }
    }

    private void agregarBotones() {
        Callback<TableColumn<Producto, Void>, TableCell<Producto, Void>> cellFactory = new Callback<TableColumn<Producto, Void>, TableCell<Producto, Void>>() {
            @Override
            public TableCell<Producto, Void> call(final TableColumn<Producto, Void> param) {
                final TableCell<Producto, Void> cell = new TableCell<Producto, Void>() {

                    private final Button btnSelect = new Button();

                    {
                        btnSelect.setOnAction((event) -> {
                            // se captura el seleccionado , se almacena y se cierra
                            Producto data = getTableView().getItems().get(getIndex());
                            if (ventana == 1) {// seleccion para descuentos
                                producto.asignarObjeto(data);
                                text.setText(data.getNombre());
                                //labelDesc.setText(data.getDescripcion());
                            } else if (ventana == 2) {// seleccion de productos para ventas

                                producto.asignarObjeto(data);
                                eliminarProdXcodigoEnList(producto.getCodigo());
                                codigoP = 0;
                                cantP = 0;

                                Integer cod = productoService.hallarIdByCodigoBarras(producto.getCodigo());
                                producto.setPrecio(productoService.buscarPrecioActualById(cod));

                                producto.setCantidadMax(productoService.hallarStockDisponibleById(productoService.hallarIdByCodigoBarras(producto.getCodigo())));
                                //producto.setDescuento(calcularDescuento(producto.getCodigo(),200,producto.getPrecio()));
                                Producto proReg = new Producto();
                                if (codigoP != 0) {// asignar producto de regalo
                                    producto.setIdRegalo(codigoP);
                                    ProductoModel productoM = productoService.hallarProductoXid(codigoP);
                                    proReg.getComboBox().setValue(String.valueOf(cantP)); //proReg.getComboBox().setEditable(false);
                                    proReg.getComboBox().setDisable(true);
                                    proReg.setImporte(0.0);
                                    proReg.setPrecio(0.0);
                                    proReg.setUnidadVenta(productoM.getUnidadVenta());
                                    proReg.setDescuento(0.0);
                                    proReg.setNombre(productoM.getNombre());
                                    proReg.setCodigo(productoM.getCodigoBarras());

                                }
                                producto.setDescuento(0.0);
                                producto.setImporte(0.0);

                                asignoRegalo = 0;
                                producto.getComboBox().setValue("0");

                                producto.getComboBox().setOnAction((event2 -> {
                                    int inde;
                                    inde = hallarIndiceListaCantidades();

                                    listaCantidades.set(inde, Integer.valueOf(productosOL.get(inde).getComboBox().getValue()));
                                    if (Integer.parseInt(productosOL.get(inde).getComboBox().getValue().toString()) > productosOL.get(inde).getCantidadMax()) {
                                        // se sobrepaso la cantidad maxima
                                        productosOL.get(inde).getComboBox().setValue(String.valueOf(productosOL.get(inde).getCantidadMax())); //se asigna la cantidad Maxima
                                    }

                                    Double valDescuento = calcularDescuento(productosOL.get(inde).getCodigo(), Integer.parseInt(productosOL.get(inde).getComboBox().getValue().toString()), productosOL.get(inde).getPrecio());
                                    String strDescuento = FormateadorDecimal.formatear(valDescuento);
                                    Double valDescuento2 = Double.parseDouble(strDescuento);
                                    productosOL.get(inde).setDescuento(valDescuento2);
                                    int stockRegalo = productoService.hallarStockDisponibleById(codigoP);

                                    if (codigoP != 0 && asignoRegalo == 0 && cantP > 0 && stockRegalo > 0) {// asignar producto de regalo

                                        asignoRegalo = 1;
                                        ProductoModel productoM = productoService.hallarProductoXid(codigoP);
                                        proReg.getComboBox().setValue(String.valueOf(cantP)); //proReg.getComboBox().setEditable(false);
                                        proReg.getComboBox().setDisable(true);
                                        proReg.setImporte(0.0);
                                        proReg.setPrecio(0.0);
                                        proReg.setUnidadVenta(productoM.getUnidadVenta());
                                        proReg.setDescuento(0.0);
                                        proReg.setNombre(productoM.getNombre());
                                        proReg.setCodigo(productoM.getCodigoBarras());
                                        productosOL.get(inde).setIdRegalo(codigoP);
                                        productosOL.add(proReg);
                                        listaCantidades.add(-1);
                                    } else if (codigoP != 0 && asignoRegalo == 0 && cantP == 0) {//no se llego a la cantidad
                                        asignoRegalo = 0;
                                    } else if (codigoP != 0 && asignoRegalo == 1 && cantP == 0) {// si asigno regalo pero se disminuye la cantidad
                                        productosOL.remove(proReg);
                                        asignoRegalo = 0;

                                    } else if (codigoP != 0 && asignoRegalo == 1 && cantP > 0) {
                                        proReg.getComboBox().setValue(String.valueOf((cantP)));

                                    }

                                    productosOL.get(inde).setImporte(Math.rint(Integer.parseInt(productosOL.get(inde).getComboBox().getValue().toString()) * productosOL.get(inde).getPrecio() * 100) / 100);
                                    tablaNuevaVenta.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE); // para refrescar la tabla*//*
                                    actualizarListaLabels();
                                }));
                                producto.getButtonEliminar().setText("Quitar");
                                producto.getButtonEliminar().setOnAction((event3) -> {
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
                                        listaCantidades.remove(hallarIndice(pro));
                                        if (producto.getIdRegalo() != 0) {
                                            productosOL.remove(hallarIndiceXid(producto.getIdRegalo()));
                                            asignoRegalo = 0;
                                            for (int k = 0; k < listaCantidades.size(); k++) {
                                                if (listaCantidades.get(k) == -1) {
                                                    listaCantidades.remove(k);
                                                    break;
                                                }
                                            }
                                        }
                                        System.out.println("tamaño " + productosOL.size());
                                        tablaNuevaVenta.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                                    }
                                    //setUsuarios();
                                    actualizarListaLabels();
                                });
                                listaCantidades.add(0);
                                productosOL.add(producto);
                                actualizarListaLabels();
                                //if(codigoP!=0) productosOL.add(proReg);

                            } else if (ventana == 3) {
                                producto.asignarObjeto(data);
                                productosOL.add(producto);

                                //productosOL.add(producto);
                            }


                            ((Node) (event.getSource())).getScene().getWindow().hide(); //para cerrar la ventana
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {

                            Image image = new Image(getClass().getResourceAsStream("/fxml/imagenes/select.jpg"));
                            ImageView iv1 = new ImageView(image);
                            iv1.setFitHeight(17);
                            iv1.setFitWidth(20);
                            btnSelect.setGraphic(iv1);

                            HBox pane = new HBox(btnSelect);
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

    /***************************metodos para ventas nuevas********************************************************/


    private int hallarIndiceListaCantidades() {
        int index = 0;
        int num;
        for (int i = 0; i < listaCantidades.size(); i++) {
            num = Integer.valueOf((productosOL.get(i).getComboBox().getValue().toString()));
            if (num != listaCantidades.get(i) && listaCantidades.get(i) != -1) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void eliminarProdXcodigoEnList(String codigo) {
        for (int i = 0; i < listaProductosSel.size(); i++) {
            if (codigo.compareTo(listaProductosSel.get(i).getCodigo()) == 0) {
                listaProductosSel.remove(i);
                break;
            }
        }
    }

    private int hallarIndiceXid(int id) {
        int ind = 0;
        String codigo = productoService.hallarCodigoById(id);
        for (int i = 0; i < productosOL.size(); i++) {
            if (productosOL.get(i).getCodigo().compareTo(codigo) == 0) {
                ind = i;
                break;
            }
        }
        return ind;
    }

    private int hallarIndice(Producto pro) {  // calcula el indice de un producto en el observable list
        int ind = 0;
        for (int i = 0; i < productosOL.size(); i++) {
            if (productosOL.get(i).getCodigo().compareTo(pro.getCodigo()) == 0) {
                ind = i;
                break;
            }
        }
        return ind;
    }

    private void actualizarListaLabels() {
        Double acum = 0.0;
        Double descuentos = 0.0;
        for (int i = 0; i < productosOL.size(); i++) {
            acum += productosOL.get(i).getImporte();
            descuentos += productosOL.get(i).getDescuento();
        }
        listaLabels.get(0).setText(FormateadorDecimal.formatear(Math.rint(acum * 100) / 100));
        listaLabels.get(1).setText(FormateadorDecimal.formatear(Math.rint(acum * 0.18 * 100) / 100));
        listaLabels.get(3).setText(FormateadorDecimal.formatear(Math.rint(descuentos * 100) / 100));
        listaLabels.get(4).setText(FormateadorDecimal.formatear(Math.rint(((acum * 1.18 - descuentos + Double.valueOf(listaLabels.get(2).getText())) * 100)) / 100));
    }

    @FXML
    void clickBuscar(MouseEvent event) throws IOException {
        this.inicializarTablaProductos();

        String nombre = textFieldNombre.getText();
        String codigo = textFieldCodigoBarras.getText();
        String categoriaNombre = comboBoxCategoria.getValue();
        String marca = comboBoxMarca.getValue();
        String modelo = comboBoxModelo.getValue();


        if (categoriaNombre.compareTo("-Seleccione-") == 0) {
            categoriaNombre = "";
        }
        if (marca.compareTo("-Seleccione-") == 0) {
            marca = "";
        }
        if (modelo.compareTo("-Seleccione-") == 0) {
            modelo = "";
        }

        //CategoriaProductoModel categoriaProducto = categoriaProductoService.obtenerCategoriaPorNombre ( categoriaNombre );
        ArrayList<ProductoModel> productosfiltrados = null;
        if (ventana == 1) {
            productosfiltrados = (ArrayList) productoService.findByAllFiltros(codigo, nombre, categoriaNombre, marca, modelo);
        } else if (ventana == 2 || ventana == 3) {
            productosfiltrados = (ArrayList) productoService.findByAllFiltrosConStockDisponible(codigo, nombre, categoriaNombre, marca, modelo);
        }

        Integer res;
        // = (ArrayList) productoService.filtrarProductostodos ( nombre, codigo, marca, modelo, categoriaProducto );
        for (int i = 0; i < productosfiltrados.size(); i++) {
            Producto producto = new Producto();
            res = seEncuentraEnOL2(productosfiltrados.get(i).getCodigoBarras());
            producto.setCodigo(productosfiltrados.get(i).getCodigoBarras());
            producto.setN(i + 1);
            producto.setNombre(productosfiltrados.get(i).getNombre());
            producto.setMarca(productosfiltrados.get(i).getMarca());
            producto.setUnidadVenta(productosfiltrados.get(i).getUnidadVenta());
            producto.setCantidadMax(productoService.hallarStockDisponibleById(productoService.hallarIdByCodigoBarras(producto.getCodigo())));
            producto.setModelo(productosfiltrados.get(i).getModelo());
            producto.setDescripcion(productosfiltrados.get(i).getDescripcion());
            producto.setCategoria(productosfiltrados.get(i).getCategoriaProducto().getNombre());
            producto.setCantidadMax(productoService.hallarStockDisponibleById(productoService.hallarIdByCodigoBarras(producto.getCodigo())));
            if (ventana == 1) {
                productos.add(producto);
            } else if (res == 0) {

                if (producto.getCantidadMax() > 0 /*&& res==0*/) {
                    //res = seEncuentraEnOL((producto));
                    //if(res ==0)
                    productos.add(producto);
                }
            }
        }
    }

    public void cargarComboBox() {
        ArrayList<String> categorias = new ArrayList<>();
        ArrayList<CategoriaProductoModel> categoriasModels = (ArrayList<CategoriaProductoModel>) categoriaProductoService.findAllCategoriaProducto();
        for (int i = 0; i < categoriasModels.size(); i++) {
            categorias.add(categoriasModels.get(i).getNombre());
        }
        ObservableList<String> observableListCategorias = FXCollections.observableArrayList(categorias);
        comboBoxCategoria.getItems().add("-Seleccione-");
        comboBoxCategoria.getItems().addAll(observableListCategorias);
        comboBoxCategoria.setEditable(false);
        comboBoxCategoria.setValue(comboBoxCategoria.getItems().get(0));
        TextFields.bindAutoCompletion(comboBoxCategoria.getEditor(), comboBoxCategoria.getItems());


        ArrayList<String> modelosAux = new ArrayList<>();
        modelosAux.add("-Seleccione-");
        modelosAux.addAll(productoService.findAllMarca());
        ObservableList<String> marcas = FXCollections.observableArrayList(modelosAux);
        comboBoxMarca.getItems().addAll(marcas);
        comboBoxMarca.setEditable(false);
        comboBoxMarca.setValue(marcas.get(0));
        TextFields.bindAutoCompletion(comboBoxMarca.getEditor(), comboBoxMarca.getItems());

        modelosAux.clear();
        modelosAux.add("-Seleccione-");
        modelosAux.addAll(productoService.findAllModelo());
        ObservableList<String> modelos = FXCollections.observableArrayList(modelosAux);
        comboBoxModelo.getItems().addAll(modelos);
        comboBoxModelo.setEditable(false);
        comboBoxModelo.setValue(modelos.get(0));
        TextFields.bindAutoCompletion(comboBoxModelo.getEditor(), comboBoxModelo.getItems());
    }

    @FXML
    public void asignar(Producto data, TextField dataT, Label dataL) {

        producto = data;
        text = dataT;
        labelDesc = dataL;
        ventana = 1;
        cargarGrilla();
    }

    @FXML
    public void asignarAventas(Producto dataP, ObservableList<Producto> dataPr, TableView<Producto> t, List<Label> listaLabels, List<Producto> listaProductosSel, List<Integer> listaCantidades, ComboBox comboBoxMoneda) {

        producto = dataP;
        ventana = 2;
        productosOL = dataPr;
        tablaNuevaVenta = t;
        this.listaLabels = listaLabels;
        this.comboBoxMoneda = comboBoxMoneda;

        this.listaProductosSel = listaProductosSel;
        this.listaCantidades = listaCantidades;
        cargarGrilla();
    }

    @FXML
    public void asignarAeditar(Producto producto, ObservableList<Producto> dataPr, TableView<Producto> t) {
        this.producto = producto;
        ventana = 3;
        productosOL = dataPr;
        tablaEditarVenta = t;
        cargarGrilla();
    }

    private Double calcularDescuento(String codigo, int cantidad, Double precio) {
        ArrayList<CondicionesComercialesModel> condicionModels = (ArrayList) condicionService.findAllOrdenado();
        Double desc = 0.0;
        int indicador1 = 0, indicador2 = 0, indicador3 = 0, indicador4 = 0, id, cant1, cant2;
        String categoria;
        for (int i = 0; i < condicionModels.size(); i++) {
            if(condicionModels.get(i).getFechaIni().compareTo(java.sql.Date.valueOf(LocalDate.now())) > 0
                    || condicionModels.get(i).getFechaFin().compareTo(java.sql.Date.valueOf(LocalDate.now())) < 0)
                continue; // El descuento aún/ya no es válido
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
                if (id == condicionModels.get(i).getCodigo1()) {
                    indicador3 = 1;
                    if (indicador4 == 0)
                        desc += cantidad * precio * condicionModels.get(i).getPorcentaje() / 100;
                    else {
                        Double precioNuevoTotal = precio * cantidad - desc;
                        desc += precioNuevoTotal * condicionModels.get(i).getPorcentaje() / 100;
                    }
                }
            } else if (condicionModels.get(i).getTipo().compareTo("porcentaje*categoria") == 0 && indicador4 == 0) {//indicador4
                categoria = productoService.hallarNombreCategoria(codigo);
                if (categoria.compareTo(condicionModels.get(i).getCategoria()) == 0) {
                    indicador4 = 1;
                    if (indicador3 == 0)
                        desc += cantidad * precio * condicionModels.get(i).getPorcentaje() / 100;
                    else {
                        Double precioNuevoTotal = precio * cantidad - desc;
                        desc += precioNuevoTotal * condicionModels.get(i).getPorcentaje() / 100;
                    }
                }
            }
            if (indicador3 == 1 && indicador4 == 1) break;
            if (indicador1 == 1) break;
            if (indicador2 == 1) break;

        }
        //descuento = desc;

        return desc;
    }

    private Double hallarPrecio(List<PrecioModel> listaPrecios) { // calcula el precio más actual
        int indice = 0;
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse("1900-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listaPrecios.size(); i++) {
            if (date.compareTo(listaPrecios.get(i).getFechaInicio()) < 0) {
                indice = i;
                date = listaPrecios.get(i).getFechaFin();
            }
        }
        return listaPrecios.get(indice).getPrecio();
    }
}
