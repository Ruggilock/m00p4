package absortio.m00p4.negocio.ui.menu.solMovimientos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AnadirLoteSolMovimiento implements Initializable {

    @FXML
    Pane panelAnadirLote;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void buttonAnadir(MouseEvent event) throws IOException {
        //aca hacer el save

        //Pane pane = (Pane)panelAnadirLote.getParent();
        //Pane paneParent= FXMLLoader.load(getClass().getResource("/fxml/menu/solMovimientos/solMovimientos.fxml.fxml"));
        //pane.getChildren().setAll(paneParent);
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {
        //Pane pane = (Pane)panelAnadirLote.getParent();
        //Pane paneParent= FXMLLoader.load(getClass().getResource("/fxml/menu/solMovimientos/solMovimientos.fxml.fxml"));
        //pane.getChildren().setAll(paneParent);
    }

    @FXML
    void buttonBuscar(MouseEvent event) throws IOException {
        //aca se mete el select y se setea la ventana

        //Pane pane = (Pane)panelAnadirLote.getParent();
        //Pane paneParent= FXMLLoader.load(getClass().getResource("/fxml/menu/solMovimientos/solMovimientos.fxml.fxml"));
        //pane.getChildren().setAll(paneParent);
    }
}
