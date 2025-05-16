package gui;
import aircraft.Aircraft;
import aircraft.PassengerAircraft;
import database.DatabaseManager;
import database.TestDatabaseUtils;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import manager.Airline;
import manager.LoggerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class MainAppTest extends ApplicationTest {

    private MainApp mainApp;
    private final FxRobot robot = new FxRobot();

    @Override
    public void start(Stage stage) throws Exception {
        mainApp = new MainApp();
        mainApp.start(stage);
    }

    @BeforeEach
    public void setUp() {
        Airline airline = lookupAirline();
        clearDatabase();
        airline.getAircraftList().clear();
        mainApp.updateAircraftListInGUI(airline.getAircraftList());
        WaitForAsyncUtils.waitForFxEvents();
    }

    private Airline lookupAirline() {
        try {
            java.lang.reflect.Field field = MainApp.class.getDeclaredField("airline");
            field.setAccessible(true);
            return (Airline) field.get(mainApp);
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося отримати доступ до Airline", e);
        }
    }

    @Test
    public void testMainWindowLoaded() {
        Button addButton = lookup((Predicate<javafx.scene.Node>) node ->
                node instanceof Button && ((Button) node).getText().equals("Додати літак")
        ).query();
        assertNotNull(addButton);
        assertTrue(addButton.isVisible());
    }

    @Test
    public void testTableViewIsPresent() {
        TableView<?> table = lookup(".table-view").query();
        assertNotNull(table);
        assertTrue(table.isVisible());
    }

    @Test
    public void testOpenAddAircraftDialog() {
        robot.clickOn("Додати літак");
        assertTrue(robot.lookup("Тип літака:").tryQuery().isPresent());
        assertTrue(robot.lookup("Модель:").tryQuery().isPresent());
        assertTrue(robot.lookup("Дальність:").tryQuery().isPresent());
        assertTrue(robot.lookup("Витрата пального:").tryQuery().isPresent());
        assertTrue(robot.lookup("Кількість місць:").tryQuery().isPresent());
        assertTrue(robot.lookup("Вантажопідйомність:").tryQuery().isPresent());
    }

    @Test
    public void testAddAircraftValidation() {
        robot.clickOn("Додати літак");
        robot.clickOn("Додати");
        verifyThat("Виберіть тип літака!", isVisible());
    }

    @Test
    public void testAddPassengerAircraft() {
        FxRobot robot = new FxRobot();
        robot.clickOn("Додати літак");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#aircraftTypeComboBox");
        robot.clickOn("Пасажирський");

        robot.clickOn("#modelField").write("Boeing 737");
        robot.clickOn("#rangeField").write("5500");
        robot.clickOn("#fuelConsumptionField").write("15.5");
        robot.clickOn("#passengerCapacityField").write("180");


        robot.clickOn("Додати");
        WaitForAsyncUtils.waitForFxEvents();

        TableView<Aircraft> table = lookup(".table-view").query();
        verifyThat(table, TableViewMatchers.hasTableCell("Boeing 737"));
        verifyThat(table, TableViewMatchers.hasTableCell("Пасажирський"));
        // Уникай double-порівняння без округлення
        verifyThat(table, TableViewMatchers.hasTableCell((Object)5500.0));
        verifyThat(table, TableViewMatchers.hasTableCell((Object)15.5));
        verifyThat(table, TableViewMatchers.hasTableCell((Object)180));
        verifyThat(table, TableViewMatchers.hasTableCell((Object)0.0)); // Чи точно 0.0? Перевір логіку в Aircraft
    }

    @Test
    public void testAddCargoAircraft() {
        robot.clickOn("Додати літак");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#aircraftTypeComboBox");
        robot.clickOn("Вантажний");
        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent(), "Dialog should remain open after selecting aircraft type");
        robot.clickOn("#modelField").write("Boeing 737");
        robot.clickOn("#rangeField").write("5500");
        robot.clickOn("#fuelConsumptionField").write("15.5");
        robot.clickOn("#cargoCapacityField").write("20");
        robot.clickOn("Додати");
        TableView<Aircraft> table = lookup(".table-view").query();
        verifyThat(table, TableViewMatchers.hasTableCell("Boeing 737"));
        verifyThat(table, TableViewMatchers.hasTableCell(5500.0));
        verifyThat(table, TableViewMatchers.hasTableCell(15.5));
        verifyThat(table, TableViewMatchers.hasTableCell("Вантажний"));
        verifyThat(table, TableViewMatchers.hasTableCell(0));
        verifyThat(table, TableViewMatchers.hasTableCell(20.0)); // Fails here
        assertEquals(1, table.getItems().size());
    }

    @Test
    public void testAddPrivateJet() {
        robot.clickOn("Додати літак");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#aircraftTypeComboBox");
        robot.clickOn("Приватний літак");
        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent(), "Dialog should remain open after selecting aircraft type");
        robot.clickOn("#modelField").write("Boeing 737");
        robot.clickOn("#rangeField").write("5500");
        robot.clickOn("#fuelConsumptionField").write("15.5");
        robot.clickOn("#passengerCapacityField").write("180");
        robot.clickOn("#cargoCapacityField").write("20");
        robot.clickOn("Додати");
        TableView<Aircraft> table = lookup(".table-view").query();
        verifyThat(table, TableViewMatchers.hasTableCell("Boeing 737"));
        verifyThat(table, TableViewMatchers.hasTableCell(5500.0));
        verifyThat(table, TableViewMatchers.hasTableCell(15.5));
        verifyThat(table, TableViewMatchers.hasTableCell("Приватний літак"));
        verifyThat(table, TableViewMatchers.hasTableCell(180));
        verifyThat(table, TableViewMatchers.hasTableCell(20.0)); // Fails here
        assertEquals(1, table.getItems().size());
    }

    @Test
    public void testOpenRemoveAircraftDialog() {
        robot.clickOn("Видалити літак");
        assertTrue(robot.lookup("Введіть ID літака для видалення:").tryQuery().isPresent());
        assertTrue(robot.lookup(".text-field").tryQuery().isPresent());
    }
    private void clearDatabase() {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM aircrafts")) {
            statement.executeUpdate();
            LoggerManager.logInfo("База даних очищена перед тестом.");
        } catch (SQLException e) {
            LoggerManager.logCritical("Помилка при очищенні бази даних: " + e.getMessage(), e);
            throw new RuntimeException("Не вдалося очистити базу даних", e);
        }
    }
    @Test
    public void testRemoveAircraft() {
        Airline airline = lookupAirline();

        // Очищаємо базу даних перед тестом
        clearDatabase();

        // Додаємо літак
        airline.addAircraft("Boeing 737", 5500, 15.5, 180, 20, 1);
        mainApp.updateAircraftListInGUI(airline.getAircraftList());

        // Отримуємо ID доданого літака (припускаємо, що це останній доданий літак)
        Aircraft addedAircraft = airline.getAircraftList().get(0);
        int aircraftId = addedAircraft.getId();

        robot.clickOn("Видалити літак");
        robot.clickOn(".text-field").write(String.valueOf(aircraftId));
        robot.clickOn("OK");

        TableView<Aircraft> table = lookup(".table-view").query();
        // Перевіряємо, що літак більше не відображається
        verifyThat(table, TableViewMatchers.hasNumRows(0));
    }

    @Test
    public void testRemoveAircraftInvalidID() {
        robot.clickOn("Видалити літак");
        robot.clickOn(".text-field").write("abc");
        robot.clickOn("OK");

        verifyThat("Введіть коректне числове значення ID.", isVisible());
    }

    @Test
    public void testOpenSortAircraftDialog() {
        robot.clickOn("Сортувати літаки за дальністю");
        assertTrue(robot.lookup("Виберіть порядок сортування:").tryQuery().isPresent());
        assertTrue(robot.lookup("За зростанням").tryQuery().isPresent());
        assertTrue(robot.lookup("За спаданням").tryQuery().isPresent());
    }

    @Test
    public void testSortAircraftAscending() {
        Airline airline = lookupAirline();
        airline.addAircraft("Aircraft A", 3000, 10, 100, 10, 1);
        airline.addAircraft("Aircraft B", 5000, 12, 150, 15, 1);
        mainApp.updateAircraftListInGUI(airline.getAircraftList());

        robot.clickOn("Сортувати літаки за дальністю");
        robot.clickOn("За зростанням");
        if (robot.lookup("OK").tryQuery().isPresent()) {
            robot.clickOn("OK");
        }

        WaitForAsyncUtils.waitForFxEvents();

        TableView<Aircraft> table = lookup(".table-view").query();

        // Друк для діагностики
        table.getItems().forEach(item ->
                System.out.println("ID: " + item.getId() + ", Range: " + item.getRange())
        );

        // Перевірка сортування за дальністю
        verifyThat(table, tableView -> {
            List<Aircraft> items = tableView.getItems();
            return items.size() == 2 &&
                    items.get(0).getRange() <= items.get(1).getRange();
        });
    }


    @Test
    public void testOpenFindAircraftByFuelDialog() {
        robot.clickOn("Пошук літаків по витраті пального");
        assertTrue(robot.lookup("Мінімальна витрата:").tryQuery().isPresent());
        assertTrue(robot.lookup("Максимальна витрата:").tryQuery().isPresent());
        assertTrue(robot.lookup(".text-field").tryQuery().isPresent());
    }

    @Test
    public void testFindAircraftByFuel() {
        Airline airline = lookupAirline();
        airline.addAircraft("Aircraft 1", 3000, 10, 100, 10, 1);
        airline.addAircraft("Aircraft 2", 5000, 15, 150, 15, 1);
        mainApp.updateAircraftListInGUI(airline.getAircraftList());

        robot.clickOn("Пошук літаків по витраті пального");
        robot.clickOn(".text-field").write("9"); // Мін
        robot.clickOn(".text-field").write("\t").write("11"); // Макс
        robot.clickOn("Знайти");

        TableView<Aircraft> table = lookup(".table-view").query();
        verifyThat(table, TableViewMatchers.hasTableCell("Aircraft 1"));
        assertEquals(1, table.getItems().size());
    }

    @Test
    public void testFindAircraftByFuelInvalidInput() {
        robot.clickOn("Пошук літаків по витраті пального");
        robot.clickOn(".text-field").write("abc"); // Мін
        robot.clickOn(".text-field").write("\t").write("xyz"); // Макс
        robot.clickOn("Знайти");

        verifyThat("Введіть числове значення для мінімальної витрати.", isVisible());
    }

    @Test
    public void testDisplayStatisticsDialog() {
        Airline airline = lookupAirline();
        airline.addAircraft("Boeing 73", 5507, 15.5, 180, 20, 1);
        mainApp.updateAircraftListInGUI(airline.getAircraftList());

        robot.clickOn("Показати статистику");
        verifyThat("Статистика авіакомпанії", isVisible());
        Label label = robot.lookup(".label")
                .match(node -> node instanceof Label && ((Label) node).getText().contains("пасажирська"))
                .queryAs(Label.class);

        assertNotNull(label);
    }

    @Test
    public void testSaveToFile() {
        Airline airline = lookupAirline();
        airline.addAircraft("Boeing 737", 5500, 15.5, 180, 20, 1);
        mainApp.updateAircraftListInGUI(airline.getAircraftList());

        File file = new File("aircraft_data.csv");
        if (file.exists()) {
            file.delete();
        }

        // Натискаємо кнопку, яка викликає Alert
        robot.clickOn("Зберегти у файл");

        // Чекаємо, поки Alert з'явиться
        sleep(1000); // або waitForFxEvents();

        // Пробуємо натиснути кнопку "ОК" у вікні повідомлення
        robot.lookup(".button").queryAll().stream()
                .filter(node -> node instanceof javafx.scene.control.Button)
                .map(node -> (javafx.scene.control.Button) node)
                .filter(button -> button.getText().equals("ОК") || button.getText().equals("OK"))
                .findFirst()
                .ifPresent(robot::clickOn);

        // Тепер перевіряємо, чи файл збережено
        assertTrue(file.exists(), "Файл має бути створений після збереження");
    }



    @Test
    public void testErrorDialogAnimation() throws TimeoutException {
        robot.clickOn("Додати літак");
        robot.clickOn("Додати"); // Викликаємо помилку

        // Чекаємо, поки opacity діалогу буде не менше 0.99
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () -> {
            DialogPane pane = lookup(".alert").queryAs(DialogPane.class);
            return pane != null && pane.getOpacity() >= 1;
        });

        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertNotNull(dialogPane, "Діалог повинен бути створений");
        assertEquals(1.0, dialogPane.getOpacity(), "Діалог має бути повністю видимим після анімації");
        assertEquals(1.0, dialogPane.getScaleX(), "Діалог має бути повністю масштабованим після анімації");
    }



    @Test
    public void testCSSLoading() {
        VBox root = lookup(".root").query();
        assertTrue(root.getStyleClass().contains("root"), "Кореневий елемент має мати застосовану CSS-клас");

        robot.clickOn("Додати літак");

        // Очікування на завершення застосування CSS
        WaitForAsyncUtils.waitForFxEvents();

        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertFalse(dialogPane.getStylesheets().isEmpty(), "Діалог має мати застосовані CSS-стилі");
    }


    @Test
    public void testUpdateAircraftListInGUI() {
        Airline airline = lookupAirline();
        // Додаємо літак через метод addAircraft із відповідними параметрами
        airline.addAircraft("Boeing 737", 5500.0, 15.5, 180, 20, 1); // aircraftType = 1 для пасажирського
        mainApp.updateAircraftListInGUI(airline.getAircraftList());

        TableView<Aircraft> table = lookup(".table-view").query();
        verifyThat(table, TableViewMatchers.hasTableCell("Boeing 737"));
        verifyThat(table, TableViewMatchers.hasTableCell(5500.0));
        verifyThat(table, TableViewMatchers.hasTableCell(15.5));
        verifyThat(table, TableViewMatchers.hasTableCell("Пасажирський"));
        verifyThat(table, TableViewMatchers.hasTableCell(180));
        verifyThat(table, TableViewMatchers.hasTableCell(0.0));
    }
    @AfterAll
    static void cleanUpDatabase() {
        TestDatabaseUtils.resetAircraftTable();
    }
}