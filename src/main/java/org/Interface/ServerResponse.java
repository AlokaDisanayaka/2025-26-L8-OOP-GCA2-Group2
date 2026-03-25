package org.Interface;

/**
 * Generic wrapper used for all the server replies.
 * this contains,
 * status  -> success or error
 * message -> a readable explanation
 * data    -> the actual object or list returned
 **/
public class ServerResponse<T> {

    private String status;
    private String message;
    private T data;


    //No-argument constructor for Jackson
    public ServerResponse()
    {}


    //Full constructor

    public ServerResponse(String status, String message, T data)
    {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    //  method for success responses

    public static <T> ServerResponse<T> success(String message, T data)
    {
        return new ServerResponse<>("success", message, data);
    }


    // method for error responses- failure:

    public static <T> ServerResponse<T> error(String message)
    {
        return new ServerResponse<>("error", message, null);
    }
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}

