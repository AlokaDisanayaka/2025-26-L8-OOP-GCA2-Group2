package org.DAO;

import org.DTO.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemDao
{
    List<OrderItem> getAllOrderItems();
    List<OrderItem> getOrderItemsByOrderId(int orderId);
    Optional<OrderItem> getOrderItemById(int id);

    boolean deleteOrderItemById(int id);
    OrderItem insertOrderItem(OrderItem orderItem);
    OrderItem updateOrderItem(int id, OrderItem orderItem);
}