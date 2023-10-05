package simulations.one;

import cargo.AbstractCargo;
import cargo.CargoType;
import cargo.Hazard;
import management.WarehouseManagementSystem;
import simulations.AbstractCargoProducer;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class CargoProducer extends AbstractCargoProducer
{
    public CargoProducer(WarehouseManagementSystem system, BlockingQueue<AbstractCargo> cargoQueue)
    {
        super(system, cargoQueue);
    }

    @Override
    protected void produceAndAddCargo()
    {
        AbstractCargo cargo = generateRandomCargo();
        try
        {
            assert cargo != null;
            cargoQueue.put(cargo);
            System.out.println("Cargo produced: " + cargo);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private AbstractCargo generateRandomCargo()
    {
        synchronized (cargoQueue)
        {
            CargoType cargoType = generateRandomCargoType();
            String customer = generateRandomCustomer();
            BigDecimal value = getRandomValue();
            List<Hazard> hazards = getRandomHazards();
            boolean fragile = getRandomBoolean(), pressurized = getRandomBoolean();
            int grainSize = getRandomGrainSize();

            system.createCustomer(customer);
            system.insertCargoItem(cargoType, customer, value, hazards, fragile, pressurized, grainSize);

            List<AbstractCargo> cargoList = system.getCargos();
            CopyOnWriteArrayList<AbstractCargo> list = new CopyOnWriteArrayList<>(cargoList);

            for (AbstractCargo cargo : list)
            {
                assert cargo != null;
                if ((cargoType.getCargoType().equals(cargo.getClass().getSimpleName().replace("Impl", "")))
                        && (customer.equals(cargo.getOwner().getName()))
                        && (value.compareTo(cargo.getValue()) == 0)
                        && (hazards.equals(cargo.getHazards())))
                {
                    return cargo;
                }
            }
            return null;
        }
    }
}
