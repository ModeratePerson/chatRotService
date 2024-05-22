package com.moderatePerson.service.impl;

import com.moderatePerson.domain.VO.UserInfo;
import com.moderatePerson.domain.PO.User;
import com.moderatePerson.mapper.UserMapper;
import com.moderatePerson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Integer insertUserByPhoneNumber(String phoneNumber,String username) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setUsername(username);
        return userMapper.insert(user);
    }

    @Override
    public Integer updatePasswordByPhoneNumber(String phoneNumber, String password) {
        return userMapper.updatePasswordByPhoneNumber(phoneNumber,password);
    }

    @Override
    public UserInfo selectUserInfo(String phoneNumber) {
        return userMapper.selectUserInfo(phoneNumber);
    }

    @Override
    public Integer updateUserNameByPhoneNumber(String phoneNumber) {
        return userMapper.updateUserNameByPhoneNumber(phoneNumber);
    }
}
