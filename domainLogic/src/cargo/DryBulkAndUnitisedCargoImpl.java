package cargo;

import administration.Customer;

import java.math.BigDecimal;
import java.util.Collection;

public class DryBulkAndUnitisedCargoImpl extends AbstractCargo implements DryBulkAndUnitisedCargo
{
    private int grainSize;
    private boolean fragile;

    public DryBulkAndUnitisedCargoImpl(Customer owner, BigDecimal value, Collection<Hazard> hazards, int storageLocation, int grainSize, boolean fragile)
    {
        super(owner, value, hazards, storageLocation);
        this.grainSize = grainSize;
        this.fragile = fragile;
    }

    @Override
    public int getGrainSize()
    {
        return grainSize;
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
                "DryBulkAndUnitisedCargo{" +
                "grainSize=" + grainSize +
                ", fragile=" + fragile +
                '}';
    }
}
