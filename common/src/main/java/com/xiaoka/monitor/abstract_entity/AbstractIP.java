package com.xiaoka.monitor.abstract_entity;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractIP extends BaseCacheEntity {
    /**
     * 主机ID
     */
    private String hostId;
    /**
     * IP
     */
    private String ip;
    /**
     * 是否禁用
     */
    private Boolean disabled;
    /**
     * 备注
     */
    private String remarks;
}
