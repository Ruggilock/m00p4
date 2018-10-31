package absortio.m00p4.negocio.ui.menu.productos;

import absortio.m00p4.negocio.model.*;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Producto;
import absortio.m00p4.negocio.reportes.ReporteProducto;
import absortio.m00p4.negocio.service.CategoriaProductoService;
import absortio.m00p4.negocio.service.LoteService;
import absortio.m00p4.negocio.service.PermisoService;
import absortio.m00p4.negocio.service.ProductoService;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import absortio.m00p4.negocio.service.singleton.UsuarioSingleton;
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
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
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

@Component
public class ProductosController implements Initializable {
   // private static final Logger LOG = LogManager.getLogger ( ProductosController.class );
    @FXML
    Pane paneProductos;

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
    private TableColumn columnaAcciones;
    @FXML
    private TableColumn columnaStockLogico;
    @FXML
    private TableColumn columnaId;
    @Autowired
    private ApplicationContext context;

    @Autowired
    private LoteService loteService;

    @Autowired
    private PermisoService permisoService;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarComboBox ();
        inicializarTablaProductos ();
        cargarGrilla ();

    }
                                                
    @FXML
    void clickNuevoProductos(MouseEvent event) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("productosCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "/fxml/menu/productos/nuevoproducto.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        Pane pane = fxmlLoader.load ();
        paneProductos.getChildren ().setAll ( pane );
    }

    private void inicializarTablaProductos() {

        columnaN.setCellValueFactory ( new PropertyValueFactory<Producto, Integer> ( "n" ) );
        columnaCodigo.setCellValueFactory ( new PropertyValueFactory<Producto, String> ( "codigo" ) );
        columnaNombre.setCellValueFactory ( new PropertyValueFactory<Producto, String> ( "nombre" ) );
        columnaMarca.setCellValueFactory ( new PropertyValueFactory<Producto, String> ( "marca" ) );
        columnaCategoria.setCellValueFactory ( new PropertyValueFactory<Producto, String> ( "categoria" ) );
        columnaModelo.setCellValueFactory ( new PropertyValueFactory<Producto, String> ( "modelo" ) );
        columnaAcciones.setCellValueFactory ( new PropertyValueFactory<> ( "button" ) );
        columnaStockLogico.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("cantidadMax"));
        agregarBotones ();
        productos = FXCollections.observableArrayList ();
        tablaProductos.setItems ( productos );

    }

    public void cargarGrilla() {
        productos.clear ();
        ArrayList<ProductoModel> productoModels = (ArrayList) productoService.findAllProducto ();
        for (int i = 0; i < productoModels.size (); i++) {
            Producto producto = new Producto ();
            producto.setCodigo ( productoModels.get ( i ).getCodigoBarras () );
            producto.setN ( i+1 );
            producto.setNombre ( productoModels.get ( i ).getNombre () );
            producto.setMarca ( productoModels.get ( i ).getMarca () );
            producto.setCategoria ( productoModels.get ( i ).getCategoriaProducto ().getNombre () );
            producto.setModelo ( productoModels.get ( i ).getModelo () );
            producto.setCantidadMax(productoService.hallarStockDisponibleById(productoService.hallarIdByCodigoBarras(producto.getCodigo())));
            productos.add(producto);
        }
        tablaProductos.setItems ( productos );
    }

    private void agregarBotones() {
        Callback<TableColumn<Producto, Void>, TableCell<Producto, Void>> cellFactory = new Callback<TableColumn<Producto, Void>, TableCell<Producto, Void>> () {
            @Override
            public TableCell<Producto, Void> call(final TableColumn<Producto, Void> param) {
                final TableCell<Producto, Void> cell = new TableCell<Producto, Void> () {
                    private final Button btnVer = new Button ();
                    private final Button btnEditar = new Button ();
                    private final Button btnEliminar = new Button ();

                    {
                        btnVer.setOnAction ( (event) -> {
                            Producto producto = getTableView ().getItems ().get ( getIndex () );
                            paneVer ( producto );
                        } );
                    }

                    {
                        btnEditar.setOnMouseClicked ( (event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("productosEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Producto producto = getTableView ().getItems ().get ( getIndex () );
                            paneEditar ( producto );
                        } );
                    }

                    {
                        btnEliminar.setOnAction ( (event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("productosEliminar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Producto producto = getTableView ().getItems ().get ( getIndex () );
                            Alert dialogoAlerta = new Alert ( Alert.AlertType.CONFIRMATION );
                            dialogoAlerta.setTitle ( "Ventana de Confirmacion" );
                            dialogoAlerta.setHeaderText ( null );
                            dialogoAlerta.initStyle ( StageStyle.UTILITY );
                            dialogoAlerta.setContentText ( "¿Realmente quieres elimarlo?" );
                            Optional<ButtonType> result = dialogoAlerta.showAndWait ();
                            if (result.get () == ButtonType.OK) {
                                productoService.deleteProductoModelByCodigoBarras ( producto.getCodigo () );
                                cargarGrilla ();
                            }
                        } );
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem ( item, empty );
                        if (empty) {
                            setGraphic ( null );
                        } else {
                            Image image = new Image ( getClass ().getResourceAsStream ( "/fxml/imagenes/editicon.png" ) );
                            ImageView iv1 = new ImageView ( image );
                            iv1.setFitHeight ( 17 );
                            iv1.setFitWidth ( 20 );
                            btnEditar.setGraphic ( iv1 );

                            image = new Image ( getClass ().getResourceAsStream ( "/fxml/imagenes/deleteicon.png" ) );
                            iv1 = new ImageView ( image );
                            iv1.setFitHeight ( 17 );
                            iv1.setFitWidth ( 20 );
                            btnEliminar.setGraphic ( iv1 );

                            image = new Image ( getClass ().getResourceAsStream ( "/fxml/imagenes/viewicon.png" ) );
                            iv1 = new ImageView ( image );
                            iv1.setFitHeight ( 17 );
                            iv1.setFitWidth ( 20 );
                            btnVer.setGraphic ( iv1 );

                            HBox pane = new HBox ( btnVer, btnEditar, btnEliminar );
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

    @FXML
    void buscar(MouseEvent event) throws IOException {
        productos.clear ();
        String nombre = textFieldNombre.getText ();
        String codigo = textFieldCodigoBarras.getText ();
        CategoriaProductoModel categoriaProducto = null;
        String categoriaNombre = comboBoxCategoria.getValue ();
        if (!categoriaNombre.equals ( "-Seleccione-" )) {
            categoriaProducto = categoriaProductoService.obtenerCategoriaPorNombre ( categoriaNombre );
        }
        String marca = comboBoxMarca.getValue ();
        String modelo = comboBoxModelo.getValue ();

        if (!textFieldNombre.equals ( "" )) {
            ArrayList<ProductoModel> productosfiltrados = (ArrayList) productoService.filtrarProductostodos ( nombre, codigo, marca, modelo, categoriaProducto );
            for (int i = 0; i < productosfiltrados.size (); i++) {
                Producto producto = new Producto ();
                producto.setCodigo ( productosfiltrados.get ( i ).getCodigoBarras () );
                producto.setN ( i );
                producto.setNombre ( productosfiltrados.get ( i ).getNombre () );
                producto.setMarca ( productosfiltrados.get ( i ).getMarca () );
                producto.setCategoria ( productosfiltrados.get ( i ).getCategoriaProducto ().getNombre () );
                producto.setModelo ( productosfiltrados.get ( i ).getModelo () );
                producto.setCantidadMax(productoService.hallarStockDisponibleById(productoService.hallarIdByCodigoBarras(producto.getCodigo())));
                productos.add ( producto );
            }
        }
    }


    private void paneEditar(Producto producto) {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "/fxml/menu/productos/editarproducto.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        try {
            Pane pane = fxmlLoader.load ();
            fxmlLoader.<ProductoEditarController>getController ().cargarData ( producto );
            paneProductos.getChildren ().setAll ( pane );

        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    private void paneVer(Producto producto) {
        FXMLLoader fxmlLoader = new FXMLLoader ( getClass ().getResource ( "/fxml/menu/productos/verproducto.fxml" ) );
        fxmlLoader.setControllerFactory ( context::getBean );
        try {
            Pane pane = fxmlLoader.load ();
            fxmlLoader.<ProductoVerController>getController ().cargarData ( producto );
            paneProductos.getChildren ().setAll ( pane );
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public void cargarComboBox() {
        ArrayList<String> categorias = new ArrayList<> ();
        ArrayList<CategoriaProductoModel> categoriasModels = (ArrayList<CategoriaProductoModel>) categoriaProductoService.findAllCategoriaProducto ();
        for (int i = 0; i < categoriasModels.size (); i++) {
            categorias.add ( categoriasModels.get ( i ).getNombre () );
        }
        ObservableList<String> observableListCategorias = FXCollections.observableArrayList ( categorias );
        comboBoxCategoria.getItems ().add ( "-Seleccione-" );
        comboBoxCategoria.getItems ().addAll ( observableListCategorias );
        comboBoxCategoria.setEditable ( true );
        comboBoxCategoria.setValue ( comboBoxCategoria.getItems ().get ( 0 ) );
        TextFields.bindAutoCompletion ( comboBoxCategoria.getEditor (), comboBoxCategoria.getItems () );


        ArrayList<String> modelosAux = new ArrayList<>();
        modelosAux.add("-Seleccione-");
        modelosAux.addAll(productoService.findAllMarca());
        ObservableList<String> marcas = FXCollections.observableArrayList(modelosAux);
        comboBoxMarca.getItems().addAll(marcas);
        comboBoxMarca.setEditable(true);
        comboBoxMarca.setValue(marcas.get(0));
        TextFields.bindAutoCompletion(comboBoxMarca.getEditor(), comboBoxMarca.getItems());

        modelosAux.clear();
        modelosAux.add("-Seleccione-");
        modelosAux.addAll(productoService.findAllModelo());
        ObservableList<String> modelos = FXCollections.observableArrayList(modelosAux);
        comboBoxModelo.getItems().addAll(modelos);
        comboBoxModelo.setEditable(true);
        comboBoxModelo.setValue(modelos.get(0));
        TextFields.bindAutoCompletion(comboBoxModelo.getEditor(), comboBoxModelo.getItems());

    }


    @FXML
    void clickExportarProductos(MouseEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(null);
        SystemSingleton.getInstance().setRute(file.getPath());
        ReporteProducto reporteProducto = new ReporteProducto();
        reporteProducto.generarReporteProductos(productoService.findAllProducto());

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ventana de Aviso");
        alert.setHeaderText(null);
        alert.setContentText("La información se ha exportado correctamente");
        alert.showAndWait();
    }


}
