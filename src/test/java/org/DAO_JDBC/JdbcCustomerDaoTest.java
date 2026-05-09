package org.DAO_JDBC;

import org.DTO.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

 //Tests for Customer DAO

/**
 * @author Aloka (primary)
 */
public class JdbcCustomerDaoTest {

    private JdbcCustomerDao dao;

    @BeforeEach
    void setUp()
    {
        dao = new JdbcCustomerDao();
    }

    // READ
    @Test
    void getCustomerById_returnsEmptyOptional_whenIdDoesNotExist()
    {
        Optional<Customer> result = dao.getCustomerById(-99);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCustomerById_returnsCustomer_whenIdExists() {

        Customer inserted = dao.insertCustomer(new Customer("Test Customer", "test@email.com", "0870000000"));

        Optional<Customer> result = dao.getCustomerById(inserted.getCustomerId());

        assertTrue(result.isPresent());
        assertEquals("Test Customer", result.get().getName());
    }

    // INSERT
    @Test
    void insertCustomer_returnsGeneratedId_whenSuccessful()
    {
        Customer customer = new Customer(
                "Insert Test",
                "insert@email.com",
                "0871111111"
        );

        Customer inserted = dao.insertCustomer(customer);

        assertNotNull(inserted);
        assertTrue(inserted.getCustomerId() > 0);
    }

    // Tests that all customers are returned when customers exist
    @Test
    void getAllCustomers_returnsList_whenCustomersExist()
    {
        List<Customer> customers = dao.getAllCustomers();

        assertNotNull(customers);
        assertTrue(customers.size() > 0);
    }

    // Tests that a customer can be updated and retrieved with new values
    @Test
    void updateCustomer_returnsUpdatedCustomer_whenSuccessful()
    {
        String email = "update" + System.nanoTime() + "@email.com";
        String updatedEmail = "updated" + System.nanoTime() + "@email.com";

        Customer inserted = dao.insertCustomer(new Customer("Update Test", email, "0872222222"));

        Customer updatedCustomer = new Customer("Updated Customer", updatedEmail, "0873333333");
        dao.updateCustomer(inserted.getCustomerId(), updatedCustomer);

        Optional<Customer> result = dao.getCustomerById(inserted.getCustomerId());

        assertTrue(result.isPresent());
        assertEquals("Updated Customer", result.get().getName());
        assertEquals(updatedEmail, result.get().getEmail());
        assertEquals("0873333333", result.get().getPhone());
    }

    // Tests that deleting an existing customer returns true
    @Test
    void deleteCustomer_returnsTrue_whenCustomerExists()
    {
        String email = "delete" + System.nanoTime() + "@email.com";

        Customer inserted = dao.insertCustomer(new Customer("Delete Test", email, "0874444444"));

        boolean deleted = dao.deleteCustomerById(inserted.getCustomerId());

        assertTrue(deleted);
    }

    // Tests that filtering customers returns matching customers
    @Test
    void filterCustomers_returnsMatchingCustomers()
    {
        String email = "filter" + System.nanoTime() + "@email.com";

        Customer inserted = dao.insertCustomer(new Customer("Filter Test", email, "0875555555"));

        List<Customer> customers = dao.findCustomersByFilter(customer ->
                customer.getEmail().equals(inserted.getEmail()));

        assertNotNull(customers);
        assertFalse(customers.isEmpty());
    }

    // Tests that uploaded customer file bytes can be retrieved
    @Test
    void uploadCustomerFileAndRetrieve_returnsMatchingBytes()
    {
        byte[] fileData = "Hello".getBytes();
        String email = "file" + System.nanoTime() + "@email.com";

        Customer inserted = dao.insertCustomer(new Customer("File Test", email, "0876666666"));

        Customer customerFile = new Customer("File Test", email, "0876666666");
        customerFile.setFileName("hello.txt");
        customerFile.setContentType("text/plain");
        customerFile.setFileSize(fileData.length);
        customerFile.setFileData(fileData);

        dao.uploadCustomerFile(inserted.getCustomerId(), customerFile);

        Optional<Customer> result = dao.getCustomerFileById(inserted.getCustomerId());

        assertTrue(result.isPresent());
        assertArrayEquals(fileData, result.get().getFileData());
    }
}
