package org.Services;

import org.DAO.CustomerDao;
import org.DTO.Customer;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

//Acts as a middle layer between the app and DAO; calls DAO methods without knowing how the database works
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao)
    {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers()
    {
        return customerDao.getAllCustomers();
    }

    public Optional<Customer> getCustomerById(int id)
    {
        return customerDao.getCustomerById(id);
    }

    public Customer addCustomer(Customer customer)
    {
        return customerDao.insertCustomer(customer);
    }

    public Customer updateCustomer(int id, Customer customer)
    {
        return customerDao.updateCustomer(id, customer);
    }

    public boolean deleteCustomer(int id)
    {
        return customerDao.deleteCustomerById(id);
    }

    //filter customers using a Predicate
    public List<Customer> findCustomersByFilter(Predicate<Customer> filter)
    {
        return customerDao.findCustomersByFilter(filter);
    }
    public Customer uploadCustomerFile(int id, Customer customer)
    {
        return customerDao.uploadCustomerFile(id, customer);
    }

    public Optional<Customer> getCustomerFileById(int id)
    {
        return customerDao.getCustomerFileById(id);
    }

    public Optional<Customer> getCustomerFileMetadataById(int id)
    {
        return customerDao.getCustomerFileMetadataById(id);
    }
}