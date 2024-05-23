package com.moderatePerson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moderatePerson.domain.VO.UserInfo;
import com.moderatePerson.domain.PO.User;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 继承BaseMapper,mybatis-plus整合了单表基本查询
 */
public interface UserMapper extends BaseMapper<User> {
//    使用手机号和密码查询用户
    @Select("SELECT * FROM user WHERE phoneNumber=#{phoneNumber} And password=#{password}")
    public User selectUserByPassword(String phoneNumber, String password);
    @Select("SELECT * FROM user WHERE phoneNumber=#{phoneNumber}")
    public User selectUserByPhoneNumber(String phoneNumber);
    @Select("UPDATE user SET password=#{password} WHERE phoneNumber=#{phoneNumber}")
    public Integer updatePasswordByPhoneNumber(String phoneNumber,String password);
    @Select("SELECT u.username, u.points, i.itemName, l.levelName FROM USER u LEFT JOIN Item i ON u.itemId = i.itemId LEFT JOIN  Level l ON u.levelId = l.levelId WHERE u.phoneNumber = #{phoneNumber}")
    public UserInfo selectUserInfo(String phoneNumber);
    @Select("UPDATE user SET username=#{username} WHERE phoneNumber=#{phoneNumber}")
    public Integer updateUserNameByPhoneNumber(String phoneNumber);
    @Select("SELECT * FROM user WHERE item_expiration_date > #{now}")
    List<User> findByItemExpirationDateAfter(Date now);
}
