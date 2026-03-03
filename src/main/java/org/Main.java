package org;

public class Main {
    public static void main(String[] args) {

        CustomerDao customerDao = new JdbcCustomerDao();
        CustomerService customerService = new CustomerService(customerDao);

        // test ...
        Customer newCustomer = new Customer("Test User", "testuser@email.com", "0871234567");
        Customer inserted = customerService.addCustomer(newCustomer);

        System.out.println("Inserted customer id = " + inserted.getCustomerId());
        System.out.println("Total customers = " + customerService.getAllCustomers().size());
    }
}