package commands;

import manager.Airline;

public class AddAircraftCommand implements Command {
    private final Airline airline;
    private final String model;
    private final double range;
    private final double fuelConsumptionPerUnit;
    private final int passengerCapacity;
    private final double cargoCapacity;
    private final int aircraftType;

    // Конструктор для передавання параметрів
    public AddAircraftCommand(Airline airline, String model, double range, double fuelConsumptionPerUnit,
                              int passengerCapacity, double cargoCapacity, int aircraftType) {
        this.airline = airline;
        this.model = model;
        this.range = range;
        this.fuelConsumptionPerUnit = fuelConsumptionPerUnit;
        this.passengerCapacity = passengerCapacity;
        this.cargoCapacity = cargoCapacity;
        this.aircraftType = aircraftType;
    }

    @Override
    public void execute() {
        // Викликає метод addAircraft з параметрами, які передаються з графічного інтерфейсу
        airline.addAircraft(model, range, fuelConsumptionPerUnit, passengerCapacity, cargoCapacity, aircraftType);
    }
}
