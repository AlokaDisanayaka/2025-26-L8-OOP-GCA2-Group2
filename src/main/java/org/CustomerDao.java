package org;
//Defines the database operations (CRUD methods) that must be implemented for Customer.
import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(int id);

    boolean deleteCustomerById(int id);

    Customer insertCustomer(Customer customer);

    Customer updateCustomer(int id, Customer customer);
}