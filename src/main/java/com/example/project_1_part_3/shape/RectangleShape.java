package com.example.project_1_part_3.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * Implementation of a rectangular shape.
 */
public class RectangleShape implements Shape {
    @Override
    public void draw(GraphicsContext gc, Color color, int x, int y, int width, int height) {
        // Left Paddle - Yellow
        //x1 = 20
        //y1 = 150
        //x2 = 560
        //y2 = 150
        //w = 10
        // h = 125
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
    }
}
