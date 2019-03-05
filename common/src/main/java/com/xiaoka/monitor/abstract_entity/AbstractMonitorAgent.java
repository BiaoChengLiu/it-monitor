package com.xiaoka.monitor.abstract_entity;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractMonitorAgent extends BaseCacheEntity {
    /**
     * 所属系统
     */
    private String sysId;
    /**
     * 所属集群
     */
    private String clusterId;
    /**
     * 关联的服务器
     */
    private String hostId;
    /**
     * 组件类型
     */
    private String type;
    /**
     * 告警模板ID
     */
    private String templateId;
    /**
     * 组件名称
     */
    private String name;
    /**
     * 监控的周期
     */
    private Integer period;
    /**
     * 监控的周期单位
     */
    private Integer periodUnit;
    /**
     * 组件监控需要的参数
     */
    private String argument;
}
