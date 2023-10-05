package main;

import cli.CommandLineInterface;
import management.WarehouseManagementSystem;
import org.jetbrains.annotations.NotNull;

public class CLI {
    // Default protocol and capacity values
    private static final String DEFAULT_PROTOCOL = "TCP";
    private static final int DEFAULT_CAPACITY = 100;

    public static void main(String @NotNull [] args) {
        String protocol = DEFAULT_PROTOCOL;
        int capacity = DEFAULT_CAPACITY;

        if (args.length >= 1) {
            // If protocol argument is provided, use it (case-insensitive)
            protocol = args[0].toUpperCase();
        }

        if (args.length >= 2) {
            // If capacity argument is provided, parse it (handle invalid input gracefully)
            try {
                capacity = Integer.parseInt(args[1]);
                if (capacity <= 0) {
                    System.out.println("Invalid capacity. Please specify a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid capacity. Please specify a valid positive number.");
                return;
            }
        }

        if (!protocol.equals("TCP") && !protocol.equals("UDP")) {
            System.out.println("Invalid protocol. Please specify TCP or UDP.");
            return;
        }

        WarehouseManagementSystem system = new WarehouseManagementSystem(capacity);
        CommandLineInterface cli = new CommandLineInterface(system);

        switch (protocol) {
            case "TCP" -> cli.start("TCP");
            case "UDP" -> cli.start("UDP");
            default -> System.out.println("Invalid protocol. Please specify TCP or UDP.");
        }
    }
}
