package cargo;

import administration.Storable;

public interface LiquidBulkCargo extends Cargo, Storable
{
    boolean isPressurized();
}
