package simulations.three;

import cargo.AbstractCargo;
import management.WarehouseManagementSystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class IntervalThreadSimulation
{
    private final int numThreads;
    private final long interval;
    private final WarehouseManagementSystem system;

    public IntervalThreadSimulation(int capacity, int numThreads, long interval)
    {
        this.numThreads = numThreads;
        this.interval = interval;
        this.system = new WarehouseManagementSystem(capacity);
    }

    public void start()
    {
        BlockingQueue<AbstractCargo> cargoQueue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads + 2);

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

        // Start inspection thread
        CargoInspector inspector = new CargoInspector(system);
        Thread inspectorThread = new Thread(inspector);
        inspectorThread.start();

        // Start state display thread at specified interval
        StateDisplay stateDisplay = new StateDisplay(system, interval);
        Thread stateDisplayThread = new Thread(stateDisplay);
        stateDisplayThread.start();

        // Shutdown the executor service when all threads have finished
        executorService.shutdown();
    }
}
