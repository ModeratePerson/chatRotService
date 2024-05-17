package com.moderatePerson.service.impl;

import com.moderatePerson.mapper.ItemMapper;
import com.moderatePerson.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

}
