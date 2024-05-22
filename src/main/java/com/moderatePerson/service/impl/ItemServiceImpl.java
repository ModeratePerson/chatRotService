package com.moderatePerson.service.impl;

import com.moderatePerson.domain.VO.ItemInfo;
import com.moderatePerson.mapper.ItemMapper;
import com.moderatePerson.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<ItemInfo> selectItemInfo() {
        return itemMapper.selectItemInfo();
    }
}
