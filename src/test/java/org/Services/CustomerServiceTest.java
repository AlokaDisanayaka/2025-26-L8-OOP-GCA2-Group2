package org.Services;

import org.DAO_JDBC.JdbcCustomerDao;
import org.DTO.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

 //Tests for CustomerService


/**
 * @author Aloka (primary)
 */
public class CustomerServiceTest {

    private CustomerService service;
    private JdbcCustomerDao dao;

    @BeforeEach
    void setUp()
    {
        dao = new JdbcCustomerDao();
        service = new CustomerService(dao);
    }

    // READ
    @Test
    void getCustomerById_returnsCustomer_whenExists()
    {

        Customer inserted = dao.insertCustomer(new Customer("Service Test", "service@email.com", "0870000000"));

        Optional<Customer> result = service.getCustomerById(inserted.getCustomerId());

        assertTrue(result.isPresent());
        assertEquals(inserted.getCustomerId(), result.get().getCustomerId());
    }

    // INSERT
    @Test
    void addCustomer_returnsCustomerWithGeneratedId()
    {

        Customer customer = new Customer(
                "Add Test",
                "add@email.com",
                "0871111111"
        );

        Customer result = service.addCustomer(customer);

        assertNotNull(result);
        assertTrue(result.getCustomerId() > 0);
    }

    // DELETE
    @Test
    void deleteCustomer_returnsTrue_whenExists()
    {

        Customer inserted = dao.insertCustomer(new Customer("Delete Test", "delete@email.com", "0872222222"));

        boolean deleted = service.deleteCustomer(inserted.getCustomerId());

        assertTrue(deleted);
    }

    // Tests that all customers are returned when customers exist
    @Test
    void getAllCustomers_returnsList_whenCustomersExist()
    {
        List<Customer> customers = service.getAllCustomers();

        assertNotNull(customers);
        assertTrue(customers.size() > 0);
    }

    // Tests that a customer can be updated using the service
    @Test
    void updateCustomer_returnsUpdatedCustomer_whenSuccessful()
    {
        String email = "serviceupdate" + System.nanoTime() + "@email.com";
        String updatedEmail = "serviceupdated" + System.nanoTime() + "@email.com";

        Customer inserted = dao.insertCustomer(new Customer("Service Update", email, "0873333333"));

        Customer updatedCustomer = new Customer("Service Updated", updatedEmail, "0874444444");
        Customer result = service.updateCustomer(inserted.getCustomerId(), updatedCustomer);

        assertNotNull(result);
        assertEquals("Service Updated", result.getName());
        assertEquals(updatedEmail, result.getEmail());
        assertEquals("0874444444", result.getPhone());
    }

    // Tests that filtering customers returns matching customers
    @Test
    void filterCustomers_returnsMatchingCustomers()
    {
        String email = "servicefilter" + System.nanoTime() + "@email.com";

        Customer inserted = dao.insertCustomer(new Customer("Service Filter", email, "0875555555"));

        List<Customer> customers = service.findCustomersByFilter(customer ->
                customer.getEmail().equals(inserted.getEmail()));

        assertNotNull(customers);
        assertFalse(customers.isEmpty());
    }

    // Tests that a customer file can be uploaded using the service
    @Test
    void uploadCustomerFile_returnsCustomerWithFileData()
    {
        byte[] fileData = "Hello".getBytes();
        String email = "servicefile" + System.nanoTime() + "@email.com";

        Customer inserted = dao.insertCustomer(new Customer("Service File", email, "0876666666"));

        Customer customerFile = new Customer("Service File", email, "0876666666");
        customerFile.setFileName("hello.txt");
        customerFile.setContentType("text/plain");
        customerFile.setFileSize(fileData.length);
        customerFile.setFileData(fileData);

        Customer result = service.uploadCustomerFile(inserted.getCustomerId(), customerFile);

        assertNotNull(result);
        assertEquals("hello.txt", result.getFileName());
        assertArrayEquals(fileData, result.getFileData());
    }
}
