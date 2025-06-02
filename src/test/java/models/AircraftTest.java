package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AircraftTest {

    static class TestAircraft extends Aircraft {
        public TestAircraft(String model, double range, double fuelConsumptionPerUnit) {
            super(model, range, fuelConsumptionPerUnit);
        }
    }

    @Test
    void testConstructorAndGetters() {
        Aircraft aircraft = new TestAircraft("Boeing 747", 10000, 5);

        assertEquals("Boeing 747", aircraft.getModel(), "Модель повинна бути Boeing 747");
        assertEquals(10000, aircraft.getRange(), "Дальність повинна бути 10000");
        assertEquals(5, aircraft.getFuelConsumptionPerUnit(), "Споживання пального на одиницю повинно бути 5");
        assertTrue(aircraft.getId() > 0, "ID повинен бути більше нуля");
    }

    @Test
    void testCalculateFuelConsumption() {
        Aircraft aircraft = new TestAircraft("Airbus A320", 5000, 3);

        double expectedFuelConsumption = 5000 * 3; // Дальність * Споживання на одиницю
        assertEquals(expectedFuelConsumption, aircraft.calculateFuelConsumption(),
                "Неправильне обчислення споживання пального");
    }

    @Test
    void testToString() {
        Aircraft aircraft = new TestAircraft("Cessna 172", 2000, 2);

        String expectedOutput = "Літак { ID: " + aircraft.getId() + ", Модель: Cessna 172, Дальність: 2000.0, Споживання пального: 4000.0 }";
        assertEquals(expectedOutput, aircraft.toString(), "Метод toString повертає неправильний результат");
    }
}
