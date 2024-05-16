package com.moderatePerson.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

//使用的ai模型
//使用lombok关联数据库表
@Data
public class Model {
    @TableId
    private String modelId; // 模型ID
    private String modelName; // 模型名称
    private String modelDescription; // 模型描述
    private String modelVersion; // 模型版本
    private String modelApiKey; // 模型调用api
    private Double modelUnitPrice; //调用api每1000token费用
    private String modelFeature; //模型特点
    private Date creationTime; // 创建时间
    private Date updateTime; // 更新时间
}
