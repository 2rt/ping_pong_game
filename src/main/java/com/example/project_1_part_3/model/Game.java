package com.example.project_1_part_3.model;

import com.example.project_1_part_3.Controller;
import com.example.project_1_part_3.view.CollisionManager;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.Random;
/**
 * The core logic class that manages the Pong game state and physics.
 * <p>
 *      This class coordinates the ball, players, scoring, and collision logic.
 * </p>
 *      It also handles the "speed-up" mechanics based on a modulus counter and
 * <p>
 *      manages responsive resizing of the game world.
 * </p>
 * @author Ray E. Crowley
 */
public class Game implements Resizable, Serializable {
    private Ball ball;
    private Player player1;
    private Player player2;
    private final Random random = new Random();
    /** Acceleration values applied to the ball during speed-up events. */
    private double accelerationX, accelerationY;
    /** Current logical dimensions of the game area. */
    private double dimX, dimY;
    private double newSpeed;
    private Controller controller;
    /** The base speeds used when the ball is reset after a point. */
    private double originalSpeedX =4.0;
    private double originalSpeedY = 4.0;
    /** Tracks which player lost the last match to determine who serves. */
    private boolean soreLoser = random.nextBoolean();;
    private int id;
    private int player1Score;
    private int player2Score;
    private String player1Name;
    private String player2Name;
    private Double speedX;
    private Double speedY;
    private int score;
    private String title;
    /**
     * Constructs a new Game instance and initializes entities.
     * @param width  The initial width of the game area.
     * @param height The initial height of the game area.
     */
    public Game(double width, double height,Controller controller) {
        this.controller = controller;
        this.dimX = width;
        this.dimY = height-100;
        // Initialize objects centered
        double ballRadius = 10;
        this.ball = new Ball((width / 2) - ballRadius, (height / 2) - ballRadius, (random.nextInt((int) (originalSpeedX - 2.5 + 1)) + 2.5)
                , (random.nextInt((int) (originalSpeedY - 2.5 + 1)) + 2.5), ballRadius);

        this.player1 = new Player("Player 1", new Racket(20, height / 2 - 62, 10, 125));
        this.player2 = new Player("Player 2", new Racket(width - 30, height / 2 - 62, 10, 125));
    }

    private Game(GameBuilder builder) {
        // 1. Set the primitive/String fields
        this.id = builder.id;
        this.player1Name = builder.player1Name;
        this.player2Name = builder.player2Name;
        this.player1Score = builder.player1Score;
        this.player2Score = builder.player2Score;
        this.ball = new Ball(0, 0, builder.speedX, builder.speedY, 10);
        this.score = builder.score;

        // 2. INITIALIZE THE ACTUAL PLAYER OBJECTS
        // This prevents the NullPointerException in the DAO
        this.player1 = new Player(builder.player1Name, null); // Pass null for racket if not needed for DB
        this.player1.setScore(builder.player1Score);

        this.player2 = new Player(builder.player2Name, null);
        this.player2.setScore(builder.player2Score);
    }

    public int getScore() {
        if (controller != null) {
            return controller.getFinalScore();
        }
        return this.score; // fallback for builder-created games
    }
    /** Updates physics, checks collisions, and handles scoring for a single frame. */
    public void update(double upperLimit) {
        ball.move();

        if (CollisionManager.isTopCollision(ball, upperLimit)){

            ball.setPosY(upperLimit + 1);
            ball.setSpeedY(Math.abs(ball.getSpeedY()));
        }

        else if(CollisionManager.isBottomCollision(ball,dimY)){

            ball.setPosY(dimY - (ball.getRadius() * 2) - 1);
            ball.setSpeedY(-Math.abs(ball.getSpeedY()));
        }

        if (CollisionManager.isRacketCollision(ball, player1.getRacket())) {

            if (getController().getModulusCounter()%getController().getModulusTime()==0&& ball.getSpeedX() <0) {
                System.out.println(getController().getModulusTime());

                accelerationX = 0.25 + (random.nextDouble() * 0.03);
                accelerationY = 0.25 + (random.nextDouble() * 0.03);

            }
            ball.setPosX(player1.getRacket().getPosX() + player1.getRacket().getWidth());
            newSpeed = Math.abs(ball.getSpeedX()) + accelerationX;
            ball.setSpeedX(newSpeed);
            newSpeed = ball.getSpeedY() + accelerationY;
            ball.setSpeedY(newSpeed);
            getController().modulusIncrement();

        }
        else if (CollisionManager.isRacketCollision(ball, player2.getRacket())) {
            System.out.println(getController().getModulusCounter());
            if (getController().getModulusCounter()%getController().getModulusTime()==0&& ball.getSpeedX() >0){


                accelerationX = 0.25 + (random.nextDouble() * 0.03);
                accelerationY = 0.25 + (random.nextDouble() * 0.03);



            }
            ball.setPosX(player2.getRacket().getPosX() - (ball.getRadius() * 2) +2);
            newSpeed = Math.abs(ball.getSpeedX()) + accelerationX;
            ball.setSpeedX(-newSpeed);
            newSpeed = ball.getSpeedY() + accelerationY;
            ball.setSpeedY(newSpeed);
            getController().modulusIncrement();


        }

        if (CollisionManager.isLeftWallCollision(ball)) {
            player2.addScore();
            if (player2.getScore() >= getController().getFinalScore()){
                Platform.runLater(()-> getController().winner(player2.getName()));
                setSoreLoser(true);
            } else{
                Platform.runLater(()-> getController().playerAlert(false));
                resetBall(true);
            }

        }
        else if (CollisionManager.isRightWallCollision(ball, dimX)) {
            player1.addScore();
            if (player1.getScore() >= getController().getFinalScore()){
                Platform.runLater(()-> getController().winner(player1.getName()));
                setSoreLoser(false);
            } else{
                Platform.runLater(()-> getController().playerAlert(true));
                resetBall(false);
            }



        }
    }
    /** Resets the ball to the center of the screen for a new point. */
    private void resetBall(boolean serveToLeft) {
        System.out.println(serveToLeft);
        // Center ball exactly in the middle of the current screen dimensions
        ball.setPosX((getDimX() / 2) - ball.getRadius());
        ball.setPosY((getDimY() / 2) - ball.getRadius());
        double direction = serveToLeft ? -1.0 : 1.0;
        ball.setSpeedX(getOriginalSpeedX() * direction);
        setOriginalSpeedX(getOriginalSpeedX());
        ball.setSpeedY(getOriginalSpeedY());

    }
    public void resetBallSer() {
        // Center ball exactly in the middle of the current screen dimensions
        ball.setPosX((getDimX() / 2) - ball.getRadius());
        ball.setPosY((getDimY() / 2) - ball.getRadius());
    }
    /**
     * Resets the game
     */
    public void resetGame(){
        player1.setScore(0);
        player2.setScore(0);
        ball.setSpeedY(0);
        ball.setSpeedX(0);
        resetBall(isSoreLoser());

    }
    /**
     * Overloaded method.
     * @param ThisIsAOverloadedMethod, is in {@link Controller} if you want to go look.
     */
    public void resetGame(boolean ThisIsAOverloadedMethod){
        player1.setScore(0);
        player2.setScore(0);
        ball.setSpeedY(0);
        ball.setSpeedX(0);
        resetBall(ThisIsAOverloadedMethod);



    }

