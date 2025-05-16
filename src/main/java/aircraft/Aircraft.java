package aircraft;

public class Aircraft {
    private static int idCounter = 1;
    protected final int id;
    protected final String model;
    protected final double range;
    protected final double fuelConsumptionPerUnit;

    // Конструктор для створення нового літака вручну (через GUI)
    public Aircraft(String model, double range, double fuelConsumptionPerUnit) {
        this.id = idCounter++;
        this.model = model;
        this.range = range;
        this.fuelConsumptionPerUnit = fuelConsumptionPerUnit;
    }

    // Конструктор для створення літака з наявним ID (із бази даних)
    public Aircraft(int id, String model, double range, double fuelConsumptionPerUnit) {
        this.id = id;
        this.model = model;
        this.range = range;
        this.fuelConsumptionPerUnit = fuelConsumptionPerUnit;

        // Якщо id з бази більше або дорівнює поточному лічильнику, оновлюємо лічильник
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public double getRange() {
        return range;
    }

    public double getFuelConsumptionPerUnit() {
        return fuelConsumptionPerUnit;
    }

    public double calculateFuelConsumption() {
        return range * fuelConsumptionPerUnit;
    }

    @Override
    public String toString() {
        return "Літак { ID: " + id + ", Модель: " + model +
                ", Дальність: " + range + ", Споживання пального: " + calculateFuelConsumption() + " }";
    }
}
