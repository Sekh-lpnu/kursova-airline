package commands;

import database.TestDatabaseUtils;
import gui.AircraftListUpdater;
import manager.Airline;
import aircraft.Aircraft;
import aircraft.PassengerAircraft;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SortAircraftsCommandTest {

    private Airline airline;
    private AircraftListUpdater updater;
    private SortAircraftsCommand sortAircraftsCommand;

    @BeforeEach
    void setUp() {
        airline = mock(Airline.class);
        updater = mock(AircraftListUpdater.class);
        sortAircraftsCommand = new SortAircraftsCommand(airline, 1, updater); // 1 - сортування за зростанням
    }

    @Test
    void testExecuteSortsAircraftsInAscendingOrder() {
        // Arrange
        Aircraft aircraft1 = new PassengerAircraft(1, "Boeing 737", 5000, 2.5, 180, 5000);
        Aircraft aircraft2 = new PassengerAircraft(2, "Airbus A320", 6000, 3.0, 150, 4000);

        List<Aircraft> unsortedList = Arrays.asList(aircraft2, aircraft1);
        List<Aircraft> sortedList = Arrays.asList(aircraft1, aircraft2);

        when(airline.sortAircraftByRange(1)).thenReturn(sortedList);

        // Act
        sortAircraftsCommand.execute();

        // Assert
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(updater).updateAircraftListInGUI(captor.capture());
        List<Aircraft> capturedList = captor.getValue();

        assertEquals(sortedList, capturedList, "Літаки повинні бути відсортовані за зростанням дальності польоту.");
    }

    @Test
    void testExecuteSortsAircraftsInDescendingOrder() {
        // Arrange
        Aircraft aircraft1 = new PassengerAircraft(1, "Boeing 737", 5000, 2.5, 180, 5000);
        Aircraft aircraft2 = new PassengerAircraft(2, "Airbus A320", 6000, 3.0, 150, 4000);

        List<Aircraft> unsortedList = Arrays.asList(aircraft1, aircraft2);
        List<Aircraft> sortedList = Arrays.asList(aircraft2, aircraft1);

        when(airline.sortAircraftByRange(2)).thenReturn(sortedList);

        // Act
        SortAircraftsCommand descendingCommand = new SortAircraftsCommand(airline, 2, updater);
        descendingCommand.execute();

        // Assert
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(updater).updateAircraftListInGUI(captor.capture());
        List<Aircraft> capturedList = captor.getValue();

        assertEquals(sortedList, capturedList, "Літаки повинні бути відсортовані за спаданням дальності польоту.");
    }
    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}