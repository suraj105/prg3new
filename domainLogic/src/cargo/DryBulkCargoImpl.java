package cargo;

import administration.Customer;

import java.math.BigDecimal;
import java.util.Collection;

public class DryBulkCargoImpl extends AbstractCargo implements DryBulkCargo
{
    private int grainSize;

    public DryBulkCargoImpl(Customer owner, BigDecimal value, Collection<Hazard> hazards, int storageLocation, int grainSize)
    {
        super(owner, value, hazards, storageLocation);
        this.grainSize = grainSize;
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
                "DryBulkCargo{" +
                "grainSize=" + grainSize +
                '}';
    }
}
