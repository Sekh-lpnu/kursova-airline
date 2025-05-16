package commands;

import display.JOptionPaneMessageDisplay;
import display.MessageDisplay;
import manager.Airline;
import utils.FileManager;

public class SaveToFileCommand implements Command {
    private final Airline airline;
    private final String filename;
    private final FileManager fileManager;
    private final MessageDisplay messageDisplay;

    public SaveToFileCommand(Airline airline, String filename, FileManager fileManager, MessageDisplay messageDisplay) {
        this.airline = airline;
        this.filename = filename;
        this.fileManager = fileManager;
        this.messageDisplay = messageDisplay;
    }

    public SaveToFileCommand(Airline airline, String filename) {
        this(airline, filename, new FileManager(), new JOptionPaneMessageDisplay());
    }

    @Override
    public void execute() {
        boolean success = fileManager.saveAllAircraftsToFile(filename);
        if (success) {
            messageDisplay.showMessage("Файл успішно збережено: " + filename);
        } else {
            messageDisplay.showMessage("Помилка під час збереження файлу: " + filename);
        }
    }
}