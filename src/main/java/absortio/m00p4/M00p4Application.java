package absortio.m00p4;

import absortio.m00p4.negocio.service.singleton.SystemSingleton;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@SpringBootApplication

public class M00p4Application extends AbstractJavaFxApplicationSupport {

    private static final Logger LOGGER = LogManager.getLogger(M00p4Application.class);
    private static final Logger LOGGERAUDIT= LogManager.getLogger("FileAuditAppender") ;


    public static String usernameMail;
    public static String passwordMail;
    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(String[] args) {
        mailConfig();
        Application.launch(args);
    }
    private static void mailConfig() {
        File mailConfigFile = new File("MailConfig.txt");
        BufferedReader br = null;
        String user, pass;
        if (mailConfigFile.exists()) {
            try {
                br = new BufferedReader(new FileReader(mailConfigFile));
                user = br.readLine();
                pass = br.readLine();
                usernameMail = user;
                passwordMail = pass;
                br.close();
            } catch (Exception e) {
                LOGGER.error("Error configuracion correo",e);
            }
        }
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(M00p4Application.class);
        SystemSingleton.getInstance().cargardatalugares();
        SystemSingleton.getInstance().cargargarcombospesoyvol();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/loginform.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();

    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info("Iniciando Aplicacion de MOPPA");
        stage.setScene(new Scene(rootNode));
        stage.setResizable(false);
        stage.setWidth(705.0);
        stage.setHeight(435.0);
        stage.show();
    }
}
