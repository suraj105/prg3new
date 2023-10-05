package management.observers;

import cargo.AbstractCargo;
import cargo.Hazard;
import management.WarehouseManagementSystem;
import management.events.CargoItemInsertedEvent;
import management.events.Event;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class HazardsChangedObserver implements Observer, Serializable
{
    @Serial
    private static final long serialVersionUID = 987654321L;
    private Set<Hazard> existingHazards;

    public HazardsChangedObserver(WarehouseManagementSystem system)
    {
        this.existingHazards = new HashSet<>(system.getHazards());
    }

    @Override
    public void update(Event event)
    {
        if (event instanceof CargoItemInsertedEvent)
        {
            AbstractCargo cargoItem = ((CargoItemInsertedEvent) event).getCargoItem();
            Set<Hazard> newHazards = new HashSet<>(cargoItem.getHazards());

            // Check for changes in hazards and produce a notification if any changes occurred
            if (!existingHazards.equals(newHazards))
            {
                System.out.println("Notification: Hazards for cargo item at location " + cargoItem.getStorageLocation() +
                        " have changed.");
                existingHazards = newHazards; // Update the existing hazards
            }
        }
    }
}
