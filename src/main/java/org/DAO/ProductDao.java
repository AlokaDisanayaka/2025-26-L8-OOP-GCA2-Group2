package org.DAO;
import org.DTO.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDao {

    // Return all products
    List<Product> getAllProducts();

    // Return one product by ID
    Optional<Product> getProductById(int id);

    // Insert a new product and return it with generated ID
    Product insertProduct(Product product);

    // Update product by ID
    Product updateProduct(int id, Product product);

    // Delete product by ID
    boolean deleteProductById(int id);
}