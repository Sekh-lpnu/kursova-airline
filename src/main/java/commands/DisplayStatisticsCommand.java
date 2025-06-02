package commands;

import manager.Airline;
import manager.LoggerManager;

public class DisplayStatisticsCommand implements Command {
    private final Airline airline;

    public DisplayStatisticsCommand(Airline airline) {
        this.airline = airline;
    }

    @Override
    public void execute() {
        String statisticsText = airline.getStatisticsText();
        LoggerManager.logInfo("Statistics retrieved for display.");

    }

    public String getStatisticsText() {
        return airline.getStatisticsText();
    }
}