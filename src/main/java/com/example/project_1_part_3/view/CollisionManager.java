package com.example.project_1_part_3.view;

import com.example.project_1_part_3.model.Ball;
import com.example.project_1_part_3.model.Racket;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Static utility class for detecting collisions between game objects and boundaries.
 * @author Ray E. Crowley
 */
public class CollisionManager {
    /**
     * Checks if the ball has hit a player's racket using JavaFX hitboxes.
     * @param ball   The ball object.
     * @param racket The racket object to check against.
     * @return True if the ball and racket overlap.
     */
    public static boolean isRacketCollision(Ball ball, Racket racket) {
        Rectangle racketHitbox = new Rectangle(racket.getPosX(), racket.getPosY(),
                racket.getWidth(), (racket.getHeight()));
        Circle ballHitbox = new Circle(ball.getPosX() + ball.getRadius(),
                ball.getPosY() + ball.getRadius(),
                ball.getRadius());
        return ballHitbox.getBoundsInParent().intersects(racketHitbox.getBoundsInParent());
    }
    /** @return True if the ball hits or passes the left screen edge. */
    public static boolean isLeftWallCollision(Ball ball) {
        return ball.getPosX() <= 0;
    }
    /** @return True if the ball hits or passes the right screen edge. */
    public static boolean isRightWallCollision(Ball ball, double dimX) {
        return ball.getPosX() >= dimX - (ball.getRadius() * 2);
    }
    /** @return True if the ball hits the top boundary (upperLimit). */
    public static boolean isTopCollision(Ball ball, double upperLimit){
        return ball.getPosY() <= upperLimit;
    }
    /** @return True if the ball hits the bottom screen edge. */
    public static boolean isBottomCollision(Ball ball, double dimY){
        return ball.getPosY() + (ball.getRadius() * 2) >= dimY;
    }
}