package org.DAO_JDBC;

import org.DTO.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

 //Tests for Customer DAO

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
}