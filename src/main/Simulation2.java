package main;

import org.jetbrains.annotations.NotNull;
import simulations.two.NThreadSimulation;

public class Simulation2
{
    public static void main(String @NotNull [] args)
    {
        if (args.length < 2)
        {
            System.out.println("Insufficient arguments. Please specify initial capacity and number of threads.");
            return;
        }

        int capacity;
        int numThreads;

        try
        {
            capacity = Integer.parseInt(args[0]);
            numThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e)
        {
            System.out.println("Invalid capacity or number of threads. Please specify valid positive numbers.");
            return;
        }

        NThreadSimulation simulation = new NThreadSimulation(capacity, numThreads);
        simulation.start();
    }
}