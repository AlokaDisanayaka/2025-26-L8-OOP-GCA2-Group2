package org.DAO_JDBC;

import org.DAO.OrderItemDao;
import org.DB.DBConnection;
import org.DTO.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcOrderItemDao implements OrderItemDao
{
    @Override
    public List<OrderItem> getAllOrderItems()
    {
        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT order_item_id, order_id, product_id, quantity, unit_price_at_time FROM orderitem";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                items.add(new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price_at_time")
                ));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error getting all order items", e);
        }

        return items;
    }

    @Override
    public Optional<OrderItem> getOrderItemById(int id)
    {
        String sql = "SELECT order_item_id, order_id, product_id, quantity, unit_price_at_time FROM orderitem WHERE order_item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return Optional.of(new OrderItem(
                            rs.getInt("order_item_id"),
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price_at_time")
                    ));
                }
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error getting order item by id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId)
    {
        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT order_item_id, order_id, product_id, quantity, unit_price_at_time FROM orderitem WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    items.add(new OrderItem(
                            rs.getInt("order_item_id"),
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price_at_time")
                    ));
                }
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error getting order items by order id", e);
        }

        return items;
    }

    @Override
    public boolean deleteOrderItemById(int id)
    {
        String sql = "DELETE FROM orderitem WHERE order_item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error deleting order item", e);
        }
    }

    @Override
    public OrderItem insertOrderItem(OrderItem item)
    {

        String sql = "INSERT INTO orderitem (order_id, product_id, quantity, unit_price_at_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPriceAtTime());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                if (keys.next())
                {
                    int newId = keys.getInt(1);

                    return new OrderItem(
                            newId,
                            item.getOrderId(),
                            item.getProductId(),
                            item.getQuantity(),
                            item.getUnitPriceAtTime()
                    );
                }
            }

            throw new RuntimeException("Insert worked but no ID returned");
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error inserting order item", e);
        }
    }

    @Override
    public OrderItem updateOrderItem(int id, OrderItem item)
    {
        String sql = "UPDATE orderitem SET order_id=?, product_id=?, quantity=?, unit_price_at_time=? WHERE order_item_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPriceAtTime());
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows == 0)
            {
                throw new RuntimeException("No order item found with id " + id);
            }

            return new OrderItem(
                    id,
                    item.getOrderId(),
                    item.getProductId(),
                    item.getQuantity(),
                    item.getUnitPriceAtTime()
            );
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error updating order item", e);
        }
    }
}