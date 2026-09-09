package com.example.project_1_part_2.view;

import com.example.project_1_part_3.model.Ball;
import com.example.project_1_part_3.model.Game;
import com.example.project_1_part_3.model.Racket;
import com.example.project_1_part_3.view.CollisionManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link CollisionManager} model.
 * Verifies collisions.
 */
class CollisionManagerTest {
    CollisionManager collisionManager = new CollisionManager();
    Game game = new Game(100,100, this);
    private Ball ball;
    private Racket racket;
    @Test
    public void testIsRacketCollision(){
        ball = new Ball(15,15, 0,0,10);
        racket = new Racket(15,15,10,10);
        assertTrue(collisionManager.isRacketCollision(ball, racket));


    }
    @Test
    public void testIsLeftWallCollision(){
        ball = new Ball(0,15, 0,0,10);

        assertTrue(collisionManager.isLeftWallCollision(ball));


    }
    @Test
    public void testIsRightWallCollision(){
        ball = new Ball(game.getDimX(), 15, 0,0,10);

        assertTrue(collisionManager.isRightWallCollision(ball, game.getDimX()));


    }
    @Test
    public void testIsTopCollision(){
        int upperLimit =10;
        ball = new Ball(0, upperLimit, 0,0,10);

        assertTrue(collisionManager.isTopCollision(ball, upperLimit));


    }
    @Test
    public void testBottomCollision(){

        ball = new Ball(0, game.getDimY(), 0,0,10);

        assertTrue(collisionManager.isTopCollision(ball, game.getDimY()));


    }

}