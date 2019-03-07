package com.xiaoka.monitor.collector.common.router;

import org.apache.camel.builder.RouteBuilder;

/**
 * 这个是一个seda
 * 用于异步处理agent转发
 */
public class TransferSedaRouter extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        from("seda:transfer?concurrentConsumers=5").
                to("bean:transferProcessor?method=process");
    }
}
