package database;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import database.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class DatabaseManagerTest {

    @Test
    void testGetConnection() throws SQLException {
        // Мокування статичного методу
        try (MockedStatic<DatabaseManager> mockedStatic = mockStatic(DatabaseManager.class)) {
            // Мокування повернення з'єднання
            Connection mockConnection = mock(Connection.class);
            mockedStatic.when(DatabaseManager::getConnection).thenReturn(mockConnection);

            // Перевірка
            Connection conn = DatabaseManager.getConnection();
            assertNotNull(conn);
            assertEquals(mockConnection, conn);
        }
    }
}
