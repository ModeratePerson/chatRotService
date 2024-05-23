package com.moderatePerson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moderatePerson.domain.PO.Order;
import org.apache.ibatis.annotations.Select;

public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT orderName FROM orders WHERE orderId=#{orderId}")
    public String selectOrderNameById(String orderId);
}
