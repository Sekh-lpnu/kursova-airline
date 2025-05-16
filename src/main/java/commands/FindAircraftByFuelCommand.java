package commands;

import manager.Airline;
import aircraft.Aircraft;
import display.MessageDisplay;
import display.JOptionPaneMessageDisplay;

import java.util.List;

public class FindAircraftByFuelCommand implements Command {
    private final Airline airline;
    private final double minFuel;
    private final double maxFuel;
    private final MessageDisplay messageDisplay;

    public FindAircraftByFuelCommand(Airline airline, double minFuel, double maxFuel, MessageDisplay messageDisplay) {
        this.airline = airline;
        this.minFuel = minFuel;
        this.maxFuel = maxFuel;
        this.messageDisplay = messageDisplay;
    }



    @Override
    public void execute() {
        List<Aircraft> foundAircrafts = airline.findAircraftByFuel(minFuel, maxFuel);

        if (foundAircrafts.isEmpty()) {
            messageDisplay.showMessage("Літаків з таким споживанням пального не знайдено.");
        } else {
            StringBuilder result = new StringBuilder();
            for (Aircraft aircraft : foundAircrafts) {
                result.append(aircraft.toString()).append("\n");
            }
            messageDisplay.showMessage(result.toString());
        }
    }
}
