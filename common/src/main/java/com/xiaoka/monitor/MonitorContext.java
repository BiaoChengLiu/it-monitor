package com.xiaoka.monitor;


import com.xiaoka.monitor.cache.AgentMetric;
import com.xiaoka.monitor.cache.AlarmEventCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 监控的上下文
 * 单机部署时使用
 */
public class MonitorContext {
    /**
     * 内置指标队列
     */
    public static final LinkedBlockingQueue<AgentMetric> AGENT_METRIC_QUEUE = new LinkedBlockingQueue<AgentMetric>();
    /**
     * 处理指标线程池
     */
    public static final ExecutorService AGENT_METRIC_JUDGE_POOL = Executors.newFixedThreadPool(50);

    /**
     * 等待发送的告警队列
     */
    public static final LinkedBlockingQueue<AlarmEventCache> WAIT_SENDER_ALARM_QUEUE = new LinkedBlockingQueue<AlarmEventCache>();
    /**
     * 告警发送线程池
     */
    public static final ExecutorService ALARM_SENDER_POOL = Executors.newFixedThreadPool(50);

}
