package org.DAO;
import org.DTO.Product;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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

    //filter products using a Predicate
    List<Product> findProductsByFilter(Predicate<Product> filter);

    // Stage 3- upload file for existing product
    Product uploadProductFile(int id, Product product);

    // Stage 3- retrieve product with full binary file
    Optional<Product> getProductFileById(int id);

    // Stage 3- retrieve only metadata, not the BLOB
    Optional<Product> getProductFileMetadataById(int id);
}