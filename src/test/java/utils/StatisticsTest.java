package utils;

import aircraft.Aircraft;
import aircraft.CargoAircraft;
import aircraft.PassengerAircraft;
import aircraft.PrivateJet;
import database.TestDatabaseUtils;
import manager.Airline;
import manager.LoggerManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsTest {

    private Statistics statistics;

    @Mock
    private Airline airline;

    @BeforeEach
    public void setUp() {
        statistics = new Statistics();
    }

    @Test
    public void testGenerateStatisticsWithEmptyAircraftList() {
        // Arrange
        when(airline.getAircraftList()).thenReturn(Collections.emptyList());

        // Act
        try (MockedStatic<LoggerManager> loggerManagerMock = mockStatic(LoggerManager.class)) {
            String result = statistics.generateStatisticsText(airline);

            // Assert
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Start generating statistics for 0 aircrafts."));
            loggerManagerMock.verify(() -> LoggerManager.logInfo(contains("Generated statistics:")));

            assertTrue(result.contains("Загальна кількість літаків: 0"));
            assertTrue(result.contains("Загальна пасажирська місткість: 0"));
            assertTrue(result.contains("Загальна вантажна місткість: 0,00 т"));
        }
    }

    @Test
    public void testGenerateStatisticsWithPassengerAircraftOnly() {
        // Arrange
        PassengerAircraft aircraft1 = mock(PassengerAircraft.class);
        when(aircraft1.getModel()).thenReturn("Boeing 737");
        when(aircraft1.getPassengerCapacity()).thenReturn(180);
        when(aircraft1.getCargoCapacity()).thenReturn(2.5);

        PassengerAircraft aircraft2 = mock(PassengerAircraft.class);
        when(aircraft2.getModel()).thenReturn("Airbus A320");
        when(aircraft2.getPassengerCapacity()).thenReturn(150);
        when(aircraft2.getCargoCapacity()).thenReturn(2.0);

        List<Aircraft> aircraftList = Arrays.asList(aircraft1, aircraft2);
        when(airline.getAircraftList()).thenReturn(aircraftList);

        // Act
        try (MockedStatic<LoggerManager> loggerManagerMock = mockStatic(LoggerManager.class)) {
            String result = statistics.generateStatisticsText(airline);

            // Assert
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Start generating statistics for 2 aircrafts."));
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Processing passenger aircraft: Boeing 737"));
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Processing passenger aircraft: Airbus A320"));
            loggerManagerMock.verify(() -> LoggerManager.logInfo(contains("Generated statistics:")));

            assertTrue(result.contains("Загальна кількість літаків: 2"));
            assertTrue(result.contains("Загальна пасажирська місткість: 330"));
            assertTrue(result.contains("Загальна вантажна місткість: 4,50 т"));
        }
    }

    @Test
    public void testGenerateStatisticsWithCargoAircraftOnly() {
        // Arrange
        CargoAircraft aircraft1 = mock(CargoAircraft.class);
        when(aircraft1.getModel()).thenReturn("Boeing 747F");
        when(aircraft1.getCargoCapacity()).thenReturn(120.0);

        CargoAircraft aircraft2 = mock(CargoAircraft.class);
        when(aircraft2.getModel()).thenReturn("Antonov An-124");
        when(aircraft2.getCargoCapacity()).thenReturn(150.0);

        List<Aircraft> aircraftList = Arrays.asList(aircraft1, aircraft2);
        when(airline.getAircraftList()).thenReturn(aircraftList);

        // Act
        try (MockedStatic<LoggerManager> loggerManagerMock = mockStatic(LoggerManager.class)) {
            String result = statistics.generateStatisticsText(airline);

            // Assert
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Start generating statistics for 2 aircrafts."));
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Processing cargo aircraft: Boeing 747F"));
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Processing cargo aircraft: Antonov An-124"));
            loggerManagerMock.verify(() -> LoggerManager.logInfo(contains("Generated statistics:")));

            assertTrue(result.contains("Загальна кількість літаків: 2"));
            assertTrue(result.contains("Загальна пасажирська місткість: 0"));
            assertTrue(result.contains("Загальна вантажна місткість: 270,00 т"));
        }
    }

    @Test
    public void testGenerateStatisticsWithPrivateJetsOnly() {
        // Arrange
        PrivateJet aircraft1 = mock(PrivateJet.class);
        when(aircraft1.getModel()).thenReturn("Gulfstream G650");
        when(aircraft1.getPassengerCapacity()).thenReturn(19);
        when(aircraft1.getCargoCapacity()).thenReturn(0.5);

        PrivateJet aircraft2 = mock(PrivateJet.class);
        when(aircraft2.getModel()).thenReturn("Bombardier Global 7500");
        when(aircraft2.getPassengerCapacity()).thenReturn(17);
        when(aircraft2.getCargoCapacity()).thenReturn(0.4);

        List<Aircraft> aircraftList = Arrays.asList(aircraft1, aircraft2);
        when(airline.getAircraftList()).thenReturn(aircraftList);

        // Act
        try (MockedStatic<LoggerManager> loggerManagerMock = mockStatic(LoggerManager.class)) {
            String result = statistics.generateStatisticsText(airline);

            // Assert
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Start generating statistics for 2 aircrafts."));
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Processing private jet: Gulfstream G650"));
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Processing private jet: Bombardier Global 7500"));
            loggerManagerMock.verify(() -> LoggerManager.logInfo(contains("Generated statistics:")));

            assertTrue(result.contains("Загальна кількість літаків: 2"));
            assertTrue(result.contains("Загальна пасажирська місткість: 36"));
            assertTrue(result.contains("Загальна вантажна місткість: 0,90 т"));
        }
    }

    @Test
    public void testGenerateStatisticsWithMixedAircraftTypes() {
        // Arrange
        PassengerAircraft passengerAircraft = mock(PassengerAircraft.class);
        when(passengerAircraft.getModel()).thenReturn("Boeing 787");
        when(passengerAircraft.getPassengerCapacity()).thenReturn(300);
        when(passengerAircraft.getCargoCapacity()).thenReturn(5.0);

        CargoAircraft cargoAircraft = mock(CargoAircraft.class);
        when(cargoAircraft.getModel()).thenReturn("Boeing 777F");
        when(cargoAircraft.getCargoCapacity()).thenReturn(100.0);

        PrivateJet privateJet = mock(PrivateJet.class);
        when(privateJet.getModel()).thenReturn("Cessna Citation X");
        when(privateJet.getPassengerCapacity()).thenReturn(12);
        when(privateJet.getCargoCapacity()).thenReturn(0.3);

        List<Aircraft> aircraftList = Arrays.asList(passengerAircraft, cargoAircraft, privateJet);
        when(airline.getAircraftList()).thenReturn(aircraftList);

        // Act
        try (MockedStatic<LoggerManager> loggerManagerMock = mockStatic(LoggerManager.class)) {
            String result = statistics.generateStatisticsText(airline);

            // Assert
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Start generating statistics for 3 aircrafts."));
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Processing passenger aircraft: Boeing 787"));
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Processing cargo aircraft: Boeing 777F"));
            loggerManagerMock.verify(() -> LoggerManager.logInfo("Processing private jet: Cessna Citation X"));
            loggerManagerMock.verify(() -> LoggerManager.logInfo(contains("Generated statistics:")));

            assertTrue(result.contains("Загальна кількість літаків: 3"));
            assertTrue(result.contains("Загальна пасажирська місткість: 312"));
            assertTrue(result.contains("Загальна вантажна місткість: 105,30 т"));
        }
    }



    @Test
    public void testFormatting() {
        // Arrange
        PassengerAircraft passengerAircraft = mock(PassengerAircraft.class);
        when(passengerAircraft.getModel()).thenReturn("Airbus A380");
        when(passengerAircraft.getPassengerCapacity()).thenReturn(525);
        when(passengerAircraft.getCargoCapacity()).thenReturn(12.345);

        List<Aircraft> aircraftList = Collections.singletonList(passengerAircraft);
        when(airline.getAircraftList()).thenReturn(aircraftList);

        // Act
        try (MockedStatic<LoggerManager> loggerManagerMock = mockStatic(LoggerManager.class)) {
            String result = statistics.generateStatisticsText(airline);

            // Assert
            // Перевіряємо форматування чисел, особливо вантажної місткості з двома десятковими знаками
            assertTrue(result.contains("Загальна кількість літаків: 1"));
            assertTrue(result.contains("Загальна пасажирська місткість: 525"));
            assertTrue(result.contains("Загальна вантажна місткість: 12,35 т"));
        }
    }

}