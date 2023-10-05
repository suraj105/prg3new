package simulations;

import cargo.AbstractCargo;
import management.WarehouseManagementSystem;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractCargoConsumer implements Runnable
{
    protected final WarehouseManagementSystem system;
    protected final BlockingQueue<AbstractCargo> cargoQueue;

    public AbstractCargoConsumer(WarehouseManagementSystem system, BlockingQueue<AbstractCargo> cargoQueue)
    {
        this.system = system;
        this.cargoQueue = cargoQueue;
    }

    @Override
    public void run()
    {
        //noinspection InfiniteLoopStatement
        while (true)
        {
            synchronized (cargoQueue)
            {
                consumeAndDeleteCargo();
            }
        }
    }

    protected abstract void consumeAndDeleteCargo();
}
