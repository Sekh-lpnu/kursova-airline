package commands;

import aircraft.Aircraft;
import database.TestDatabaseUtils;
import display.MessageDisplay;
import manager.Airline;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.util.List;

public class FindAircraftByFuelCommandTest {

    private Airline airlineMock;
    private MessageDisplay messageDisplayMock;

    @BeforeEach
    void setUp() {
        airlineMock = mock(Airline.class);
        messageDisplayMock = mock(MessageDisplay.class);
    }

    @Test
    void testExecuteShouldShowMessageWhenNoAircraftFound() {
        // Arrange
        double minFuel = 1000;
        double maxFuel = 3000;

        // Мокаємо результат пошуку, щоб повертався порожній список
        when(airlineMock.findAircraftByFuel(minFuel, maxFuel)).thenReturn(List.of());

        // Створюємо команду
        FindAircraftByFuelCommand command = new FindAircraftByFuelCommand(airlineMock, minFuel, maxFuel, messageDisplayMock);

        // Act
        command.execute();

        // Assert
        verify(messageDisplayMock, times(1)).showMessage("Літаків з таким споживанням пального не знайдено.");
        verify(airlineMock, times(1)).findAircraftByFuel(minFuel, maxFuel);
    }

    @Test
    void testExecuteShouldShowMessageWithAircraftDetailsWhenAircraftFound() {
        // Arrange
        double minFuel = 1000;
        double maxFuel = 3000;

        // Мокаємо результат пошуку, щоб повертався список з літаком
        Aircraft aircraftMock = mock(Aircraft.class);
        when(aircraftMock.toString()).thenReturn("Boeing 747");
        when(airlineMock.findAircraftByFuel(minFuel, maxFuel)).thenReturn(List.of(aircraftMock));

        // Створюємо команду
        FindAircraftByFuelCommand command = new FindAircraftByFuelCommand(airlineMock, minFuel, maxFuel, messageDisplayMock);

        // Act
        command.execute();

        // Assert
        String expectedMessage = "Boeing 747\n";
        verify(messageDisplayMock, times(1)).showMessage(expectedMessage);
        verify(airlineMock, times(1)).findAircraftByFuel(minFuel, maxFuel);
    }

    @Test
    void testExecuteShouldCallFindAircraftByFuel() {
        // Arrange
        double minFuel = 1000;
        double maxFuel = 3000;

        // Мокаємо результат пошуку, щоб повертався порожній список
        when(airlineMock.findAircraftByFuel(minFuel, maxFuel)).thenReturn(List.of());

        // Створюємо команду
        FindAircraftByFuelCommand command = new FindAircraftByFuelCommand(airlineMock, minFuel, maxFuel, messageDisplayMock);

        // Act
        command.execute();

        // Assert
        verify(airlineMock, times(1)).findAircraftByFuel(minFuel, maxFuel);
    }
    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}