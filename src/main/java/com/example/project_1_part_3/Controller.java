package com.example.project_1_part_3;

import com.example.project_1_part_3.data.database.DAO;
import com.example.project_1_part_3.data.serialization.SerializationManager;
import com.example.project_1_part_3.model.Game;
import com.example.project_1_part_3.view.BallManager;
import com.example.project_1_part_3.view.GameCanvas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * This class is the "brain" of the game that connects the logic to the screen.
 * <p>
 * This class implements the FXML UI elements and manages the game states,
 * like pausing and scoring.
 * </p>
 * <p>
 * It also catches keyboard inputs to move the paddles and updates,
 * the canvas so the players see what's happening.
 * </p>
 * @author Ray E. Crowley
 */
public class Controller {

    private final DAO gameDAO = new DAO();
    @FXML private Canvas canvas;
    @FXML private Label player1Name, player2Name, player1Score, player2Score, finalScore,gamePaused;
    @FXML private StackPane canvascontroller;
    private BallManager ballManager;
    private GameCanvas gameRenderer;
    private Game game;
    /** Magic number 30, for the fonts */
    private final Font font = new Font(30);
    /** The pixel distance a paddle moves per input frame. */
    private final int MOVE_SPEED = 15;
    private boolean paused = false;
    private boolean canRun = true;
    /** Controls how often the ball speed increases based on hits. Starts at 2 */
    private int modulusTime = 2;
    /** Increments on racket hits; checked against modulusTime for speed. */
    private int modulusCounter = 0;
    /** Holds the Keys currently held down by the user. */
    private Set<KeyCode> pressedKeys = new HashSet<>();

