package com.example.project_1_part_3.view;

import com.example.project_1_part_3.Controller;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.HashSet;
import java.util.Set;
/**
 * Handles all keyboard input for the game.
 * <p>
 *     This class listens for {@link KeyEvent}s and updates the {@link Controller}
 * </p>
 * <p>
 *     with the currently pressed keys. It also manages the game's pause state
 * </p>
 * <p>
 *     via the ESCAPE key.
 * </p>
 * @author Ray E. Crowley
 */
public class KeyboardListener implements EventHandler<KeyEvent> {
    private final Controller controller;
    private final Set<KeyCode> activeKeys = new HashSet<>();
    /**
     * Constructs a new KeyboardListener.
     * @param controller The {@link Controller} instance this listener will communicate with.
     */
    public KeyboardListener(Controller controller) {
        this.controller = controller;
    }
    /**
     * Handles key press and release events.
     * <p>
     *      If the game is running, it tracks active keys and checks for the ESCAPE
     * </p>
     * <p>
     *     key to pause the game. If the game is paused, it listens specifically
     * </p>
     * <p>
     *      for the ESCAPE key to resume play.
     * </p>
     * @param event The {@link KeyEvent} captured by the JavaFX event loop.
     */
    @Override
    public void handle(KeyEvent event) {
        if (controller.running()) {
            Platform.runLater(controller::setGamePaused);
            if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                activeKeys.add(event.getCode());


                if (event.getCode() == KeyCode.ESCAPE) {
                    controller.setRunning(false);
                    activeKeys.clear();
                }
            } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
                activeKeys.remove(event.getCode());
            }
            controller.setPressedKeys(activeKeys);
        } else {

            if (event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ESCAPE) {
                controller.setRunning(true);
                Platform.runLater(controller::setGamePaused);
            }

        }




    }
}