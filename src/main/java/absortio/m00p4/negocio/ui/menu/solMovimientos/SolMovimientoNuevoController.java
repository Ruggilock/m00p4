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

public class SolMovimientoNuevoController implements Initializable {

    @FXML
    Pane panelNuevoSolMov;
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void buttonAceptar(MouseEvent event) throws IOException {
        //aca hacer el save

        Pane pane = (Pane)panelNuevoSolMov.getParent();
        Pane paneParent= FXMLLoader.load(getClass().getResource("/fxml/menu/solMovimientos/solMovimientos.fxml.fxml"));
        pane.getChildren().setAll(paneParent);
    }

    @FXML
    void buttonCancelar(MouseEvent event) throws IOException {
        Pane pane = (Pane)panelNuevoSolMov.getParent();
        Pane paneParent= FXMLLoader.load(getClass().getResource("/fxml/menu/solMovimientos/solMovimientos.fxml.fxml"));
        pane.getChildren().setAll(paneParent);
    }

    @FXML
    void buttonAnadirLote(MouseEvent event) throws IOException {
        //Pane pane = (Pane)panelNuevoSolMov.getParent();
        //Pane paneParent= FXMLLoader.load(getClass().getResource("/fxml/menu/solMovimientos/solMovimientos.fxml.fxml"));
        //pane.getChildren().setAll(paneParent);
    }
}

