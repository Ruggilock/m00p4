package absortio.m00p4.negocio.ui.menu.entregas;

import absortio.m00p4.negocio.model.FleteCapacidadModel;
import absortio.m00p4.negocio.model.FleteDistritoModel;
import absortio.m00p4.negocio.model.FleteModel;
import absortio.m00p4.negocio.model.ProductoModel;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.FormateadorDecimal;
import absortio.m00p4.negocio.model.algoritmo.herramientasAlgoritmos.Validador;
import absortio.m00p4.negocio.model.auxiliares.Entrega;
import absortio.m00p4.negocio.service.*;
import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ProgramarEntregasController implements Initializable {
    @FXML
    public ArrayList<EntregaController> entregaControllers = new ArrayList<>();

    @FXML
    Pane paneProgramarEntregas;

    @FXML
    TabPane tabPaneEntregas;

    @FXML
    ComboBox<String> comboBoxDepartamento;

    @FXML
    ComboBox<String> comboBoxDistrito;

    @FXML
    ComboBox<String> comboBoxProvincia;


    @FXML
    TextField textFieldDireccion;
    DatePicker datePicker;
    ObservableMap<Integer, Integer> cantidadespedidas;
    /* HashMap<Integer, Integer> cantidadesprogramadas;*/
    FleteModel fleteCalculado;
    @Autowired
    FleteModelService fleteModelService;
    @Autowired
    FleteCapacidadService fleteCapacidadService;
    @Autowired
    FleteDistritoService fleteDistritoService;
    @Autowired
    ProductoService productoService;
    ObservableList<FleteModel> olfletes;
    @FXML
    Label labelflete;

    @FXML
    Label labeltotal;
    private ObservableList<Entrega> productosAEntregar;
    @Autowired
    private ApplicationContext context;

    @Autowired
    private MonedaService monedaService;

    private String moneda;

    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePicker = new DatePicker();
        productosAEntregar = FXCollections.observableArrayList();
        cantidadespedidas = FXCollections.observableHashMap();

        cargarComboBox();
        tabPaneEntregas.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);


    }

    private Tab addNewTab(final TabPane tabPane, String newTabName, Node newTabContent, boolean isCloseable) {
        Tab newTab = new Tab(newTabName);
        newTab.setContent(newTabContent);
        newTab.setClosable(isCloseable);


        newTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (tabPane.getTabs()
                        .size() == 2) {
                    event.consume();
                }
                String n = (newTab.getText().split("°"))[1];
                Integer num = Integer.valueOf(n);
                entregaControllers.remove(num - 1);
            }
        });

        tabPane.getTabs()
                .add(newTab);
        return newTab;

    }

    private void setBehaviourForPlusTabClick(final TabPane tabPane) {
        tabPane.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                        if (newTab.getText()
                                .equals("+")) {
                            String tabname = "N°" + tabPane.getTabs().size();
                            Tab addedTab = null;
                            try {
                                addedTab = addNewTab(tabPane, tabname, getTabContent(), true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            tabPane.getTabs()
                                    .remove(addedTab);
                            tabPane.getTabs()
                                    .set(tabPane.getTabs()
                                            .size() - 1, addedTab);
                            tabPane.getTabs()
                                    .add(newTab);
                            tabPane.getSelectionModel()
                                    .select(addedTab);

                        }
                    }
                });
    }

    private Node getTabContent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/entregas/entrega.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);

        EntregaController entregaController = new EntregaController();
        //EntregaController entregaController = fxmlLoader.<EntregaController>getController();
        fxmlLoader.setController(entregaController);
        /*        if (entregaControllers.size()>0)
            entregaControllers.get(entregaControllers.size() - 1).setCantidadProgramada();*/
        Parent root1 = fxmlLoader.load();
        entregaController.asignarProductosYEntregas(productosAEntregar);
        entregaControllers.add(entregaController);
        return root1;
    }

    private Node getEmptyTabContent() {
        return new Label("Añadir entrega");
    }


    private void cargarComboBox() {

        HashMap<String, List<String>> provincias = SystemSingleton.getInstance().getProvinciasxdepartamento();

        ObservableList departamentos = FXCollections.observableList(SystemSingleton.getInstance().getDepartamentos());
        comboBoxDepartamento.setItems(departamentos);
        comboBoxDepartamento.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                comboBoxProvincia.getItems().clear();
                if (t1 != null) {
                    ObservableList provincias = FXCollections.observableArrayList((List) SystemSingleton.getInstance().getProvinciasxdepartamento().get(t1));
                    comboBoxProvincia.setItems(provincias);
                    comboBoxProvincia.getItems().add(null);
                }
            }
        });

        comboBoxProvincia.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                comboBoxDistrito.getItems().clear();
                if (t1 != null) {
                    ObservableList distritos = FXCollections.observableArrayList((List) SystemSingleton.getInstance().getDistritosxprovincia().get(t1));
                    comboBoxDistrito.setItems(distritos);
                    comboBoxDistrito.getItems().add(null);
                }
            }
        });
    }

    @FXML
    private void buttonCancelar(MouseEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void buttonAceptar(MouseEvent event) throws IOException {
        HashMap<Integer, Integer> sumaprogramadas = new HashMap<>();

        if (textFieldDireccion.getText().equals("")) {
            Validador.mostrarDialogError("¡Debe ingresar una dirección!");
            return;
        }


        for (Integer key : cantidadespedidas.keySet()) {
            sumaprogramadas.put(key, 0);
        }
        if (asignarFlete()) {
            productosAEntregar.clear();
            for (EntregaController entController : entregaControllers) {
                ObservableList<Entrega> entregasProgramadas = FXCollections.observableArrayList(entController.asignarEntrega());
                for (int i = 0; i < entregasProgramadas.size(); i++) {
                    Entrega ent = entregasProgramadas.get(i);
                    if (ent.getCantidadEntrega() > 0) {
                        ent.setDepartamento(comboBoxDepartamento.getValue());
                        ent.setDistrito(comboBoxDistrito.getValue());
                        ent.setProvincia(comboBoxProvincia.getValue());
                        ent.setDireccion(textFieldDireccion.getText());
                        System.out.println(ent.getCantidadEntrega().toString());
                        sumaprogramadas.put(ent.getN(), sumaprogramadas.get(ent.getN()) + ent.getCantidadEntrega());
                        productosAEntregar.add(ent);
                    }
                }
            }
            for (Integer key : cantidadespedidas.keySet()) {
                if (cantidadespedidas.get(key).compareTo(sumaprogramadas.get(key)) != 0) {
                    System.out.println();
                    Validador.mostrarDialogError("¡La cantidad total en las entregas no es igual a la de los pedidos!");
                    return;
                }
            }
            this.entregaControllers.clear();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Flete");
            alert.setHeaderText(null);
            alert.setContentText("Ups! No se encuentra flete para la zona");
            alert.showAndWait();
        }
    }


    @FXML
    public void asignarProductos(ObservableList<Entrega> entregas, ObservableList<FleteModel> olfletes, Label fletelabel, Label totallabel, String pmoneda) throws IOException {
        productosAEntregar = entregas;
        for (Entrega e : productosAEntregar) {
            cantidadespedidas.put(e.getN(), e.getCantPedida());
        }
        this.olfletes = olfletes;
        fleteCalculado = this.olfletes.get(0);
        moneda = pmoneda;
        labelflete = fletelabel;

        labeltotal = totallabel;

        addNewTab(tabPaneEntregas, "N°1", getTabContent(), false);
        addNewTab(tabPaneEntregas, "+", getEmptyTabContent(), false);
        setBehaviourForPlusTabClick(tabPaneEntregas);

    }

    public boolean asignarFlete() {
        String departamento = comboBoxDepartamento.getValue();
        String provincia = comboBoxProvincia.getValue();
        String distrito = comboBoxDistrito.getValue();
        Double peso = 0.0;
        Double volumen = 0.0;
        FleteDistritoModel fleteDistritoModel = fleteDistritoService.getByDepartamentoAndProvinciaAndDistritoAndEstadoLike(departamento, provincia, distrito, "activo");

        for (Entrega e : productosAEntregar
                ) {
            ProductoModel productoModel = productoService.obtenerProducto(e.getCodigo());
            peso += productoModel.getPeso() * e.getCantPedida();
            volumen += productoModel.getVolumen() * e.getCantPedida();
        }
        FleteCapacidadModel fleteCapacidadModel = fleteCapacidadService.obtenerFleteCapacidad(peso, volumen);
        if (fleteCapacidadModel != null && fleteDistritoModel != null) {
            fleteCalculado = fleteModelService.getByCapacidad_IdAndDistrito_Id(fleteCapacidadModel.getId(), fleteDistritoModel.getId());
            Double valorFlete;
            if (moneda.equals("dolares"))
                valorFlete = fleteCalculado.getCosto() / monedaService.findByNombre(moneda).getValor();
            else
                valorFlete = fleteCalculado.getCosto();
            olfletes.add(fleteCalculado);
            if (fleteCalculado != null) {
                Double fleteAnterior = Double.parseDouble(labelflete.getText());
                labelflete.setText(FormateadorDecimal.formatear(valorFlete));
                labeltotal.setText(FormateadorDecimal.formatear(Double.valueOf(labeltotal.getText()) + valorFlete - fleteAnterior));
                return true;
            }
        }
        return false;
    }

}
