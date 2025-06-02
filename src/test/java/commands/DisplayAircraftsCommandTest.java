package commands;

import manager.Airline;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Aircraft;
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
        command = new DisplayAircraftsCommand (
        airline, aircraftTableView);
        aircraftList = FXCollections.observableArrayList(
                mock(Aircraft.class),
                mock(Aircraft.class)
        );
    }

    @Test
    void testExecute_SetsAircraftListToTableView() {
        when(airline.getAircraftListObservable()).thenReturn(aircraftList);
        command.execute();

        assertEquals(aircraftList, aircraftTableView.getItems(), "TableView should contain the aircraft list");
    }

    @Test
    void testExecute_EmptyAircraftList() {
        ObservableList<Aircraft> emptyList = FXCollections.observableArrayList();
        when(airline.getAircraftListObservable()).thenReturn(emptyList);
        command.execute();

        assertTrue(aircraftTableView.getItems().isEmpty(), "TableView should have an empty list");
    }

    @Test
    void testExecute_NullAircraftList() {
        when(airline.getAircraftListObservable()).thenReturn(null);
        command.execute();

        assertNull(aircraftTableView.getItems(), "TableView items should be null");
    }

    @Test
    void testConstructor_ValidParameters() {
        DisplayAircraftsCommand newCommand = new DisplayAircraftsCommand(airline, aircraftTableView);

        assertNotNull(newCommand, "Command should be created successfully");
    }

    @Test
    void testExecute_AirlineReturnsNonEmptyList() {
        when(airline.getAircraftListObservable()).thenReturn(aircraftList);
        command.execute();

        assertEquals(2, aircraftTableView.getItems().size(), "TableView should contain 2 aircraft");
    }

}