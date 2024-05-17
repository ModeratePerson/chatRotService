package com.moderatePerson.service.impl;

import com.moderatePerson.mapper.LevelMapper;
import com.moderatePerson.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LevelServiceImpl implements LevelService {
    @Autowired
    private LevelMapper levelMapper;
}
