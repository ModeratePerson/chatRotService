package com.moderatePerson.service;

import com.moderatePerson.domain.VO.UserInfo;
import com.moderatePerson.domain.PO.User;


public interface UserService {
    User selectUserByPassword(String phoneNumber,String password);
    User selectUserByPhoneNumber(String phoneNumber);
    Integer insertUserByPhoneNumber(String phoneNumber,String username);
    Integer updatePasswordByPhoneNumber(String phoneNumber,String password);
    UserInfo selectUserInfo(String phoneNumber);
    Integer updateUserNameByPhoneNumber(String phoneNumber);
}
