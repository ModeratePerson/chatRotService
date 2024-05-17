package com.moderatePerson.service.impl;

import com.moderatePerson.mapper.PromptMapper;
import com.moderatePerson.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromptServiceImpl implements PromptService {
    @Autowired
    private PromptMapper promptMapper;
}
