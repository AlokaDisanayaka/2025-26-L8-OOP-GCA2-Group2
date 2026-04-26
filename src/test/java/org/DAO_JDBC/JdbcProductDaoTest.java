package org.DAO_JDBC;

import org.DTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


//Tests for Product DAO

public class JdbcProductDaoTest {

    private JdbcProductDao dao;

    @BeforeEach
    void setUp() {
        dao = new JdbcProductDao();
    }

    // READ
    @Test
    void getProductById_returnsEmptyOptional_whenIdDoesNotExist()
    {
        Optional<Product> result = dao.getProductById(-99);
        assertTrue(result.isEmpty());
    }

    @Test
    void getProductById_returnsProduct_whenIdExists()
    {
        Product inserted = dao.insertProduct(new Product("Test Product", 10.0, 5, true));

        Optional<Product> result = dao.getProductById(inserted.getProductId());

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getProductName());
    }

    //INSERT
    @Test
    void insertProduct_returnsGeneratedId_whenSuccessful() {
        Product product = new Product("Insert Product", 20.0, 10, true);

        Product inserted = dao.insertProduct(product);

        assertNotNull(inserted);
        assertTrue(inserted.getProductId() > 0);
    }
}