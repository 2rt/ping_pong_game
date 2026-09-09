package com.example.project_1_part_3.view;

import com.example.project_1_part_3.Controller;
import com.example.project_1_part_3.model.*;
import com.example.project_1_part_3.model.Ball;
import com.example.project_1_part_3.model.Game;
import com.example.project_1_part_3.model.Racket;
import com.example.project_1_part_3.shape.Shape;
import com.example.project_1_part_3.shape.ShapeFactory;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * The custom Canvas component responsible for rendering the game world.
 * <p>
 *      This class uses a {@link ShapeFactory} to create visual representations of the
 * </p>
 * <p>
 *     game objects and draws the ball and rackets based on the current {@link Game} state.
 * </p>
 * @author Ray E. Crowley
 */
public class GameCanvas extends Canvas{
    private Canvas canvas;
    private StackPane pane;
    private Controller ctrl;
    private final Shape ballShape = ShapeFactory.createShape("circle");
    private final Shape racketShape = ShapeFactory.createShape("rectangle");
    /**
     * Constructor for the game's drawing surface.
     * @param width Initial width.
     * @param height Initial height.
     */
    public GameCanvas(double width, double height){
        super(width,height);
    }
    /**
     * Links the canvas to the UI layout and enables responsive resizing.
     * @param canvas The FXML Canvas where the game is drawn.
     * @param pane   The parent StackPane to which the canvas size is bound.
     */
    public void initialize(Canvas canvas, StackPane pane) {
        this.canvas = canvas;
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
    }
    /**
     * Redraws the entire game state on the canvas.
     * @param game The current game model containing ball and racket data.
     */
    public void drawGame(Game game) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Ball b = game.getBall();
        ballShape.draw(gc, Color.BLACK, (int)b.getPosX(), (int)b.getPosY(), (int)b.getRadius()*2, (int)b.getRadius()*2);


        Racket r1 = game.getPlayer1().getRacket();
        racketShape.draw(gc, Color.BLUE, (int)r1.getPosX(), (int)r1.getPosY(), (int)r1.getWidth(), (int)r1.getHeight());

        Racket r2 = game.getPlayer2().getRacket();
        racketShape.draw(gc, Color.RED, (int)r2.getPosX(), (int)r2.getPosY(), (int)r2.getWidth(), (int)r2.getHeight());
    }
}