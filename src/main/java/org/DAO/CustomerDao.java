package org.DAO;
//Defines the database operations (CRUD methods) that must be implemented for Customer.
import org.DTO.Customer;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface CustomerDao {

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(int id);

    boolean deleteCustomerById(int id);

    Customer insertCustomer(Customer customer);

    Customer updateCustomer(int id, Customer customer);

    //filter customers using a Predicate
    List<Customer> findCustomersByFilter(Predicate<Customer> filter);

    // Stage 3- upload file for existing customer
    Customer uploadCustomerFile(int id, Customer customer);

    // Stage 3-retrieve customer with full binary file
    Optional<Customer> getCustomerFileById(int id);

    // Stage 3-retrieve only metadata, not the BLOB
    Optional<Customer> getCustomerFileMetadataById(int id);


}