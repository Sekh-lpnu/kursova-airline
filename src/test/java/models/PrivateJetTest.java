package models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PrivateJetTest {

    @Test
    void testConstructorAndGetters() {
        PrivateJet privateJet = new PrivateJet("Gulfstream G650", 12000, 2.5, 18, 2.0);

        Assertions.assertEquals("Gulfstream G650", privateJet.getModel(), "Модель має бути Gulfstream G650");
        Assertions.assertEquals(12000, privateJet.getRange(), 0.001, "Дальність має бути 12000 км");
        Assertions.assertEquals(2.5, privateJet.getFuelConsumptionPerUnit(), 0.001, "Споживання пального має бути 2.5 л/км");
        Assertions.assertEquals(18, privateJet.getPassengerCapacity(), "Пасажиромісткість має бути 18 осіб");
        Assertions.assertEquals(2.0, privateJet.getCargoCapacity(), 0.001, "Вантажопідйомність має бути 2.0 тон");
    }

    @Test
    void testToString() {
        PrivateJet privateJet = new PrivateJet("Cessna Citation", 4000, 3.0, 8, 0.5);

        String actual = privateJet.toString();

        assertTrue(actual.contains("Приватний літак"));
        assertTrue(actual.contains("Модель: Cessna Citation"));
        assertTrue(actual.contains("Дальність: 4000.0"));
        assertTrue(actual.contains("Споживання пального на одиницю: 3.0"));
        assertTrue(actual.contains("Загальне споживання пального: 12000.0"));
        assertTrue(actual.contains("Пасажиромісткість: 8"));
        assertTrue(actual.contains("Вантажопідйомність: 0.5"));
    }


    @Test
    void testCalculateFuelConsumption() {
        PrivateJet privateJet = new PrivateJet("Learjet 75", 5000, 2.0, 9, 1.0);
        double expectedFuelConsumption = 5000 * 2.0;

        Assertions.assertEquals(expectedFuelConsumption, privateJet.calculateFuelConsumption(), 0.001, "Розрахунок загального споживання пального має бути коректним");
    }

    @Test
    void testInheritance() {
        PrivateJet privateJet = new PrivateJet("HondaJet Elite", 3000, 1.8, 6, 0.8);
        assertTrue(privateJet instanceof Aircraft, "PrivateJet має бути підкласом Aircraft");
    }
}
