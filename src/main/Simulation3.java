package main;

import org.jetbrains.annotations.NotNull;
import simulations.three.IntervalThreadSimulation;

public class Simulation3
{
    public static void main(String @NotNull [] args)
    {
        if (args.length < 3)
        {
            System.out.println("Insufficient arguments. Please specify initial capacity, number of threads, and interval.");
            return;
        }

        int capacity;
        int numThreads;
        long interval;

        try
        {
            capacity = Integer.parseInt(args[0]);
            numThreads = Integer.parseInt(args[1]);
            interval = Long.parseLong(args[2]);
        } catch (NumberFormatException e)
        {
            System.out.println("Invalid capacity, number of threads, or interval. Please specify valid positive numbers.");
            return;
        }

        IntervalThreadSimulation simulation = new IntervalThreadSimulation(capacity, numThreads, interval);
        simulation.start();
    }
}
