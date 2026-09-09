package com.example.project_1_part_3.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // 1. Correct PostgreSQL URL (matches your hibernate.cfg.xml)
    private static final String URL = "jdbc:postgresql://localhost:5432/game";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    static {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // 2. Fixed PostgreSQL syntax: SERIAL instead of AUTOINCREMENT
            // 3. Removed TEXT/REAL and used standard VARCHAR/DECIMAL
            //game.getPlayer1().getName());  // String
            //game.getPlayer2().getName());  // String
            //game.getPlayer1().getScore()); // Integer
            //game.getPlayer2().getScore()); // Integer
            //game.getPosSpeedX());      // Double
            //game.getPosSpeedY());      // Double
            //game.isSoreLoser());            // Boolean (Direction)
            //player1Name, player2Name,
            // player1Score, player2Score, speedX, speedY
            String sql = """
                    CREATE TABLE IF NOT EXISTS game (
                        id SERIAL PRIMARY KEY,
                        player1Name VARCHAR(255),
                        player2Name VARCHAR(255),
                        player1Score INTEGER,
                        player2Score INTEGER,
                        speedX DECIMAL,
                        speedY DECIMAL,
                        finalScore INTEGER,
                        title VARCHAR(255)
                    )
                    """;

            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        // 4. Crucial: Pass USER and PASSWORD to fix the Authentication error
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}