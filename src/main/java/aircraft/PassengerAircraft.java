package aircraft;

public class PassengerAircraft extends Aircraft {
    protected int passengerCapacity;
    protected double cargoCapacity;

    // Конструктор для нових літаків
    public PassengerAircraft(String model, double range, double fuelConsumptionPerUnit, int passengerCapacity, double cargoCapacity) {
        super(model, range, fuelConsumptionPerUnit);
        this.passengerCapacity = passengerCapacity;
        this.cargoCapacity = cargoCapacity;
    }

    // Конструктор для літаків із бази даних (із заданим ID)
    public PassengerAircraft(int id, String model, double range, double fuelConsumptionPerUnit, int passengerCapacity, double cargoCapacity) {
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
        return "Пасажирський літак { ID: " + getId() +
                ", Модель: " + getModel() +
                ", Дальність: " + getRange() +
                ", Споживання пального на одиницю: " + getFuelConsumptionPerUnit() +
                ", Загальне споживання пального: " + calculateFuelConsumption() +
                ", Пасажиромісткість: " + passengerCapacity +
                ", Вантажопідйомність: " + cargoCapacity + " }";
    }
}
