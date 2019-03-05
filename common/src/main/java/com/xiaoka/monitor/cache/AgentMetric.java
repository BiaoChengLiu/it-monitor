package com.xiaoka.monitor.cache;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

import java.util.Map;

@Data
public class AgentMetric extends BaseCacheEntity {
    /**
     * 组件类型
     */
    private String type;
    /**
     * 服务器ID
     */
    private String hostId;
    /**
     * 关联的具体监控的组件
     */
    private String confId;
    /**
     * 收集的时间戳
     */
    private Long timestamp;

    /**
     * 使用map格式存储指标数据
     */
    private Map<String, Object> metricMap;

}
