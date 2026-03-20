package org.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Interface.ClientRequest;
import org.Interface.ServerResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//testing client
public class ClientMain {

    public static void main(String[] args) {

        // Test different requests one by one
        sendRequest(new ClientRequest("getAll", "customer", null, null));
        sendRequest(new ClientRequest("getById", "customer", 1, null));

        sendRequest(new ClientRequest("getAll", "product", null, null));
        sendRequest(new ClientRequest("getById", "product", 1, null));
    }

    /**
     * the method sendRequest=>
     * Opens a connection to the server,
     * sends one request,
     * prints the response,
     * then closes the connection.
     * .....................................
     */
    public static void sendRequest(ClientRequest request) {

        try (
                Socket socket = new Socket("localhost", 5000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                )
        )
        {
            ObjectMapper mapper = new ObjectMapper();

            // converting  the request object into JSON
            String requestJson = mapper.writeValueAsString(request);

            System.out.println("\nSending Request:");
            System.out.println(requestJson);

            // Send the JSON request to the server
            out.println(requestJson);

            // read the JSON response from the server
            String responseJson = in.readLine();

            System.out.println("\nResponse from the Server:");
            System.out.println(responseJson);

        }
        catch (Exception e)
        {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}