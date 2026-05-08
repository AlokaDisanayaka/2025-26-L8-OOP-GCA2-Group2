package org.GenericServices;
import org.DAO_JDBC.JdbcProductDao;
import org.DTO.Product;
import org.Interface.ClientRequest;

public class FileUploadService implements GenericService {

    private JdbcProductDao productDao;

    public FileUploadService()
    {
        productDao = new JdbcProductDao();
    }

    @Override
    public boolean validate(ClientRequest request)
    {
        return request.getId() != null &&
                request.getJsonData() != null &&
                !request.getJsonData().trim().isEmpty();
    }

    @Override
    public Object execute(ClientRequest request) {
        try
        {
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    org.Interface.ObjectMapperProvider.getMapper();

            Product product = mapper.readValue(request.getJsonData(), Product.class);

            return productDao.uploadProductFile(request.getId(), product);
        }
        catch (Exception e)
        {
            throw new RuntimeException("File upload service failed: " + e.getMessage());
        }
    }

    @Override
    public String getSuccessMessage()
    {
        return "File uploaded successfully using FileUploadService";
    }
}