    /**
     * The initializer, controls the first instance of the game.
     * <p>
     *     It setups the font, and the canvas dimensions.
     * </p>
     */
    @FXML
    public void initialize() {
        gameRenderer = new GameCanvas(canvas.getWidth(),canvas.getHeight());
        gameRenderer.initialize(canvas, canvascontroller);
        //*Sends the data the controller knows throughout the project*/
        Platform.runLater(() -> {
            drawGame();
            double w = canvas.getWidth() > 0 ? canvas.getWidth() : 800;
            double h = canvas.getHeight() > 0 ? canvas.getHeight() : 600;
            game = new Game(w, h, this);
            game.getPlayer1().setName(player1Name.getText());
            game.getPlayer2().setName(player2Name.getText());
            game.getPlayer1().setScore(Integer.parseInt(player1Score.getText()));
            game.getPlayer2().setScore(Integer.parseInt(player2Score.getText()));
            //game.setController(this);
            System.out.println("labels name: " + player1Name.getText());
            System.out.println("games name: " + game.getPlayer1().getName());


            ballManager = new BallManager(this);
            Thread t = new Thread(ballManager);
            t.setDaemon(true);
            t.start();

        });
        player1Name.setText(playerName("Player 1"));
        player2Name.setText(playerName("Player 2"));

        setFinalScore();
        applyFont(font,
                player1Name, player2Name,
                player1Score, player2Score, finalScore,
                gamePaused
        );

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (game != null && oldVal.doubleValue() > 0) {
                double factor = newVal.doubleValue() / oldVal.doubleValue();
                game.resizeX(factor);
                drawGame();
            }
        });
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (game != null && oldVal.doubleValue() > 0) {
                double factor = newVal.doubleValue() / oldVal.doubleValue();
                game.resizeY(factor);
                drawGame();
            }
        });
    }
    /**
     *Method is used to update the scores/redraw canvas, it kept its name due to already existing.
     * <p>
     *     In part 1 of the project.
     * </p>
     */
    public void drawGame() {
        if (gameRenderer != null && game != null) {
            gameRenderer.drawGame(game);

            player1Score.setText(String.valueOf(game.getPlayer1().getScore()));
            player2Score.setText(String.valueOf(game.getPlayer2().getScore()));
            game.getPlayer1().setScore(Integer.parseInt(player1Score.getText()));
            game.getPlayer2().setScore(Integer.parseInt(player2Score.getText()));

        }
    }
    /**
     * Method is the setter for the set of currently pressed keys.
     * @param keys A set of KeyCodes passed from the KeyboardListener.
     */
    public void setPressedKeys(Set<KeyCode> keys) {
        this.pressedKeys = keys;
    }
    /**
     * Method handles the input processing for paddle movement.
     * <p>
     *    Checks if the game is initialized and running before moving.
     * </p>
     */
    public void processInput() {
        if ((game == null) || (!running()) ) return;

        if (pressedKeys.contains(KeyCode.Q)) movePlayer1Up();
        if (pressedKeys.contains(KeyCode.A)) movePlayer1Down();

        if (pressedKeys.contains(KeyCode.UP)) movePlayer2Up();
        if (pressedKeys.contains(KeyCode.DOWN)) movePlayer2Down();
    }
    /**
     *Method is the racket movement UP for Player 1.
     */
    public void movePlayer1Up() {
        if (game == null) return;
        double currentY = game.getPlayer1().getRacket().getPosY();
        if (currentY - MOVE_SPEED >= getUpperLimit()) {
            game.getPlayer1().getRacket().setPosY(currentY - MOVE_SPEED);
            drawGame();
        }
    }
    /**
     *Method is the racket movement DOWN for Player 1.
     */
    public void movePlayer1Down() {
        if (game == null) return;
        double currentY = game.getPlayer1().getRacket().getPosY();
        double h = game.getPlayer1().getRacket().getHeight();
        if (currentY + h + MOVE_SPEED <= canvas.getHeight()+5) {
            game.getPlayer1().getRacket().setPosY(currentY + MOVE_SPEED);
            drawGame();
        }
    }
    /**
     *Method is the racket movement UP for Player 2.
     */
    public void movePlayer2Up() {
        if (game == null) return;
        double currentY = game.getPlayer2().getRacket().getPosY();
        if (currentY - MOVE_SPEED >= getUpperLimit()) {
            game.getPlayer2().getRacket().setPosY(currentY - MOVE_SPEED);
            drawGame();
        }
    }
    /**
     *Method is the racket movement DOWN for Player 2.
     */
    public void movePlayer2Down() {
        if (game == null) return;
        double currentY = game.getPlayer2().getRacket().getPosY();
        double h = game.getPlayer2().getRacket().getHeight();
        if (currentY + h + MOVE_SPEED <= canvas.getHeight()+5) {
            game.getPlayer2().getRacket().setPosY(currentY + MOVE_SPEED);
            drawGame();
        }
    }

    /** Method handles the FXML implementation, of menu bars for Player 1*/
    @FXML protected void player1Rename() {
        player1Name.setText(playerName("Player 1"));
        game.getPlayer1().setName(player1Name.getText());
    }
    /** Method handles the FXML implementation, of menu bars for Player 2*/
    @FXML protected void player2Rename() {
        player2Name.setText(playerName("Player 2"));
        game.getPlayer2().setName(player2Name.getText());
    }
    /**
     * Method used to return what name a player should have via a Dialog box.
     * @param playerNum A String representing the default player label.
     * @return The name entered by the user or the existing name if cancelled.
     */
    private String playerName (String playerNum) {
        setRunning(false);
        String[] split = playerNum.split(" ");
        int num = Integer.parseInt(split[1]);
        TextInputDialog dialog = new TextInputDialog();
        if (num == 1) {
            dialog.setTitle("Player 1 Selection");
        } else if (num == 2) {
            dialog.setTitle("Player 2 Selection");
        }
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your name:");
        String result = "";
        if (num == 1) {
            result = dialog.showAndWait().orElse(player1Name.getText());
        } else if (num == 2) {
            result = dialog.showAndWait().orElse(player2Name.getText());
        }
        if (result == "") {
            if (split[0] != "Player") {
                if (num == 1) {
                    interfaceChecker();
                    return player1Name.getText();
                } else if (num == 2) {
                    interfaceChecker();
                    return player2Name.getText();
                }
            }
        } else {
            /* Handles when the user exits out of the dialog*/
            interfaceChecker();
            return result;
        }
        interfaceChecker();
        return "";
    }
    /**Method is the FXML version, referencing an actual important method.*/
    @FXML protected void changeFinalScore() { setFinalScore(); }
    private void loadFinalScore(int num){
        finalScore.setText(String.valueOf(num));
    }
    /**
     * Method is a text dialog, that queries the players.
     * <p>
     *     For a suitable final score to set, but it still does control.
     * </p>
     * <p>
     *     How ludicrous a game can be upper limit is 20, also makes sure no negatives.
     * </p>
     */
    private void setFinalScore() {
        setRunning(false);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Enter final score:");
        String result = dialog.showAndWait().orElse(finalScore.getText());
        if (result == ""){
            finalScore.setText(finalScore.getText());
            interfaceChecker();
            finalScore.getText();
        } else if (Integer.parseInt(result) > 20 || Integer.parseInt(result) <=0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Number is not realistic");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    setFinalScore();
                }
            });
        } else{
            finalScore.setText(result);
            interfaceChecker();
        }
        finalScore.setText(finalScore.getText());
        interfaceChecker();
    }
    /**Method a getter for finalScore
     * @return  Integer of the String text in the finalScore Label.
     */
    public int getFinalScore(){
        return Integer.parseInt(finalScore.getText());
    }
    /** Method handles the FXML implementation, of predefined widths of Rackets*/
    @FXML protected void setThickness() { choiceSelector(0); }
    /** Method handles the FXML implementation, of predefined lengths of Rackets*/
    @FXML protected void setSize() { choiceSelector(1); }
    /**
     * Method handles the resizing logic of Rackets.
     *<p>
     *     Method takes in an integer, and picks one of two arraylists.
     *</p>
     * <p>
     *     0 being for width of Rackets.
     * </p>
     * <p>
     *     1 being for height of Rackets.
     * </p>
     * @param choice it's an integer, and it's either a 0 or a 1
     */
    private void choiceSelector(int choice) {
        setRunning(false);
        List<String> options = (choice == 0) ? Arrays.asList("Thin", "Normal", "Wide") : Arrays.asList("Small", "Normal", "Long");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(1), options);
        dialog.showAndWait().ifPresent(selection -> {
            if (game != null) {
                if (choice == 0) {
                    double width = switch (selection) {
                        case "Thin" -> 5;
                        case "Wide" -> 20;
                        default -> 10;
                    };
                    game.getPlayer1().getRacket().setWidth(width);
                    game.getPlayer2().getRacket().setWidth(width);
                    game.setDimX(canvas.getWidth());
                } else {
                    double height = switch (selection) {
                        case "Small" -> 50;
                        case "Long" -> 200;
                        default -> 125;
                    };
                    game.getPlayer1().getRacket().setHeight(height);
                    game.getPlayer2().getRacket().setHeight(height);
                }
                drawGame();
                interfaceChecker();
            }
        });
    }
    public int getModulusTime() { return modulusTime; }
    public void setModulusTime(int modulusTime) { this.modulusTime = modulusTime; }
    @FXML
    protected void onSaveGame() {
        try {
            setRunning(false);
            List<Object> dataList = new ArrayList<>();

            // Add values in a specific order (important for loading!)

            dataList.add(game.getPlayer1().getName());  // String
            dataList.add(game.getPlayer2().getName());  // String
            dataList.add(game.getPlayer1().getScore()); // Integer
            dataList.add(game.getPlayer2().getScore()); // Integer
            dataList.add(game.getPosSpeedX());      // Double
            dataList.add(game.getPosSpeedY());      // Double
            dataList.add(getFinalScore());            // Boolean (Direction)
            System.out.println(dataList);
            // Call your Singleton to save
            SerializationManager.getInstance().saveData(dataList);

            System.out.println("Success " + "Game Data Saved!");
            interfaceChecker();
        } catch (Exception e) {
            System.out.println( "Error " + "Could not save: " + e.getMessage());
        }
    }
    @FXML
    protected void onLoadGame(){
        try {
            setRunning(false);
            List<Object> loadedData = SerializationManager.getInstance().loadData();
            //System.out.println(loadedData.get(0) + loadedData.get(1), loadedData.get(2)));
            // Pull them out by the exact same index order
            System.out.println("--- Loaded Data Snapshot ---");
            System.out.println(loadedData);
            System.out.println("----------------------------");
            System.out.println(game.getPlayer1().getScore());
            game.getPlayer1().setName((String) loadedData.get(0));
            game.getPlayer2().setName((String) loadedData.get(1));
            game.getPlayer1().setScore((int) loadedData.get(2));
            game.getPlayer2().setScore((int) loadedData.get(3));
            System.out.println(game.getPlayer1().getScore());
            double speedX = (double) loadedData.get(4);
            double speedY = (double) loadedData.get(5);
            loadFinalScore((int) loadedData.get(6));
            // Apply physics reset
            game.setPosSpeedX(speedX);
            game.setPosSpeedY(speedY);
            game.resetBallSer();
            player1Name.setText(game.getPlayer1().getName());
            player2Name.setText(game.getPlayer2().getName());
            player1Score.setText(Integer.toString(game.getPlayer1().getScore()));
            player2Score.setText(Integer.toString(game.getPlayer2().getScore()));
            System.out.println("Success " + "Game Data Saved!");
            // Redraw UI
            //gameRenderer.drawGame(game);
            interfaceChecker();
        } catch (Exception e) {
            //System.out.println( "Error " + "Could not save: " + e.getMessage());
            helperException(e.getMessage());
        }
    }
    @FXML
    protected void storeCurrentGame() {

        try {
            //game.getPlayer1().getName());  // String
            //game.getPlayer2().getName());  // String
            //game.getPlayer1().getScore()); // Integer
            //game.getPlayer2().getScore()); // Integer
            //game.getPosSpeedX());      // Double
            //game.getPosSpeedY());      // Double
            //game.isSoreLoser());            // Boolean (Direction)
            setRunning(false);
            Game g =
                    new Game.GameBuilder()
                            .setPlayer1Name(player1Name.getText())
                            .setPlayer2Name(player2Name.getText())
                            .setPlayer1Score(game.getPlayer1().getScore())
                            .setPlayer2Score(game.getPlayer2().getScore())
                            .setSpeedX(game.getPosSpeedX())
                            .setSpeedY(game.getPosSpeedY())
                            .setScore(getFinalScore())
                            .setTitle(game.getTitle())
                            .build();

            gameDAO.addGame(g);
            System.out.println("Game Added\n");
            interfaceChecker();

        } catch (Exception e) {
            System.out.println("Invalid Input\n");

        }
    }
    @FXML
    private void createRecentGame() {
        setRunning(false);
        List<Game> games =
                DAO.getGame();
        Game g =
                games.getFirst();
        //stmt.setString(1, game.getPlayer1().getName());
        //stmt.setString(2, game.getPlayer2().getName());
        //stmt.setInt(3, game.getPlayer1().getScore());
        //stmt.setInt(4, game.getPlayer2().getScore());
        //stmt.setDouble(5, game.getPosSpeedX());
        //stmt.setDouble(6, game.getPosSpeedY());
        //stmt.setInt(7, game.getScore());
        //stmt.setString(8, game.getTitle());
        player1Name.setText(g.getPlayer1().getName());
        player2Name.setText(g.getPlayer2().getName());
        game.getPlayer1().setName(g.getPlayer1().getName());
        game.getPlayer2().setName(g.getPlayer2().getName());
        player1Score.setText(String.valueOf(g.getPlayer1().getScore()));
        player2Score.setText(String.valueOf(g.getPlayer2().getScore()));
        game.getPlayer1().setScore(g.getPlayer1().getScore());
        game.getPlayer2().setScore(g.getPlayer2().getScore());
        game.setPosSpeedX(g.getPosSpeedX());
        game.setPosSpeedY(g.getPosSpeedY());
        loadFinalScore(g.getScore());
        System.out.println(g.getScore());
        game.resetBallSer();
        interfaceChecker();



    }
    /**
     * Method is an FXML implementation, it asks for a new modulus.
     * <p>
     *     Has the limit inside as well, as if it was above 5, you will encounter problems.
     * </p>
     */
    @FXML
    protected void changeModulusTime(){
        setRunning(false);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new Modulus:");
        String result = dialog.showAndWait().orElse(finalScore.getText());
        if (result == ""){
            interfaceChecker();

        } else if (Integer.parseInt(result) >= 5 || Integer.parseInt(result) <1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Number is not realistic");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    changeModulusTime();
                }
            });
        } else{
            interfaceChecker();
            setModulusTime(Integer.parseInt(result));
        }
        interfaceChecker();

    }

    /**
     * Method compares the parameter to see if it's false.
     * <p>
     *     if it is false it clears the hashmap of keys.
     * </p>
     * <p>
     *     So the rackets don't get stuck doing what they were doing before the pause.
     * </p>
     * @param paused either true or false
     */
    public void setRunning(boolean paused){
        this.paused = paused;
        if (!this.paused){
            this.pressedKeys.clear();
        }
    }
    public boolean running(){ return paused; }
    public Game getGame() { return game; }

    public double getUpperLimit() { return 20.0; }
    /**
     * Helper Method for setting the font.
     */
    private void applyFont(Font font, Label... labels) {
        for (Label label : labels) label.setFont(font);
    }
    /**
     * Triggers an alert when a player scores.
     * @param isleft Boolean to determine which side scored.
     */
    public void playerAlert(boolean isleft){
        setRunning(false);
        String playerName;
        if (isleft) {
            playerName = player1Name.getText();
        }else{
            playerName = player2Name.getText();
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(playerName);
        alert.setContentText(playerName +" has scored.");
        alert.showAndWait();
        interfaceChecker();
    }
    /**
     * Handles the end-of-game logic, offering a rematch or exit.
     * @param name The name of the winning player.
     */
    public void winner(String name){

        setRunning(false);
        ButtonType Exit = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType Rematch = new ButtonType("Rematch?", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION ,name +" has won.",Rematch,Exit);
        alert.setGraphic(null);
        alert.setTitle(null);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();


        if (result.isPresent() && result.get() == Rematch) {
            getGame().resetGame();
            interfaceChecker();
        } else {

            onExit();
        }
    }

    public int getModulusCounter() {
        return modulusCounter;
    }
    public void modulusIncrement(){
        this.modulusCounter++;
    }
    /**
     * FXML method to manually set the base speed of the ball (Slow, Normal, Fast).
     */
    @FXML
    private void speedSetter() {
        setRunning(false);
        List<String> options = Arrays.asList("Slow", "Normal", "Fast") ;
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(1), options);
        dialog.showAndWait().ifPresent(selection -> {
            if (game != null) {

                    double speed = switch (selection) {
                        case "Slow" -> 2;
                        case "Fast" -> 6;
                        default -> 4;
                    };

                    if (game.getBall().getSpeedX()>0) {
                        game.setOriginalSpeedX(speed);
                        game.getBall().setSpeedX(speed);
                    } else {
                        game.setOriginalSpeedX(-speed);
                        game.getBall().setSpeedX(-speed);
                    }
                    if (game.getBall().getSpeedY()>0) {
                        game.setOriginalSpeedY(speed);
                        game.getBall().setSpeedY(speed);
                    } else {
                        game.setOriginalSpeedY(-speed);
                        game.getBall().setSpeedY(-speed);
                    }
                }

                interfaceChecker();
            });
        }

    /**
     * Updates the UI Label to show "PAUSED" or empty based on state.
     */
    @FXML
    public void setGamePaused() {
        if (!running()){
            gamePaused.setText("PAUSED");
        } else{
            gamePaused.setText("");

        }

    }
    private void helperException(String e){
        setRunning(false);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error Dialog");
        alert.setContentText("The Error message is " + e);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                interfaceChecker();
            }
        });
    }
    private boolean helperAlert(){
        setRunning(false);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setContentText("Number is not realistic");
        boolean[] result = {false};
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                interfaceChecker();
                result[0] = true;
            }
        });
        return result[0];

    }
    /**
     * Resets the game to initial state with a random starting direction
     * <p>
     *     {@link Game}
     * </p>.
     */
    @FXML
    public void onRestart(){
        Random random = new Random();

        game.resetGame(random.nextBoolean());
    }
    /** Closes the application. */
    @FXML protected void onExit() { System.exit(0); }
    public void interfaceChecker(){
        if (gamePaused.getText().isEmpty()) {
            setRunning(true);
        }
    }
}