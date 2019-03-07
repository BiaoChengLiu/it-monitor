package com.xiaoka.monitor.collector.common.bean;

import com.alibaba.fastjson.JSON;
import com.xiaoka.monitor.cache.AgentMetric;
import com.xiaoka.monitor.collector.common.ICollector;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * agent格式化后的数据进行转发
 * 这里缓存下
 */
@Component("transferProcessor")
public class TransferProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICollector collector;

    public void process(Exchange exchange) throws Exception {
        AgentMetric agentMetric = exchange.getProperty(AgentMetric.class.getSimpleName(), AgentMetric.class);
        if (agentMetric != null) {
            // 发送到缓存中
            collector.putToCache(agentMetric);
        }
    }
}
