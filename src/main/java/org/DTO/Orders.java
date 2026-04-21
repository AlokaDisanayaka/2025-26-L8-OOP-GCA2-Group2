package org.DTO;

import java.time.LocalDateTime;

public class Orders
{
    public enum OrderStatus {NEW, PAID, SHIPPED, CANCELLED}

    private int orderId;
    private int customerId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    //Binary Schema Extension
    private byte[] fileData;
    private String fileName;
    private String contentType;
    private int fileSize;

    //Constructors
    public Orders(int orderId, int customerId, LocalDateTime orderDate, OrderStatus status)
    {
        setOrderId(orderId);
        setCustomerId(customerId);
        setOrderDate(orderDate);
        setStatus(status);
    }
    public Orders(int customerId, LocalDateTime orderDate, OrderStatus status)
    {
        setCustomerId(customerId);
        setOrderDate(orderDate);
        setStatus(status);
    }

    public Orders(int orderId, int customerId, LocalDateTime orderDate, OrderStatus status,
                  byte[] fileData, String fileName, String contentType, int fileSize)
    {
        setOrderId(orderId);
        setCustomerId(customerId);
        setOrderDate(orderDate);
        setStatus(status);
        setFileData(fileData);
        setFileName(fileName);
        setContentType(contentType);
        setFileSize(fileSize);
    }

    public Orders()
    {
    }

    //Getters and Setters
    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        if (orderId < 0)
        {
            throw new IllegalArgumentException("OrderId must be greater than 0");
        }
        this.orderId = orderId;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(int customerId)
    {
        if (customerId <= 0)
        {
            throw new IllegalArgumentException("CustomerId must be greater than 0");
        }
        this.customerId = customerId;
    }

    public LocalDateTime getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate)
    {
        if (orderDate == null)
        {
            throw new IllegalArgumentException("Order date must be greater than 0");
        }
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus()
    {
        return status;
    }

    public void setStatus(OrderStatus status)
    {
        if (status == null)
        {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    //Binary Schema Extension
    public byte[] getFileData()
    {
        return fileData;
    }

    public void setFileData(byte[] fileData)
    {
        this.fileData = fileData; //allow null
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = (fileName == null) ? "" : fileName.trim();
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = (contentType == null) ? "" : contentType.trim();
    }

    public int getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(int fileSize)
    {
        this.fileSize = Math.max(0, fileSize);
    }
}

