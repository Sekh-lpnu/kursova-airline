package commands;

import database.TestDatabaseUtils;
import display.MessageDisplay;
import manager.Airline;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import utils.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class SaveToFileCommandTest {

    private Airline airlineMock;
    private FileManager fileManagerMock;
    private MessageDisplay messageDisplayMock;
    private SaveToFileCommand saveToFileCommand;
    private String filename;

    @BeforeEach
    void setUp() {
        airlineMock = mock(Airline.class);
        fileManagerMock = mock(FileManager.class);
        messageDisplayMock = mock(MessageDisplay.class);
        filename = "aircrafts.txt";
        saveToFileCommand = new SaveToFileCommand(airlineMock, filename, fileManagerMock, messageDisplayMock);
    }

    @Test
    void testExecuteShouldShowSuccessMessageWhenFileIsSaved() {
        when(fileManagerMock.saveAllAircraftsToFile(filename)).thenReturn(true);
        saveToFileCommand.execute();

        verify(messageDisplayMock, times(1)).showMessage("Файл успішно збережено: " + filename);
        verify(fileManagerMock, times(1)).saveAllAircraftsToFile(filename);
    }

    @Test
    void testExecuteShouldShowErrorMessageWhenFileSaveFails() {
        when(fileManagerMock.saveAllAircraftsToFile(filename)).thenReturn(false);
        saveToFileCommand.execute();

        verify(messageDisplayMock, times(1)).showMessage("Помилка під час збереження файлу: " + filename);
        verify(fileManagerMock, times(1)).saveAllAircraftsToFile(filename);
    }

    @Test
    void testExecuteShouldCallSaveAllAircraftsToFileMethod() {
        when(fileManagerMock.saveAllAircraftsToFile(filename)).thenReturn(true);
        saveToFileCommand.execute();

        verify(fileManagerMock, times(1)).saveAllAircraftsToFile(filename);
    }
    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}