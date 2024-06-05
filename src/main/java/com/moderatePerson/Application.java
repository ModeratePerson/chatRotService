package com.moderatePerson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.reflect.Field;

@SpringBootApplication
@EnableScheduling // 开启定时任务
@ServletComponentScan(basePackages = "com.moderatePerson.utils.jwt")
public class Application {
    public static void main(String[] args) {
//        System.setProperty("java.library.path", "C:\\Program Files\\Python311\\Lib\\site-packages\\jep");
//
//        // 重新加载库路径，以便新设置生效
//        try {
//            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
//            fieldSysPath.setAccessible(true);
//            fieldSysPath.set(null, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        SpringApplication.run(Application.class, args);}
}
