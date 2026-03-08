package org.DTO;

public class OrderItem
{
    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;
    private double unitPriceAtTime;

    //Constructors
    public OrderItem(int orderItemId, int orderId, int productId, int quantity, double unitPriceAtTime)
    {
        setOrderItemId(orderItemId);
        setOrderId(orderId);
        setProductId(productId);
        setQuantity(quantity);
        setUnitPriceAtTime(unitPriceAtTime);
    }
    public OrderItem(int orderId, int productId, int quantity, double unitPriceAtTime)
    {
        setOrderId(orderId);
        setProductId(productId);
        setQuantity(quantity);
        setUnitPriceAtTime(unitPriceAtTime);
    }
    public OrderItem()
    {
    }

    //Getters and Setters
    public int getOrderItemId()
    {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId)
    {
        if (orderItemId < 0)
        {
            throw new IllegalArgumentException("OrderItemId must be greater than 0");
        }
        this.orderItemId = orderItemId;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        if (orderId <= 0)
        {
            throw new IllegalArgumentException("OrderId must be greater than 0");
        }
        this.orderId = orderId;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        if (productId <= 0)
        {
            throw new IllegalArgumentException("ProductId must be greater than 0");
        }
        this.productId = productId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        if (quantity <= 0)
        {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
    }

    public double getUnitPriceAtTime()
    {
        return unitPriceAtTime;
    }

    public void setUnitPriceAtTime(double unitPriceAtTime)
    {
        if (unitPriceAtTime < 0)
        {
            throw new IllegalArgumentException("UnitPriceAtTime must be 0 or greater");
        }
        this.unitPriceAtTime = unitPriceAtTime;
    }
}

