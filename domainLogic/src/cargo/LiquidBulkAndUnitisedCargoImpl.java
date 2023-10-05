package cargo;

import administration.Customer;

import java.math.BigDecimal;
import java.util.Collection;

public class LiquidBulkAndUnitisedCargoImpl extends AbstractCargo implements LiquidBulkAndUnitisedCargo
{
    private boolean pressurized;
    private boolean fragile;

    public LiquidBulkAndUnitisedCargoImpl(Customer owner, BigDecimal value, Collection<Hazard> hazards, int storageLocation, boolean pressurized, boolean fragile)
    {
        super(owner, value, hazards, storageLocation);
        this.pressurized = pressurized;
        this.fragile = fragile;
    }

    @Override
    public boolean isPressurized()
    {
        return pressurized;
    }

    @Override
    public boolean isFragile()
    {
        return fragile;
    }

    @Override
    public String toString()
    {
        return super.toString() +
                "LiquidBulkAndUnitisedCargo{" +
                "pressurized=" + pressurized +
                ", fragile=" + fragile +
                '}';
    }
}
