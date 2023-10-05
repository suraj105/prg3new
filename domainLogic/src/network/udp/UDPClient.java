package network.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient
{
    public UDPClient()
    {
        try (DatagramSocket clientSocket = new DatagramSocket())
        {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 8080;

            byte[] sendData;

            //noinspection InfiniteLoopStatement
            while (true)
            {
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                String clientText = inFromUser.readLine().trim() + "\n";
                sendData = clientText.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                clientSocket.send(sendPacket);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
