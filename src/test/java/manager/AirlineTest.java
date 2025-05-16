package manager;

import aircraft.Aircraft;
import aircraft.CargoAircraft;
import aircraft.PassengerAircraft;
import aircraft.PrivateJet;
import database.DatabaseManager;
import database.TestDatabaseUtils;
import gui.AircraftListUpdater;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AirlineTest {

    @Mock
    private AircraftListUpdater updater;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private Airline airline;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenReturn(connection);

            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false); // База порожня

            airline = new Airline(updater);
        }
    }

    @Test
    void testAddPassengerAircraft() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenReturn(connection);

            // Налаштування для INSERT
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1); // Успішний INSERT
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true); // Є згенерований ключ
            when(resultSet.getInt(1)).thenReturn(1); // ID = 1

            // Налаштування для SELECT у getAircraftList
            ResultSet emptyResultSet = mock(ResultSet.class);
            when(emptyResultSet.next()).thenReturn(false);
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(emptyResultSet); // для load

            ResultSet filledResultSet = mock(ResultSet.class);
            when(filledResultSet.next()).thenReturn(true, false);
            when(filledResultSet.getInt("id")).thenReturn(1);
            when(filledResultSet.getString("model")).thenReturn("Boeing 737");
            when(filledResultSet.getDouble("range")).thenReturn(5000.0);
            when(filledResultSet.getDouble("fuelConsumptionPerUnit")).thenReturn(2.5);
            when(filledResultSet.getString("type")).thenReturn("passenger");
            when(filledResultSet.getInt("passengerCapacity")).thenReturn(180);
            when(filledResultSet.getDouble("cargoCapacity")).thenReturn(20.0);
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(filledResultSet); // для getAircraftList

            airline.addAircraft("Boeing 737", 5000, 2.5, 180, 20, 1);

            List<Aircraft> aircraftList = airline.getAircraftList();
            assertEquals(1, aircraftList.size());
            Aircraft aircraft = aircraftList.get(0);
            assertTrue(aircraft instanceof PassengerAircraft);
            assertEquals("Boeing 737", aircraft.getModel());
            assertEquals(1, aircraft.getId());
            verify(updater, times(2)).updateAircraftListInGUI(any()); // Ініціалізація + додавання
        }
    }


    @Test
    void testAddAircraftWithInvalidType() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenReturn(connection);

            // Налаштування порожньої бази
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);

            airline.addAircraft("Test Model", 4000, 3.0, 100, 10, 999);

            List<Aircraft> aircraftList = airline.getAircraftList();
            assertTrue(aircraftList.isEmpty());
            verify(updater, times(1)).updateAircraftListInGUI(any()); // Лише ініціалізація
            verify(preparedStatement, never()).executeUpdate();
        }
    }

    @Test
    void testRemoveAircraftSuccessfully() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenReturn(connection);

            // Налаштування для INSERT
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(1);

            // Налаштування для SELECT після додавання
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false); // Один запис
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("model")).thenReturn("Boeing 737");
            when(resultSet.getDouble("range")).thenReturn(5000.0);
            when(resultSet.getDouble("fuelConsumptionPerUnit")).thenReturn(2.5);
            when(resultSet.getString("type")).thenReturn("passenger");
            when(resultSet.getInt("passengerCapacity")).thenReturn(180);
            when(resultSet.getDouble("cargoCapacity")).thenReturn(20.0);

            airline.addAircraft("Boeing 737", 5000, 2.5, 180, 20, 1);

            // Налаштування для DELETE
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            // Налаштування для SELECT після видалення
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false); // База порожня

            boolean result = airline.removeAircraft(1);

            assertTrue(result);
            assertTrue(airline.getAircraftList().isEmpty());
            verify(updater, times(3)).updateAircraftListInGUI(any()); // Ініціалізація, додавання, видалення
        }
    }

    @Test
    void testRemoveAircraftNotFound() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(0);

            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            boolean result = airline.removeAircraft(999);

            assertFalse(result);
            assertTrue(airline.getAircraftList().isEmpty());
            verify(updater, times(1)).updateAircraftListInGUI(any()); // Лише ініціалізація
        }
    }

    @Test
    void testSortAircraftByRangeAscending() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenReturn(connection);

            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false); // Два записи
            when(resultSet.getInt("id")).thenReturn(1, 2);
            when(resultSet.getString("model")).thenReturn("Boeing 737", "Airbus A320");
            when(resultSet.getDouble("range")).thenReturn(5000.0, 6000.0);
            when(resultSet.getDouble("fuelConsumptionPerUnit")).thenReturn(2.5, 2.8);
            when(resultSet.getString("type")).thenReturn("passenger", "passenger");
            when(resultSet.getInt("passengerCapacity")).thenReturn(180, 150);
            when(resultSet.getDouble("cargoCapacity")).thenReturn(20.0, 15.0);

            List<Aircraft> sortedList = airline.sortAircraftByRange(1);

            assertEquals(2, sortedList.size());
            assertEquals("Boeing 737", sortedList.get(0).getModel());
            assertEquals("Airbus A320", sortedList.get(1).getModel());
            verify(updater, times(2)).updateAircraftListInGUI(any()); // Ініціалізація та сортування
        }
    }

    @Test
    void testSortAircraftByRangeDescending() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenReturn(connection);

            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false); // Два записи
            when(resultSet.getInt("id")).thenReturn(1, 2);
            when(resultSet.getString("model")).thenReturn("Boeing 737", "Airbus A320");
            when(resultSet.getDouble("range")).thenReturn(5000.0, 6000.0);
            when(resultSet.getDouble("fuelConsumptionPerUnit")).thenReturn(2.5, 2.8);
            when(resultSet.getString("type")).thenReturn("passenger", "passenger");
            when(resultSet.getInt("passengerCapacity")).thenReturn(180, 150);
            when(resultSet.getDouble("cargoCapacity")).thenReturn(20.0, 15.0);

            List<Aircraft> sortedList = airline.sortAircraftByRange(2);

            assertEquals(2, sortedList.size());
            assertEquals("Airbus A320", sortedList.get(0).getModel());
            assertEquals("Boeing 737", sortedList.get(1).getModel());
            verify(updater, times(2)).updateAircraftListInGUI(any()); // Ініціалізація та сортування
        }
    }

    @Test
    void testFindAircraftByFuel() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenReturn(connection);

            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false); // Один запис
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("model")).thenReturn("Boeing 737");
            when(resultSet.getDouble("range")).thenReturn(5000.0);
            when(resultSet.getDouble("fuelConsumptionPerUnit")).thenReturn(2.5);
            when(resultSet.getString("type")).thenReturn("passenger");
            when(resultSet.getInt("passengerCapacity")).thenReturn(180);
            when(resultSet.getDouble("cargoCapacity")).thenReturn(20.0);

            List<Aircraft> foundAircraft = airline.findAircraftByFuel(2.0, 3.0);

            assertEquals(1, foundAircraft.size());
            assertEquals("Boeing 737", foundAircraft.get(0).getModel());
            verify(updater, times(2)).updateAircraftListInGUI(any()); // Лише виклик у findAircraftByFuel
        }
    }

    @Test
    void testGetAircraftListObservable() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenReturn(connection);

            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery("SELECT * FROM aircrafts")).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false); // Один запис
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("model")).thenReturn("Boeing 737");
            when(resultSet.getDouble("range")).thenReturn(5000.0);
            when(resultSet.getDouble("fuelConsumptionPerUnit")).thenReturn(2.5);
            when(resultSet.getString("type")).thenReturn("passenger");
            when(resultSet.getInt("passengerCapacity")).thenReturn(180);
            when(resultSet.getDouble("cargoCapacity")).thenReturn(20.0);

            ObservableList<Aircraft> observableList = airline.getAircraftListObservable();

            assertEquals(1, observableList.size());
            assertEquals("Boeing 737", observableList.get(0).getModel());
            verify(updater, times(1)).updateAircraftListInGUI(any()); // Лише ініціалізація
        }
    }

    @Test
    void testDatabaseConnectionFailure() throws SQLException {
        try (MockedStatic<DatabaseManager> mockedDatabase = mockStatic(DatabaseManager.class)) {
            mockedDatabase.when(DatabaseManager::getConnection).thenThrow(new SQLException("Connection failed"));

            Airline airlineWithError = new Airline(updater);
            assertTrue(airlineWithError.getAircraftList().isEmpty());
            verify(updater, times(1)).updateAircraftListInGUI(any()); // Виклик при ініціалізації
        }
    }
    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}