package com.example.project_1_part_3.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * Interface for drawable game objects.
 */
public interface Shape {
    /** Draws the shape using specified graphics, color, and dimensions. */
    void draw(GraphicsContext gc, Color color, int x, int y, int width, int height);
}
