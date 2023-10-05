package cli;

import management.WarehouseManagementSystem;
import management.observers.CapacityExceededObserver;
import management.observers.HazardsChangedObserver;
import network.tcp.TCPClient;
import network.udp.UDPClient;
import network.tcp.TCPServer;
import network.udp.UDPServer;

public class CommandLineInterface
{
    private final WarehouseManagementSystem system;

    public CommandLineInterface(WarehouseManagementSystem system)
    {
        this.system = system;
    }

    public void start(String protocol)
    {
        if (protocol.equals("TCP") || protocol.equals("UDP"))
        {
            CapacityExceededObserver capacityObserver = new CapacityExceededObserver(system);
            HazardsChangedObserver hazardsObserver = new HazardsChangedObserver(system);
            system.registerObserver(capacityObserver);
            system.registerObserver(hazardsObserver);
        }

        switch (protocol)
        {
            case "TCP" -> startAsTCP();
            case "UDP" -> startAsUDP();
            default -> System.out.println("Invalid protocol. Please specify TCP or UDP.");
        }
    }

    private void startAsTCP()
    {
        Thread serverThread = new Thread(() ->
        {
            new TCPServer(system);
        });

        Thread clientThread = new Thread(TCPClient::new);

        serverThread.start();
        clientThread.start();
    }

    private void startAsUDP()
    {
        Thread serverThread = new Thread(() ->
        {
            new UDPServer(system);
        });

        Thread clientThread = new Thread(UDPClient::new);

        serverThread.start();
        clientThread.start();
    }
}

