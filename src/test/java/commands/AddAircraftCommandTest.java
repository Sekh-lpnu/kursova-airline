package commands;

import database.TestDatabaseUtils;
import manager.Airline;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class AddAircraftCommandTest {

    @Mock
    private Airline airline;

    private AddAircraftCommand command;

    @BeforeEach
    void setUp() {
// Ініціалізація мок-об'єктів
        MockitoAnnotations.openMocks(this);
// Створення тестової команди з валідними параметрами
        command = new AddAircraftCommand(airline, "Boeing 737", 5000.0, 2.5, 180, 20.0, 1);
    }

    @Test
    void testExecute_CallsAddAircraftWithCorrectParameters() {
// Виклик методу execute
        command.execute();

// Перевірка, що метод addAircraft викликано з правильними параметрами
        verify(airline, times(1)).addAircraft("Boeing 737", 5000.0, 2.5, 180, 20.0, 1);
    }

    @Test
    void testExecute_WithZeroRange() {
// Створення команди з range = 0
        command = new AddAircraftCommand(airline, "Airbus A320", 0.0, 2.0, 150, 15.0, 2);
        command.execute();

// Перевірка, що метод викликано з range = 0
        verify(airline, times(1)).addAircraft("Airbus A320", 0.0, 2.0, 150, 15.0, 2);
    }

    @Test
    void testExecute_WithZeroPassengerCapacity() {
// Створення команди з passengerCapacity = 0
        command = new AddAircraftCommand(airline, "Cargo Plane", 10000.0, 3.0, 0, 50.0, 3);
        command.execute();

// Перевірка, що метод викликано з passengerCapacity = 0
        verify(airline, times(1)).addAircraft("Cargo Plane", 10000.0, 3.0, 0, 50.0, 3);
    }

    @Test
    void testExecute_WithNegativeAircraftType() {
// Створення команди з від'ємним aircraftType
        command = new AddAircraftCommand(airline, "Test Plane", 3000.0, 1.5, 100, 10.0, -1);
        command.execute();

// Перевірка, що метод викликано з aircraftType = -1
        verify(airline, times(1)).addAircraft("Test Plane", 3000.0, 1.5, 100, 10.0, -1);
    }
    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}