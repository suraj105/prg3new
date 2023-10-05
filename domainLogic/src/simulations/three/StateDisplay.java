package simulations.three;

import cargo.AbstractCargo;
import management.WarehouseManagementSystem;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StateDisplay implements Runnable
{
    private final WarehouseManagementSystem system;
    private final long interval;

    public StateDisplay(WarehouseManagementSystem system, long interval)
    {
        this.system = system;
        this.interval = interval;
    }

    @Override
    public void run()
    {
        //noinspection InfiniteLoopStatement
        while (true)
        {
            displayState();
            try
            {
                TimeUnit.MILLISECONDS.sleep(interval);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void displayState()
    {
        List<AbstractCargo> cargoList = system.getCargos();

        System.out.println("==== Current State of Business Logic ====");
        System.out.println("Total number of cargo items: " + cargoList.size());
        for (AbstractCargo cargo : cargoList)
        {
            System.out.println("Cargo: " + cargo);
        }
        System.out.println("=========================================");
    }
}