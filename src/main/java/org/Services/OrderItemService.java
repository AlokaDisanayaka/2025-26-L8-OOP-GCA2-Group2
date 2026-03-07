package org.Services;

import org.DAO.OrderItemDao;
import org.DTO.OrderItem;

import java.util.List;
import java.util.Optional;

public class OrderItemService
{
    private final OrderItemDao orderItemDao;

    public OrderItemService(OrderItemDao orderItemDao)
    {
        this.orderItemDao = orderItemDao;
    }

    public List<OrderItem> getAllOrderItems()
    {
        return orderItemDao.getAllOrderItems();
    }

    public Optional<OrderItem> getOrderItemById(int id)
    {
        return orderItemDao.getOrderItemById(id);
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId)
    {
        return orderItemDao.getOrderItemsByOrderId(orderId);
    }

    public OrderItem addOrderItem(OrderItem item)
    {
        return orderItemDao.insertOrderItem(item);
    }

    public OrderItem updateOrderItem(int id, OrderItem item)
    {
        return orderItemDao.updateOrderItem(id, item);
    }

    public boolean deleteOrderItem(int id)
    {
        return orderItemDao.deleteOrderItemById(id);
    }
}