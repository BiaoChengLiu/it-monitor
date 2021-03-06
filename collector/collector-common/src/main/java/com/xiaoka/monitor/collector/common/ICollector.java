package com.xiaoka.monitor.collector.common;


import com.xiaoka.monitor.cache.AgentMetric;

/**
 * 收集器接口
 */
public interface ICollector {
    /**
     * 收集到的agent上发的数据放入到缓存中
     * TODO 这里后面需要支持一致性hash算法实现
     *
     * @param agentMetric
     */
    void putToCache(AgentMetric agentMetric);
}
