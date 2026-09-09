package com.example.project_1_part_3.data.database;

import com.example.project_1_part_3.model.Game;
import com.example.project_1_part_3.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    public void addGame(Game game) {

        String sql =
                "INSERT INTO game (player1Name, player2Name, player1Score, player2Score, speedX, speedY, finalScore, title) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {
            //game.getPlayer1().getName());  // String
            //game.getPlayer2().getName());  // String
            //game.getPlayer1().getScore()); // Integer
            //game.getPlayer2().getScore()); // Integer
            //game.getPosSpeedX());      // Double
            //game.getPosSpeedY());      // Double
            //game.isSoreLoser());            // Boolean (Direction)
            stmt.setString(1, game.getPlayer1().getName());
            stmt.setString(2, game.getPlayer2().getName());
            stmt.setInt(3, game.getPlayer1().getScore());
            stmt.setInt(4, game.getPlayer2().getScore());
            stmt.setDouble(5, game.getPosSpeedX());
            stmt.setDouble(6, game.getPosSpeedY());
            stmt.setInt(7, game.getScore());
            stmt.setString(8, game.getTitle());




            stmt.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
    public static List<Game> getGame() {

        List<Game> games = new ArrayList<>();

        String sql = "SELECT * FROM game ORDER BY id DESC LIMIT 1";

        try (
                Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            if (rs.next()){
                //player1Name VARCHAR(255),
                        //player2Name VARCHAR(255),
                        //player1Score INTEGER,
                        //player2Score INTEGER,
                        //speedX DECIMAL,
                        //speedY DECIMAL,
                        //loser INTEGER
                //title VARCHAR(255)
                Game game =
                        new Game.GameBuilder()
                                .setId(rs.getInt("id"))
                                .setPlayer1Name(rs.getString("player1Name"))
                                .setPlayer2Name(rs.getString("player2Name"))
                                .setPlayer1Score(rs.getInt("player1Score"))
                                .setPlayer2Score(rs.getInt("player2Score"))
                                .setSpeedX(rs.getDouble("speedX"))
                                .setSpeedY(rs.getDouble("speedY"))
                                .setScore(rs.getInt("finalScore"))
                                .setTitle(rs.getString("title"))
                                .build();
                games.add(game);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return games;
    }
}