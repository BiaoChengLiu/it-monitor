package com.xiaoka.monitor.sender.memory;

import com.xiaoka.monitor.MonitorContext;
import com.xiaoka.monitor.cache.AgentMetric;
import com.xiaoka.monitor.cache.AlarmEventCache;
import com.xiaoka.monitor.sender.common.SenderHandlerRunnable;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 基于内存的消费队列中的告警事件
 */
public class MemorySenderConsumer {

    @PostConstruct
    public void init() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 10;
                while (true) {
                    try {
                        AlarmEventCache alarmEvent = MonitorContext.WAIT_SENDER_ALARM_QUEUE.poll(5, TimeUnit.SECONDS);
                        if (alarmEvent == null) {
                            Thread.sleep(time);
                            continue;
                        }
                        MonitorContext.ALARM_SENDER_POOL.execute(new SenderHandlerRunnable(alarmEvent));
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e1) {
                        }
                    }
                }
            }
        }).start();

    }

}
