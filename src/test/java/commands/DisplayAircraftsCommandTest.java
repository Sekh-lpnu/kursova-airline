package commands;

import manager.Airline;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import aircraft.Aircraft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.stage.Stage;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
class DisplayAircraftsCommandTest {

    @Mock
    private Airline airline;

    private TableView<Aircraft> aircraftTableView;
    private DisplayAircraftsCommand command;
    private ObservableList<Aircraft> aircraftList;

    @Start
    void start(Stage stage) {
        aircraftTableView = new TableView<>();
    }

    @BeforeEach
    void setUp() {
        // Initialize command
        command = new DisplayAircraftsCommand (
        airline, aircraftTableView);
        // Initialize aircraft list with valid mocks
        aircraftList = FXCollections.observableArrayList(
                mock(Aircraft.class),
                mock(Aircraft.class)
        );
    }

    @Test
    void testExecute_SetsAircraftListToTableView() {
        // Arrange
        when(airline.getAircraftListObservable()).thenReturn(aircraftList);

        // Act
        command.execute();

        // Assert
        assertEquals(aircraftList, aircraftTableView.getItems(), "TableView should contain the aircraft list");
    }

    @Test
    void testExecute_EmptyAircraftList() {
        // Arrange
        ObservableList<Aircraft> emptyList = FXCollections.observableArrayList();
        when(airline.getAircraftListObservable()).thenReturn(emptyList);

        // Act
        command.execute();

        // Assert
        assertTrue(aircraftTableView.getItems().isEmpty(), "TableView should have an empty list");
    }

    @Test
    void testExecute_NullAircraftList() {
        // Arrange
        when(airline.getAircraftListObservable()).thenReturn(null);

        // Act
        command.execute();

        // Assert
        assertNull(aircraftTableView.getItems(), "TableView items should be null");
    }

    @Test
    void testConstructor_ValidParameters() {
        // Act
        DisplayAircraftsCommand newCommand = new DisplayAircraftsCommand(airline, aircraftTableView);

        // Assert
        assertNotNull(newCommand, "Command should be created successfully");
    }

    @Test
    void testExecute_AirlineReturnsNonEmptyList() {
        // Arrange
        when(airline.getAircraftListObservable()).thenReturn(aircraftList);

        // Act
        command.execute();

        // Assert
        assertEquals(2, aircraftTableView.getItems().size(), "TableView should contain 2 aircraft");
    }

}