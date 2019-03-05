package com.xiaoka.monitor.collector.common;

import com.alibaba.fastjson.JSON;
import com.xiaoka.monitor.cache.AgentMetric;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息处理器
 */
@Component
public class CollectorProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICollector collector;

    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        logger.info("收集器收到一条上发数据:{}", body);
        if (!StringUtils.isNotBlank(body)) {
            //数据为空
            return;
        }
        AgentMetric agentMetric = JSON.parseObject(body, AgentMetric.class);
        if (agentMetric == null) {
            logger.error("数据格式化后为空、被忽略!");
            return;
        }
        agentMetric.setTimestamp(System.currentTimeMillis());
        // 发送到缓存中
        collector.putToCache(agentMetric);
    }
}
