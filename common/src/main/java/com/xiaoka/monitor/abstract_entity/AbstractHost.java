package com.xiaoka.monitor.abstract_entity;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractHost extends BaseCacheEntity {

    /**
     * 主机名称
     */
    private String name;
    /**
     * 主机ssh端口
     */
    private Integer port;
    /**
     * 主机python bin 路径
     */
    private String pathBin;
}
