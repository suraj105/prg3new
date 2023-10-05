package main;

import cli.AlternativeCommandLineInterface;
import management.WarehouseManagementSystem;

import java.util.Scanner;

public class AlternativeCLI
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter storage capacity: ");
        int capacity = scanner.nextInt();

        WarehouseManagementSystem system = new WarehouseManagementSystem(capacity);
        AlternativeCommandLineInterface alternativeCommandLineInterface = new AlternativeCommandLineInterface(system);
        alternativeCommandLineInterface.start();
    }
}
