package com.xiaoka.monitor.abstract_entity;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractSenderScript extends BaseCacheEntity {
    /**
     * 发送类型
     */
    private String sendType;
    /**
     * 对应的脚本
     */
    private String script;
}
