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

        try (
                Socket socket = new Socket("localhost", 5000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                )
        )
        {

            ObjectMapper mapper = new ObjectMapper();

            // Create a simple request
            ClientRequest request = new ClientRequest(
                    "getAll",
                    "product",
                    null,
                    null
            );

            // Convert request object to JSON
            String requestJson = mapper.writeValueAsString(request);

            // Send request to server
            out.println(requestJson);

            // Read server response
            String responseJson = in.readLine();

            // Print raw JSON response
            System.out.println("Response from server:");
            System.out.println(responseJson);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
