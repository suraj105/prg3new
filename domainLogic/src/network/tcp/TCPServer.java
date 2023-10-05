package network.tcp;

import cargo.CargoType;
import cargo.Hazard;

import java.io.*;
import java.net.*;

import management.WarehouseManagementSystem;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TCPServer
{
    private BufferedReader inFromClient;
    private PrintWriter outToClient;
    private WarehouseManagementSystem system;

    public TCPServer(WarehouseManagementSystem system)
    {
        this.system = system;

        try (ServerSocket serverSocket = new ServerSocket(8080))
        {
            System.out.println("ServerTCP listening on port 8080...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("ClientTCP connected: " + clientSocket.getInetAddress());

            this.inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.outToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            this.start();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    private void start() throws IOException
    {
        System.out.println("Warehouse Management System");

        while (true)
        {
            System.out.println("""
                    Command set:
                    • :c Switch to insertion mode
                    • :d Switch to deletion mode
                    • :r Switch to display mode
                    • :u Switch to modification mode
                    • :p Switch to persistence
                    • :x Exit the system
                    """);
            System.out.print("Enter command: ");
            String input = this.inFromClient.readLine().trim();

            if (input.isEmpty())
            {
                continue;
            }

            switch (input)
            {
                case ":c" ->
                {
                    handleInsertionMode();
                    outToClient.println("Insertion mode completed.");
                }
                case ":d" ->
                {
                    handleDeletionMode();
                    outToClient.println("Deletion mode completed.");
                }
                case ":r" ->
                {
                    handleDisplayMode();
                    outToClient.println("Display mode completed.");
                }
                case ":u" ->
                {
                    handleModificationMode();
                    outToClient.println("Modification mode completed.");
                }
                case ":p" ->
                {
                    handlePersistenceMode();
                    outToClient.println("Persistence mode completed.");
                }
                case ":x" -> System.exit(0);
                default ->
                        outToClient.println("Invalid command.");
            }
        }
    }


    private void handleInsertionMode() throws IOException
    {
        while (true)
        {
            System.out.println("""
                    Insertion mode:
                    • customer => Insert a customer
                    • cargo => Insert a cargo item
                    """);
            System.out.print("Enter insertion command (or 'exit' to return to main menu): ");
            String input = this.inFromClient.readLine().trim();

            if (input.equalsIgnoreCase("exit"))
            {
                break;
            }

            if (!input.isEmpty())
            {
                switch (input)
                {
                    case "customer" ->
                    {
                        System.out.print("Enter customer name (or 'exit' to return to main menu): ");
                        input = this.inFromClient.readLine().trim();
                        if (input.equalsIgnoreCase("exit"))
                        {
                            break;
                        }
                        if (!input.isEmpty())
                        {
                            this.system.createCustomer(input);
                        }
                    }
                    case "cargo" ->
                    {
                        System.out.println("""
                                Below are the examples for entering cargo details
                                o LiquidAndDryBulkCargo Alice 4004.50 flammable,toxic true 10
                                o UnitisedCargo Bob 10000 , false
                                """);
                        System.out.print("Enter cargo details (or 'exit' to return to main menu): ");
                        input = this.inFromClient.readLine().trim();
                        if (input.equalsIgnoreCase("exit"))
                        {
                            break;
                        }
                        if (!input.isEmpty())
                        {
                            insertCargo(input);
                        }
                    }
                    default -> System.out.println("Invalid command.");
                }
            }
        }
    }

    private void insertCargo(String input)
    {
        String[] cargo = input.split(" ");

        CargoType type = CargoType.valueOf(cargo[0]);
        switch (type)
        {
            case DryBulkAndUnitisedCargo ->
            {
                if (cargo.length == 6)
                {
                    system.insertCargoItem(CargoType.DryBulkAndUnitisedCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), Boolean.parseBoolean(cargo[4]), false, Integer.parseInt(cargo[5]));
                } else
                {
                    System.out.println("Invalid command.");
                }
            }

            case DryBulkCargo ->
            {
                if (cargo.length == 5)
                {
                    system.insertCargoItem(CargoType.DryBulkCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), false, false, Integer.parseInt(cargo[4]));
                } else
                {
                    System.out.println("Invalid command.");
                }
            }
            case LiquidAndDryBulkCargo ->
            {
                if (cargo.length == 6)
                {
                    system.insertCargoItem(CargoType.LiquidAndDryBulkCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), false, Boolean.parseBoolean(cargo[4]), Integer.parseInt(cargo[5]));
                } else
                {
                    System.out.println("Invalid command.");
                }
            }
            case LiquidBulkAndUnitisedCargo ->
            {
                if (cargo.length == 6)
                {
                    system.insertCargoItem(CargoType.LiquidBulkAndUnitisedCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), Boolean.parseBoolean(cargo[4]), Boolean.parseBoolean(cargo[5]), 0);
                } else
                {
                    System.out.println("Invalid command.");
                }
            }
            case LiquidBulkCargo ->
            {
                if (cargo.length == 5)
                {
                    system.insertCargoItem(CargoType.LiquidBulkCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), false, Boolean.parseBoolean(cargo[4]), 0);
                } else
                {
                    System.out.println("Invalid command.");
                }
            }
            case UnitisedCargo ->
            {
                if (cargo.length == 5)
                {
                    system.insertCargoItem(CargoType.UnitisedCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), Boolean.parseBoolean(cargo[4]), false, 0);
                } else
                {
                    System.out.println("Invalid command.");
                }
            }

            default -> System.out.println("Invalid command.");
        }
    }

    private List<Hazard> splitHazards(String input)
    {
        List<Hazard> result = new ArrayList<>();
        String[] splitValues = input.split(",");
        for (String value : splitValues)
        {
            result.add(Hazard.valueOf(value.trim()));
        }
        return result;
    }

    private void handleDeletionMode() throws IOException
    {
        while (true)
        {
            System.out.println("""
                    Deletion mode:
                    • customer => deletes the customer
                    • location => removes the cargo item
                    """);
            System.out.print("Enter deletion command (or 'exit' to return to main menu): ");
            String input = this.inFromClient.readLine().trim();

            if (input.equalsIgnoreCase("exit"))
            {
                break;
            }

            if (!input.isEmpty())
            {
                switch (input)
                {
                    case "customer" ->
                    {
                        System.out.print("Enter customer name (or 'exit' to return to main menu): ");
                        input = this.inFromClient.readLine().trim();
                        if (input.equalsIgnoreCase("exit"))
                        {
                            break;
                        }
                        if (!input.isEmpty())
                        {
                            system.deleteCustomer(input);
                        }
                    }

                    case "location" ->
                    {
                        System.out.print("Enter location of cargo (or 'exit' to return to main menu): ");
                        input = this.inFromClient.readLine().trim();
                        if (input.equalsIgnoreCase("exit"))
                        {
                            break;
                        }
                        if (!input.isEmpty())
                        {
                            system.removeCargoItem(Integer.parseInt(input));
                        }
                    }
                    default -> System.out.println("Invalid command.");
                }
            }
        }
    }

    private void handleDisplayMode() throws IOException
    {
        while (true)
        {
            System.out.println("""
                    Display mode:
                    • customers => Display all customers
                    • cargos => Display all cargos
                    • type  => Display all cargos with specific cargo type
                    • included / i => Display the present hazards
                    • not included / e => Display the absent hazards
                    """);
            System.out.print("Enter display command (or 'exit' to return to main menu): ");
            String input = this.inFromClient.readLine().trim();

            if (input.equalsIgnoreCase("exit"))
            {
                break;
            }

            if (!input.isEmpty())
            {
                switch (input)
                {
                    case "customers" -> system.displayCustomers();
                    case "cargos" -> system.displayCargoItems(null);
                    case "type" ->
                    {
                        System.out.print("Enter cargo type (or 'exit' to return to main menu): ");
                        String cargoType = this.inFromClient.readLine().trim();
                        if (cargoType.equalsIgnoreCase("exit"))
                        {
                            break;
                        }
                        if (!cargoType.isEmpty())
                        {
                            system.displayCargoItems(cargoType);
                        }
                    }
                    case "included", "i" -> system.displayHazards(true);
                    case "not included", "e" -> system.displayHazards(false);
                    default -> System.out.println("Invalid command.");
                }
            }
        }
    }

    private void handleModificationMode() throws IOException
    {
        while (true)
        {
            System.out.println("""
                    Modification mode:
                    • location => sets the inspection date
                    """);
            System.out.print("Enter cargo location (or 'exit' to return to main menu): ");
            String input = this.inFromClient.readLine().trim();

            if (input.equalsIgnoreCase("exit"))
            {
                break;
            }

            if (!input.isEmpty())
            {
                try
                {
                    int location = Integer.parseInt(input);
                    System.out.print("Enter inspection date (dd.MM.yyyy) (or TODAY to set today's date): ");
                    String dateInput = this.inFromClient.readLine().trim();
                    Date inspectionDate;
                    if (dateInput.equals("TODAY"))
                    {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                        inspectionDate = parseDate(formatter.format(new Date()));
                        system.setInspectionDate(location, inspectionDate);
                    } else
                    {
                        inspectionDate = parseDate(dateInput);
                        if (inspectionDate != null)
                        {
                            system.setInspectionDate(location, inspectionDate);
                        } else
                        {
                            System.out.println("Invalid date format. Please use dd.MM.yyyy");
                        }
                    }

                } catch (NumberFormatException e)
                {
                    System.out.println("Invalid location format. Please enter a number.");
                }
            }
        }
    }

    private void handlePersistenceMode() throws IOException
    {
        while (true)
        {
            System.out.println("""
                    Persistence mode:
                    • saveJOS => Save using JOS
                    • loadJOS => Load using JOS
                    """);
            System.out.print("Enter persistence command (or 'exit' to return to main menu): ");
            String input = this.inFromClient.readLine().trim();

            if (input.equalsIgnoreCase("exit"))
            {
                break;
            }

            if (!input.isEmpty())
            {
                switch (input)
                {
                    case "saveJOS" ->
                    {
                        System.out.print("Enter the filename: ");
                        String filename = this.inFromClient.readLine().trim();
                        system.saveJOS(filename);
                    }
                    case "loadJOS" ->
                    {
                        System.out.print("Enter the filename: ");
                        String loadFilename = this.inFromClient.readLine().trim();
                        system.loadJOS(loadFilename);
                    }
                    default -> System.out.println("Invalid command.");
                }
            }
        }
    }

    private java.util.Date parseDate(String dateString)
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            return format.parse(dateString);
        } catch (ParseException e)
        {
            return null;
        }
    }

}
