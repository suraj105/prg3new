package management;

import administration.Customer;
import administration.CustomerImpl;
import cargo.*;
import io.FileSystem;
import management.events.CargoItemInsertedEvent;
import management.events.Event;
import management.observers.Observer;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class WarehouseManagementSystem implements Serializable
{
    @Serial
    private static final long serialVersionUID = 123456789L;
    private List<Customer> customers;
    private List<AbstractCargo> cargos;
    private Set<Hazard> hazards;
    private int storageCapacity;

    private List<Observer> observers = new ArrayList<>();

    public WarehouseManagementSystem(int storageCapacity)
    {
        this.customers = new ArrayList<>();
        this.cargos = new ArrayList<>();
        this.hazards = new HashSet<>();
        this.storageCapacity = storageCapacity;
    }

    public List<Customer> getCustomers()
    {
        return customers;
    }

    public List<AbstractCargo> getCargos()
    {
        return cargos;
    }

    public Set<Hazard> getHazards()
    {
        return hazards;
    }

    public int getStorageCapacity()
    {
        return storageCapacity;
    }

    public List<Observer> getObservers()
    {
        return observers;
    }

    // Methods for managing customers
    public void createCustomer(String name)
    {
        if (!isCustomerExists(name))
        {
            customers.add(new CustomerImpl(name));
            System.out.println("Customer created: " + name);
        } else
        {
            System.out.println("Customer with the same name already exists.");
        }
    }

    public void deleteCustomer(String name)
    {
        Customer customer = getCustomerByName(name);
        if (customer == null)
        {
            System.out.println("Customer not found.");
            return;
        }

        customers.remove(customer);
        cargos.stream().filter(cargo -> cargo.getOwner().equals(customer)).flatMap(cargo -> cargo.getHazards().stream()).toList().forEach(hazards::remove);
        cargos.removeIf(cargo -> cargo.getOwner().equals(customer));

        System.out.println("Customer deleted: " + name);
    }


    public void displayCustomers()
    {
        if (customers.isEmpty())
        {
            System.out.println("No Customers");
            return;
        }

        System.out.println("Customers:");
        for (Customer customer : customers)
        {
            System.out.println("- " + customer.getName() + " (" + getCargoItemCount(customer) + " cargo items)");
        }
    }

    private boolean isCustomerExists(String name)
    {
        return getCustomerByName(name) != null;
    }

    private Customer getCustomerByName(String name)
    {
        for (Customer customer : customers)
        {
            if (customer.getName().equals(name))
            {
                return customer;
            }
        }
        return null;
    }

    public int getCargoItemCount(Customer customer)
    {
        int count = 0;
        for (AbstractCargo cargo : cargos)
        {
            if (cargo.getOwner().equals(customer))
            {
                count++;
            }
        }
        return count;
    }

    // Caro Managment Methods
    public void insertCargoItem(CargoType cargoType, String customerName, BigDecimal value, List<Hazard> hazards,
                                boolean fragile, boolean pressurized, int grainSize)
    {
        Customer owner = getCustomerByName(customerName);
        if (owner == null)
        {
            System.out.println("Customer not found.");
            return;
        }

        AbstractCargo cargo = createCargoItem(cargoType, owner, value, hazards, fragile, pressurized, grainSize);
        if (cargo != null)
        {
            if (isCargoLocationAvailable(cargo))
            {
                cargos.add(cargo);
                notifyObservers(new CargoItemInsertedEvent(cargo));
                System.out.println("Cargo item inserted successfully.");
            } else
            {
                System.out.println("Unable to insert cargo item. Storage location not available.");
            }
        } else
        {
            System.out.println("Invalid cargo type.");
        }
    }

    private AbstractCargo createCargoItem(CargoType cargoType, Customer owner, BigDecimal value, List<Hazard> hazards,
                                          boolean fragile, boolean pressurized, int grainSize)
    {
        this.hazards.addAll(hazards);
        return switch (cargoType)
                {
                    case DryBulkAndUnitisedCargo ->
                            new DryBulkAndUnitisedCargoImpl(owner, value, hazards, generateStorageLocation(), grainSize, fragile);
                    case DryBulkCargo ->
                            new DryBulkCargoImpl(owner, value, hazards, generateStorageLocation(), grainSize);
                    case LiquidAndDryBulkCargo ->
                            new LiquidAndDryBulkCargoImpl(owner, value, hazards, generateStorageLocation(), pressurized, grainSize);
                    case LiquidBulkAndUnitisedCargo ->
                            new LiquidBulkAndUnitisedCargoImpl(owner, value, hazards, generateStorageLocation(), pressurized, fragile);
                    case LiquidBulkCargo ->
                            new LiquidBulkCargoImpl(owner, value, hazards, generateStorageLocation(), pressurized);
                    case UnitisedCargo ->
                            new UnitisedCargoImpl(owner, value, hazards, generateStorageLocation(), fragile);
                    default -> null;
                };
    }

    private int generateStorageLocation()
    {
        // Generate a unique storage location for each cargo item
        int location = 1; // Start with the first storage location

        // Find the next available storage location
        while (isLocationOccupied(location))
        {
            location++;
        }

        return location;
    }

    private boolean isCargoLocationAvailable(AbstractCargo cargo)
    {
        // Check if the storage location is available for the cargo item
        int storageLocation = cargo.getStorageLocation();

        // Check if any other cargo item already occupies the same location
        for (AbstractCargo existingCargo : cargos)
        {
            if (existingCargo.getStorageLocation() == storageLocation)
            {
                return false; // Location is occupied
            }
        }

        return true; // Location is available
    }

    private boolean isLocationOccupied(int location)
    {
        // Check if any cargo item already occupies the given location
        for (AbstractCargo existingCargo : cargos)
        {
            if (existingCargo.getStorageLocation() == location)
            {
                return true; // Location is occupied
            }
        }

        return false; // Location is available
    }


    public void removeCargoItem(int storageLocation)
    {
        AbstractCargo cargo = getCargoByLocation(storageLocation);
        if (cargo != null)
        {
            Customer customer = getCustomerByName(cargo.getOwner().getName());
            cargos.stream().filter(c -> c.getOwner().equals(customer)).flatMap(c -> c.getHazards().stream()).toList().forEach(hazards::remove);
            cargos.remove(cargo);
            System.out.println("Cargo item removed: " + storageLocation);
        } else
        {
            System.out.println("Cargo item not found.");
        }
    }

    private AbstractCargo getCargoByLocation(int storageLocation)
    {
        for (AbstractCargo cargo : cargos)
        {
            if (cargo.getStorageLocation() == storageLocation)
            {
                return cargo;
            }
        }
        return null;
    }

    public void displayCargoItems(String cargoType)
    {
        if (cargos.isEmpty())
        {
            System.out.println("No Cargo Items");
            return;
        }

        boolean filterByType = cargoType != null;

        System.out.println(filterByType ? "Cargo Items of Type " + cargoType + ":" : "All Cargo Items:");

        for (AbstractCargo cargo : cargos)
        {
            if (!filterByType || cargo.getClass().getSimpleName().replace("Impl", "").equalsIgnoreCase(cargoType))
            {
                System.out.println("- Location: " + cargo.getStorageLocation());
                System.out.println("- Inspection date: " + getFormattedDate(cargo.getLastInspectionDate()));
                System.out.println("- Storage duration: " + getFormattedDuration(cargo.getDurationOfStorage()));
            }
        }
    }

    private String getFormattedDate(Date date)
    {
        if (date == null)
        {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(date);
    }


    private String getFormattedDuration(Duration duration)
    {
        long totalSeconds = duration.getSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void displayHazards(boolean included)
    {
        if (included)
        {
            if (!hazards.isEmpty())
            {
                System.out.println("Present Hazards:");
                for (Hazard hazard : hazards)
                {
                    System.out.println("- " + hazard.name());
                }
            } else
            {
                System.out.println("No present hazards.");
            }
        } else
        {
            EnumSet<Hazard> absentHazards;

            if (hazards.isEmpty())
            {
                absentHazards = EnumSet.allOf(Hazard.class);
            } else
            {
                absentHazards = EnumSet.complementOf(EnumSet.copyOf(hazards));
            }

            if (!absentHazards.isEmpty())
            {
                System.out.println("Absent Hazards:");
                for (Hazard hazard : absentHazards)
                {
                    System.out.println("- " + hazard.name());
                }
            } else
            {
                System.out.println("No absent hazards.");
            }
        }
    }

    public double calculateCapacityUtilization()
    {
        int totalCargoItemCount = cargos.size();
        return (double) totalCargoItemCount / storageCapacity;
    }

    public void setInspectionDate(int storageLocation, Date inspectionDate)
    {
        AbstractCargo cargo = getCargoByLocation(storageLocation);
        if (cargo != null)
        {
            // Set the inspection date for the cargo item
            cargo.setLastInspectionDate(inspectionDate);
            System.out.println("Inspection date set for cargo item: " + storageLocation);
        } else
        {
            System.out.println("Cargo item not found.");
        }
    }

    public void saveJOS(String filename)
    {
        FileSystem io = new FileSystem();
        io.saveJOS(this, filename);
    }

    public void loadJOS(String filename)
    {
        FileSystem io = new FileSystem();
        WarehouseManagementSystem deserializedSystem = io.loadJOS(filename);
        if (deserializedSystem != null)
        {
            this.customers = deserializedSystem.getCustomers();
            this.cargos = deserializedSystem.getCargos();
            this.hazards = deserializedSystem.getHazards();
            this.storageCapacity = deserializedSystem.getStorageCapacity();
            System.out.println("Data loaded using JOS: " + filename);
        }
    }

    public void registerObserver(Observer observer)
    {
        observers.add(observer);
    }

    public void unregisterObserver(Observer observer)
    {
        observers.remove(observer);
    }

    private void notifyObservers(Event event)
    {
        for (Observer observer : observers)
        {
            observer.update(event);
        }
    }

}
