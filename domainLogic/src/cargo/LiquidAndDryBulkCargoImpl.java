package cargo;

import administration.Customer;

import java.math.BigDecimal;
import java.util.Collection;

public class LiquidAndDryBulkCargoImpl extends AbstractCargo implements LiquidAndDryBulkCargo
{
    private boolean pressurized;
    private int grainSize;

    public LiquidAndDryBulkCargoImpl(Customer owner, BigDecimal value, Collection<Hazard> hazards, int storageLocation, boolean pressurized, int grainSize)
    {
        super(owner, value, hazards, storageLocation);
        this.pressurized = pressurized;
        this.grainSize = grainSize;
    }

    @Override
    public boolean isPressurized()
    {
        return pressurized;
    }

    @Override
    public int getGrainSize()
    {
        return grainSize;
    }

    @Override
    public String toString()
    {
        return super.toString() +
                "LiquidAndDryBulkCargo{" +
                "pressurized=" + pressurized +
                ", grainSize=" + grainSize +
                '}';
    }
}
