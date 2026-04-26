package org.Services;

import org.DAO_JDBC.JdbcCustomerDao;
import org.DTO.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

 //Tests for CustomerService


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
}
