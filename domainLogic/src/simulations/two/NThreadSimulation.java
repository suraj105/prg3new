package simulations.two;

import cargo.AbstractCargo;
import management.WarehouseManagementSystem;
import simulations.one.CargoConsumer;
import simulations.one.CargoProducer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class NThreadSimulation
{
    private final int numThreads;
    private final WarehouseManagementSystem system;

    public NThreadSimulation(int capacity, int numThreads)
    {
        this.numThreads = numThreads;
        this.system = new WarehouseManagementSystem(capacity);
    }

    public void start()
    {
        BlockingQueue<AbstractCargo> cargoQueue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(2 * this.numThreads);

        // Start inserting threads
        for (int i = 0; i < this.numThreads; i++)
        {
            Runnable producer = new CargoProducer(system, cargoQueue);
            executorService.submit(producer);
        }

        // Start deleting threads
        for (int i = 0; i < this.numThreads; i++)
        {
            Runnable consumer = new CargoConsumer(system, cargoQueue);
            executorService.submit(consumer);
        }

        // Shutdown the executor service when all threads have finished
        executorService.shutdown();
    }
}
