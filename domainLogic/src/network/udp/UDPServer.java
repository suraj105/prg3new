package network.udp;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cargo.CargoType;
import cargo.Hazard;
import management.WarehouseManagementSystem;

public class UDPServer
{
    private DatagramPacket receivePacket;
    private final byte[] receiveData;
    private WarehouseManagementSystem system;

    public UDPServer(WarehouseManagementSystem system)
    {
        this.system = system;
        this.receiveData = new byte[1024];

        try (DatagramSocket serverSocket = new DatagramSocket(8080))
        {
            System.out.println("Server listening on port 8080...");

            while (true)
            {
                System.out.println("""
                        Commands:
                        • :c Switch to insertion mode
                        • :d Switch to deletion mode
                        • :r Switch to display mode
                        • :u Switch to modification mode
                        • :p Switch to persistence mode
                        • :x Exit the system
                        """);
                System.out.print("Enter command: ");

                receiveInput(serverSocket);

                InetAddress clientAddress = this.receivePacket.getAddress();
                int clientPort = this.receivePacket.getPort();
                String input = getReceivedInput();

                if (input.isEmpty())
                {
                    continue;
                }

                byte[] sendData;
                String serverText;

                switch (input)
                {
                    case ":c" ->
                    {
                        handleInsertionMode(serverSocket);
                        serverText = "Insertion mode completed.";
                    }
                    case ":d" ->
                    {
                        handleDeletionMode(serverSocket);
                        serverText = "Deletion mode completed.";
                    }
                    case ":r" ->
                    {
                        handleDisplayMode(serverSocket);
                        serverText = "Display mode completed.";
                    }
                    case ":u" ->
                    {
                        handleModificationMode(serverSocket);
                        serverText = "Modification mode completed.";
                    }
                    case ":p" ->
                    {
                        handlePersistenceMode(serverSocket);
                        serverText = "Persistence mode completed.";
                    }
                    case ":x" ->
                    {
                        System.exit(0);
                        return; // Exit the method, no need to send a response packet
                    }
                    default -> serverText = "Invalid command.";
                }

                sendData = serverText.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void handleInsertionMode(DatagramSocket serverSocket) throws IOException
    {
        while (true)
        {
            System.out.println("""
                    Insertion mode:
                    • customer => Insert a customer
                    • cargo => Insert a cargo item
                    """);
            System.out.print("Enter insertion command (or 'exit' to return to main menu): ");
            receiveInput(serverSocket);
            String input = getReceivedInput();

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
                        receiveInput(serverSocket);
                        input = getReceivedInput();
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
                        receiveInput(serverSocket);
                        input = getReceivedInput();
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

        CargoType type = CargoType.valueOf(cargo[0].toString());
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

    private void handleDeletionMode(DatagramSocket serverSocket) throws IOException
    {
        while (true)
        {
            System.out.println("""
                    Deletion mode:
                    • customer => deletes the customer
                    • location => removes the cargo item
                    """);
            System.out.print("Enter deletion command (or 'exit' to return to main menu): ");
            receiveInput(serverSocket);
            String input = getReceivedInput();

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
                        receiveInput(serverSocket);
                        input = getReceivedInput();
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
                        receiveInput(serverSocket);
                        input = getReceivedInput();
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

    private void handleDisplayMode(DatagramSocket serverSocket) throws IOException
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
            receiveInput(serverSocket);
            String input = getReceivedInput();

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
                        receiveInput(serverSocket);
                        String cargoType = getReceivedInput();
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

    private void handleModificationMode(DatagramSocket serverSocket) throws IOException
    {
        while (true)
        {
            System.out.println("""
                    Modification mode:
                    • location => sets the inspection date
                    """);
            System.out.print("Enter cargo location (or 'exit' to return to main menu): ");
            receiveInput(serverSocket);
            String input = getReceivedInput();

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
                    receiveInput(serverSocket);
                    String dateInput = getReceivedInput();
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

    private void handlePersistenceMode(DatagramSocket serverSocket) throws IOException
    {
        while (true)
        {
            System.out.println("""
                    Persistence mode:
                    • saveJOS => Save using JOS
                    • loadJOS => Load using JOS
                    """);
            System.out.print("Enter persistence command (or 'exit' to return to main menu): ");
            receiveInput(serverSocket);
            String input = getReceivedInput();

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
                        receiveInput(serverSocket);
                        String filename = getReceivedInput();
                        system.saveJOS(filename);
                    }
                    case "loadJOS" ->
                    {
                        System.out.print("Enter the filename: ");
                        receiveInput(serverSocket);
                        String loadFilename = getReceivedInput();
                        system.loadJOS(loadFilename);
                    }
                    default -> System.out.println("Invalid command.");
                }
            }
        }
    }

    private void receiveInput(DatagramSocket serverSocket) throws IOException
    {
        this.receivePacket = new DatagramPacket(this.receiveData, this.receiveData.length);
        serverSocket.receive(this.receivePacket);

    }

    private String getReceivedInput()
    {
        return new String(this.receivePacket.getData(), 0, this.receivePacket.getLength()).trim();
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
