package commands;

import database.TestDatabaseUtils;
import display.MessageDisplay;
import manager.Airline;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


public class RemoveAircraftCommandTest {

    private Airline airlineMock;
    private MessageDisplay messageDisplayMock;
    private RemoveAircraftCommand removeAircraftCommand;

    @BeforeEach
    void setUp() {
        airlineMock = mock(Airline.class);
        messageDisplayMock = mock(MessageDisplay.class);
    }

    @Test
    void testExecuteShouldShowMessageWhenAircraftRemoved() {
        // Arrange
        int aircraftId = 1;

        // Мокаємо метод removeAircraft, щоб він повертав true (літак видалений)
        when(airlineMock.removeAircraft(aircraftId)).thenReturn(true);

        // Створюємо команду
        removeAircraftCommand = new RemoveAircraftCommand(airlineMock, aircraftId, messageDisplayMock);

        // Act
        removeAircraftCommand.execute();

        // Assert
        verify(messageDisplayMock, times(1)).showMessage("Літак з ID " + aircraftId + " успішно видалено.");
        verify(airlineMock, times(1)).removeAircraft(aircraftId);
    }

    @Test
    void testExecuteShouldShowMessageWhenAircraftNotFound() {
        // Arrange
        int aircraftId = 1;

        // Мокаємо метод removeAircraft, щоб він повертав false (літак не знайдений)
        when(airlineMock.removeAircraft(aircraftId)).thenReturn(false);

        // Створюємо команду
        removeAircraftCommand = new RemoveAircraftCommand(airlineMock, aircraftId, messageDisplayMock);

        // Act
        removeAircraftCommand.execute();

        // Assert
        verify(messageDisplayMock, times(1)).showMessage("Літак з ID " + aircraftId + " не знайдено.");
        verify(airlineMock, times(1)).removeAircraft(aircraftId);
    }

    @Test
    void testExecuteShouldCallRemoveAircraft() {
        // Arrange
        int aircraftId = 1;

        // Мокаємо метод removeAircraft, щоб уникнути неочікуваної поведінки
        when(airlineMock.removeAircraft(aircraftId)).thenReturn(false);

        // Створюємо команду
        removeAircraftCommand = new RemoveAircraftCommand(airlineMock, aircraftId, messageDisplayMock);

        // Act
        removeAircraftCommand.execute();

        // Assert
        verify(airlineMock, times(1)).removeAircraft(aircraftId);
    }
    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}