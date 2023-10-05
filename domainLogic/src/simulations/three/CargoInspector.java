package simulations.three;

import cargo.AbstractCargo;
import management.WarehouseManagementSystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CargoInspector implements Runnable
{
    private final WarehouseManagementSystem system;
    private final Random random;

    public CargoInspector(WarehouseManagementSystem system)
    {
        this.system = system;
        this.random = new Random();
    }

    @Override
    public void run()
    {
        //noinspection InfiniteLoopStatement
        while (true)
        {
            List<AbstractCargo> cargoList = system.getCargos();
            if (!cargoList.isEmpty())
            {
                AbstractCargo randomCargo = cargoList.get(random.nextInt(cargoList.size()));
                triggerInspection(randomCargo);
            }

            try
            {
                TimeUnit.SECONDS.sleep(5); // Wait for 5 seconds before triggering the next inspection
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void triggerInspection(AbstractCargo cargo)
    {
        int location = cargo.getStorageLocation();
        Date date = generateRandomDate();
        system.setInspectionDate(location, date);
        System.out.println("Inspection triggered for cargo: " + cargo);
    }

    public static Date generateRandomDate()
    {
        LocalDate today = LocalDate.now();
        Random random = new Random();

        int year = random.nextInt(today.getYear() - 2000 + 1) + 2000; // Assuming minimum year is 2000
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1; // Assuming maximum of 28 days in a month

        try
        {
            LocalDate randomDate = LocalDate.of(year, month, day);
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            return format.parse(randomDate.toString());
        } catch (ParseException e)
        {
            return null;
        }
    }
}
