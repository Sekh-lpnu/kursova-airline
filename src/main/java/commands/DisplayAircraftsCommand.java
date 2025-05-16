package commands;

import manager.Airline;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import aircraft.Aircraft;

public class DisplayAircraftsCommand implements Command {
    private final Airline airline;
    private final TableView<Aircraft> aircraftTableView; // Використовуємо TableView

    public DisplayAircraftsCommand(Airline airline, TableView<Aircraft> aircraftTableView) {
        this.airline = airline;
        this.aircraftTableView = aircraftTableView;
    }

    @Override
    public void execute() {
        // Оновлюємо TableView, передаючи список літаків з Airline
        ObservableList<Aircraft> aircraftList = airline.getAircraftListObservable();
        aircraftTableView.setItems(aircraftList); // Оновлюємо таблицю літаків
    }
}
