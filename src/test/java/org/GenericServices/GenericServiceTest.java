package org.GenericServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DAO_JDBC.JdbcProductDao;
import org.DTO.Product;
import org.GenericServices.FileUploadService;
import org.GenericServices.GenericService;
import org.GenericServices.GenericServiceFactory;
import org.GenericServices.ProductReportService;
import org.Interface.ClientRequest;
import org.Interface.ObjectMapperProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenericServiceTest {

    // Tests that FileUploadService validation returns true when the request has an id and JSON data
    @Test
    void fileUploadService_validate_returnsTrue_whenRequestValid() throws Exception
    {
        FileUploadService service = new FileUploadService();
        ObjectMapper mapper = ObjectMapperProvider.getMapper();

        Product product = new Product("Validate Product", 10.0, 5, true);
        String json = mapper.writeValueAsString(product);

        ClientRequest request = new ClientRequest("runService", null, 1, json, "FILE_UPLOAD");

        assertTrue(service.validate(request));
    }

    // Tests that FileUploadService uploads a file and returns the uploaded product
    @Test
    void fileUploadService_execute_returnsUploadedProduct() throws Exception
    {
        FileUploadService service = new FileUploadService();
        JdbcProductDao dao = new JdbcProductDao();
        ObjectMapper mapper = ObjectMapperProvider.getMapper();

        Product inserted = dao.insertProduct(new Product("Upload Service Product " + System.nanoTime(), 20.0, 10, true));

        byte[] fileData = "Hello Product".getBytes();
        Product product = new Product(inserted.getProductName(), 20.0, 10, true);
        product.setFileName("hello-product.txt");
        product.setContentType("text/plain");
        product.setFileSize(fileData.length);
        product.setFileData(fileData);

        String json = mapper.writeValueAsString(product);
        ClientRequest request = new ClientRequest("runService", null, inserted.getProductId(), json, "FILE_UPLOAD");

        Object result = service.execute(request);
        Product uploadedProduct = (Product) result;

        assertNotNull(uploadedProduct);
        assertEquals(inserted.getProductId(), uploadedProduct.getProductId());
        assertEquals("hello-product.txt", uploadedProduct.getFileName());
        assertArrayEquals(fileData, uploadedProduct.getFileData());
    }

    // Tests that ProductReportService returns a report string
    @Test
    void productReportService_execute_returnsReportString()
    {
        ProductReportService service = new ProductReportService();
        ClientRequest request = new ClientRequest("runService", null, null, null, "PRODUCT_REPORT");

        Object result = service.execute(request);

        assertNotNull(result);
        assertTrue(result instanceof String);
        assertTrue(result.toString().contains("Total products"));
    }

    // Tests that the factory returns the correct service classes
    @Test
    void genericServiceFactory_returnsCorrectServiceType()
    {
        GenericService fileUploadService = GenericServiceFactory.getService("FILE_UPLOAD");
        GenericService productReportService = GenericServiceFactory.getService("PRODUCT_REPORT");

        assertTrue(fileUploadService instanceof FileUploadService);
        assertTrue(productReportService instanceof ProductReportService);
    }
}
