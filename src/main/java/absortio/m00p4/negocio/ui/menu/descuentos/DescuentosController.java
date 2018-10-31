package absortio.m00p4.negocio.ui.menu.descuentos;

import absortio.m00p4.negocio.model.CategoriaProductoModel;
import absortio.m00p4.negocio.model.CondicionesComercialesModel;
import absortio.m00p4.negocio.model.RolModel;
import absortio.m00p4.negocio.model.UsuarioModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Descuento;
import absortio.m00p4.negocio.service.CategoriaProductoService;
import absortio.m00p4.negocio.service.CondicionesComercialesService;
import absortio.m00p4.negocio.service.PermisoService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class DescuentosController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(DescuentosController.class);
    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    @FXML
    Pane paneDescuentos;
    ObservableList<Descuento> descuentos;
    @Autowired
    CondicionesComercialesService condicionService;
    @Autowired
    CategoriaProductoService categoriaProductoService;
    @Autowired
    PermisoService permisoService;
    @FXML
    private ComboBox comboBoxTipo;
    @FXML
    private TableView<Descuento> tablaDescuentos;
    @FXML
    private TableColumn columnaN;
    @FXML
    private TableColumn columnaCodigo;
    @FXML
    private TableColumn columnaAcciones;
    @FXML
    private TableColumn columnaTipo;
    @FXML
    private TableColumn columnaFechaIni;
    @FXML
    private TableColumn columnaFechaFin;
    @FXML
    private TableColumn columnaCodigo1;
    @FXML
    private TableColumn columnaCodigo2;
    @FXML
    private TableColumn columnaCantidad1;
    @FXML
    private TableColumn columnaCantidad2;
    @FXML
    private TableColumn columnaPorcentaje;
    @FXML
    private TableColumn columnaCategoria;
    @FXML
    private ComboBox comboBoxCategoria;
    @Autowired
    private ApplicationContext context;


    //@FXML
    //private Button buttonNuevo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.inicializarTablaDescuentos();
        iniciarComboBoxCategoria();
        iniciarComboBoxTipo();

        cargarGrilla();
    }

    private void iniciarComboBoxTipo() {
        ArrayList<String> tipos = new ArrayList<>();
        tipos.add("");
        tipos.add("cantidad*cantidad");
        tipos.add("cantidad*gratis");
        tipos.add("porcentaje");
        ObservableList<String> oTipos = FXCollections.observableArrayList(tipos);
        comboBoxTipo.getItems().addAll(oTipos);
        comboBoxTipo.setValue(oTipos.get(0));
        comboBoxTipo.setEditable(false);
    }

    private void iniciarComboBoxCategoria() {
        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("");
        ArrayList<CategoriaProductoModel> categoriasModels = (ArrayList<CategoriaProductoModel>) categoriaProductoService.findAllCategoriaProducto();
        for (int i = 0; i < categoriasModels.size(); i++) {
            categorias.add(categoriasModels.get(i).getNombre());
        }
        ObservableList<String> observableListCategorias = FXCollections.observableArrayList(categorias);
        comboBoxCategoria.getItems().addAll(observableListCategorias);
        comboBoxCategoria.setValue(categorias.get(0));
        comboBoxCategoria.setEditable(true);
        TextFields.bindAutoCompletion(comboBoxCategoria.getEditor(), comboBoxCategoria.getItems());
    }

    private void addButtonToTable() {
        Callback<TableColumn<Descuento, Void>, TableCell<Descuento, Void>> cellFactory = new Callback<TableColumn<Descuento, Void>, TableCell<Descuento, Void>>() {
            @Override
            public TableCell<Descuento, Void> call(final TableColumn<Descuento, Void> param) {
                final TableCell<Descuento, Void> cell = new TableCell<Descuento, Void>() {
                    private final Button btnVer = new Button();
                    private final Button btnEditar = new Button();
                    private final Button btnEliminar = new Button();

                    {

                        btnVer.setOnAction((event) -> {
                            Descuento data = getTableView().getItems().get(getIndex());
                            levantarPaneVer(data);
                        });
                    }

                    {

                        btnEditar.setOnMouseClicked((event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("descuentosEditar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Descuento data = getTableView().getItems().get(getIndex());
                            levantarPaneEditar(data);
                        });
                    }

                    {

                        btnEliminar.setOnAction((event) -> {
                            UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
                            RolModel rolUsuario = usuarioModel.getIdRol();
                            if (!Validador.tienePermiso("descuentosEliminar", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
                                Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
                                return;
                            }
                            Descuento data = getTableView().getItems().get(getIndex());
                            //Aca llamar a la ventana de confirmacion de Eliminar

                            Alert dialogoAlerta = new Alert(Alert.AlertType.CONFIRMATION);
                            dialogoAlerta.setTitle("Ventana de Confirmacion");
                            dialogoAlerta.setHeaderText(null);
                            dialogoAlerta.initStyle(StageStyle.UTILITY);
                            dialogoAlerta.setContentText("Realmente Quieres Elimarlo");
                            Optional<ButtonType> result = dialogoAlerta.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                condicionService.delete(data.getId());
                                //vuelve a cargar la grilla
                                descuentos.clear();
                                //inicializarTablaDescuentos();
                                cargarGrilla();
                            }
                            //setUsuarios();


                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {

                            Image image = new Image(getClass().getResourceAsStream("/fxml/imagenes/editicon.png"));
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

    @FXML
    void clickNuevoDescuento(MouseEvent event) throws IOException {
        UsuarioModel usuarioModel = UsuarioSingleton.getInstance().getUsuarioModel();
        RolModel rolUsuario = usuarioModel.getIdRol();
        if (!Validador.tienePermiso("descuentosCrear", permisoService.findAllPermisoByIdRol(rolUsuario.getId()))) {
            Validador.mostrarDialogError("No tiene permiso para realizar esta acción.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/nuevoDescuento.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane = fxmlLoader.load();
        paneDescuentos.getChildren().setAll(pane);
    }

    private void inicializarTablaDescuentos() {
        columnaCantidad1.setCellValueFactory(new PropertyValueFactory<Descuento, Integer>("cantidad1"));
        columnaCantidad2.setCellValueFactory(new PropertyValueFactory<Descuento, Integer>("cantidad2"));
        columnaCategoria.setCellValueFactory(new PropertyValueFactory<Descuento, Integer>("categoria"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<Descuento, Integer>("id"));
        columnaCodigo1.setCellValueFactory(new PropertyValueFactory<Descuento, Integer>("codigo1"));
        columnaCodigo2.setCellValueFactory(new PropertyValueFactory<Descuento, Integer>("codigo2"));
        columnaFechaFin.setCellValueFactory(new PropertyValueFactory<Descuento, Date>("fechaFin"));
        columnaFechaIni.setCellValueFactory(new PropertyValueFactory<Descuento, Date>("fechaIni"));
        columnaPorcentaje.setCellValueFactory(new PropertyValueFactory<Descuento, String>("porcentaje"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<Descuento, String>("tipo"));
        columnaN.setCellValueFactory(new PropertyValueFactory<Descuento, Integer>("n"));

        descuentos = FXCollections.observableArrayList();
        addButtonToTable();
        tablaDescuentos.setItems(descuentos);
    }

    public void cargarGrilla() {
        ArrayList<CondicionesComercialesModel> condicionModels = (ArrayList) condicionService.findAllCondicion();

        for (int i = 0; i < condicionModels.size(); i++) {
            Descuento descuento = new Descuento();

            descuento.setN(i);
            descuento.setCantidad1(condicionModels.get(i).getCantidad1());
            descuento.setCantidad2(condicionModels.get(i).getCantidad2());
            descuento.setCodigo1(condicionModels.get(i).getCodigo1());
            descuento.setCodigo2(condicionModels.get(i).getCodigo2());
            descuento.setFechaFin(condicionModels.get(i).getFechaFin());
            descuento.setFechaIni(condicionModels.get(i).getFechaIni());
            descuento.setCategoria(condicionModels.get(i).getCategoria());
            descuento.setId(condicionModels.get(i).getId());
            descuento.setPorcentaje(condicionModels.get(i).getPorcentaje());
            descuento.setTipo(condicionModels.get(i).getTipo());

            descuentos.add(descuento);
        }
    }

    private void levantarPaneEditar(Descuento data) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuentoEditar.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane pane = fxmlLoader.load();
            fxmlLoader.<DescuentoEditarController>getController().cargarData(data);
            paneDescuentos.getChildren().setAll(pane);

        } catch (IOException e) {
            LOGGER.error("Error al cargar la data dell rol",e);
        }
    }

    private void levantarPaneVer(Descuento data) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/descuentos/descuentoVer.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Pane pane = fxmlLoader.load();
            fxmlLoader.<DescuentoVerController>getController().cargarData(data);
            paneDescuentos.getChildren().setAll(pane);
        } catch (IOException e) {
            LOGGER.error("Error al cargar la data dell rol",e);
        }
    }
}
