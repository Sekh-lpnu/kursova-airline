package commands;

import display.JOptionPaneMessageDisplay;
import display.MessageDisplay;
import manager.Airline;

public class RemoveAircraftCommand implements Command {
    private final Airline airline;
    private final int aircraftId;
    private final MessageDisplay messageDisplay;

    public RemoveAircraftCommand(Airline airline, int aircraftId, MessageDisplay messageDisplay) {
        this.airline = airline;
        this.aircraftId = aircraftId;
        this.messageDisplay = messageDisplay;
    }



    @Override
    public void execute() {
        boolean removed = airline.removeAircraft(aircraftId);
        if (removed) {
            messageDisplay.showMessage("Літак з ID " + aircraftId + " успішно видалено.");
        } else {
            messageDisplay.showMessage("Літак з ID " + aircraftId + " не знайдено.");
        }
    }
}