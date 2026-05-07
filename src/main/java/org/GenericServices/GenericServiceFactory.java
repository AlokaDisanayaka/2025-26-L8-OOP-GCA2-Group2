package org.GenericServices;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GenericServiceFactory {

    private static final Map<String, Supplier<GenericService>> services = new HashMap<>();

    static
    {
        services.put("FILE_UPLOAD", FileUploadService::new);
        services.put("PRODUCT_REPORT", ProductReportService::new);
    }

    public static GenericService getService(String serviceType)
    {
        Supplier<GenericService> supplier = services.get(serviceType);

        if (supplier == null)
        {
            throw new RuntimeException("Unknown service type: " + serviceType);
        }

        return supplier.get();
    }
}