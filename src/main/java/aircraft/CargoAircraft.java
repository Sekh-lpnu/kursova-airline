package aircraft;

public class CargoAircraft extends Aircraft {
    private final double cargoCapacity;

    // Конструктор для нових літаків
    public CargoAircraft(String model, double range, double fuelConsumptionPerUnit, double cargoCapacity) {
        super(model, range, fuelConsumptionPerUnit);
        this.cargoCapacity = cargoCapacity;
    }

    // Конструктор для літаків із бази даних (із заданим ID)
    public CargoAircraft(int id, String model, double range, double fuelConsumptionPerUnit, double cargoCapacity) {
        super(id, model, range, fuelConsumptionPerUnit);
        this.cargoCapacity = cargoCapacity;
    }

    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public String toString() {
        return "Вантажний літак { ID: " + getId() +
                ", Модель: " + getModel() +
                ", Дальність: " + getRange() +
                ", Споживання пального на одиницю: " + getFuelConsumptionPerUnit() +
                ", Загальне споживання пального: " + calculateFuelConsumption() +
                ", Вантажопідйомність: " + cargoCapacity + " }";
    }
}
