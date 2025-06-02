package commands;

import manager.Airline;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import models.Aircraft;

public class DisplayAircraftsCommand implements Command {
    private final Airline airline;
    private final TableView<Aircraft> aircraftTableView;

    public DisplayAircraftsCommand(Airline airline, TableView<Aircraft> aircraftTableView) {
        this.airline = airline;
        this.aircraftTableView = aircraftTableView;
    }

    @Override
    public void execute() {
        ObservableList<Aircraft> aircraftList = airline.getAircraftListObservable();
        aircraftTableView.setItems(aircraftList);
    }
}
