package com.moderatePerson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moderatePerson.domain.PO.Item;
import com.moderatePerson.domain.VO.UserInfo;
import com.moderatePerson.domain.PO.User;
import com.moderatePerson.mapper.ItemMapper;
import com.moderatePerson.mapper.UserMapper;
import com.moderatePerson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ItemMapper itemMapper;
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
    /**
     * 用户提升权限
     */
    public void upgradeUserPermissions(String phoneNumber, String itemName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phoneNumber", phoneNumber);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        QueryWrapper<Item> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("itemName", itemName);
        Item item = itemMapper.selectOne(itemQueryWrapper);
        if (item == null) {
            throw new IllegalArgumentException("Item not found");
        }

        switch (itemName) {
            case "包月套餐":
                setItemExpirationDate(user, 30); // 设置套餐过期时间
                user.setLevelId("2"); // 设置user等级
                user.setPoints(100);
                break;
            case "包月套餐2":
                setItemExpirationDate(user, 30);
                user.setLevelId("3");
                user.setPoints(300);
                break;
            case "100积分套餐":
                user.setPoints(120);
            case "1000积分套餐":
                user.setPoints(1300);
            default:
                throw new IllegalArgumentException("Unknown item name");
        }

        userMapper.updateById(user);
    }
    // 设置用户套餐过期时间
    private void setItemExpirationDate(User user, int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        user.setItemExpirationDate(cal.getTime());
    }
    // 查找套餐未过期用户
    public List<User> getUsersWithActivePackages() {
        Date now = new Date();
        return userMapper.findByItemExpirationDateAfter(now);
    }
    // 重置用户积分数
    public void resetDailyPoints(User user) {
        if (user.getItemId() != null) {
            switch (user.getItemId()) {
                case "包月套餐":
                    user.setPoints(100);
                    break;
                case "包月套餐2":
                    user.setPoints(300);
                    break;
            }
            userMapper.updateById(user);
        }
    }
}
