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
        int aircraftId = 1;

        when(airlineMock.removeAircraft(aircraftId)).thenReturn(true);
        removeAircraftCommand = new RemoveAircraftCommand(airlineMock, aircraftId, messageDisplayMock);

        removeAircraftCommand.execute();

        verify(messageDisplayMock, times(1)).showMessage("Літак з ID " + aircraftId + " успішно видалено.");
        verify(airlineMock, times(1)).removeAircraft(aircraftId);
    }

    @Test
    void testExecuteShouldShowMessageWhenAircraftNotFound() {
        int aircraftId = 1;

        when(airlineMock.removeAircraft(aircraftId)).thenReturn(false);
        removeAircraftCommand = new RemoveAircraftCommand(airlineMock, aircraftId, messageDisplayMock);

        removeAircraftCommand.execute();

        verify(messageDisplayMock, times(1)).showMessage("Літак з ID " + aircraftId + " не знайдено.");
        verify(airlineMock, times(1)).removeAircraft(aircraftId);
    }

    @Test
    void testExecuteShouldCallRemoveAircraft() {
        int aircraftId = 1;

        when(airlineMock.removeAircraft(aircraftId)).thenReturn(false);
        removeAircraftCommand = new RemoveAircraftCommand(airlineMock, aircraftId, messageDisplayMock);
        removeAircraftCommand.execute();

        verify(airlineMock, times(1)).removeAircraft(aircraftId);
    }
    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}