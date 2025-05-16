package utils;

import aircraft.Aircraft;
import aircraft.CargoAircraft;
import aircraft.PassengerAircraft;
import aircraft.PrivateJet;
import manager.Airline;
import database.DatabaseManager;
import manager.LoggerManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

public class FileManager {


    // Метод для збереження всіх літаків з бази даних у файл
    public boolean saveAllAircraftsToFile(String fileName) {
        LoggerManager.logInfo("Starting to save all aircrafts to file: " + fileName);

        try (FileWriter writer = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            // Підключення до бази даних для отримання всіх літаків
            String query = "SELECT * FROM aircrafts";
            try (Connection connection = DatabaseManager.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(query)) {

                // Записуємо заголовки
                bufferedWriter.write("ID,Тип,Модель,Дальність,Споживання пального,Місткість пасажирів,Вантажопідйомність\n");
                LoggerManager.logInfo("Retrieved aircraft data from database.");

                // Записуємо дані про літаки з бази даних
                while (rs.next()) {
                    String model = rs.getString("model");
                    double range = rs.getDouble("range");
                    double fuel = rs.getDouble("fuelConsumptionPerUnit");
                    String type = rs.getString("type");
                    int passenger = rs.getInt("passengerCapacity");
                    double cargo = rs.getDouble("cargoCapacity");

                    // Формуємо об'єкт літака відповідно до його типу
                    Aircraft aircraft = switch (type) {
                        case "cargo" -> new CargoAircraft(model, range, fuel, cargo);
                        case "passenger" -> new PassengerAircraft(model, range, fuel, passenger, cargo);
                        case "private" -> new PrivateJet(model, range, fuel, passenger, cargo);
                        default -> null;
                    };

                    if (aircraft != null) {
                        bufferedWriter.write(formatAircraftData(aircraft, type));
                        LoggerManager.logInfo("Saved aircraft data: " + aircraft.getModel());
                    }
                }

                LoggerManager.logInfo("Successfully saved all aircraft data to file: " + fileName);
                return true; // успішно збережено

            } catch (SQLException e) {
                LoggerManager.logCritical("Error while retrieving aircraft data from database: " , e);
                return false; // сталася помилка при роботі з базою даних
            }

        } catch (IOException e) {
            LoggerManager.logCritical("Error while saving all aircraft data to file: " , e);
            return false; // сталася помилка при записі у файл
        }
    }

    // Допоміжний метод для форматування даних літака за його типом
    protected String formatAircraftData(Aircraft aircraft, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append(aircraft.getId()).append(",")
                .append(type).append(",")
                .append(aircraft.getModel()).append(",")
                .append(aircraft.getRange()).append(",")
                .append(aircraft.calculateFuelConsumption()).append(",");

        // Додаємо інформацію про місткість пасажирів
        if (aircraft instanceof PassengerAircraft) {
            sb.append(((PassengerAircraft) aircraft).getPassengerCapacity()).append(",");
        } else if (aircraft instanceof PrivateJet) {
            sb.append(((PrivateJet) aircraft).getPassengerCapacity()).append(",");
        } else {
            sb.append("Н/Д,"); // Для вантажного літака немає місткості пасажирів
        }

        // Додаємо інформацію про вантажопідйомність
        if (aircraft instanceof CargoAircraft) {
            sb.append(((CargoAircraft) aircraft).getCargoCapacity()).append("\n");
        } else if (aircraft instanceof PrivateJet) {
            sb.append(((PrivateJet) aircraft).getCargoCapacity()).append("\n");
        } else {
            sb.append(((PassengerAircraft) aircraft).getCargoCapacity()).append("\n"); // Для пасажирського літака додаємо вантажопідйомність
        }

        return sb.toString();
    }
}
