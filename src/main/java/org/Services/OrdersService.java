package org.Services;

import org.DAO.OrdersDao;
import org.DTO.Orders;

import java.util.List;
import java.util.Optional;

public class OrdersService
{
    private final OrdersDao ordersDao;

    public OrdersService(OrdersDao ordersDao)
    {
        this.ordersDao = ordersDao;
    }

    public List<Orders> getAllOrders()
    {
        return ordersDao.getAllOrders();
    }

    public Optional<Orders> getOrderById(int id)
    {
        return ordersDao.getOrderById(id);
    }

    public List<Orders> getOrdersByCustomerId(int customerId)
    {
        return ordersDao.getOrdersByCustomerId(customerId);
    }

    public Orders addOrder(Orders order)
    {
        return ordersDao.insertOrder(order);
    }

    public Orders updateOrder(int id, Orders order)
    {
        return ordersDao.updateOrder(id, order);
    }

    public boolean deleteOrder(int id)
    {
        return ordersDao.deleteOrderById(id);
    }
}