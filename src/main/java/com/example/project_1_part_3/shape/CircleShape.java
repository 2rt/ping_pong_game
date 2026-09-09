package com.example.project_1_part_3.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * Implementation of a circular shape.
 */
public class CircleShape implements Shape {
    @Override
    public void draw(GraphicsContext gc, Color color, int x, int y, int width, int height) {
        gc.setFill(color);
        // x 200
        // y 250
        gc.fillOval(x, y, width, height);
    }
}
