package gui.model;

import administration.CustomerImpl;

public class CustomerModel extends CustomerImpl
{
    private int itemsCount;

    public CustomerModel(String name, int itemsCount)
    {
        super(name);
        this.itemsCount = itemsCount;
    }

    public int getItemsCount()
    {
        return itemsCount;
    }
}
