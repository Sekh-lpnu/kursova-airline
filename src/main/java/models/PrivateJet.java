package models;

public class PrivateJet extends Aircraft {
    private final int passengerCapacity;
    private final double cargoCapacity;

    public PrivateJet(String model, double range, double fuelConsumptionPerUnit, int passengerCapacity, double cargoCapacity) {
        super(model, range, fuelConsumptionPerUnit);
        this.passengerCapacity = passengerCapacity;
        this.cargoCapacity = cargoCapacity;
    }

    public PrivateJet(int id, String model, double range, double fuelConsumptionPerUnit, int passengerCapacity, double cargoCapacity) {
        super(id, model, range, fuelConsumptionPerUnit);
        this.passengerCapacity = passengerCapacity;
        this.cargoCapacity = cargoCapacity;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public String toString() {
        return "Приватний літак { ID: " + getId() +
                ", Модель: " + getModel() + ", Дальність: " + getRange() +
                ", Споживання пального на одиницю: " + getFuelConsumptionPerUnit() +
                ", Загальне споживання пального: " + calculateFuelConsumption() +
                ", Пасажиромісткість: " + passengerCapacity + ", Вантажопідйомність: " + cargoCapacity + " }";
    }
}
