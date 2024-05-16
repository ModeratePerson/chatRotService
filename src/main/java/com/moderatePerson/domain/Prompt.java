package com.moderatePerson.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

//关联不同app时用户使用的提示词
//使用lombok关联数据库表
@Data
public class Prompt {
    @TableId
    private String promptId; // 提示词ID
    private String appId; // 关联的 App ID
    private String modelId; // 关联的 AI 模型 ID
    private String promptText; // 提示词文本
    private Date creationTime; // 创建时间
    private Date updateTime; // 更新时间
}
