package network.tcp;

import java.io.*;
import java.net.*;

public class TCPClient
{
    private PrintWriter outToServer;
    private BufferedReader inFromUser;

    public TCPClient()
    {
        try (Socket clientSocket = new Socket("localhost", 8080))
        {
            System.out.println("Connected to server: " + clientSocket.getInetAddress());

            this.outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
            this.inFromUser = new BufferedReader(new InputStreamReader(System.in));

            this.start();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void start() throws IOException
    {
        //noinspection InfiniteLoopStatement
        while (true)
        {
            String clientText = inFromUser.readLine().trim() + "\n";
            this.outToServer.print(clientText);
            this.outToServer.flush();
        }
    }
}
