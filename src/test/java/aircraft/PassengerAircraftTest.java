package aircraft;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PassengerAircraftTest {

    @Test
    void testPassengerAircraftInitialization() {
        // Створюємо пасажирський літак
        PassengerAircraft passengerAircraft = new PassengerAircraft(
                "Airbus A380", 15000, 4.0, 800, 20000);

        // Перевіряємо, чи правильно ініціалізовані поля
        assertEquals("Airbus A380", passengerAircraft.getModel());
        assertEquals(15000, passengerAircraft.getRange());
        assertEquals(4.0, passengerAircraft.getFuelConsumptionPerUnit());
        assertEquals(800, passengerAircraft.getPassengerCapacity());
        assertEquals(20000, passengerAircraft.getCargoCapacity());
    }

    @Test
    void testCalculateFuelConsumption() {
        // Створюємо пасажирський літак
        PassengerAircraft passengerAircraft = new PassengerAircraft(
                "Airbus A320", 6000, 3.5, 180, 10000);

        // Розрахунок споживання пального
        double expectedFuelConsumption = 6000 * 3.5;
        assertEquals(expectedFuelConsumption, passengerAircraft.calculateFuelConsumption());
    }

    @Test
    void testToString() {
        PassengerAircraft passengerAircraft = new PassengerAircraft(
                "Boeing 737", 5000, 2.8, 150, 8000);

        String actual = passengerAircraft.toString();

        assertTrue(actual.contains("Модель: Boeing 737"));
        assertTrue(actual.contains("Пасажиромісткість: 150"));
        assertTrue(actual.contains("Вантажопідйомність: 8000.0"));
        assertTrue(actual.contains("Загальне споживання пального: 14000.0"));
    }


    @Test
    void testGetPassengerCapacity() {
        // Створюємо пасажирський літак
        PassengerAircraft passengerAircraft = new PassengerAircraft(
                "Embraer E190", 4000, 3.0, 100, 5000);

        // Перевіряємо, чи правильно повертається пасажиромісткість
        assertEquals(100, passengerAircraft.getPassengerCapacity());
    }

    @Test
    void testGetCargoCapacity() {
        // Створюємо пасажирський літак
        PassengerAircraft passengerAircraft = new PassengerAircraft(
                "CRJ 900", 3500, 2.5, 90, 4000);

        // Перевіряємо, чи правильно повертається вантажопідйомність
        assertEquals(4000, passengerAircraft.getCargoCapacity());
    }
}
