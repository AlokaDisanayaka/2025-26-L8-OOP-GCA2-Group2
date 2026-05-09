package org.DTO;

/**
 * @author Aloka (primary)
 */
public class Product {

    private int productId;
    private String productName;
    private double unitPrice;
    private int stockQty;
    private boolean isActive;

    //NEW FILE FIELDS FOR STAGE 3
    private String fileName;
    private String contentType;
    private int fileSize;
    private byte[] fileData;


    // Constructor used when reading from DB
    public Product(int productId, String productName, double unitPrice,
                   int stockQty, boolean isActive)
    {

        this.productId=productId;
        this.productName=productName;
        this.unitPrice=unitPrice;
        this.stockQty=stockQty;
        this.isActive=isActive;
    }
    public Product ()
    {

    }
    public Product(String productName, double unitPrice,
                   int stockQty, boolean isActive)
    {
          this.productId=0;
          this.productName=productName;
          this.unitPrice=unitPrice;
          this.stockQty=stockQty;
          this.isActive=isActive;
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


    //stage 3 binary schema integration

    public String getFileName()
    {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(int fileSize) {
        if (fileSize < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        this.fileSize = fileSize;
    }

    public byte[] getFileData() {
        return this.fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
