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
        // Повертаємо текст статистики
        String statisticsText = airline.getStatisticsText();
        LoggerManager.logInfo("Statistics retrieved for display.");
        // Відображення відбуватиметься в MainApp
    }

    public String getStatisticsText() {
        return airline.getStatisticsText();
    }
}