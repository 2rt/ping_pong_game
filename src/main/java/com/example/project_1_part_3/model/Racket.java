package com.example.project_1_part_3.model;
/**
 * Represents a player's racket in the game.
 * <p>
 * This class stores the dimensions of the racket and handles
 * </p>
 * <p>
 *     scaling logic for responsive window resizing.
 * </p>
 */
public class Racket extends PosAndSpeed implements Resizable {
    private double width;
    private double height;

    public Racket(double posX, double posY, double width, double height) {
        super(posX, posY, 0, 0);
        this.width = width;
        this.height = height;
    }
    /** Adjusts horizontal position and width by a scale factor. */
    @Override
    public void resizeX(double factor) {
        posX *= factor;
        width *= factor;
    }
    /** Adjusts vertical position and height by a scale factor. */
    @Override
    public void resizeY(double factor) {
        posY *= factor;
        height *= factor;
    }


    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

}