package org.DAO_JDBC;

import org.DAO.CustomerDao;
import org.DB.DBConnection;
import org.DTO.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

//Implements the CustomerDao interface using JDBC and SQL with PreparedStatements
public class JdbcCustomerDao implements CustomerDao {

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT customer_id, name, email, phone FROM customer";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {

                while (rs.next())
                {
                int id = rs.getInt("customer_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");

                customers.add(new Customer(id, name, email, phone));
                 }

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error getting all customers", e);
        }

        return customers;
    }

    @Override
    public Optional<Customer> getCustomerById(int id) {
        String sql = "SELECT customer_id, name, email, phone FROM customer WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                    return Optional.of(c);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting customer by id", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteCustomerById(int id) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customer", e);
        }
    }

    @Override
    public Customer insertCustomer(Customer customer) {
        String sql = "INSERT INTO customer (name, email, phone) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    return new Customer(newId, customer.getName(), customer.getEmail(), customer.getPhone());
                }
            }

            throw new RuntimeException("Insert worked but no ID returned");

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting customer", e);
        }
    }

    @Override
    public Customer updateCustomer(int id, Customer customer) {
        String sql = "UPDATE customer SET name = ?, email = ?, phone = ? WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No customer found with id " + id);
            }

            return new Customer(id, customer.getName(), customer.getEmail(), customer.getPhone());

        } catch (SQLException e) {
            throw new RuntimeException("Error updating customer", e);
        }
    }

    @Override
    public List<Customer> findCustomersByFilter(Predicate<Customer> filter)
    {

        // Create a list to hold matching customers
        List<Customer> matchingCustomers = new ArrayList<>();

        // Get all customers first
        List<Customer> allCustomers = getAllCustomers();

        // Loop through all customers
        for (Customer customer : allCustomers) {

            // If the customer matches the filter, add it to the result list
            if (filter.test(customer))
            {
                matchingCustomers.add(customer);
            }
        }

        return matchingCustomers;
    }
}
