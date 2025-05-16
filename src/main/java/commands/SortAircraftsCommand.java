package commands;

import gui.AircraftListUpdater;
import manager.Airline;
import aircraft.Aircraft;
import java.util.List;

public class SortAircraftsCommand implements Command {
    private final Airline airline;
    private final int sortOrder; // 1 для зростання, 2 для спадання
    private final AircraftListUpdater updater;

    public SortAircraftsCommand(Airline airline, int sortOrder, AircraftListUpdater updater) {
        this.airline = airline;
        this.sortOrder = sortOrder;
        this.updater = updater;
    }

    @Override
    public void execute() {
        List<Aircraft> sortedList = airline.sortAircraftByRange(sortOrder);
        updater.updateAircraftListInGUI(sortedList);
    }
}