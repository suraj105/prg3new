package simulations.one;

import cargo.AbstractCargo;
import management.WarehouseManagementSystem;
import simulations.AbstractCargoConsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class CargoConsumer extends AbstractCargoConsumer
{
    private final Random random;

    public CargoConsumer(WarehouseManagementSystem system, BlockingQueue<AbstractCargo> cargoQueue)
    {
        super(system, cargoQueue);
        this.random = new Random();
    }

    @Override
    protected void consumeAndDeleteCargo()
    {
        int queueSize = cargoQueue.size();
        if (queueSize > 0)
        {
            int index = random.nextInt(queueSize);
            AbstractCargo[] cargoArray = new AbstractCargo[queueSize];
            cargoQueue.toArray(cargoArray);
            AbstractCargo cargoToDelete = cargoArray[index];
            if (cargoQueue.remove(cargoToDelete))
            {
                system.removeCargoItem(cargoToDelete.getStorageLocation());
                System.out.println("Cargo deleted: " + cargoToDelete);
            }
        }
    }
}
