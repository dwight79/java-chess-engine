# Java Chess Engine

A console-based chess engine built in Java using object-oriented principles.

## Architecture

- **Language**: Java (OpenJDK 19 via GraalVM CE 22.3)
- **Database**: SQLite (via sqlite-jdbc 3.43.0.0 JDBC driver stored in `lib/`)
- **Build**: Manual `javac` compilation — no Maven required
- **Output**: Console TUI (not a web app)

## Project Structure

```
src/           - Java source files
  ChessGame.java     - Main entry point, game loop
  Board.java         - 8x8 board logic
  Piece.java         - Base piece class
  Bishop/King/Knight/Pawn/Queen/Rook.java - Piece subclasses
  Move.java          - Move representation
  Notation.java      - Algebraic notation parsing (e.g. e2e4)
  Player.java        - Player representation
  Position.java      - Board position helper
  DatabaseManager.java - SQLite move logging
lib/           - External JARs
  sqlite-jdbc.jar    - SQLite JDBC driver
out/           - Compiled .class files (generated at build)
```

## Running

The workflow compiles and runs the game:

```bash
javac -cp lib/sqlite-jdbc.jar -d out src/*.java
java -cp out:lib/sqlite-jdbc.jar ChessGame
```

## Gameplay

- Enter moves in coordinate notation: `e2e4`
- Type `exit` to quit
- Supports: pawn promotion, check detection, checkmate detection

## Database

SQLite database `chess.db` is created at runtime. It stores moves in a `chess` table with columns: `move_number`, `white`, `black`.
