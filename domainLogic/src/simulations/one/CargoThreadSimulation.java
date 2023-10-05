package simulations.one;

import cargo.AbstractCargo;
import management.WarehouseManagementSystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CargoThreadSimulation
{
    private WarehouseManagementSystem system;

    public CargoThreadSimulation(int capacity)
    {
        this.system = new WarehouseManagementSystem(capacity);
    }

    public void start()
    {
        BlockingQueue<AbstractCargo> cargoQueue = new LinkedBlockingQueue<>();

        Thread producerThread = new Thread(new CargoProducer(system, cargoQueue));
        Thread consumerThread = new Thread(new CargoConsumer(system, cargoQueue));

        producerThread.start();
        consumerThread.start();
    }
}
