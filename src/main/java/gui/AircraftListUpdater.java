package gui;

import models.Aircraft;
import java.util.List;

public interface AircraftListUpdater {
    void updateAircraftListInGUI(List<Aircraft> aircraftList);
}
