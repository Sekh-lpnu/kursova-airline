package main;

import javafx.application.Application;
import gui.MainApp;
import manager.LoggerManager;

public class Main {

    public static void main(String[] args) {
        LoggerManager.logInfo("Запуск програми...");
        Application.launch(MainApp.class, args);
    }
}
