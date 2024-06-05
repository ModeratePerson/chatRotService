package com.moderatePerson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moderatePerson.domain.PO.Order;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT orderName FROM orders WHERE orderId=#{orderId}")
    public String selectOrderNameById(String orderId);
    @Select("UPDATE orders SET orderNumber=#{orderNumber}, payment=#{payment}, payTime=#{payTime}, status=#{status} WHERE orderId=#{orderId}")
    public Integer updateOrderBYId( String orderNumber, String payment, Date payTime, Integer status, String orderId);
}
