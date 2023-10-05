package management.events;

import cargo.AbstractCargo;

public class CargoItemInsertedEvent implements Event
{
    private AbstractCargo cargoItem;

    public CargoItemInsertedEvent(AbstractCargo cargoItem)
    {
        this.cargoItem = cargoItem;
    }

    public AbstractCargo getCargoItem()
    {
        return cargoItem;
    }
}
