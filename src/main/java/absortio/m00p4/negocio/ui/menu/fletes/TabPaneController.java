package absortio.m00p4.negocio.ui.menu.fletes;

import absortio.m00p4.negocio.model.auxiliares.FleteDistrito;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class TabPaneController implements Initializable {
    @FXML
    TabPane tabPaneFletes;

    @FXML
    Tab tabfletes;

    @FXML
    private FletesController fletesController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void clickTabFlete(Event event) {
        fletesController.refresh();
    }
}
