package com.moderatePerson.service;

import com.moderatePerson.domain.PO.Order;

import java.util.Date;

public interface OrderService {
    public Integer updateOrderById(Order order);
    public Integer insertOrder(Order order);
    public String selectOrderNameById(String orderId);
    public Integer updateOrderBYId(String orderNumber, String payment, Date payTime, Integer status, String orderId);
}
