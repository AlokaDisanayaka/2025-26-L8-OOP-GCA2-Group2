package org.GenericServices;

import org.DAO_JDBC.JdbcProductDao;
import org.DTO.Product;
import org.Interface.ClientRequest;

import java.util.List;

public class ProductReportService implements GenericService {

    private JdbcProductDao productDao;

    public ProductReportService()
    {
        productDao = new JdbcProductDao();
    }

    @Override
    public boolean validate(ClientRequest request)
    {
        return true;
    }

    @Override
    public Object execute(ClientRequest request)
    {
        List<Product> products = productDao.getAllProducts();

        int totalProducts = products.size();
        int activeProducts = 0;
        int lowStockProducts = 0;

        for (int i = 0; i < products.size(); i++)
        {
            Product p = products.get(i);

            if (p.isActive())
            {
                activeProducts++;
            }

            if (p.getStockQty() < 10)
            {
                lowStockProducts++;
            }
        }

        return "Total products: " + totalProducts +
                ", Active products: " + activeProducts +
                ", Low stock products: " + lowStockProducts;
    }

    @Override
    public String getSuccessMessage()
    {
        return "Product report generated successfully";
    }
}