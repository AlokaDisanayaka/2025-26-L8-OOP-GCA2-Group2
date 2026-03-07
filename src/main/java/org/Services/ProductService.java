package org.Services;
import org.DAO.ProductDao;
import org.DTO.Product;

import java.util.List;
import java.util.Optional;

/**
 * Service layer depends ONLY on ProductDao interface.
 * Does not know about JDBC implementation.
 */
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public Optional<Product> getProductById(int id) {
        return productDao.getProductById(id);
    }

    public Product addProduct(Product product) {
        return productDao.insertProduct(product);
    }

    public Product updateProduct(int id, Product product) {
        return productDao.updateProduct(id, product);
    }

    public boolean deleteProduct(int id) {
        return productDao.deleteProductById(id);
    }
}