    public void setDimX(double newWidth) {
        this.dimX = newWidth;
        double rightMargin = 20;
        player2.getRacket().setPosX(newWidth - player2.getRacket().getWidth() - rightMargin);
    }

    public Ball getBall() { return ball; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public double getDimY() {
        return dimY;
    }

    public double getDimX() {
        return dimX;
    }
    public Controller getController() {
        return controller;
    }
    public void setController(Controller controller){
        this.controller = controller;
    }
    public double getOriginalSpeedX() {
        return originalSpeedX;
    }

    public void setOriginalSpeedX(double originalSpeedX) {
        this.originalSpeedX = originalSpeedX;
    }

    public double getOriginalSpeedY() {
        return originalSpeedY;
    }

    public void setOriginalSpeedY(double originalSpeedY) {
        this.originalSpeedY = originalSpeedY;
    }
    public double getPosSpeedX() {return ball.getSpeedX();}
    public double getPosSpeedY() {return ball.getSpeedY();}
    public void setPosSpeedX(double temp){
        ball.setSpeedX(temp);
    }
    public void setPosSpeedY(double temp){
        ball.setSpeedY(temp);
    }
    @Override
    public void resizeX(double factor) {
        this.dimX *= factor;
        ball.resizeX(factor);
        player1.resizeX(factor);
        player2.resizeX(factor);
        //becuase the canvas draws from the left
        double rightMargin = 20;
        player2.getRacket().setPosX(this.dimX - player2.getRacket().getWidth() - rightMargin);
    }

    @Override
    public void resizeY(double factor) {
        this.dimY *= factor;
        ball.resizeY(factor);
        player1.resizeY(factor);
        player2.resizeY(factor);
    }
    public boolean isSoreLoser() {
        return soreLoser;
    }

    public void setSoreLoser(boolean soreLoser) {
        this.soreLoser = soreLoser;
    }
    public String getTitle(){
        return getPlayer1().getName() + " VS " + getPlayer2().getName();
    }
    public static class GameBuilder {
        private int id;
        private int player1Score;
        private int player2Score;
        private String player1Name;
        private String player2Name;
        private Double speedX;
        private Double speedY;
        private int score;
        private String title;
        //private String
        //dataList.add(game.getPlayer1().getName());  // String
            //dataList.add(game.getPlayer2().getName());  // String
            //dataList.add(game.getPlayer1().getScore()); // Integer
            //dataList.add(game.getPlayer2().getScore()); // Integer
            //dataList.add(game.getOriginalSpeedX());      // Double
            //dataList.add(game.isSoreLoser());            // Boolean (Direction)
        public GameBuilder setId(int id) {
            this.id = id;
            return this;
        }
        public GameBuilder setPlayer1Score(int player1Score) {
            this.player1Score = player1Score;
            return this;
        }

        public GameBuilder setPlayer2Score(int player2Score) {
            this.player2Score = player2Score;
            return this;
        }

        public GameBuilder setPlayer1Name(String player1Name) {
            this.player1Name = player1Name;
            return this;
        }

        public GameBuilder setPlayer2Name(String player2Name) {
            this.player2Name = player2Name;
            return this;
        }

        public GameBuilder setSpeedX(Double speedX) {
            this.speedX = speedX;
            return this;
        }

        public GameBuilder setSpeedY(Double speedY) {
            this.speedY = speedY;
            return this;
        }

        public GameBuilder setScore(int score) {
            this.score = score;
            return this;
        }
        public GameBuilder setTitle(String title){
            this.title = title;
            return this;
        }
        public Game build() {
            return new Game(this);
        }
    }


}
