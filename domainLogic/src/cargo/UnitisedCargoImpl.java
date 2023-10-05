package cargo;

import administration.Customer;

import java.math.BigDecimal;
import java.util.Collection;

public class UnitisedCargoImpl extends AbstractCargo implements UnitisedCargo
{
    private boolean fragile;

    public UnitisedCargoImpl(Customer owner, BigDecimal value, Collection<Hazard> hazards, int storageLocation, boolean fragile)
    {
        super(owner, value, hazards, storageLocation);
        this.fragile = fragile;
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
                "UnitisedCargo{" +
                "fragile=" + fragile +
                '}';
    }
}
