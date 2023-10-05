package io;

import management.WarehouseManagementSystem;

import java.io.*;

public class FileSystem
{
    public void saveJOS(WarehouseManagementSystem system, String filename)
    {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("domainLogic/src/persistence/" + filename + ".jos")))
        {
            outputStream.writeObject(system);
            System.out.println("Data saved using JOS: " + filename);
        } catch (IOException e)
        {
            System.out.println("Error saving data using JOS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public WarehouseManagementSystem loadJOS(String filename)
    {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("domainLogic/src/persistence/" + filename + ".jos")))
        {
            return (WarehouseManagementSystem) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            System.out.println("Error loading data using JOS: " + e.getMessage());
        }
        return null;
    }
}
