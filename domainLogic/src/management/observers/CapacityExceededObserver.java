package management.observers;

import management.WarehouseManagementSystem;
import management.events.CargoItemInsertedEvent;
import management.events.Event;

import java.io.Serial;
import java.io.Serializable;

public class CapacityExceededObserver implements Observer, Serializable
{
    @Serial
    private static final long serialVersionUID = 987654321L;

    private final WarehouseManagementSystem system;

    public CapacityExceededObserver(WarehouseManagementSystem warehouseManagementSystem)
    {
        this.system = warehouseManagementSystem;
    }

    @Override
    public void update(Event event)
    {
        if (event instanceof CargoItemInsertedEvent)
        {
            // Check capacity and produce a notification if it exceeds 90%
            double capacityUtilization = system.calculateCapacityUtilization();
            if (capacityUtilization > 0.9)
            {
                System.out.println("Warning: Warehouse capacity exceeded 90%!");
            }
        }
    }
}
