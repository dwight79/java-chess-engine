import java.sql.*;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:chess.db";

    static {
        createTableIfNotExists();
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private static void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS chess (
                move_number INTEGER PRIMARY KEY AUTOINCREMENT,
                white TEXT,
                black TEXT
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // White move → new row
    public static void insertWhiteMove(String move) {
        String sql = "INSERT INTO chess (white) VALUES (?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, move);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Black move → update last row
    public static void insertBlackMove(String move) {
        String sql = """
            UPDATE chess
            SET black = ?
            WHERE move_number = (SELECT MAX(move_number) FROM chess);
        """;

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, move);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}