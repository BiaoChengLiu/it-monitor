package com.xiaoka.monitor.collector.common.router;

import org.apache.camel.builder.RouteBuilder;

/**
 * 收集器路由
 */
public class CollectorRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        /**
         * 收集器上发的地址
         */
        from("jetty:http://localhost:8888/eip").to("bean:collectorProcessor?method=process");
    }
}
