package com.example.project_1_part_3.model;



public class Player implements Resizable {
    /** The player's display name. */
    private String name;
    /** The player's current point total. */
    private int score;
    /** The paddle object controlled by this player. */
    private Racket racket;

    /**
     * Constructs a new Player with a name and a racket.
     * @param name   The player's name.
     * @param racket The racket assigned to the player.
     */
    public Player(String name, Racket racket) {
        this.name = name;
        this.racket = racket;
        this.score = getScore();
    }
    /** Scales the player's racket horizontally. */
    @Override
    public void resizeX(double factor) {
        if (racket != null) racket.resizeX(factor);
    }
    /** Scales the player's racket vertically. */
    @Override
    public void resizeY(double factor) {
        if (racket != null) racket.resizeY(factor);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getScore() { return score; }
    public void setScore(int newScore){this.score = newScore;}
    public void addScore() { this.score++; }
    public Racket getRacket() { return racket; }


}