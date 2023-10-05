package administration;

import java.io.Serial;
import java.io.Serializable;

public class CustomerImpl implements Customer, Serializable
{
    @Serial
    private static final long serialVersionUID = 123456789L;
    private String name;

    public CustomerImpl(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
