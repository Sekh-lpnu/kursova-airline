package manager;

import models.Aircraft;
import models.CargoAircraft;
import models.PassengerAircraft;
import models.PrivateJet;
import database.DatabaseManager;
import gui.AircraftListUpdater;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Statistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Airline {

    private List<Aircraft> aircraftList = new ArrayList<>();
    private final AircraftListUpdater updater;

    public Airline(AircraftListUpdater updater) {
        this.updater = updater;
        loadAircraftsFromDatabase();
    }

    public void addAircraft(String model, double range, double fuelConsumptionPerUnit,
                                    int passengerCapacity, double cargoCapacity, int aircraftType) {
        String query = "INSERT INTO aircrafts (model, range, fuelConsumptionPerUnit, type, passengerCapacity, cargoCapacity) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, model);
            statement.setDouble(2, range);
            statement.setDouble(3, fuelConsumptionPerUnit);

            String type;
            switch (aircraftType) {
                case 1:
                    type = "passenger";
                    break;
                case 2:
                    type = "cargo";
                    break;
                case 3:
                    type = "private";
                    break;
                default:
                    LoggerManager.logWarn("Невідомий тип літака: " + aircraftType);
                    return;
            }
            statement.setString(4, type);
            statement.setInt(5, passengerCapacity);
            statement.setDouble(6, cargoCapacity);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        Aircraft aircraft = switch (aircraftType) {
                            case 1 -> new PassengerAircraft(id, model, range, fuelConsumptionPerUnit, passengerCapacity, cargoCapacity);
                            case 2 -> new CargoAircraft(id, model, range, fuelConsumptionPerUnit, cargoCapacity);
                            case 3 -> new PrivateJet(id, model, range, fuelConsumptionPerUnit, passengerCapacity, cargoCapacity);
                            default -> null;
                        };
                        if (aircraft != null) {
                            aircraftList.add(aircraft);
                            LoggerManager.logInfo("Літак успішно додано до бази даних: " + model);
                            updater.updateAircraftListInGUI(aircraftList); // Оновлення GUI
                        }
                    }
                }
            } else {
                LoggerManager.logWarn("Не вдалося додати літак до бази даних: " + model);
            }
        } catch (SQLException e) {
            LoggerManager.logCritical("Помилка при додаванні літака до бази даних: " + e.getMessage(), e);
        }
    }

    public boolean removeAircraft(int id) {
        String query = "DELETE FROM aircrafts WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            int rows = statement.executeUpdate();

            if (rows > 0) {
                aircraftList.removeIf(aircraft -> aircraft.getId() == id);
                LoggerManager.logInfo("Літак успішно видалено з бази даних: ID " + id);
                updater.updateAircraftListInGUI(aircraftList); // Оновлення GUI
                return true;
            } else {
                LoggerManager.logWarn("Літак з таким ID не знайдено: ID " + id);
                return false;
            }
        } catch (SQLException e) {
            LoggerManager.logCritical("Помилка при видаленні літака: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Aircraft> sortAircraftByRange(int sortOrder) {
        String query = "SELECT * FROM aircrafts";

        List<Aircraft> result = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            aircraftList.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                double range = rs.getDouble("range");
                double fuel = rs.getDouble("fuelConsumptionPerUnit");
                String type = rs.getString("type");
                int passenger = rs.getInt("passengerCapacity");
                double cargo = rs.getDouble("cargoCapacity");

                Aircraft aircraft = switch (type) {
                    case "cargo" -> new CargoAircraft(id, model, range, fuel, cargo);
                    case "passenger" -> new PassengerAircraft(id, model, range, fuel, passenger, cargo);
                    case "private" -> new PrivateJet(id, model, range, fuel, passenger, cargo);
                    default -> null;
                };

                if (aircraft != null) {
                    aircraftList.add(aircraft);
                    result.add(aircraft);
                }
            }

            result.sort((a1, a2) -> {
                if (sortOrder == 1) {
                    return Double.compare(a1.getRange(), a2.getRange());
                } else {
                    return Double.compare(a2.getRange(), a1.getRange());
                }
            });

            LoggerManager.logInfo("Літаки відсортовано за дальністю: " + (sortOrder == 1 ? "ASC" : "DESC"));
            aircraftList = result;
            updater.updateAircraftListInGUI(aircraftList); // Оновлення GUI
        } catch (SQLException e) {
            LoggerManager.logCritical("Помилка при сортуванні літаків: " + e.getMessage(), e);
        }

        return result;
    }

    public List<Aircraft> findAircraftByFuel(double minFuel, double maxFuel) {
        List<Aircraft> result = new ArrayList<>();
        String query = "SELECT * FROM aircrafts WHERE fuelConsumptionPerUnit BETWEEN ? AND ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, minFuel);
            statement.setDouble(2, maxFuel);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                double range = rs.getDouble("range");
                double fuel = rs.getDouble("fuelConsumptionPerUnit");
                String type = rs.getString("type");
                int passenger = rs.getInt("passengerCapacity");
                double cargo = rs.getDouble("cargoCapacity");

                Aircraft aircraft = switch (type) {
                    case "cargo" -> new CargoAircraft(id, model, range, fuel, cargo);
                    case "passenger" -> new PassengerAircraft(id, model, range, fuel, passenger, cargo);
                    case "private" -> new PrivateJet(id, model, range, fuel, passenger, cargo);
                    default -> null;
                };

                if (aircraft != null) {
                    result.add(aircraft);
                }
            }

            LoggerManager.logInfo("Знайдено літаків за витратою пального: " + result.size());
            updater.updateAircraftListInGUI(result); // Оновлення GUI
        } catch (SQLException e) {
            LoggerManager.logCritical("Помилка при пошуку літаків: " + e.getMessage(), e);
        }

        return result;
    }

    public ObservableList<Aircraft> getAircraftListObservable() {
        return FXCollections.observableArrayList(getAircraftList());
    }

    public List<Aircraft> getAircraftList() {
        List<Aircraft> list = new ArrayList<>();
        String query = "SELECT * FROM aircrafts";

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                double range = rs.getDouble("range");
                double fuel = rs.getDouble("fuelConsumptionPerUnit");
                String type = rs.getString("type");
                int passenger = rs.getInt("passengerCapacity");
                double cargo = rs.getDouble("cargoCapacity");

                Aircraft aircraft = switch (type) {
                    case "cargo" -> new CargoAircraft(id, model, range, fuel, cargo);
                    case "passenger" -> new PassengerAircraft(id, model, range, fuel, passenger, cargo);
                    case "private" -> new PrivateJet(id, model, range, fuel, passenger, cargo);
                    default -> null;
                };

                if (aircraft != null) {
                    list.add(aircraft);
                }
            }
        } catch (SQLException e) {
            LoggerManager.logCritical("Помилка при отриманні списку літаків: " + e.getMessage(), e);
        }

        return list;
    }

    public String getStatisticsText() {
        Statistics statistics = new Statistics();
        return statistics.generateStatisticsText(this);
    }

    private void loadAircraftsFromDatabase() {
        String query = "SELECT * FROM aircrafts";
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            aircraftList.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                double range = rs.getDouble("range");
                double fuel = rs.getDouble("fuelConsumptionPerUnit");
                String type = rs.getString("type");
                int passenger = rs.getInt("passengerCapacity");
                double cargo = rs.getDouble("cargoCapacity");

                Aircraft aircraft = switch (type) {
                    case "cargo" -> new CargoAircraft(id, model, range, fuel, cargo);
                    case "passenger" -> new PassengerAircraft(id, model, range, fuel, passenger, cargo);
                    case "private" -> new PrivateJet(id, model, range, fuel, passenger, cargo);
                    default -> null;
                };

                if (aircraft != null) {
                    aircraftList.add(aircraft);
                }
            }
            LoggerManager.logInfo("Завантажено літаків з бази даних: " + aircraftList.size());
            updater.updateAircraftListInGUI(aircraftList); // Оновлення GUI при ініціалізації
        } catch (SQLException e) {
            LoggerManager.logCritical("Помилка при завантаженні літаків з бази даних: " + e.getMessage(), e);
        }
    }
}