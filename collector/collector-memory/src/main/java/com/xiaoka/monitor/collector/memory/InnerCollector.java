package com.xiaoka.monitor.collector.memory;

import com.ustcinfo.ishare.eip.si.cache.common.ICache;
import com.xiaoka.monitor.MonitorContext;
import com.xiaoka.monitor.cache.AgentMetric;
import com.xiaoka.monitor.collector.common.ICollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 基于内部缓存的收集器实现
 */
@Component
public class InnerCollector implements ICollector {

    @Autowired
    private ICache cache;

    @Override
    public void putToCache(AgentMetric agentMetric) {
        /**
         * 添加到内置队列中
         * 供juege来判断
         */
        MonitorContext.AGENT_METRIC_QUEUE.add(agentMetric);
        /**
         * 添加到缓存中
         * 供其他地方获取
         * 如页面实时展示
         */
        cache.put(agentMetric);
    }
}
