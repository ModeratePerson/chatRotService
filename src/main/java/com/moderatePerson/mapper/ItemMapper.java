package com.moderatePerson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moderatePerson.domain.PO.Item;
import com.moderatePerson.domain.VO.ItemInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 继承BaseMapper,mybatis-plus整合了单表基本查询
 */
public interface ItemMapper extends BaseMapper<Item> {
    @Select("SELECT itemName, itemMsg, discount, price, endDate FROM item WHERE CURRENT_DATE BETWEEN startDate AND endDate")
    public List<ItemInfo> selectItemInfo();
}
