package org.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//main server class
/**
 * Stage 2=>
 * - Uses ExecutorService
 * - Each client is handled on its own thread
 */
public class ServerMain {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        // Create a thread pool for handling multiple clients
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT))
        {

            System.out.println("Server listening on port " + PORT);

            // Keep server running forever
            while (true)
            {

                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();

                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Give this client to a separate thread
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                executorService.submit(clientHandler);
            }

        }
        catch (IOException e)
        {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}