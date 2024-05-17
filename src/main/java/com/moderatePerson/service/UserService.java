package com.moderatePerson.service;

import com.moderatePerson.domain.PO.User;

import java.util.List;


public interface UserService {
    User selectUserByPassword(String phoneNumber,String password);
    User selectUserByPhoneNumber(String phoneNumber);
}
