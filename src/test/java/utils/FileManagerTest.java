package utils;

import models.CargoAircraft;
import models.PassengerAircraft;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private FileManager fileManager;
    private final String testFileName = "test_aircrafts_output.csv";

    @BeforeEach
    void setUp() {
        fileManager = new FileManager();
    }

    @AfterEach
    void cleanUp() throws IOException {
        Files.deleteIfExists(Path.of(testFileName));
    }

    @Test
    void testFormatPassengerAircraftData() {
        PassengerAircraft aircraft = new PassengerAircraft("Boeing 737", 5000, 2.5, 180, 3000);
        String result = fileManager.formatAircraftData(aircraft, "passenger");
        assertTrue(result.contains("Boeing 737"));
        assertTrue(result.contains("passenger"));
        assertTrue(result.contains("180"));
        assertTrue(result.contains("3000"));
    }

    @Test
    void testFormatCargoAircraftData() {
        CargoAircraft aircraft = new CargoAircraft("Antonov An-124", 4000, 4.0, 10000);
        String result = fileManager.formatAircraftData(aircraft, "cargo");
        assertTrue(result.contains("Antonov An-124"));
        assertTrue(result.contains("cargo"));
        assertTrue(result.contains("Н/Д"));
        assertTrue(result.contains("10000"));
    }

    @Test
    void testSaveAllAircraftsToFile() {
        boolean result = fileManager.saveAllAircraftsToFile(testFileName);
        assertTrue(result, "Очікується успішне збереження файлу");

        File file = new File(testFileName);
        assertTrue(file.exists(), "Файл повинен існувати після збереження");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertNotNull(header, "Файл повинен містити заголовок");
            assertTrue(header.contains("ID"), "Заголовок повинен містити ID");
        } catch (IOException e) {
            fail("Помилка читання збереженого файлу");
        }
    }

}
