package org.DTO;

public class Product {

    private int productId;
    private String productName;
    private double unitPrice;
    private int stockQty;
    private boolean isActive;

    // Constructor used when reading from DB
    public Product(int productId, String productName, double unitPrice,
                   int stockQty, boolean isActive)
    {

        setProductId(productId);
        setProductName(productName);
        setUnitPrice(unitPrice);
        setStockQty(stockQty);
        setActive(isActive);
    }
    public Product(String productName, double unitPrice,
                   int stockQty, boolean isActive)
    {

        this(0, productName, unitPrice, stockQty, isActive);
    }

    // Getters and Setters with simple validation

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        if (productId < 0) {
            throw new IllegalArgumentException("Product ID cannot be negative");
        }
        this.productId = productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        this.productName = productName.trim();
    }

    public double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice)
    {
        if (unitPrice < 0)
        {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitPrice = unitPrice;
    }

    public int getStockQty()
    {
        return stockQty;
    }

    public void setStockQty(int stockQty)
    {
        if (stockQty < 0)
        {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.stockQty = stockQty;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }
}