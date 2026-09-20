# Pong Game (JavaFX)

A 2-player Pong game built in Java/JavaFX. Made this to get hands on with multi threading, serialization and database saving with PostgreSQL rather than just reading about them.

## How it works

- The game runs on its own thread separate from the UI thread, looping roughly every 16ms (~60 times a second) to update physics and redraw the canvas.
- Both paddles read from a shared set of currently pressed keys each frame, so both players can move at the same time instead of one input blocking the other.
- Game state (names, scores, ball speed) can be saved and reloaded using Java serialization.
- Match history can be saved to and pulled from a PostgreSQL database.

## Controls

| Action | Key |
|---|---|
| Player 1 up | Q |
| Player 1 down | A |
| Player 2 up | ↑ |
| Player 2 down | ↓ |
| Pause / Resume | ESC |

## Requirements

- JDK 25
- PostgreSQL running locally on port 5432
- Maven not required — the project ships with the Maven wrapper (`mvnw` / `mvnw.cmd`)

## Setup

1. Clone the repo:
   ```
   git clone https://github.com/2rt/ping_pong_game
   cd Project_1_Part_3
   ```
2. Create the database:
   ```sql
   CREATE DATABASE game;
   ```
   Make sure your local `postgres` user's password is set to `root`, or edit the credentials in `DatabaseManager.java` to match your own setup.
3. Run it:
   ```
   ./mvnw clean javafx:run
   ```
   (Windows: `mvnw.cmd clean javafx:run`)

The required table is created automatically the first time it runs.

## Notes

- DB credentials are hardcoded in `DatabaseManager.java` for now (`postgres` / `root`, `localhost:5432/game`) — fine for local dev/coursework, not meant for production use.
- Racket width/height and ball speed can be adjusted from the menu bar in-app.

## Author

Ray E. Crowley
