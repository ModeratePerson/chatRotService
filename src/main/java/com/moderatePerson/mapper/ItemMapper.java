package com.moderatePerson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moderatePerson.domain.PO.Item;
/**
 * 继承BaseMapper,mybatis-plus整合了单表基本查询
 */
public interface ItemMapper extends BaseMapper<Item> {
}
