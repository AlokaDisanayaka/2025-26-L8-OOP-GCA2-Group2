package org.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Interface.ObjectMapperProvider;
import org.Interface.ClientRequest;
import org.Interface.ServerResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

 // Handles one connected client and runs on its own thread.

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final ObjectMapper mapper;

    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        this.mapper = ObjectMapperProvider.getMapper();
    }

    @Override
    public void run() {

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        )
        {

            // Read one line of JSON sent by the client
            String requestJson = in.readLine();

            if (requestJson == null || requestJson.trim().isEmpty()) {
                ServerResponse<String> response =
                        ServerResponse.error("Empty request received");

                out.println(mapper.writeValueAsString(response));
                return;
            }

            // Convert JSON text into a RequestMessage object
            ClientRequest request = mapper.readValue(requestJson, ClientRequest.class);

            // confirm that the server received the request
            String message = "Request received successfully. Action = " +
                    request.getAction() + ", Entity = " + request.getEntity();

            ServerResponse<String> response =
                    ServerResponse.success(message, null);

            // Send JSON response back to client
            out.println(mapper.writeValueAsString(response));

        }
        catch (Exception e)
        {
            // always return a structured error response
            try
            {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                ServerResponse<String> errorResponse =
                        ServerResponse.error("Server failed to process request");

                out.println(mapper.writeValueAsString(errorResponse));

            }
            catch (IOException ioException)
            {
                System.out.println("Error sending error response: " + ioException.getMessage());
            }

            System.out.println("Client handler error: " + e.getMessage());
            e.printStackTrace();

        }
        finally
        {
            // always close the socket when finished..
            try
            {
                clientSocket.close();
                System.out.println("Client disconnected");
            }
            catch (IOException e)
            {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
