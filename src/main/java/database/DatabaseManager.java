package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import manager.LoggerManager;

public class DatabaseManager {

    private static final String url = "jdbc:sqlserver://SEKH-PC\\MSSQLSERVER02:1433;databaseName=airlineDB;encrypt=false;";
    private static final String USER = "anhel";
    private static final String PASSWORD = "12345";

    public static Connection getConnection() throws SQLException {
        try {
            LoggerManager.logInfo("Завантаження драйвера SQL Server...");

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");


            LoggerManager.logInfo("Підключення до бази даних...");
            Connection connection = DriverManager.getConnection(url, USER, PASSWORD);
            LoggerManager.logInfo("Успішне підключення до бази даних.");
            return connection;

        } catch (ClassNotFoundException e) {
            LoggerManager.logError("Драйвер SQL Server не знайдений: " + e.getMessage(), e);
            throw new SQLException("SQL Server driver not found.", e);
        } catch (SQLException e) {
            LoggerManager.logError("Помилка підключення до бази даних: " + e.getMessage(), e);
            throw e;
        }
    }
}
