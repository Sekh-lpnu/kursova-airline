package utils;

import aircraft.Aircraft;
import aircraft.CargoAircraft;
import aircraft.PassengerAircraft;
import aircraft.PrivateJet;
import manager.Airline;
import manager.LoggerManager;

import java.util.List;
import java.util.logging.Logger;

public class Statistics {


    public String generateStatisticsText(Airline airline) {
        List<Aircraft> aircraftList = airline.getAircraftList();
        LoggerManager.logInfo("Start generating statistics for " + aircraftList.size() + " aircrafts.");

        int totalPassengerCapacity = 0;
        double totalCargoCapacity = 0;
        int totalAircraftCount = 0;

        for (Aircraft aircraft : aircraftList) {
            totalAircraftCount++;

            if (aircraft instanceof PassengerAircraft) {
                totalPassengerCapacity += ((PassengerAircraft) aircraft).getPassengerCapacity();
                totalCargoCapacity += ((PassengerAircraft) aircraft).getCargoCapacity();
                LoggerManager.logInfo("Processing passenger aircraft: " + aircraft.getModel());
            } else if (aircraft instanceof CargoAircraft) {
                totalCargoCapacity += ((CargoAircraft) aircraft).getCargoCapacity();
                LoggerManager.logInfo("Processing cargo aircraft: " + aircraft.getModel());
            } else if (aircraft instanceof PrivateJet) {
                totalPassengerCapacity += ((PrivateJet) aircraft).getPassengerCapacity();
                totalCargoCapacity += ((PrivateJet) aircraft).getCargoCapacity();
                LoggerManager.logInfo("Processing private jet: " + aircraft.getModel());
            }
        }

        String statistics = "Загальна кількість літаків: " + totalAircraftCount + "\n"
                + "Загальна пасажирська місткість: " + totalPassengerCapacity + "\n"
                + "Загальна вантажна місткість: " + String.format("%.2f т", totalCargoCapacity) + "\n";

        LoggerManager.logInfo("Generated statistics: \n" + statistics);
        return statistics;
    }
}