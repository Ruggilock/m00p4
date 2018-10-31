package absortio.m00p4.negocio.ui.menu.ventas;

import absortio.m00p4.negocio.model.ClienteModel;
import absortio.m00p4.negocio.model.auxiliares.ClienteVentas;
import absortio.m00p4.negocio.service.ClienteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class listaClientesVentaController implements Initializable {

    private static final Logger LOGGERAUDIT = LogManager.getLogger("FileAuditAppender");
    ObservableList<ClienteModel> olClientes;
    ObservableList<ClienteVentas> clienteVentas;
    @Autowired
    ClienteService clienteService;
    @FXML
    private Pane paneProductos;
    @FXML
    private Pane paneClienteSeleccionar;
    @FXML
    private TableView<ClienteVentas> tablaClientes;
    @FXML
    private TableColumn<ClienteVentas, Integer> columnaN;
    @FXML
    private TableColumn<ClienteVentas, String> columnaDocumento;
    @FXML
    private TableColumn<ClienteVentas, String> columnaNombre;
    @FXML
    private TableColumn<ClienteVentas, String> columnaContacto;
    @FXML
    private TableColumn<ClienteVentas, String> columnaTelefono;
    @FXML
    private TableColumn<ClienteVentas, Button> columnaAcciones;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldNDocumento;
    @FXML
    private TextField clienteOtraPantalla;
    @FXML
    private TextField numeroDocumentoOtraPantalla;
    @FXML
    private TextField telefonoOtraPantalla;

    @FXML
    void clickBuscar(MouseEvent event) {
        String nombre;
        String documento;

        nombre = textFieldNombre.getText();
        documento = textFieldNDocumento.getText();
        clienteVentas.clear();

        ArrayList<ClienteModel> arrayClientes = (ArrayList<ClienteModel>) clienteService.findByTodosFiltros(nombre, documento);
        for (int i = 0; i < arrayClientes.size(); i++) {
            ClienteVentas clienteVenta = new ClienteVentas();
            clienteVenta.setId(arrayClientes.get(i).getId());
            clienteVenta.setN(i + 1);
            clienteVenta.setDocumentoIdentidad(arrayClientes.get(i).getDocumentoIdentidad());
            clienteVenta.setNombre(arrayClientes.get(i).getNombres());
            clienteVenta.setCorreo(arrayClientes.get(i).getCorreo());
            clienteVenta.setTelefono(arrayClientes.get(i).getTelefono());
            clienteVenta.setAccion(new Button());
            clienteVenta.getAccion().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    ClienteModel clienteModel = clienteService.getByDocumentoIdentidad(clienteVenta.getDocumentoIdentidad());
                    olClientes.add(clienteModel);
                    numeroDocumentoOtraPantalla.setText(clienteVenta.getDocumentoIdentidad());
                    //clienteOtraPantalla.setText(clienteVenta.getNombre());
                    //telefonoOtraPantalla.setText(clienteVenta.getTelefono());
                    ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                }
            });
            clienteVentas.add(clienteVenta);
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        olClientes = FXCollections.observableArrayList();

        inicializarTablaClientes();
        setClientes();

    }

    private void setClientes() {
        ArrayList<ClienteModel> arrayClientes = (ArrayList<ClienteModel>) clienteService.findAll();
        for (int i = 0; i < arrayClientes.size(); i++) {
            ClienteVentas clienteVenta = new ClienteVentas();
            clienteVenta.setId(arrayClientes.get(i).getId());
            clienteVenta.setN(i + 1);
            clienteVenta.setDocumentoIdentidad(arrayClientes.get(i).getDocumentoIdentidad());
            clienteVenta.setNombre(arrayClientes.get(i).getNombres());
            clienteVenta.setCorreo(arrayClientes.get(i).getCorreo());
            clienteVenta.setTelefono(arrayClientes.get(i).getTelefono());
            clienteVenta.setAccion(new Button());
            clienteVenta.getAccion().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    ClienteModel clienteModel = clienteService.getByDocumentoIdentidad(clienteVenta.getDocumentoIdentidad());
                    olClientes.add(clienteModel);
                    numeroDocumentoOtraPantalla.setText(clienteVenta.getDocumentoIdentidad());
                    //clienteOtraPantalla.setText(clienteVenta.getNombre());
                    //telefonoOtraPantalla.setText(clienteVenta.getTelefono());
                    ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                }
            });
            clienteVentas.add(clienteVenta);
        }

    }


    private void inicializarTablaClientes() {

        columnaN.setCellValueFactory(new PropertyValueFactory<ClienteVentas, Integer>("n"));
        columnaDocumento.setCellValueFactory(new PropertyValueFactory<ClienteVentas, String>("documentoIdentidad"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<ClienteVentas, String>("nombre"));
        columnaContacto.setCellValueFactory(new PropertyValueFactory<ClienteVentas, String>("correo"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<ClienteVentas, String>("telefono"));
        columnaAcciones.setCellValueFactory(new PropertyValueFactory<ClienteVentas, Button>("accion"));

        clienteVentas = FXCollections.observableArrayList();
        tablaClientes.setItems(clienteVentas);
    }


    public void recibirDato(ObservableList<ClienteModel> ventaClientes, TextField textFieldNumeroDocumento, TextField textFieldCliente, TextField textFieldTelefono) {
        this.olClientes = ventaClientes;
        this.numeroDocumentoOtraPantalla = textFieldNumeroDocumento;
        this.clienteOtraPantalla = textFieldCliente;
        this.telefonoOtraPantalla = textFieldTelefono;

    }
}
