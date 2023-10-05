package main;

import org.jetbrains.annotations.NotNull;
import simulations.one.CargoThreadSimulation;

public class Simulation1 {
    // Default initial capacity value
    private static final int DEFAULT_CAPACITY = 100;

    public static void main(String @NotNull [] args) {
        int capacity = DEFAULT_CAPACITY;

        // No need to check for arguments or handle exceptions
        CargoThreadSimulation simulation = new CargoThreadSimulation(capacity);
        simulation.start();
    }
}
