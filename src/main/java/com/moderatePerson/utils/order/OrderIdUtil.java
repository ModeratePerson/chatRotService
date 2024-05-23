package com.moderatePerson.utils.order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderIdUtil {

    public static String generateUniqueOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = sdf.format(new Date());
        int randomNum = new Random().nextInt(900000) + 100000; // 生成6位随机数
        return timestamp + randomNum;
    }
}
