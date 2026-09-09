package com.example.project_1_part_3.model;
/**
 * Abstract base class for game entities with motion.
 * <p>
 *      Tracks the 2D position and velocity vectors used for movement and
 * </p>
 * <p>
 *      collision logic.
 * </p>
 */
public abstract class PosAndSpeed {
    protected double posX, posY;
    protected double speedX, speedY;
    /**
     * Constructs an entity with initial position and speed.
     */
    public PosAndSpeed(double posX, double posY, double speedX, double speedY) {
        this.posX = posX;
        this.posY = posY;
        this.speedX = speedX;
        this.speedY = speedY;

    }

    public double getPosX() { return posX; }
    public void setPosX(double posX) { this.posX = posX; }
    public double getPosY() { return posY; }
    public void setPosY(double posY) { this.posY = posY; }
    public double getSpeedX() { return speedX; }
    public void setSpeedX(double speedX) { this.speedX = speedX; }
    public double getSpeedY() { return speedY; }
    public void setSpeedY(double speedY) { this.speedY = speedY; }
}
