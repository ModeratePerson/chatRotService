package com.moderatePerson.domain.VO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/*
* 封装套餐界面查询信息
*/
@Getter
@Setter
public class ItemInfo {
    private String itemName;
    private String itemMsg;
    private Double price;
    private Double discount;
    private Date endDate;
}
