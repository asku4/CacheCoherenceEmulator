package io.github.AnaK89.emulator;

import com.google.inject.Singleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class ApplicationGui extends Application {
    private static final Logger logger = LogManager.getLogger(ApplicationGui.class);

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Application started");
        final Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/gui.fxml")));
        stage.setScene(scene);
        stage.setTitle("Эмулятор MESI");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("Application finished");
    }
}
