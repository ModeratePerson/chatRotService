package com.moderatePerson.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

//用户套餐
//使用lombok关联数据库表
@Data
public class Item {
    @TableId
    private String itemId;
    private String itemName;
    private String itemMsg; //简要描述
    private String level; //套餐对应的用户等级
    private Double price; //价格
    private Double discount; //折扣
    private Date startDate; //上架时间
    private Date endDate; //下架时间
}
