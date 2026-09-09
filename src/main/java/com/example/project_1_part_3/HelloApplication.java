package com.example.project_1_part_3;

import com.example.project_1_part_3.view.KeyboardListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Entry point for the Pong application.
 * <p>
 *      This class initializes the JavaFX Stage, loads the FXML view, and
 * </p>
 * <p>
 *      attaches the {@link KeyboardListener} to the scene to capture global
 * </p>
 * <p>
 *      key events for paddle movement and pausing.
 * </p>
 */

public class HelloApplication extends Application {
    @Override
    /**
     * Initializes and displays the main game window.
     * @param stage The primary stage for this application.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setMinWidth(500);

        stage.setMinHeight(400);
        Controller controller = fxmlLoader.getController();
        KeyboardListener listener = new KeyboardListener(controller);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, listener);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, listener);


        stage.setTitle("Ping Pong Game");
        stage.setScene(scene);
        stage.show();
        scene.setFill(Color.WHITE);


    }

    public static void main(String[] args) {
        launch();
    }
}