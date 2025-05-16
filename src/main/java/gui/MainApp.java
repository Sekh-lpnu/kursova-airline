package gui;

import aircraft.Aircraft;
import aircraft.CargoAircraft;
import aircraft.PassengerAircraft;
import aircraft.PrivateJet;
import commands.DisplayStatisticsCommand;
import commands.SaveToFileCommand;
import commands.SortAircraftsCommand;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import manager.Airline;
import manager.LoggerManager;

import java.net.URL;
import java.util.List;

public class MainApp extends Application implements AircraftListUpdater { // Додано AircraftListUpdater

    private Airline airline = new Airline(this);
    private static ObservableList<Aircraft> aircraftObservableList = FXCollections.observableArrayList();
    private static TableView<Aircraft> aircraftTableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Авіакомпанія");

        VBox root = new VBox(20);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        root.getStyleClass().add("root");
        root.setAlignment(Pos.CENTER);

        initTableView();

        Button addButton = createStyledButton("Додати літак");
        Button removeButton = createStyledButton("Видалити літак");
        Button displayButton = createStyledButton("Показати літаки");
        Button statsButton = createStyledButton("Показати статистику");
        Button sortButton = createStyledButton("Сортувати літаки за дальністю");
        Button findButton = createStyledButton("Пошук літаків по витраті пального");
        Button saveButton = createStyledButton("Зберегти у файл");

        addButton.setOnAction(e -> {
            LoggerManager.logInfo("Натиснута кнопка 'Додати літак'");
            showAddAircraftDialog();
        });

        removeButton.setOnAction(e -> {
            LoggerManager.logInfo("Натиснута кнопка 'Видалити літак'");
            showRemoveAircraftDialog();
        });

        displayButton.setOnAction(e -> {
            LoggerManager.logInfo("Натиснута кнопка 'Показати літаки'");
            updateAircraftTable();
        });

        statsButton.setOnAction(e -> {
            LoggerManager.logInfo("Натиснута кнопка 'Показати статистику'");
            displayStatistics();
        });

        sortButton.setOnAction(e -> {
            LoggerManager.logInfo("Натиснута кнопка 'Сортувати літаки за дальністю'");
            showSortAircraftsDialog();
        });

        findButton.setOnAction(e -> {
            LoggerManager.logInfo("Натиснута кнопка 'Пошук літаків по витраті пального'");
            findAircraftByFuel();
        });

