package org.DTO;

public class OrderItem
{
    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;
    private double unitPriceAtTime;
    //Binary Schema Extension
    private byte[] fileData;         
    private String fileName;
    private String contentType;
    private int fileSize;

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

    //Binary Schema Extension
    public OrderItem(int orderItemId, int orderId, int productId, int quantity, double unitPriceAtTime,
                     byte[] fileData, String fileName, String contentType, int fileSize)
    {
        setOrderItemId(orderItemId);
        setOrderId(orderId);
        setProductId(productId);
        setQuantity(quantity);
        setUnitPriceAtTime(unitPriceAtTime);
        setFileData(fileData);
        setFileName(fileName);
        setContentType(contentType);
        setFileSize(fileSize);
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
        if (orderItemId <= 0)
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

    //Binary Schema Extension
    public byte[] getFileData()
    {
        return fileData;
    }

    public void setFileData(byte[] fileData)
    {
        if (fileData == null || fileData.length == 0)
        {
            throw new IllegalArgumentException("File data cannot be null or empty");
        }
        this.fileData = fileData;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        if (fileName == null || fileName.trim().isEmpty())
        {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        this.fileName = fileName;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        if (contentType == null || contentType.trim().isEmpty())
        {
            throw new IllegalArgumentException("Content type cannot be null or empty");
        }
        this.contentType = contentType;
    }

    public int getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(int fileSize)
    {
        if (fileSize < 0)
        {
            throw new IllegalArgumentException("File size must be 0 or greater");
        }
        this.fileSize = fileSize;
    }
}

