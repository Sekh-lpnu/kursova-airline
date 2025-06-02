package models;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class CargoAircraftTest {

    @Test
    void testCargoAircraftInitialization() {
        CargoAircraft cargoAircraft = new CargoAircraft("Boeing 747", 12000, 3.5, 50000);

        assertEquals("Boeing 747", cargoAircraft.getModel());
        assertEquals(12000, cargoAircraft.getRange());
        assertEquals(3.5, cargoAircraft.getFuelConsumptionPerUnit());
        assertEquals(50000, cargoAircraft.getCargoCapacity());
    }

    @Test
    void testCalculateFuelConsumption() {
        CargoAircraft cargoAircraft = new CargoAircraft("Boeing 747", 12000, 3.5, 50000);

        double expectedFuelConsumption = 12000 * 3.5;
        assertEquals(expectedFuelConsumption, cargoAircraft.calculateFuelConsumption());
    }

    @Test
    void testToString() {
        CargoAircraft cargoAircraft = new CargoAircraft("Boeing 737", 2.8, 14000.0, 8000.0);

        String actual = cargoAircraft.toString();
        System.out.println(actual);

        assertTrue(actual.contains("Модель: Boeing 737"));
        assertFalse(actual.contains("Пасажиромісткість"));
        assertTrue(actual.contains("Вантажопідйомність: 8000.0"));
        assertTrue(actual.contains("Загальне споживання пального:"));

        Pattern pattern = Pattern.compile("Загальне споживання пального: ([0-9.]+)");
        Matcher matcher = pattern.matcher(actual);
        assertTrue(matcher.find(), "Не знайдено споживання пального у рядку");
        double fuelConsumptionValue = Double.parseDouble(matcher.group(1));
        assertEquals(39200.0, fuelConsumptionValue, 0.0001);
    }




    @Test
    void testGetCargoCapacity() {
        CargoAircraft cargoAircraft = new CargoAircraft("Airbus A330", 8000, 4.0, 60000);

        assertEquals(60000, cargoAircraft.getCargoCapacity());
    }
}
