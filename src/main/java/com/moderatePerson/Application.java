package com.moderatePerson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 开启定时任务
@ServletComponentScan(basePackages = "com.moderatePerson.utils.jwt")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);}
}
