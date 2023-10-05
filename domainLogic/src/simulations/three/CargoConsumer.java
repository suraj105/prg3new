package simulations.three;

import cargo.AbstractCargo;
import management.WarehouseManagementSystem;
import simulations.AbstractCargoConsumer;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CargoConsumer extends AbstractCargoConsumer
{
    public CargoConsumer(WarehouseManagementSystem system, BlockingQueue<AbstractCargo> cargoQueue)
    {
        super(system, cargoQueue);
    }

    @Override
    protected void consumeAndDeleteCargo()
    {
        //noinspection InfiniteLoopStatement
        while (true)
        {
            synchronized (cargoQueue)
            {
                while (cargoQueue.isEmpty())
                {
                    try
                    {
                        cargoQueue.wait(); // Wait until there are items in the queue
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                AbstractCargo oldestCargo = getOldestCargo();
                cargoQueue.remove(oldestCargo);
                assert oldestCargo != null;
                system.removeCargoItem(oldestCargo.getStorageLocation());

                System.out.println("Cargo deleted: " + oldestCargo);

                cargoQueue.notifyAll();
            }
        }
    }

    private AbstractCargo getOldestCargo()
    {
        List<AbstractCargo> cargoList = system.getCargos();

        if (cargoList.isEmpty())
        {
            return null;
        }

        AbstractCargo oldestCargo = cargoList.get(0);
        Date oldestInspectionDate = oldestCargo.getLastInspectionDate();

        for (AbstractCargo cargo : cargoList)
        {
            Date inspectionDate = cargo.getLastInspectionDate();
            if (inspectionDate != null && inspectionDate.before(oldestInspectionDate))
            {
                oldestCargo = cargo;
                oldestInspectionDate = inspectionDate;
            }
        }
        return oldestCargo;
    }
}
