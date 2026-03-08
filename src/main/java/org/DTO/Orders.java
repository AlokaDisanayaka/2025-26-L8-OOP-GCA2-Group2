package org.DTO;

import java.time.LocalDateTime;

public class Orders
{
    public enum OrderStatus {NEW, PAID, SHIPPED, CANCELLED}

    private int orderId;
    private int customerId;
    private LocalDateTime orderDate;
    private OrderStatus status;

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
}

