package org.DAO;

import org.DTO.Orders;

import java.util.List;
import java.util.Optional;

public interface OrdersDao
{
    List<Orders> getAllOrders();
    List<Orders> getOrdersByCustomerId(int customerId);
    Optional<Orders> getOrderById(int id);

    boolean deleteOrderById(int id);
    Orders insertOrder(Orders order);
    Orders updateOrder(int id, Orders order);

    Optional<Orders> getOrdersMetadataById(int id);

}