        saveButton.setOnAction(e -> {
            LoggerManager.logInfo("Натиснута кнопка 'Зберегти у файл'");
            SaveToFileCommand saveCommand = new SaveToFileCommand(airline, "aircraft_data.csv");
            saveCommand.execute();
        });

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, removeButton, displayButton, sortButton, findButton, statsButton, saveButton);

        root.getChildren().addAll(buttonBox, aircraftTableView);

        Scene scene = new Scene(root, 800, 600);
        URL cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            LoggerManager.logWarn("CSS файл не знайдений!");
        }

        primaryStage.setScene(scene);
        primaryStage.show();

        updateAircraftTable();
    }

    private void initTableView() {
        TableColumn<Aircraft, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Aircraft, String> modelCol = new TableColumn<>("Модель");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(180);
        modelCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Aircraft, Double> rangeCol = new TableColumn<>("Дальність");
        rangeCol.setCellValueFactory(new PropertyValueFactory<>("range"));
        rangeCol.setPrefWidth(120);
        rangeCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Aircraft, Double> fuelCol = new TableColumn<>("Витрата пального");
        fuelCol.setCellValueFactory(new PropertyValueFactory<>("fuelConsumptionPerUnit"));
        fuelCol.setPrefWidth(180);
        fuelCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Aircraft, String> typeCol = new TableColumn<>("Тип");
        typeCol.setCellValueFactory(cellData -> {
            Aircraft aircraft = cellData.getValue();
            if (aircraft instanceof PassengerAircraft) {
                return new javafx.beans.property.SimpleStringProperty("Пасажирський");
            } else if (aircraft instanceof CargoAircraft) {
                return new javafx.beans.property.SimpleStringProperty("Вантажний");
            } else if (aircraft instanceof PrivateJet) {
                return new javafx.beans.property.SimpleStringProperty("Приватний літак");
            } else {
                return new javafx.beans.property.SimpleStringProperty("Невідомий");
            }
        });
        typeCol.setPrefWidth(160);
        typeCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Aircraft, Integer> passengerCapacityCol = new TableColumn<>("Пасажиромісткість");
        passengerCapacityCol.setCellValueFactory(cellData -> {
            Aircraft aircraft = cellData.getValue();
            if (aircraft instanceof PassengerAircraft) {
                return new javafx.beans.property.SimpleIntegerProperty(((PassengerAircraft) aircraft).getPassengerCapacity()).asObject();
            } else if (aircraft instanceof PrivateJet) {
                return new javafx.beans.property.SimpleIntegerProperty(((PrivateJet) aircraft).getPassengerCapacity()).asObject();
            } else {
                return new javafx.beans.property.SimpleIntegerProperty(0).asObject();
            }
        });
        passengerCapacityCol.setPrefWidth(140);
        passengerCapacityCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Aircraft, Double> cargoCapacityCol = new TableColumn<>("Вантажопідйомність");
        cargoCapacityCol.setCellValueFactory(cellData -> {
            Aircraft aircraft = cellData.getValue();
            if (aircraft instanceof CargoAircraft) {
                return new javafx.beans.property.SimpleDoubleProperty(((CargoAircraft) aircraft).getCargoCapacity()).asObject();
            } else if (aircraft instanceof PrivateJet) {
                return new javafx.beans.property.SimpleDoubleProperty(((PrivateJet) aircraft).getCargoCapacity()).asObject();
            } else {
                return new javafx.beans.property.SimpleDoubleProperty(0).asObject();
            }
        });
        cargoCapacityCol.setPrefWidth(180);
        cargoCapacityCol.setStyle("-fx-alignment: CENTER;");

        aircraftTableView.getColumns().addAll(idCol, modelCol, rangeCol, fuelCol, typeCol, passengerCapacityCol, cargoCapacityCol);
        aircraftTableView.setItems(aircraftObservableList);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("main-button");
        return button;
    }

    private void updateAircraftTable() {
        aircraftObservableList.setAll(airline.getAircraftList());
    }

    private void showAddAircraftDialog() {
        Dialog<Aircraft> dialog = createStyledDialog("Додати літак", 400);

        ComboBox<String> aircraftTypeComboBox = new ComboBox<>();
        aircraftTypeComboBox.getItems().addAll("Пасажирський", "Вантажний", "Приватний літак");
        aircraftTypeComboBox.setId("aircraftTypeComboBox");

        TextField modelField = new TextField();
        modelField.setPromptText("Модель");
        modelField.setId("modelField");

        TextField rangeField = new TextField();
        rangeField.setPromptText("Дальність");
        rangeField.setId("rangeField");

        TextField fuelConsumptionField = new TextField();
        fuelConsumptionField.setPromptText("Витрата пального");
        fuelConsumptionField.setId("fuelConsumptionField");

        TextField passengerCapacityField = new TextField();
        passengerCapacityField.setPromptText("Кількість місць");
        passengerCapacityField.setId("passengerCapacityField");

        TextField cargoCapacityField = new TextField();
        cargoCapacityField.setPromptText("Вантажопідйомність");
        cargoCapacityField.setId("cargoCapacityField");

        // Set default values and disable fields based on aircraft type
        aircraftTypeComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            passengerCapacityField.setText("");
            cargoCapacityField.setText("");
            if ("Пасажирський".equals(newValue)) {
                passengerCapacityField.setDisable(false);
                cargoCapacityField.setDisable(true);
                cargoCapacityField.setText("0");
            } else if ("Вантажний".equals(newValue)) {
                passengerCapacityField.setDisable(true);
                passengerCapacityField.setText("0");
                cargoCapacityField.setDisable(false);
            } else if ("Приватний літак".equals(newValue)) {
                passengerCapacityField.setDisable(false);
                cargoCapacityField.setDisable(false);
            }
            LoggerManager.logInfo("Aircraft type changed to: " + newValue);
        });

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(15));
        grid.add(new Label("Тип літака:"), 0, 0);
        grid.add(aircraftTypeComboBox, 1, 0);
        grid.add(new Label("Модель:"), 0, 1);
        grid.add(modelField, 1, 1);
        grid.add(new Label("Дальність:"), 0, 2);
        grid.add(rangeField, 1, 2);
        grid.add(new Label("Витрата пального:"), 0, 3);
        grid.add(fuelConsumptionField, 1, 3);
        grid.add(new Label("Кількість місць:"), 0, 4);
        grid.add(passengerCapacityField, 1, 4);
        grid.add(new Label("Вантажопідйомність:"), 0, 5);
        grid.add(cargoCapacityField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Додати", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );

        // Add logging to debug dialog behavior
        aircraftTypeComboBox.setOnAction(event -> {
            LoggerManager.logInfo("Selected aircraft type: " + aircraftTypeComboBox.getValue());
        });

        dialog.setResultConverter(button -> {
            LoggerManager.logInfo("Button clicked: " + button.getText());
            if (button.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    modelField.getStyleClass().remove("error");
                    rangeField.getStyleClass().remove("error");
                    fuelConsumptionField.getStyleClass().remove("error");
                    passengerCapacityField.getStyleClass().remove("error");
                    cargoCapacityField.getStyleClass().remove("error");

                    String type = aircraftTypeComboBox.getValue();
                    if (type == null) {
                        showError("Виберіть тип літака!");
                        LoggerManager.logWarn("No aircraft type selected");
                        return null;
                    }

                    String model = modelField.getText();
                    if (model.isEmpty()) {
                        modelField.getStyleClass().add("error");
                        showError("Введіть модель літака!");
                        LoggerManager.logWarn("Empty model field");
                        return null;
                    }

                    if (!rangeField.getText().matches("\\d*\\.?\\d+")) {
                        rangeField.getStyleClass().add("error");
                        showError("Введіть коректне числове значення для дальності!");
                        LoggerManager.logWarn("Invalid range: " + rangeField.getText());
                        return null;
                    }
                    double range = Double.parseDouble(rangeField.getText());

                    if (!fuelConsumptionField.getText().matches("\\d*\\.?\\d+")) {
                        fuelConsumptionField.getStyleClass().add("error");
                        showError("Введіть коректне числове значення для витрати пального!");
                        LoggerManager.logWarn("Invalid fuel consumption: " + fuelConsumptionField.getText());
                        return null;
                    }
                    double fuel = Double.parseDouble(fuelConsumptionField.getText());

                    // Ensure passenger capacity is 0 for cargo aircraft
                    int passengers = 0;
                    if (!passengerCapacityField.isDisable()) {
                        if (!passengerCapacityField.getText().matches("\\d+")) {
                            passengerCapacityField.getStyleClass().add("error");
                            showError("Введіть коректне ціле число для пасажиромісткості!");
                            LoggerManager.logWarn("Invalid passenger capacity: " + passengerCapacityField.getText());
                            return null;
                        }
                        passengers = Integer.parseInt(passengerCapacityField.getText());
                    }

                    // Ensure cargo capacity is 0 for passenger aircraft
                    double cargo = 0;
                    if (!cargoCapacityField.isDisable()) {
                        if (!cargoCapacityField.getText().matches("\\d*\\.?\\d+")) {
                            cargoCapacityField.getStyleClass().add("error");
                            showError("Введіть коректне числове значення для вантажопідйомності!");
                            LoggerManager.logWarn("Invalid cargo capacity: " + cargoCapacityField.getText());
                            return null;
                        }
                        cargo = Double.parseDouble(cargoCapacityField.getText());
                    }

                    int aircraftType = switch (type) {
                        case "Пасажирський" -> 1;
                        case "Вантажний" -> 2;
                        case "Приватний літак" -> 3;
                        default -> 0;
                    };

                    airline.addAircraft(model, range, fuel, passengers, cargo, aircraftType);
                    LoggerManager.logInfo("Літак доданий: " + model);
                    updateAircraftTable();
                } catch (Exception e) {
                    LoggerManager.logCritical("Помилка при додаванні літака", e);
                    showError("Перевірте правильність введення числових значень!");
                }
            }
            return null;
        });

        LoggerManager.logInfo("Showing add aircraft dialog");
        dialog.show();
    }

    private void showRemoveAircraftDialog() {
        Dialog<String> dialog = createStyledDialog("Видалення літака", 350);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER_LEFT);

        Label promptLabel = new Label("Введіть ID літака для видалення:");
        TextField idField = new TextField();
        idField.setPromptText("ID");

        content.getChildren().addAll(promptLabel, idField);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return idField.getText();
            }
            return null;
        });

        dialog.show();

        dialog.setOnHidden(event -> {
            String input = dialog.getResult();
            if (input != null) {
                try {
                    int id = Integer.parseInt(input);
                    airline.removeAircraft(id);
                    LoggerManager.logInfo("Літак з ID " + id + " видалений.");
                    updateAircraftTable();
                } catch (NumberFormatException e) {
                    LoggerManager.logCritical("Невірний формат ID", e);
                    showError("Введіть коректне числове значення ID.");
                }
            }
        });
    }

    private void showSortAircraftsDialog() {
        Dialog<Integer> dialog = createStyledDialog("Сортування літаків", 350);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ToggleGroup sortGroup = new ToggleGroup();

        RadioButton ascButton = new RadioButton("За зростанням");
        ascButton.setToggleGroup(sortGroup);
        ascButton.setSelected(true);

        RadioButton descButton = new RadioButton("За спаданням");
        descButton.setToggleGroup(sortGroup);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(new Label("Виберіть порядок сортування:"), ascButton, descButton);

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return ascButton.isSelected() ? 1 : 2;
            }
            return null;
        });

        dialog.show();

        dialog.setOnHidden(event -> {
            Integer sortOrder = dialog.getResult();
            if (sortOrder != null) {
                SortAircraftsCommand sortCommand = new SortAircraftsCommand(airline, sortOrder, this);
                sortCommand.execute();
            }
        });
    }

    private void findAircraftByFuel() {
        Dialog<Void> dialog = createStyledDialog("Пошук за витратою пального", 400);

        TextField minField = new TextField();
        minField.setPromptText("Мінімум");

        TextField maxField = new TextField();
        maxField.setPromptText("Максимум");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));
        grid.add(new Label("Мінімальна витрата:"), 0, 0);
        grid.add(minField, 1, 0);
        grid.add(new Label("Максимальна витрата:"), 0, 1);
        grid.add(maxField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Знайти", ButtonBar.ButtonData.OK_DONE),
                ButtonType.CANCEL
        );

        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                minField.getStyleClass().remove("error");
                maxField.getStyleClass().remove("error");

                try {
                    String minText = minField.getText().trim();
                    String maxText = maxField.getText().trim();

                    if (!minText.matches("\\d+(\\.\\d+)?")) {
                        minField.getStyleClass().add("error");
                        showError("Введіть числове значення для мінімальної витрати.");
                        return null;
                    }

                    if (!maxText.matches("\\d+(\\.\\d+)?")) {
                        maxField.getStyleClass().add("error");
                        showError("Введіть числове значення для максимальної витрати.");
                        return null;
                    }

                    double min = Double.parseDouble(minText);
                    double max = Double.parseDouble(maxText);

                    List<Aircraft> found = airline.findAircraftByFuel(min, max);
                    aircraftObservableList.setAll(found);
                    LoggerManager.logInfo("Пошук літаків за витратою пального виконано: " + found.size() + " знайдено.");
                } catch (Exception e) {
                    LoggerManager.logCritical("Помилка при пошуку літаків", e);
                    showError("Сталася помилка під час пошуку.");
                }
            }
            return null;
        });

        dialog.show();
    }

    private void displayStatistics() {
        DisplayStatisticsCommand command = new DisplayStatisticsCommand(airline);
        command.execute();
        String statisticsText = command.getStatisticsText();

        Dialog<Void> dialog = createStyledDialog("Статистика авіакомпанії", 450);

        VBox statsContainer = new VBox(15);
        statsContainer.setPadding(new Insets(20));
        statsContainer.setAlignment(Pos.CENTER);
        statsContainer.getStyleClass().add("stats-container");

        Label titleLabel = new Label("Статистика авіакомпанії");
        titleLabel.getStyleClass().add("stats-title");

        String[] lines = statisticsText.split("\n");
        for (String line : lines) {
            String[] parts = line.split(": ");
            if (parts.length == 2) {
                HBox statRow = new HBox(10);
                statRow.setAlignment(Pos.CENTER_LEFT);
                statRow.getStyleClass().add("stat-row");

                Label statLabel = new Label(parts[0] + ": " + parts[1]);
                statLabel.getStyleClass().add("stat-label");

                statRow.getChildren().add(statLabel);
                statsContainer.getChildren().add(statRow);
            }
        }

        statsContainer.getChildren().add(0, titleLabel);
        dialog.getDialogPane().setContent(statsContainer);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.show();

        LoggerManager.logInfo("Displayed statistics in UI.");
    }

    private <T> Dialog<T> createStyledDialog(String title, double minWidth) {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().setMinWidth(minWidth);

        URL cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            dialog.getDialogPane().getStylesheets().add(cssUrl.toExternalForm());
            LoggerManager.logInfo("CSS підключено до діалогу: " + cssUrl.toExternalForm());
        }

        return dialog;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText("Виникла помилка");
        alert.setContentText(message);
        alert.getDialogPane().getStyleClass().add("error-dialog");

        URL cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            alert.getDialogPane().getStylesheets().add(cssUrl.toExternalForm());
            LoggerManager.logInfo("CSS підключено до alert: " + cssUrl.toExternalForm());
        } else {
            LoggerManager.logWarn("CSS файл не знайдено за шляхом: /style.css");
        }

        alert.getDialogPane().setOpacity(0);
        alert.getDialogPane().setScaleX(0.8);
        alert.getDialogPane().setScaleY(0.8);
        alert.show();

        FadeTransition fade = new FadeTransition(Duration.millis(300), alert.getDialogPane());
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(300), alert.getDialogPane());
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition transition = new ParallelTransition(fade, scale);
        transition.play();

        LoggerManager.logInfo("Displayed error alert: " + message);
    }

    @Override
    public void updateAircraftListInGUI(List<Aircraft> updatedAircraftList) {
        aircraftObservableList.clear();
        aircraftObservableList.addAll(updatedAircraftList);
        aircraftTableView.refresh();
    }
}