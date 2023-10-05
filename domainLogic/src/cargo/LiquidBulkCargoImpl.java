package cargo;

import administration.Customer;

import java.math.BigDecimal;
import java.util.Collection;

public class LiquidBulkCargoImpl extends AbstractCargo implements LiquidBulkCargo
{
    private boolean pressurized;

    public LiquidBulkCargoImpl(Customer owner, BigDecimal value, Collection<Hazard> hazards, int storageLocation, boolean pressurized)
    {
        super(owner, value, hazards, storageLocation);
        this.pressurized = pressurized;
    }

    @Override
    public boolean isPressurized()
    {
        return pressurized;
    }

    @Override
    public String toString()
    {
        return super.toString() +
                "LiquidBulkCargo{" +
                "pressurized=" + pressurized +
                '}';
    }
}
