package commands;

import database.TestDatabaseUtils;
import manager.Airline;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DisplayStatisticsCommandTest {

    private Airline mockAirline;
    private DisplayStatisticsCommand command;

    @BeforeEach
    void setUp() {
        mockAirline = mock(Airline.class);
        command = new DisplayStatisticsCommand(mockAirline);
    }

    @Test
    void testGetStatisticsText_ReturnsCorrectText() {
        String expectedText = "Total aircraft: 5\nTotal fuel: 5000.0";
        when(mockAirline.getStatisticsText()).thenReturn(expectedText);

        String actualText = command.getStatisticsText();

        assertEquals(expectedText, actualText);
        verify(mockAirline, times(1)).getStatisticsText();
    }

    @Test
    void testExecute_CallsGetStatisticsText() {
        when(mockAirline.getStatisticsText()).thenReturn("Some stats");

        command.execute();

        verify(mockAirline, times(1)).getStatisticsText();
    }

    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}
