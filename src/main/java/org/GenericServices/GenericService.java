package org.GenericServices;

import org.Interface.ClientRequest;

public interface GenericService {

    // Checks whether the request has valid data
    boolean validate(ClientRequest request);

    // Executes the service and returns the result as an object
    Object execute(ClientRequest request);

    // Returns a readable message
    String getSuccessMessage();
}