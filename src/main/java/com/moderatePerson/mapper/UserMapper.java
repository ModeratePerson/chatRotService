package com.moderatePerson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moderatePerson.domain.PO.User;
import org.apache.ibatis.annotations.Select;

/**
 * 继承BaseMapper,mybatis-plus整合了单表基本查询
 */
public interface UserMapper extends BaseMapper<User> {
//    使用手机号和密码查询用户
    @Select("SELECT * FROM user WHERE phoneNumber=#{phoneNumber} And password=#{password}")
    public User selectUserByPassword(String phoneNumber, String password);
    @Select("SELECT * FROM user WHERE phoneNumber=#{phoneNumber}")
    public User selectUserByPhoneNumber(String phoneNumber);
}
