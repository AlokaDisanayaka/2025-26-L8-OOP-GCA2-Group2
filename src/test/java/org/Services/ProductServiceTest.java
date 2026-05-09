package org.Services;

import org.DAO_JDBC.JdbcProductDao;
import org.DTO.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

 // Tests for ProductService
/**
 * @author Aloka (primary)
 */
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

    // Tests that all products are returned when products exist
    @Test
    void getAllProducts_returnsList_whenProductsExist()
    {
        List<Product> products = service.getAllProducts();

        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    // Tests that a product can be updated using the service
    @Test
    void updateProduct_returnsUpdatedProduct_whenSuccessful()
    {
        String productName = "Service Update Product " + System.nanoTime();
        String updatedProductName = "Service Updated Product " + System.nanoTime();

        Product inserted = dao.insertProduct(new Product(productName, 40.0, 8, true));

        Product updatedProduct = new Product(updatedProductName, 50.0, 12, false);
        Product result = service.updateProduct(inserted.getProductId(), updatedProduct);

        assertNotNull(result);
        assertEquals(updatedProductName, result.getProductName());
        assertEquals(50.0, result.getUnitPrice());
        assertEquals(12, result.getStockQty());
        assertFalse(result.isActive());
    }

    // Tests that filtering products returns matching products
    @Test
    void filterProducts_returnsMatchingProducts()
    {
        String productName = "Service Filter Product " + System.nanoTime();

        Product inserted = dao.insertProduct(new Product(productName, 60.0, 14, true));

        List<Product> products = service.findProductsByFilter(product ->
                product.getProductName().equals(inserted.getProductName()));

        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    // Tests that a product file can be uploaded using the service
    @Test
    void uploadProductFile_returnsProductWithFileData()
    {
        byte[] fileData = "Hello Product".getBytes();
        String productName = "Service File Product " + System.nanoTime();

        Product inserted = dao.insertProduct(new Product(productName, 70.0, 16, true));

        Product productFile = new Product(productName, 70.0, 16, true);
        productFile.setFileName("hello-product.txt");
        productFile.setContentType("text/plain");
        productFile.setFileSize(fileData.length);
        productFile.setFileData(fileData);

        Product result = service.uploadProductFile(inserted.getProductId(), productFile);

        assertNotNull(result);
        assertEquals("hello-product.txt", result.getFileName());
        assertArrayEquals(fileData, result.getFileData());
    }
}
