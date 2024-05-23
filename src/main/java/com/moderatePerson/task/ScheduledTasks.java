package com.moderatePerson.task;

import com.moderatePerson.domain.PO.User;
import com.moderatePerson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
// 定时任务
@Component
public class ScheduledTasks {

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 0 * * ?") // 每天午夜执行一次
    public void resetDailyPointsForAllUsers() {
        List<User> activeUsers = userService.getUsersWithActivePackages(); // 查找使用套餐的用户
        for (User user : activeUsers) {
            userService.resetDailyPoints(user);
        }
    }
}
