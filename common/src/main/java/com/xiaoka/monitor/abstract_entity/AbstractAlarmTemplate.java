package com.xiaoka.monitor.abstract_entity;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractAlarmTemplate extends BaseCacheEntity {
    /**
     * 告警模板名称
     */
    private String name;
    /**
     * 接收者
     */
    private String receiverId;
    /**
     * 发送告警到哪个团队
     */
    private String teamId;
    /**
     * 告警的模板
     */
    private String template;
}
