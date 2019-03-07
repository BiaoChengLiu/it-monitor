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
 * agent上发数据处理器
 */
@Component("collectorProcessor")
public class CollectorProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICollector collector;

    public void process(Exchange exchange) throws Exception {
        String agentData = exchange.getIn().getBody(String.class);
        logger.info("收集器收到一条上发数据:{}", agentData);
        if (!StringUtils.isNotBlank(agentData)) {
            //数据为空
            return;
        }
        AgentMetric agentMetric = null;
        try {
            agentMetric = JSON.parseObject(agentData, AgentMetric.class);
        } catch (Exception e) {
            logger.error("格式化数据失败:{},{}", e.getMessage(), agentData);
            return;
        }
        if (agentMetric == null) {
            logger.error("数据格式化失败,数据为空");
            return;
        }
        //防止agent所在服务器和收集器所在服务器时间不同步
        agentMetric.setTimestamp(System.currentTimeMillis());
        //放入seda中
        exchange.setProperty(AgentMetric.class.getSimpleName(), agentMetric);
    }
}
