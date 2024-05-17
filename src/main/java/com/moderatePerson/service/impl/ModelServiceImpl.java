package com.moderatePerson.service.impl;

import com.moderatePerson.mapper.ModelMapper;
import com.moderatePerson.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImpl implements ModelService {
    @Autowired
    private ModelMapper mapper;
}
