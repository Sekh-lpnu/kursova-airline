package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.fail;

public class TestDatabaseUtils {

    public static void resetAircraftTable() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Видалити всі дані
            stmt.execute("DELETE FROM aircrafts");

            // Скинути автоінкремент ID до 1
            stmt.execute("DBCC CHECKIDENT ('aircrafts', RESEED, 0)");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Не вдалося скинути автоінкремент ID: " + e.getMessage());
        }
    }
}
