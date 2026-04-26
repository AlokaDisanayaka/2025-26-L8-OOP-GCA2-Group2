package org.DAO_JDBC;

import org.DAO.ProductDao;
import org.DB.DBConnection;
import org.DTO.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * JDBC implementation of ProductDao
 */
public class JdbcProductDao implements ProductDao {

    @Override
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT product_id, product_name, unit_price, stock_qty, is_active FROM Product";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("unit_price"),
                        rs.getInt("stock_qty"),
                        rs.getBoolean("is_active")
                );

                products.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while retrieving products", e);
        }

        return products;
    }

    @Override
    public Optional<Product> getProductById(int id) {

        String sql = "SELECT product_id, product_name, unit_price, stock_qty, is_active FROM Product WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("unit_price"),
                            rs.getInt("stock_qty"),
                            rs.getBoolean("is_active")
                    );

                    return Optional.of(product);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while retrieving product by ID", e);
        }

        return Optional.empty();   // never return null (Stage 1 requirement)
    }

    @Override
    public Product insertProduct(Product product) {

        String sql = "INSERT INTO Product (product_name, unit_price, stock_qty, is_active) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getProductName());
            ps.setDouble(2, product.getUnitPrice());
            ps.setInt(3, product.getStockQty());
            ps.setBoolean(4, product.isActive());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);

                    return new Product(
                            newId,
                            product.getProductName(),
                            product.getUnitPrice(),
                            product.getStockQty(),
                            product.isActive()
                    );
                }
            }

            throw new RuntimeException("Insert successful but no ID returned");

        } catch (SQLException e) {
            throw new RuntimeException("Database error while inserting product", e);
        }
    }

    @Override
    public Product updateProduct(int id, Product product) {

        String sql = "UPDATE Product SET product_name = ?, unit_price = ?, stock_qty = ?, is_active = ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getProductName());
            ps.setDouble(2, product.getUnitPrice());
            ps.setInt(3, product.getStockQty());
            ps.setBoolean(4, product.isActive());
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("No product found with ID " + id);
            }

            return new Product(
                    id,
                    product.getProductName(),
                    product.getUnitPrice(),
                    product.getStockQty(),
                    product.isActive()
            );

        } catch (SQLException e) {
            throw new RuntimeException("Database error while updating product", e);
        }
    }

    @Override
    public boolean deleteProductById(int id) {

        String sql = "DELETE FROM Product WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting product", e);
        }
    }


    @Override
    public List<Product> findProductsByFilter(Predicate<Product> filter) {

        // Create a list to hold matching products..
        List<Product> matchingProducts = new ArrayList<>();

        // Get all products first
        List<Product> allProducts = getAllProducts();

        // Loop through all products
        for (int i=0;i< allProducts.size();i++)
        {
            Product product=allProducts.get(i);
            // If the product matches the filter, add it to the result list
            if (filter.test(product))
            {
                matchingProducts.add(product);
            }
        }

        return matchingProducts;
    }
    @Override
    public Product uploadProductFile(int id, Product product)
    {

        // Store file metadata and binary data for one product
        String sql = "UPDATE Product SET file_name = ?, content_type = ?, file_size = ?, file_data = ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setString(1, product.getFileName());
            ps.setString(2, product.getContentType());
            ps.setInt(3, product.getFileSize());

            // Store actual binary file
            ps.setBytes(4, product.getFileData());

            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows == 0)
            {
                throw new RuntimeException("No product found with id " + id);
            }

            product.setProductId(id);
            return product;

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error while uploading product file", e);
        }
    }

    @Override
    public Optional<Product> getProductFileById(int id)
    {

        // this query fetches the BLOB because we are downloading the file
        String sql = "SELECT product_id, product_name, unit_price, stock_qty, is_active, file_name, content_type, file_size, file_data FROM Product WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery())
            {

                if (rs.next())
                {

                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("unit_price"),
                            rs.getInt("stock_qty"),
                            rs.getBoolean("is_active")
                    );

                    product.setFileName(rs.getString("file_name"));
                    product.setContentType(rs.getString("content_type"));
                    product.setFileSize(rs.getInt("file_size"));
                    product.setFileData(rs.getBytes("file_data"));

                    return Optional.of(product);
                }
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error while getting product file", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Product> getProductFileMetadataById(int id)
    {

        // file_data is NOT selected here
        String sql = "SELECT product_id, product_name, unit_price, stock_qty, is_active, file_name, content_type, file_size FROM Product WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery())
            {

                if (rs.next())
                {

                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("unit_price"),
                            rs.getInt("stock_qty"),
                            rs.getBoolean("is_active")
                    );

                    product.setFileName(rs.getString("file_name"));
                    product.setContentType(rs.getString("content_type"));
                    product.setFileSize(rs.getInt("file_size"));

                    return Optional.of(product);
                }
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error while getting product metadata", e);
        }

        return Optional.empty();
    }
}