package com.moderatePerson.service.impl;

import com.moderatePerson.mapper.AppMapper;
import com.moderatePerson.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppMapper appMapper;
}
