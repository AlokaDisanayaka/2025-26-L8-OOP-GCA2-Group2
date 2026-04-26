package org.Services;

import org.DAO_JDBC.JdbcProductDao;
import org.DTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

 // Tests for ProductService
public class ProductServiceTest {

    private ProductService service;
    private JdbcProductDao dao;

    @BeforeEach
    void setUp()
    {
        dao = new JdbcProductDao();
        service = new ProductService(dao);
    }

    // READ
    @Test
    void getProductById_returnsProduct_whenExists()
    {

        Product inserted = dao.insertProduct(new Product("Service Product", 10.0, 5, true));

        Optional<Product> result = service.getProductById(inserted.getProductId());

        assertTrue(result.isPresent());
        assertEquals(inserted.getProductId(), result.get().getProductId());
    }

    // INSERT
    @Test
    void addProduct_returnsProductWithGeneratedId()
    {

        Product product = new Product("Add Product", 20.0, 10, true);

        Product result = service.addProduct(product);

        assertNotNull(result);
        assertTrue(result.getProductId() > 0);
    }

    // DELETE
    @Test
    void deleteProduct_returnsTrue_whenExists() {

        Product inserted = dao.insertProduct(new Product("Delete Product", 30.0, 3, true));

        boolean deleted = service.deleteProduct(inserted.getProductId());

        assertTrue(deleted);
    }
}
