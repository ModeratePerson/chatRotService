package com.moderatePerson.service;

import com.moderatePerson.domain.PO.Order;

public interface OrderService {
    public Integer updateOrderById(Order order);
    public Integer insertOrder(Order order);
    public String selectOrderNameById(String orderId);
}
