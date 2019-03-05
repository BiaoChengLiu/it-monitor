package com.xiaoka.monitor.cache;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public class AlarmMetricStateCache extends BaseCacheEntity {
    /***
     * agentMonitorId+metricName
     */
    /**
     * 告警上一次发送时间戳
     */
    private Long preTimestamp;
}
