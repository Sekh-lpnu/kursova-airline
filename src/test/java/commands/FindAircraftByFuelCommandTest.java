package commands;

import models.Aircraft;
import database.TestDatabaseUtils;
import display.MessageDisplay;
import manager.Airline;
import org.junit.jupiter.api.AfterAll;
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
        double minFuel = 1000;
        double maxFuel = 3000;

        when(airlineMock.findAircraftByFuel(minFuel, maxFuel)).thenReturn(List.of());
        FindAircraftByFuelCommand command = new FindAircraftByFuelCommand(airlineMock, minFuel, maxFuel, messageDisplayMock);

        command.execute();

        verify(messageDisplayMock, times(1)).showMessage("Літаків з таким споживанням пального не знайдено.");
        verify(airlineMock, times(1)).findAircraftByFuel(minFuel, maxFuel);
    }

    @Test
    void testExecuteShouldShowMessageWithAircraftDetailsWhenAircraftFound() {
        double minFuel = 1000;
        double maxFuel = 3000;

        Aircraft aircraftMock = mock(Aircraft.class);
        when(aircraftMock.toString()).thenReturn("Boeing 747");
        when(airlineMock.findAircraftByFuel(minFuel, maxFuel)).thenReturn(List.of(aircraftMock));
        FindAircraftByFuelCommand command = new FindAircraftByFuelCommand(airlineMock, minFuel, maxFuel, messageDisplayMock);

        command.execute();

        String expectedMessage = "Boeing 747\n";
        verify(messageDisplayMock, times(1)).showMessage(expectedMessage);
        verify(airlineMock, times(1)).findAircraftByFuel(minFuel, maxFuel);
    }

    @Test
    void testExecuteShouldCallFindAircraftByFuel() {
        double minFuel = 1000;
        double maxFuel = 3000;

        when(airlineMock.findAircraftByFuel(minFuel, maxFuel)).thenReturn(List.of());
        FindAircraftByFuelCommand command = new FindAircraftByFuelCommand(airlineMock, minFuel, maxFuel, messageDisplayMock);

        command.execute();

        verify(airlineMock, times(1)).findAircraftByFuel(minFuel, maxFuel);
    }
    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}