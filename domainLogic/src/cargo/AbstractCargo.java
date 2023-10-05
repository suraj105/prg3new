package cargo;

import administration.Customer;
import administration.Storable;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

public abstract class AbstractCargo implements Cargo, Storable
{
    private BigDecimal value;
    private Collection<Hazard> hazards;
    private Customer owner;
    private Instant insertionDate;
    private Date lastInspectionDate;
    private int storageLocation;

    public AbstractCargo()
    {
    }

    public AbstractCargo(Customer owner, BigDecimal value, Collection<Hazard> hazards, int storageLocation)
    {
        this.owner = owner;
        this.value = value;
        this.hazards = hazards;
        this.insertionDate = Instant.now();
        this.storageLocation = storageLocation;
    }

    public void setValue(BigDecimal value)
    {
        this.value = value;
    }

    public void setHazards(Collection<Hazard> hazards)
    {
        this.hazards = hazards;
    }

    public void setOwner(Customer owner)
    {
        this.owner = owner;
    }

    public Instant getInsertionDate()
    {
        return insertionDate;
    }

    public void setInsertionDate(Instant insertionDate)
    {
        this.insertionDate = insertionDate;
    }

    public void setStorageLocation(int storageLocation)
    {
        this.storageLocation = storageLocation;
    }

    @Override
    public Customer getOwner()
    {
        return owner;
    }

    @Override
    public BigDecimal getValue()
    {
        return value;
    }

    @Override
    public Collection<Hazard> getHazards()
    {
        return hazards;
    }

    @Override
    public Duration getDurationOfStorage()
    {
        if (insertionDate == null)
        {
            return null;
        }
        Instant currentInstant = Instant.now();
        return Duration.between(insertionDate, currentInstant);
    }

    @Override
    public Date getLastInspectionDate()
    {
        return lastInspectionDate;
    }

    public void setLastInspectionDate(Date lastInspectionDate)
    {
        this.lastInspectionDate = lastInspectionDate;
    }

    @Override
    public int getStorageLocation()
    {
        return storageLocation;
    }

    @Override
    public String toString()
    {
        return "Cargo{" +
                "value=" + value +
                ", hazards=" + hazards +
                ", owner=" + owner.getName() +
                ", insertionDate=" + insertionDate +
                ", lastInspectionDate=" + lastInspectionDate +
                ", storageLocation=" + storageLocation +
                ", Cargo Type=";
    }
}
