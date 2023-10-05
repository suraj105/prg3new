package cargo;

public enum CargoType
{
    DryBulkAndUnitisedCargo("DryBulkAndUnitisedCargo"),
    DryBulkCargo("DryBulkCargo"),
    LiquidAndDryBulkCargo("LiquidAndDryBulkCargo"),
    LiquidBulkAndUnitisedCargo("LiquidBulkAndUnitisedCargo"),
    LiquidBulkCargo("LiquidBulkCargo"),
    UnitisedCargo("UnitisedCargo");

    private final String cargoType;

    CargoType(String cargoType)
    {
        this.cargoType = cargoType;
    }

    public String getCargoType()
    {
        return cargoType;
    }

    private static boolean isValidCargoType(String cargoType)
    {
        for (CargoType type : CargoType.values())
        {
            if (type.name().equalsIgnoreCase(cargoType))
            {
                return true;
            }
        }
        return false;
    }

    static
    {
        for (CargoType type : values())
        {
            if (!isValidCargoType(type.name()))
            {
                throw new IllegalArgumentException("Invalid cargo type: " + type.name());
            }
        }
    }
}
