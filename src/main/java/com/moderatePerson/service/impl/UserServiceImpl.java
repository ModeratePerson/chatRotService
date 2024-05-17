package com.moderatePerson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moderatePerson.domain.PO.User;
import com.moderatePerson.mapper.UserMapper;
import com.moderatePerson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User selectUserByPassword(String phoneNumber, String password) {
        return userMapper.selectUserByPassword(phoneNumber,password);
    }

    @Override
    public User selectUserByPhoneNumber(String phoneNumber) {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }
}
