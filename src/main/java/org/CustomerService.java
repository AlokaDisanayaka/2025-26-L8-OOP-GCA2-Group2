package org;

import java.util.List;
import java.util.Optional;

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
}