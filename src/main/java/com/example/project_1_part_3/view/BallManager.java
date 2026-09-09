package com.example.project_1_part_3.view;

import com.example.project_1_part_3.Controller;
import javafx.application.Platform;

/**
 * Manages the core game loop and frame timing.
 * <p>
 *      This class runs on a separate thread to handle physics updates and
 * </p>
 * <p>
 *     input processing every 16ms (~60 FPS).
 * </p>
 *
 * @author Ray E. Crowley
 */
public class BallManager implements Runnable {
    private final Controller controller;
    /**
     * Constructs a BallManager with the provided controller.
     * @param controller The game's main controller.
     */
    public BallManager(Controller controller) {
        this.controller = controller;
    }
    /**
     * Executes the continuous game loop.
     * <p>
     *      Updates physics, processes input, and requests frame rendering
     * </p>
     * <p>
     *     while the game is running.
     * </p>
     */
    @Override
    public void run() {
        while (true) {
            try {

                Thread.sleep(16);
            } catch (InterruptedException e) {
                break;
            }
            if (controller.running()){
                if (controller.getGame() != null) {
                    controller.processInput();
                    controller.getGame().update(controller.getUpperLimit());
                }

                Platform.runLater(() -> controller.drawGame());


            }

        }
    }
}