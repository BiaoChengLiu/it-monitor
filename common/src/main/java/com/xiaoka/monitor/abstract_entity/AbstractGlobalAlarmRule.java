package com.xiaoka.monitor.abstract_entity;

import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractGlobalAlarmRule extends AbstractBaseAlarmRule {
    /**
     * 全局告警表达式名称
     */
    private String name;
    /**
     * 发送的团队
     */
    private String teamId;
    /**
     * 告警接收者
     */
    private String receiverId;
    /**
     * 告警的模板
     */
    private String template;
}
