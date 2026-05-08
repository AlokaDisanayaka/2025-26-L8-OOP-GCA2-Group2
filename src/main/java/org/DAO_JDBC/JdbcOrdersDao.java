package org.DAO_JDBC;

import org.DAO.OrdersDao;
import org.DB.DBConnection;
import org.DTO.Orders;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Adhira Pillai (primary)
 **/

public class JdbcOrdersDao implements OrdersDao
{
    @Override
    public List<Orders> getAllOrders()
    {
        List<Orders> orders = new ArrayList<>();

        String sql = "SELECT order_id, customer_id, order_date, status, file_data, file_name, content_type, file_size FROM orders";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                orders.add(new Orders(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        Orders.OrderStatus.valueOf(rs.getString("status")),

                        rs.getBytes("file_data"),
                        rs.getString("file_name"),
                        rs.getString("content_type"),
                        rs.getInt("file_size")
                ));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error getting all orders", e);
        }

        return orders;
    }

    @Override
    public Optional<Orders> getOrderById(int id)
    {
        String sql = "SELECT order_id, customer_id, order_date, status, file_data, file_name, content_type, file_size FROM orders WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return Optional.of(new Orders(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getTimestamp("order_date").toLocalDateTime(),
                            Orders.OrderStatus.valueOf(rs.getString("status")),

                            rs.getBytes("file_data"),
                            rs.getString("file_name"),
                            rs.getString("content_type"),
                            rs.getInt("file_size")
                    ));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error getting order by id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Orders> getOrdersByCustomerId(int customerId)
    {
        List<Orders> orders = new ArrayList<>();

        String sql = "SELECT order_id, customer_id, order_date, status, file_data, file_name, content_type, file_size FROM orders WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    orders.add(new Orders(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getTimestamp("order_date").toLocalDateTime(),
                            Orders.OrderStatus.valueOf(rs.getString("status")),

                            rs.getBytes("file_data"),
                            rs.getString("file_name"),
                            rs.getString("content_type"),
                            rs.getInt("file_size")
                    ));
                }
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error getting orders by customer id", e);
        }

        return orders;
    }

    @Override
    public boolean deleteOrderById(int id)
    {
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error deleting order", e);
        }
    }

    @Override
    public Orders insertOrder(Orders order)
    {
        String sql = "INSERT INTO orders (customer_id, order_date, status, file_data, file_name, content_type, file_size) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, order.getCustomerId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            ps.setString(3, order.getStatus().name());

            ps.setBytes(4, order.getFileData());
            ps.setString(5, order.getFileName());
            ps.setString(6, order.getContentType());
            ps.setInt(7, order.getFileSize());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                if (keys.next())
                {
                    int newId = keys.getInt(1);

                    return new Orders(
                            newId,
                            order.getCustomerId(),
                            order.getOrderDate(),
                            order.getStatus(),

                            order.getFileData(),
                            order.getFileName(),
                            order.getContentType(),
                            order.getFileSize()
                    );
                }
            }
            throw new RuntimeException("Insert worked but no ID returned");

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error inserting order", e);
        }
    }

    @Override
    public Orders updateOrder(int id, Orders order)
    {
        String sql = "UPDATE orders SET customer_id=?, order_date=?, status=?, file_data=?, file_name=?, content_type=?, file_size=? WHERE order_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, order.getCustomerId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            ps.setString(3, order.getStatus().name());

            ps.setBytes(4, order.getFileData());
            ps.setString(5, order.getFileName());
            ps.setString(6, order.getContentType());
            ps.setInt(7, order.getFileSize());

            ps.setInt(8, id);

            int rows = ps.executeUpdate();

            if (rows == 0)
            {
                throw new RuntimeException("No order found with id " + id);
            }

            return new Orders(
                    id,
                    order.getCustomerId(),
                    order.getOrderDate(),
                    order.getStatus(),

                    order.getFileData(),
                    order.getFileName(),
                    order.getContentType(),
                    order.getFileSize()
            );

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error updating order", e);
        }
    }

    //Retrieves only metadata for an Orders without loading the BLOB (file_data) column.
    @Override
    public Optional<Orders> getOrdersMetadataById(int id)
    {
        String sql = "SELECT order_id, customer_id, order_date, status, " +
                        "file_name, content_type, file_size " +
                        "FROM orders WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    Orders order = new Orders(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getTimestamp("order_date").toLocalDateTime(),
                            Orders.OrderStatus.valueOf(rs.getString("status"))
                    );

                    order.setFileName(rs.getString("file_name"));
                    order.setContentType(rs.getString("content_type"));
                    order.setFileSize(rs.getInt("file_size"));

                    return Optional.of(order);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error getting order metadata by id", e);
        }

        return Optional.empty();
    }
}