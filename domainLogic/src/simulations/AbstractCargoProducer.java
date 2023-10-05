package simulations;

import cargo.AbstractCargo;
import cargo.CargoType;
import cargo.Hazard;
import management.WarehouseManagementSystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public abstract class AbstractCargoProducer implements Runnable
{
    protected final WarehouseManagementSystem system;
    protected final BlockingQueue<AbstractCargo> cargoQueue;
    protected final Random random;

    public AbstractCargoProducer(WarehouseManagementSystem system, BlockingQueue<AbstractCargo> cargoQueue)
    {
        this.system = system;
        this.cargoQueue = cargoQueue;
        this.random = new Random();
    }

    @Override
    public void run()
    {
        //noinspection InfiniteLoopStatement
        while (true)
        {
            synchronized (cargoQueue)
            {
                produceAndAddCargo();
            }
        }
    }

    protected abstract void produceAndAddCargo();

    protected CargoType generateRandomCargoType()
    {
        CargoType[] cargoTypes = CargoType.class.getEnumConstants();
        int index = random.nextInt(cargoTypes.length);
        return cargoTypes[index];
    }

    protected String generateRandomCustomer()
    {
        ArrayList<String> names = new ArrayList<>(Arrays.asList(
                "Emma", "Noah", "Mia", "Ben", "Hannah", "Leon", "Emilia", "Elias", "Sofia", "Lukas",
                "Marie", "Paul", "Anna", "Finn", "Laura", "Jonas", "Lena", "Felix", "Lina", "Max",
                "Clara", "David", "Mia", "Samuel", "Sophie", "Jan", "Emily", "Tim", "Leonie", "Luca",
                "Amelie", "Joshua", "Johanna", "Leon", "Ella", "Anton", "Lara", "Moritz", "Charlotte",
                "Julius", "Victoria", "Louis", "Luisa", "Oskar", "Pauline", "Philip", "Sarah", "Jakob",
                "Johanna", "Simon", "Annika", "Henry", "Katharina", "Fabian", "Nina", "Theodor",
                "Isabella", "Emil", "Isabelle", "Noah", "Leonie", "Milan", "Sophia", "Linus", "Helena",
                "Maximilian", "Fiona", "Adrian", "Paula", "Johann", "Marie", "Tobias", "Mira", "Florian",
                "Melina", "Valentin", "Vanessa", "Vincent", "Leonie", "Julius", "Mathilda", "Patrick",
                "Emely", "Daniel", "Romy", "Eric", "Elisa", "Lukas", "Zoe", "Sebastian", "Lea", "Niklas",
                "Jasmin", "Tobias", "Celina", "Matteo", "Johanna", "Felix", "Selina", "Julian"

                // List of customer names
        ));
        int index = random.nextInt(names.size());
        return names.get(index);
    }

    protected BigDecimal getRandomValue()
    {
        BigDecimal minValue = BigDecimal.ONE;
        BigDecimal maxValue = BigDecimal.valueOf(100000);
        return BigDecimal.valueOf(random.nextDouble())
                .multiply(maxValue.subtract(minValue))
                .add(minValue)
                .setScale(2, RoundingMode.HALF_UP);
    }

    protected List<Hazard> getRandomHazards()
    {
        int choice = random.nextInt(2);

        if (choice == 0)
        {
            return Collections.emptyList();
        } else
        {
            Hazard[] hazards = Hazard.values();
            int n = random.nextInt(hazards.length);

            List<Hazard> toReturn = new ArrayList<>();
            for (int i = 0; i < n; i++)
            {
                int index = random.nextInt(hazards.length);
                toReturn.add(hazards[index]);
            }

            return toReturn;
        }
    }

    protected boolean getRandomBoolean()
    {
        return random.nextBoolean();
    }

    protected int getRandomGrainSize()
    {
        return random.nextInt(100);
    }
}
