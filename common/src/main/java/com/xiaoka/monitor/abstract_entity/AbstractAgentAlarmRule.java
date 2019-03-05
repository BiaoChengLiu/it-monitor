package com.xiaoka.monitor.abstract_entity;

import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractAgentAlarmRule extends AbstractBaseAlarmRule {
    /**
     * 关联的告警模板ID
     */
    private String templateId;
}
