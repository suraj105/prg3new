package administration;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class StorableImpl implements Storable
{
    private Customer owner;
    private Instant insertionDate;
    private Date lastInspectionDate;
    private int storageLocation;

    public StorableImpl(Customer owner, int storageLocation)
    {
        this.owner = owner;
        this.insertionDate = Instant.now();
        this.storageLocation = storageLocation;
    }

    @Override
    public Customer getOwner()
    {
        return owner;
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
}
