package cli;

import cargo.*;
import management.WarehouseManagementSystem;
import management.observers.CapacityExceededObserver;
import management.observers.HazardsChangedObserver;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AlternativeCommandLineInterface
{
    private final WarehouseManagementSystem system;
    private final Scanner scanner;

    public AlternativeCommandLineInterface(WarehouseManagementSystem system)
    {
        this.system = system;
        this.scanner = new Scanner(System.in);
    }

    public void start()
    {
        CapacityExceededObserver capacityObserver = new CapacityExceededObserver(system);
        this.system.registerObserver(capacityObserver);

        System.out.println("Warehouse Management System");

        while (true)
        {
            System.out.println("""
                    Commands:
                    • :c Switch to insertion mode
                    • :r Switch to display mode
                    • :u Switch to modification mode
                    • :p Switch to persistence mode
                    • :x Exit
                    """);
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
            {
                continue;
            }

            switch (input)
            {
                case ":c" -> handleInsertionMode();
                case ":r" -> handleDisplayMode();
                case ":u" -> handleModificationMode();
                case ":p" -> handlePersistenceMode();
                case ":x" -> System.exit(0);
                default -> System.out.println("Invalid command.");
            }
        }
    }

    private void handleInsertionMode()
    {
        while (true)
        {
            System.out.println("""
                    Insertion mode:
                    • customer => Insert a customer
                    • cargo => Insert a cargo item
                    """);
            System.out.print("Enter insertion command (or 'exit' to return to main menu): ");
            String input = scanner.nextLine().trim();

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
                        input = scanner.nextLine().trim();
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
                                examples for entering cargo details
                                o LiquidAndDryBulkCargo Alice 4004.50 flammable,toxic true 10
                                o UnitisedCargo Bob 10000 false
                                """);
                        System.out.print("Enter cargo details (or 'exit' to return to main menu): ");
                        input = scanner.nextLine().trim();
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
                    this.system.insertCargoItem(CargoType.DryBulkAndUnitisedCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), Boolean.parseBoolean(cargo[4]), false, Integer.parseInt(cargo[5]));
                } else
                {
                    System.out.println("Invalid command.");
                }
            }

            case DryBulkCargo ->
            {
                if (cargo.length == 5)
                {
                    this.system.insertCargoItem(CargoType.DryBulkCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), false, false, Integer.parseInt(cargo[4]));
                } else
                {
                    System.out.println("Invalid command.");
                }
            }
            case LiquidAndDryBulkCargo ->
            {
                if (cargo.length == 6)
                {
                    this.system.insertCargoItem(CargoType.LiquidAndDryBulkCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), false, Boolean.parseBoolean(cargo[4]), Integer.parseInt(cargo[5]));
                } else
                {
                    System.out.println("Invalid command.");
                }
            }
            case LiquidBulkAndUnitisedCargo ->
            {
                if (cargo.length == 6)
                {
                    this.system.insertCargoItem(CargoType.LiquidBulkAndUnitisedCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), Boolean.parseBoolean(cargo[4]), Boolean.parseBoolean(cargo[5]), 0);
                } else
                {
                    System.out.println("Invalid command.");
                }
            }
            case LiquidBulkCargo ->
            {
                if (cargo.length == 5)
                {
                    this.system.insertCargoItem(CargoType.LiquidBulkCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), false, Boolean.parseBoolean(cargo[4]), 0);
                } else
                {
                    System.out.println("Invalid command.");
                }
            }
            case UnitisedCargo ->
            {
                if (cargo.length == 5)
                {
                    this.system.insertCargoItem(CargoType.UnitisedCargo, cargo[1], new BigDecimal(cargo[2]), splitHazards(cargo[3]), Boolean.parseBoolean(cargo[4]), false, 0);
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

    private void handleDisplayMode()
    {
        while (true)
        {
            System.out.println("""
                    Display mode:
                    • customers => Display all customers
                    • cargos => Display all cargos
                    • type  => Display all cargos with specific cargo type
                    """);
            System.out.print("Enter display command (or 'exit' to return to main menu): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit"))
            {
                break;
            }

            if (!input.isEmpty())
            {
                switch (input)
                {
                    case "customers" -> this.system.displayCustomers();
                    case "cargos" -> this.system.displayCargoItems(null);
                    case "type" ->
                    {
                        System.out.print("Enter cargo type (or 'exit' to return to main menu): ");
                        String cargoType = scanner.nextLine().trim();
                        if (cargoType.equalsIgnoreCase("exit"))
                        {
                            break;
                        }
                        if (!cargoType.isEmpty())
                        {
                            this.system.displayCargoItems(cargoType);
                        }
                    }
                    default -> System.out.println("Invalid command.");
                }
            }
        }
    }

    private void handleModificationMode()
    {
        while (true)
        {
            System.out.println("""
                    Modification mode:
                    • location => sets the inspection date
                    """);
            System.out.print("Enter cargo location (or 'exit' to return to main menu): ");
            String input = scanner.nextLine().trim();

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
                    String dateInput = scanner.nextLine().trim();
                    Date inspectionDate;
                    if (dateInput.equals("TODAY"))
                    {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                        inspectionDate = parseDate(formatter.format(new Date()));
                        this.system.setInspectionDate(location, inspectionDate);
                    } else
                    {
                        inspectionDate = parseDate(dateInput);
                        if (inspectionDate != null)
                        {
                            this.system.setInspectionDate(location, inspectionDate);
                        } else
                        {
                            System.out.println("Invalid date format. Please use dd.MM.yyyy.");
                        }
                    }

                } catch (NumberFormatException e)
                {
                    System.out.println("Invalid location format. Please enter a number.");
                }
            }
        }
    }

    private void handlePersistenceMode()
    {
        while (true)
        {
            System.out.println("""
                    Persistence mode:
                    • saveJOS => Save using JOS
                    • loadJOS => Load using JOS
                    """);
            System.out.print("Enter persistence command (or 'exit' to return to main menu): ");
            String input = scanner.nextLine().trim();

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
                        String filename = scanner.nextLine().trim();
                        this.system.saveJOS(filename);
                    }
                    case "loadJOS" ->
                    {
                        System.out.print("Enter the filename: ");
                        String loadFilename = scanner.nextLine().trim();
                        this.system.loadJOS(loadFilename);
                    }
                    default -> System.out.println("Invalid command.");
                }
            }
        }
    }

    private Date parseDate(String dateString)
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

