package com.example.project_1_part_3.model;
/**
 * Represents the ball in the game with movement and resizing capabilities.
 * <p>
 * This class extends {@link PosAndSpeed} to handle physics and implements
 * </p>
 * <p>
 * {@link Resizable} to scale with the game window.
 * </p>
 */
public class Ball extends PosAndSpeed implements Resizable {
    private double radius;
    /**
     * Constructs a new Ball with position, velocity, and size.
     */
    public Ball(double posX, double posY, double speedX, double speedY, double radius) {
        super(posX, posY, speedX, speedY);
        this.radius = radius;
    }
    /** Updates the ball's position based on its current velocity vectors. */
    public void move() {
        this.posX += speedX;
        this.posY += speedY;
    }
    /** Scales horizontal position and radius.
     * <p>
     *     Radius scaling is capped to prevent the ball from growing too large.
     * </p>
     */
    @Override
    public void resizeX(double factor) {

        this.posX *= factor;
        if (radius <= 16){
            this.radius *= factor;
        }
    }

    @Override
    public void resizeY(double factor) {

        this.posY *= factor;
        if (radius <= 16){
            this.radius *= factor;
        }

    }

    public double getRadius() { return radius; }
    public void setRadius(double radius) { this.radius = radius; }
}