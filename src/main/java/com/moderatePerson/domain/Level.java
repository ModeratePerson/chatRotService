package com.moderatePerson.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

//用户购买不同套餐拥有不同等级
//使用lombok关联数据库表
@Data
public class Level {
    @TableId
    private String levelId;
    private String levelName; // 等级名称
    private String itemId; // 对应套餐ID
    private String itemName; // 对应套餐名称
    private String levelDescription; // 等级描述
    private Double levelPrice; // 等级价格
    private String levelValidityPeriod; // 等级有效期
    private Integer pointRewards; // 积分奖励
    private String levelIcon; // 等级图标（图片形式）
    private Date creationTime; // 创建时间
    private Date updateTime; // 更新时间
}
