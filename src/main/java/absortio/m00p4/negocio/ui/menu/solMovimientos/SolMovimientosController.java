package absortio.m00p4.negocio.ui.menu.solMovimientos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SolMovimientosController implements Initializable {


    @FXML
    private Pane paneSolMovimientos;

    @FXML
    private ComboBox<?> comboTipoOperacion;

    @FXML
    private TextField textFieldCodigo;

    @FXML
    private TableView<?> tablaSolMovimientos;

    @FXML
    private TableColumn<?, ?> colunmaN;

    @FXML
    private TableColumn<?, ?> columnaCodigo;

    @FXML
    private TableColumn<?, ?> colunmaFecha;

    @FXML
    private TableColumn<?, ?> colunmaDescripcion;

    @FXML
    private TableColumn<?, ?> colunmaMotivo;

    @FXML
    private TableColumn<?, ?> colunmaAcciones;

    @FXML
    private Button buttonNuevo;

    @FXML
    private ComboBox<?> comboMotivo;

    @FXML
    private DatePicker dateInicio;

    @FXML
    private DatePicker dateFin;

    @Autowired
    private ApplicationContext context;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    void clickNuevoSolMovimientos(MouseEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/menu/solMovimientos/nuevosolMovimiento.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Pane pane =fxmlLoader.load();
        paneSolMovimientos.getChildren().setAll(pane);


    }


    public void clickBuscar(MouseEvent mouseEvent) {
    }

    public void clickExportarExcel(MouseEvent mouseEvent) {
    }
}