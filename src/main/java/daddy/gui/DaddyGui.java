package daddy.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Provides the JavaFX window used to interact with Daddy.
 */
public class DaddyGui extends Application {
    /**
     * Creates Daddy's JavaFX application.
     */
    public DaddyGui() {
    }

    /**
     * Creates and shows Daddy's initial application window.
     *
     * @param stage the primary JavaFX window
     */
    @Override
    public void start(Stage stage) {
        Label message = new Label("Daddy's GUI is waking up...");
        Scene scene = new Scene(new StackPane(message), 480, 640);
        stage.setTitle("Daddy");
        stage.setScene(scene);
        stage.show();
    }
}
