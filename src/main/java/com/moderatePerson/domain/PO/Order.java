package com.moderatePerson.domain.PO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

// 订单类
@Data
@TableName("orders")
public class Order {
    @TableId
    private String orderId;
    private String orderName;
    // 支付宝交易号
    private String orderNumber;
    private String payment;
    private String itemMsg;
    // 订单创建时间
    private Date creationTime;
    // 用户支付时间
    private Date payTime;
    // 订单是否被支付，1 or -1
    private Integer status;
}
