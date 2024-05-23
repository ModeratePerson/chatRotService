package com.moderatePerson.service.impl;

import com.moderatePerson.domain.PO.Order;
import com.moderatePerson.mapper.OrderMapper;
import com.moderatePerson.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Integer updateOrderById(Order order) {
        return orderMapper.updateById(order);
    }

    @Override
    public Integer insertOrder(Order order) {
        return orderMapper.insert(order);
    }

    @Override
    public String selectOrderNameById(String orderId) {
        return orderMapper.selectOrderNameById(orderId);
    }
}
