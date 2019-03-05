package com.xiaoka.monitor.abstract_entity;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractCluster extends BaseCacheEntity {

    /**
     * 系统ID
     */
    private String sysId;
    /**
     * 告警模板ID
     */
    private String templateId;
    /**
     * 集群名称
     */
    private String name;
    /**
     * 备注
     */
    private String remarks;

}
